package work1.project1.package1.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import work1.project1.package1.dto.request.EmployeeAddRequest;
import work1.project1.package1.dto.request.EmployeePersonalInfoUpdateRequest;
import work1.project1.package1.dto.request.EmployeeUpdateRequest;
import work1.project1.package1.dto.request.UpdateSalaryRequestDto;
import work1.project1.package1.dto.response.*;
import work1.project1.package1.entity.CompanyDepartmentMappingEntity;
import work1.project1.package1.entity.EmployeeEntity;
import work1.project1.package1.entity.EmployeeMappingEntity;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.exception.NotPresentException;
import work1.project1.package1.exception.ResponseHttp;
import work1.project1.package1.other.TokenGenerator;
import work1.project1.package1.repository.CompanyDepartmentMappingRepository;
import work1.project1.package1.repository.EmployeeMappingRepository;
import work1.project1.package1.repository.EmployeeRepository;
import work1.project1.package1.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static work1.project1.package1.constants.ApplicationConstants.*;

@Service
public class EmployeeService {


    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeMappingRepository employeeMappingRepository;

    @Autowired
    CompanyDepartmentMappingRepository companyDepartmentMappingRepository;

    @Autowired
    EmployeeMappingService mappingService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    Caching caching;
    @Autowired
    UserService userService;
    @Autowired
    RedisService redisService;
    public Object addEmployee(EmployeeAddRequest employeeAddRequest,Long userId) throws CustomException, NotPresentException {
        Long companyId = employeeAddRequest.getCompanyId();
        Long departmentId = employeeAddRequest.getDepartmentId();
        Long managerId = employeeAddRequest.getManagerId();
        TokenGenerator tokenGenerator=new TokenGenerator();
        if (companyId == -1 || departmentId == -1) {
            if(managerId!=-1 || !employeeAddRequest.getDesignation().equalsIgnoreCase(_NONE) || employeeAddRequest.getSalary()!=-1)
                throw new CustomException(FAILED);
            EmployeeEntity employeeEntity =new EmployeeEntity(employeeAddRequest.getName(),employeeAddRequest.getPhone(),
                    -1L,-1L,EMPLOYEE, userId,-1L);
            try {
                employeeRepository.save(employeeEntity);
            }catch(org.springframework.dao.DataIntegrityViolationException e) {
                throw new CustomException(" phone no is already used!! ");
            }
            String token=tokenGenerator.tokenGenerate(0L,0L,employeeEntity.getId());
            caching.tokenCaching(token, "1");
            userService.userAdd(employeeEntity.getId(),employeeAddRequest.getPhone());
            EmployeeAddResponse employeeAddResponse=new EmployeeAddResponse();
            return employeeAddResponse.convert(employeeEntity,token);
        }
        CompanyDepartmentMappingEntity companyDepartmentMappingEntity = companyDepartmentMappingRepository.
                findByCompanyIdAndDepartmentIdAndIsActive(companyId, departmentId, true);
        if (companyDepartmentMappingEntity != null) {
            Long mappingId = companyDepartmentMappingEntity.getId();
            if (managerId != -1 && !isManager(managerId)) { //if managerId given then designation must be manager
                throw  new NotPresentException(FAILED + " manager not present!! ");
            }
            if(employeeAddRequest.getSalary()==-1)
                throw  new NotPresentException(FAILED+" Salary Required!!");
            EmployeeEntity employeeEntity =modelMapper.map(employeeAddRequest, EmployeeEntity.class);
            employeeEntity.setCreatedBy(userId);
            try {
                employeeRepository.save(employeeEntity);
            }catch(org.springframework.dao.DataIntegrityViolationException e){
                throw new CustomException(" phone no is already used!! ");
            }
            userService.userAdd(employeeEntity.getId(),employeeAddRequest.getPhone());
            String token=tokenGenerator.tokenGenerate(companyId,departmentId,employeeEntity.getId());
            caching.tokenCaching(token, "1");
            mappingService.add(mappingId, employeeEntity.getId());
            EmployeeCompleteResponse completeResponseDto=new EmployeeCompleteResponse();
            return  completeResponseDto.convert(employeeEntity,companyId,departmentId,token);
        }
        throw  new NotPresentException(FAILED +" Department-Company "+ NOT_PRESENT);
    }

    public Object  getEmployee(Long id) throws CustomException {
       return caching.getEmployeeById(id);
    }

    public List<EmployeeGetResponse> getAllEmployees() {
     return Arrays.asList(modelMapper.map(employeeRepository.findAll(),EmployeeGetResponse[].class));
    }
    boolean isManager(Long managerId) {
        Optional<EmployeeEntity> fetchedEmployeeEntity=employeeRepository.findById(managerId);
        if(fetchedEmployeeEntity.isPresent()) {
            EmployeeEntity employeeEntity = fetchedEmployeeEntity.get();
            return true;
        }
        return false;
    }

    public Response deleteEmployee(Long id) throws NotPresentException, CustomException {
        redisService.deleteTokenOfEmployeeFromCache(id);
        return caching.deleteEmployeebyId(id);
    }

    public Response updatePersonalInfo(EmployeePersonalInfoUpdateRequest updateRequest,Long userId) throws ResponseHttp, CustomException {
        Long id=updateRequest.getId();
        Optional<EmployeeEntity> fetchedEmployeeEntity= employeeRepository.findById(id);
        if(fetchedEmployeeEntity.isPresent()) {
            EmployeeEntity employeeEntity= fetchedEmployeeEntity.get();
            if(updateRequest.getName()!=null)
                employeeEntity.setName(updateRequest.getName());
            if(updateRequest.getPhone()!=null)
                employeeEntity.setPhone(updateRequest.getPhone());
            employeeEntity.setUpdatedBy(userId);
            try {
                employeeRepository.save(employeeEntity);
                caching.updateEmployeeCache(id,employeeEntity);
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                throw new CustomException(" phone no is already used!! ");
            }
            return new Response(200,UPDATE_SUCCESS);
        }
        throw new ResponseHttp(HttpStatus.NOT_FOUND,NOT_PRESENT);
    }

    public EmployeeUpdateResponse updateDetails(EmployeeUpdateRequest requestDto , Long userId) throws CustomException, ResponseHttp, NotPresentException {
        Long employeeId=requestDto.getId();
        Optional<EmployeeEntity> fetchedEmployeeEntity= employeeRepository.findById(employeeId);
        if(fetchedEmployeeEntity.isPresent()) {
            EmployeeEntity employeeEntity=fetchedEmployeeEntity.get();
            Long companyId=requestDto.getCompanyId();
            Long departmentId=requestDto.getDepartmentId();
            Long salary=requestDto.getSalary();
            Long managerId=requestDto.getManagerId();
            String designation=requestDto.getDesignation();
            if(( companyId!=-1 && departmentId==-1) || ( companyId==-1 && departmentId!=-1)) //only one id given then error
                throw new ResponseHttp(HttpStatus.BAD_REQUEST,FAILED+ " please provide company-department Id !! ");
            if(companyId!=-1 && departmentId!=-1) {
                if(salary<=0)
                    throw new CustomException(FAILED+ " salary required!! ");
                mappingService.updateDetails(companyId, departmentId, employeeId,userId);
            }
            //if cid did not given then aupdate info after checking employment details
            boolean flag=employeeMappingRepository.existsByEmployeeIdAndIsActive(employeeId,true);
            if(!flag)
                 throw  new ResponseHttp(HttpStatus.BAD_REQUEST,FAILED+" employee not part of any company-department!! ");
            if(salary>=0)
                employeeEntity.setSalary(salary);
            if(designation!=null || designation!=_NONE)
                employeeEntity.setDesignation(designation);
            if(managerId!=-1){
                if(isManager(managerId))
                    employeeEntity.setManagerId(managerId);
                else{ //wrong managerId given then failed
                    throw  new CustomException(FAILED + " manager not present!! ");
                }
            }
            employeeEntity.setUpdatedBy(userId);
            caching.updateEmployeeCache(employeeId,employeeEntity);
            TokenGenerator tokenGenerator=new TokenGenerator();
            String token=tokenGenerator.tokenGenerate(companyId,departmentId,employeeEntity.getId());
            caching.tokenCaching(token, "1");
            employeeRepository.save(employeeEntity);
            return new EmployeeUpdateResponse(token,SUCCESS);
        }
        throw  new NotPresentException(FAILED + NOT_PRESENT);
    }


    public boolean updateSalary(UpdateSalaryRequestDto updateSalaryRequestDto) {
        Long employeeId=updateSalaryRequestDto.getEmployeeId();
        Long departmentId=updateSalaryRequestDto.getDepartmentId();
        Long companyId=updateSalaryRequestDto.getCompanyId();
        Long salary_increment=updateSalaryRequestDto.getSalary_increment();
        Long flag=updateSalaryRequestDto.getFlag();

        if(employeeId!=-1)
           return updateEmployeeSalary(employeeId,salary_increment,flag);
        if(departmentId!=-1 && companyId!=-1){
              CompanyDepartmentMappingEntity companyDepartmentMappingEntity=companyDepartmentMappingRepository.
                      findByCompanyIdAndDepartmentIdAndIsActive(companyId,departmentId,true);
              if(companyDepartmentMappingEntity==null)
                  return false;
             return updateDepartmentSalary(companyDepartmentMappingEntity.getId(),salary_increment,flag);
        }
        if(companyId!=-1){
            List<CompanyDepartmentMappingEntity> companyDepartmentMappingEntityList=companyDepartmentMappingRepository.
                    findAllByCompanyIdAndIsActive(companyId,true);
            companyDepartmentMappingEntityList.forEach(d->{
                updateDepartmentSalary(d.getId(),salary_increment,flag);
            });
            return true;
        }
        return false;
    }

    private boolean updateDepartmentSalary(Long cd_mappingId,Long salary_increment,Long flag) {

        List<EmployeeMappingEntity> employeeMappingEntityList=employeeMappingRepository.findByMappingIdAndIsActive
                (cd_mappingId,true);
        if(employeeMappingEntityList==null )
             return false;
        employeeMappingEntityList.forEach(m->{
            updateEmployeeSalary(m.getEmployeeId(),salary_increment,flag);
        });
        return true;
    }

    public boolean updateEmployeeSalary(Long employeeId, Long salary_increment,Long flag) {
        if(mappingService.getIds(employeeId)==null)
            return  false;
        if(flag==1)  //increment by absolute value
        {
            Optional<EmployeeEntity> fetchedEmployeeEntity=employeeRepository.findById(employeeId);
            if(fetchedEmployeeEntity.isPresent())
            {
                EmployeeEntity employeeEntity=fetchedEmployeeEntity.get();
                employeeEntity.setSalary(employeeEntity.getSalary()+salary_increment);
                employeeRepository.save(employeeEntity);
                return true;
            }
            return false;
        }
        else if(flag==0)
        {
            Optional<EmployeeEntity> fetchedEmployeeEntity=employeeRepository.findById(employeeId);
            if(fetchedEmployeeEntity.isPresent())
            {
                EmployeeEntity employeeEntity=fetchedEmployeeEntity.get();
                Long salary=employeeEntity.getSalary();
                employeeEntity.setSalary(salary+((salary*salary_increment)/100));
                employeeRepository.save(employeeEntity);
                return true;
            }
            return false;
        }
        else
        {
            return false;
        }
    }

}
