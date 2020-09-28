package work1.project1.package1.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import work1.project1.package1.dto.request.EmployeeAddRequest;
import work1.project1.package1.dto.request.EmployeePersonalInfoUpdateRequest;
import work1.project1.package1.dto.request.EmployeeUpdateRequest;
import work1.project1.package1.dto.request.UpdateSalaryRequest;
import work1.project1.package1.dto.response.*;
import work1.project1.package1.entity.CompanyDepartmentMappingEntity;
import work1.project1.package1.entity.EmployeeEntity;
import work1.project1.package1.exception.*;
import work1.project1.package1.myenum.EnumToLower;
import work1.project1.package1.myenum.MyEnum;
import work1.project1.package1.other.MD5;
import work1.project1.package1.other.TokenGenerator;
import work1.project1.package1.repository.*;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static work1.project1.package1.constants.ApplicationConstants.*;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    EmployeeMappingRepository employeeMappingRepository;

    @Autowired
    CompanyDepartmentMappingRepository companyDepartmentMappingRepository;

    @Autowired
    EmployeeMappingService employeeMappingService;


    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CachingService cachingService;
    @Autowired
    RedisService redisService;


    public Object addEmployee(EmployeeAddRequest employeeAddRequest,Long userId) throws CustomException,
            NotPresentException, NoSuchAlgorithmException, InsufficientDataException {
        Double doubleCompanyId = employeeAddRequest.getCompanyId();
        Double doubleDepartmentId = employeeAddRequest.getDepartmentId();
        Double doubleManagerId = employeeAddRequest.getManagerId();
        Long companyId=null,departmentId=null,managerId=null;
        if(doubleCompanyId!=null)
         companyId=doubleCompanyId.longValue();
        if(doubleDepartmentId!=null)
         departmentId=doubleDepartmentId.longValue();
        if(doubleManagerId!=null)
         managerId=doubleManagerId.longValue();
        MyEnum myEnum=employeeAddRequest.getDesignation();
        if(myEnum==null)
            myEnum=MyEnum.none;
        EnumToLower convertEnumToLower=new EnumToLower();
        employeeAddRequest.setDesignation(convertEnumToLower.convert(myEnum));
        if (companyId == null || departmentId == null) {
            if(managerId!=null || !(employeeAddRequest.getDesignation()==(MyEnum.none)) || employeeAddRequest.getSalary()!=null
            ||(companyId==null && departmentId!=null)||(companyId!=null && departmentId==null))
                throw new InsufficientDataException(" insufficient data , please provide all required data!! ");
            EmployeeEntity employeeEntity =new EmployeeEntity(employeeAddRequest.getName(),employeeAddRequest.getPhone(),
                    null,null,MyEnum.none, userId,null,MD5.getMd5(employeeAddRequest.getPhone()));
            try {
                employeeRepository.save(employeeEntity);
            }catch(org.springframework.dao.DataIntegrityViolationException e) {
                throw new CustomException(" phone no is already used!! ");
            }
            //userService.userAdd(employeeEntity.getId(),MD5.getMd5(employeeAddRequest.getPhone()));
            EmployeeAddResponse employeeAddResponse=new EmployeeAddResponse();
            return employeeAddResponse.convert(employeeEntity);
        }
        CompanyDepartmentMappingEntity companyDepartmentMappingEntity = companyDepartmentMappingRepository.
                findByCompanyIdAndDepartmentIdAndIsActive(companyId, departmentId, true);
        if (companyDepartmentMappingEntity != null) {
            Long mappingId = companyDepartmentMappingEntity.getId();
            if (managerId != null && !isManager(managerId,companyId,departmentId)) { //if managerId given then designation must be manager
                   throw  new NotPresentException(" manager not present!! ");
            }
            if(employeeAddRequest.getSalary()==null)
                throw  new CustomException(" Salary Required!!");
            if(employeeAddRequest.getSalary()<=0)
                throw  new CustomException(" Positive Salary Required!!");
            if(employeeAddRequest.getDesignation()==MyEnum.none)
                employeeAddRequest.setDesignation(MyEnum.employee);
            EmployeeEntity employeeEntity =modelMapper.map(employeeAddRequest, EmployeeEntity.class);
            employeeEntity.setCreatedBy(userId);
            employeeEntity.setUpdatedBy(null);
            employeeEntity.setPassword(MD5.getMd5(employeeAddRequest.getPhone()));
            try {
                employeeRepository.save(employeeEntity);
            }catch(org.springframework.dao.DataIntegrityViolationException e){
                throw new CustomException(" phone no is already used!! ");
            }
            //userService.userAdd(employeeEntity.getId(),MD5.getMd5(employeeAddRequest.getPhone()));
            employeeMappingService.add(mappingId, employeeEntity.getId());
            EmployeeCompleteResponse completeResponseDto=new EmployeeCompleteResponse();
            completeResponseDto= completeResponseDto.convert(employeeEntity,companyId,departmentId);
            if(userId==0) System.out.println(employeeEntity);
            return completeResponseDto;
        }
        throw  new NotPresentException(" Department-Company "+ NOT_PRESENT);
    }

    public Object  getEmployee(Long id) throws CustomException, NotPresentException {
       return cachingService.getEmployeeById(id);
    }

    public List<EmployeeGetResponse> getAllEmployees(int page,int size) throws NotPresentException {
        Pageable pageable= PageRequest.of(page,size);
        Page<EmployeeEntity> pageEmployeeEntityList=employeeRepository.findAll(pageable);
        if(pageEmployeeEntityList==null)
            throw new NotPresentException("Employee"+NOT_PRESENT);
        List<EmployeeEntity> employeeEntityList= pageEmployeeEntityList.getContent();
        if(employeeEntityList.isEmpty())
            throw new NotPresentException("Employee"+NOT_PRESENT);
        return Arrays.asList(modelMapper.map(employeeEntityList,EmployeeGetResponse[].class));
    }

    boolean isManager(Long managerId,Long companyId , Long departmentId ) {
        Optional<EmployeeEntity> fetchedEmployeeEntity=employeeRepository.findById(managerId);
        if(fetchedEmployeeEntity.isPresent()) {
            CompanyDepartmentMappingEntity cdMappingEntity=employeeMappingService.getIds(managerId);
            if(cdMappingEntity.getDepartmentId().equals(departmentId) && cdMappingEntity.getCompanyId().equals(companyId))
               return true;
            return false;
        }
        return false;
    }

    public Response deleteEmployee(Long id , Long userId) throws NotPresentException, CustomException {
        redisService.deleteTokenOfEmployeeFromCache(id);
        return cachingService.deleteEmployeeById(id,userId);
    }

    public Response updatePersonalInfo(EmployeePersonalInfoUpdateRequest updateRequest,Long userId) throws ResponseHttp,
            CustomException, NotPresentException, InsufficientDataException {
        Double doubleId=updateRequest.getId();
        Long id=doubleId.longValue();
        if(updateRequest.getName()==null && updateRequest.getPhone()==null)
            throw new InsufficientDataException(" please provide atleast one update detail!! ");
        if("".equals(updateRequest.getName()))
            throw new CustomException(" name must not be empty!! ");
        Optional<EmployeeEntity> fetchedEmployeeEntity= employeeRepository.findById(id);
        if(fetchedEmployeeEntity!=null && fetchedEmployeeEntity.isPresent()) {
            EmployeeEntity employeeEntity= fetchedEmployeeEntity.get();
            if(updateRequest.getName()!=null)
                employeeEntity.setName(updateRequest.getName());
            if(updateRequest.getPhone()!=null)
                employeeEntity.setPhone(updateRequest.getPhone());
            employeeEntity.setUpdatedBy(userId);
            try {
                employeeRepository.save(employeeEntity);
                cachingService.updateEmployeeCache(id,employeeEntity);
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                throw new CustomException(" phone no is already used!! ");
            }
            return new Response(200,UPDATE_SUCCESS);
        }
        throw new NotPresentException("Employee"+NOT_PRESENT);
    }

    public Response updateDetails(EmployeeUpdateRequest requestDto , Long userId) throws CustomException,
            ResponseHttp,NotPresentException, SuccessException {
        Long employeeId=requestDto.getId().longValue();
        Optional<EmployeeEntity> fetchedEmployeeEntity= employeeRepository.findById(employeeId);
        if(fetchedEmployeeEntity.isPresent()) {
            boolean managerPresentCheck=true;
            EmployeeEntity employeeEntity=fetchedEmployeeEntity.get();
            Long companyId=null,departmentId=null,managerId=null;
            if(requestDto.getCompanyId()!=null)
             companyId=requestDto.getCompanyId().longValue();
            if(requestDto.getDepartmentId()!=null)
             departmentId=requestDto.getDepartmentId().longValue();
            Double salary=requestDto.getSalary();
            if(requestDto.getManagerId()!=null)
             managerId=requestDto.getManagerId().longValue();
            MyEnum designation=requestDto.getDesignation();
            EnumToLower convertEnumToLower=new EnumToLower();
            if(( companyId!=null && departmentId==null) || ( companyId==null && departmentId!=null)) //only one id given then error
                throw new ResponseHttp(HttpStatus.BAD_REQUEST," please provide both company-department Id !! ");
            if(companyId!=null && departmentId!=null) {
                if(salary==null)
                    throw new CustomException(" salary required!! ");
                if(salary<=0)
                    throw new CustomException("positive salary required!! ");
                if(managerId!=null){
                    if(managerId==employeeId)
                        throw new CustomException(" manager must be a different employee in a department-company!! ");
                    if(isManager(managerId,companyId,departmentId)) {
                        managerPresentCheck=false;
                        employeeEntity.setManagerId(managerId);
                    }
                    else{        //wrong managerId given then failed
                        throw new CustomException(FAILED + " manager not present!! ");
                    }
                }
                if(designation==null)
                    designation=MyEnum.employee;
                employeeMappingService.updateDetails(companyId, departmentId, employeeId,userId);
            }//if cid did not given then update info after checking employment details
            boolean flag=employeeMappingRepository.existsByEmployeeIdAndIsActive(employeeId,true);
            if(!flag)
                throw  new ResponseHttp(HttpStatus.BAD_REQUEST," employee not part of any company-department!!");
            if(salary!=null) {
                if(salary <= 0)
                    throw new CustomException("positive salary required!! ");
                employeeEntity.setSalary(salary);
            }
            if(designation!=null)
                 employeeEntity.setDesignation(convertEnumToLower.convert(designation));
            if(managerId!=null && managerPresentCheck){
                if(managerId==employeeId)
                    throw new CustomException(" manager must be a different employee in a department-company!! ");
                CompanyDepartmentMappingEntity cdMappingEntity=employeeMappingService.getIds(employeeId);
                if(isManager(managerId,cdMappingEntity.getCompanyId(),cdMappingEntity.getDepartmentId()))
                    employeeEntity.setManagerId(managerId);
                else       //wrong managerId given then failed
                    throw new CustomException( " manager not present!! ");
            }
            employeeEntity.setUpdatedBy(userId);
            cachingService.updateEmployeeCache(employeeId, employeeEntity);
            employeeRepository.save(employeeEntity);
            return new Response(200L," Update Success!! ");
        }
        throw  new NotPresentException( "employee " +NOT_PRESENT);
    }


    public boolean updateSalary(UpdateSalaryRequest updateSalaryRequestDto) throws CustomException, NotPresentException
            , InsufficientDataException {
        Long companyId = null, departmentId = null, employeeId = null;
        Double salary_change = updateSalaryRequestDto.getSalary_change();
        Long type = updateSalaryRequestDto.getType();
        if (type < 0 || type > 1)
            throw new CustomException(" type can only be 0 or 1. 0 for % increment & 1 for absolute increment ");
        if (type == 0 && salary_change < -100)
            salary_change = -100D;
        if (updateSalaryRequestDto.getEmployeeId() != null) {
            employeeId = updateSalaryRequestDto.getEmployeeId().longValue();
            //update only employees salary
            EmployeeEntity employeeEntity = employeeRepository.findById(employeeId).orElse(null);
            if (employeeEntity != null) {
                if (employeeMappingRepository.existsByEmployeeIdAndIsActive(employeeId, true)) {
                    Double saved_salary = employeeEntity.getSalary();
                    if (type == 0)
                        employeeEntity.setSalary(saved_salary + ((saved_salary * salary_change) / 100));
                    else {
                        if (saved_salary + salary_change > 0)
                            employeeEntity.setSalary(saved_salary + salary_change);
                        else
                            employeeEntity.setSalary(0D);
                    }
                    employeeRepository.save(employeeEntity);
                    cachingService.updateEmployeeCache(employeeId, employeeEntity);
                    return true;
                }
                throw new NotPresentException("employee is not part of any company-department!!");
            }
            throw new NotPresentException("employee not present!!");
        } else if (updateSalaryRequestDto.getCompanyId() != null && updateSalaryRequestDto.getDepartmentId() != null) {
            companyId = updateSalaryRequestDto.getCompanyId().longValue();
            departmentId = updateSalaryRequestDto.getDepartmentId().longValue();
            //update salary of department of company
            if (companyDepartmentMappingRepository.existsByCompanyIdAndDepartmentIdAndIsActive(companyId, departmentId, true)) {
                if (type == 0) {//% inc
                    companyDepartmentMappingRepository.queryForChangeSalaryOfDepartmentOfCompanyByPercentage(companyId,
                            departmentId, true, salary_change);
                    return true;
                } else {
                    companyDepartmentMappingRepository.queryForChangeSalaryOfDepartmentOfCompanyByAbsValue(companyId,
                            departmentId, true, salary_change);
                    return true;
                }
            }
            throw new NotPresentException("company-department not present!");
        } else if (updateSalaryRequestDto.getCompanyId() != null) {
            companyId = updateSalaryRequestDto.getCompanyId().longValue();
            //update salary of company
            if (companyRepository.existsByIdAndIsActive(companyId, true)) {
                if (type == 0) {
                    companyRepository.queryForChangeSalaryOfCompanyByPercentage(companyId, true, salary_change);
                    return true;
                } else {
                    companyRepository.queryForChangeSalaryOfCompanyByAbsValue(companyId, true, salary_change);
                    return true;
                }
            }
        } else
            throw new InsufficientDataException("please provide valid ids!!");
        return
                false;
}
    public String tokenGenerate(Long employeeId, String password) throws UnAuthorizedUser, NoSuchAlgorithmException {
        if(!isValidCredentials(employeeId,password))
            throw new UnAuthorizedUser(" invalid credentials !! ");
         //i am always generating token for user if its provide valid credentials ,
        // and if token already present then delete that one also
        redisService.deleteTokenOfEmployeeFromCache(employeeId);
        TokenGenerator tokenGenerator=new TokenGenerator();
        CompanyDepartmentMappingEntity cdMappingEntity=companyDepartmentMappingRepository.findCompanyDepartmentQuery(employeeId,true);
        String token;
        if(cdMappingEntity==null)  //todo extract constant or impl another token gen with
            token=tokenGenerator.tokenGenerate(0L,0L,employeeId);
        else
            token=tokenGenerator.tokenGenerate(cdMappingEntity.getCompanyId(),cdMappingEntity.getDepartmentId(),employeeId);
        cachingService.tokenCaching(token,"arjun");
        return token;
    }

    public boolean isValidCredentials(Long employeeId, String password) throws NoSuchAlgorithmException {
        EmployeeEntity employeeEntity=employeeRepository.findById(employeeId).orElse(null);
        if(employeeEntity==null || !employeeEntity.getPassword().equals(MD5.getMd5(password)))
            return false;
        return true;
    }

    public Response changePassword(Long employeeId, String newPassword) throws CustomException, NoSuchAlgorithmException {
        EmployeeEntity employeeEntity=employeeRepository.findById(employeeId).orElse(null);
        if(employeeEntity==null)
            throw new CustomException(NOT_PRESENT);
        employeeEntity.setPassword(MD5.getMd5(newPassword));
        employeeRepository.save(employeeEntity);
        return new Response(200,SUCCESS);
    }
}
