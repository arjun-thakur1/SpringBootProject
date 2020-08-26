package work1.project1.package1.services;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import work1.project1.package1.dto.request.DepartmentAddRequest;
import work1.project1.package1.dto.request.DepartmentCompanyAddRequest;
import work1.project1.package1.dto.request.DepartmentUpdateRequest;
import work1.project1.package1.dto.response.*;
import work1.project1.package1.entity.*;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.exception.DuplicateDataException;
import work1.project1.package1.exception.NotPresentException;
import work1.project1.package1.exception.ResponseHttp;
import work1.project1.package1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static work1.project1.package1.constants.ApplicationConstants.*;

@Service
@Transactional      //delete request is not performed without this annotation
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private EmployeeMappingRepository departmentEmployeeMappingRepository;

    @Autowired
    CompanyDepartmentMappingRepository companyDepartmentMappingRepository;
    @Autowired
    EmployeeMappingRepository employeeMappingRepository;
    @Autowired
    EmployeeService employeeService;

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    EmployeeMappingService employeeMappingService;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    Caching caching;
    @Autowired
    RedisService redisService;
    @Autowired
    CompanyDepartmentMappingRepository mappingRepository;

    public List<DepartmentResponse> getAllDepartments() {
        return Arrays.asList(modelMapper.map(departmentRepository.findAll(),DepartmentResponse[].class));
    }

    public DepartmentResponse addDepartment(DepartmentAddRequest addRequest,Long userId) throws DuplicateDataException {
        DepartmentEntity departmentEntity = new DepartmentEntity(addRequest.getDepartmentName().toLowerCase(), userId, -1);
        if(departmentRepository.existsByDepartmentName(addRequest.getDepartmentName().toLowerCase()))
            throw new DuplicateDataException(" Department already present!! ");
        departmentRepository.save(departmentEntity);
        return new DepartmentResponse(departmentEntity.getId(),departmentEntity.getDepartmentName());
    }

    public DepartmentResponse getDepartmentDetail(Long departmentId) throws NotPresentException {
        Optional<DepartmentEntity> fetchedDepartmentEntity=departmentRepository.findById(departmentId);
        if(fetchedDepartmentEntity!=null && fetchedDepartmentEntity.isPresent()) {
            DepartmentEntity departmentEntity=fetchedDepartmentEntity.get();
            return    modelMapper.map(departmentEntity, DepartmentResponse.class);
        }
        throw new NotPresentException(FAILED+NOT_PRESENT);
    }

    public DepartmentResponse updateDetails(DepartmentUpdateRequest departmentRequestDto, Long userId) throws NotPresentException, DuplicateDataException {
        Long departmentId=departmentRequestDto.getDepartmentId();
        Optional<DepartmentEntity> fetchedDepartmentEntity=departmentRepository.findById(departmentId);
        if(fetchedDepartmentEntity!=null && fetchedDepartmentEntity.isPresent()){
            if(departmentRepository.existsByDepartmentName(departmentRequestDto.getDepartmentName()))
                throw new DuplicateDataException(FAILED+DUPLICATE_NAME_ERROR);
            DepartmentEntity departmentEntity=fetchedDepartmentEntity.get();
            departmentEntity.setDepartmentName(departmentRequestDto.getDepartmentName());
            departmentEntity.setUpdatedBy(userId);
            caching.cacheDepartmentDeleteForCompanies(departmentId);// that has this department
            departmentRepository.save(departmentEntity);
            return modelMapper.map(departmentEntity,DepartmentResponse.class);
        }
        throw new NotPresentException(NOT_PRESENT);
    }

    public List<DepartmentResponse> getAllDepartmentsOfCompany(Long companyId) throws CustomException, NotPresentException {

        return (List<DepartmentResponse>)caching.getAllDepartmentsOfCompany(companyId);
    }


    public Response addDepartmentToCompany(DepartmentCompanyAddRequest requestDepartment,Long userId) throws NotPresentException {
        Long companyId=requestDepartment.getCompanyId();
        Long departmentId=requestDepartment.getDepartmentId();
        if(companyRepository.existsByIdAndIsActive(companyId,true)) {
            if(departmentRepository.existsById(departmentId)){
               CompanyDepartmentMappingEntity mappingEntity= companyDepartmentMappingRepository.
                       findByCompanyIdAndDepartmentId(companyId,departmentId);
               if(mappingEntity==null){
                   CompanyDepartmentMappingEntity mappingEntity1=new CompanyDepartmentMappingEntity
                           (companyId,departmentId,userId,userId,true);
                  // mappingEntity1.setActive(true);
                  // mappingEntity1.setCompanyId(companyId);
                   // mappingEntity1.setDepartmentId(departmentId);
                   //mappingEntity1.setCreatedBy(userId);
                   //mappingEntity.setUpdatedBy(userId);
                   caching.deleteDepartmentsOfCompany(companyId);
                   companyDepartmentMappingRepository.save(mappingEntity1);
                   return new Response(200, SUCCESS);
               }
               if(mappingEntity.isActive()){
                   return new Response(200, " Department already present!! ");
               }
               mappingEntity.setActive(true);
               mappingEntity.setCreatedBy(userId);
               mappingEntity.setUpdatedBy(userId);
               caching.deleteDepartmentsOfCompany(companyId);
               companyDepartmentMappingRepository.save(mappingEntity);
               return new Response(200, SUCCESS);
            }
            throw new NotPresentException(" Department not present!! ");
        }
        throw new NotPresentException(" Company not present!! ");
    }

    public DepartmentCompanyResponse getDepartmentOfCompany(Long companyId, Long departmentId) throws  NotPresentException {
            Optional<DepartmentEntity> fetchedDepartmentEntity=departmentRepository.findById(departmentId);
            if(fetchedDepartmentEntity.isPresent()){
                if(mappingRepository.existsByCompanyIdAndDepartmentIdAndIsActive(companyId,departmentId,true)) {
                    DepartmentEntity departmentEntity=fetchedDepartmentEntity.get();
                    return new DepartmentCompanyResponse(companyId,departmentId,departmentEntity.getDepartmentName(),SUCCESS);
                 }
                throw  new NotPresentException(NOT_PRESENT);
            }
            throw  new NotPresentException(NOT_PRESENT);
    }

    public Response deleteDepartmentDetails(Long companyId,Long departmentId) throws ResponseHttp {
        CompanyDepartmentMappingEntity companyDepartmentMappingEntity=companyDepartmentMappingRepository.
                findByCompanyIdAndDepartmentIdAndIsActive(companyId,departmentId,true);
               if(companyDepartmentMappingEntity==null)
            throw new ResponseHttp(HttpStatus.NOT_FOUND,DEPARTMENT_NOT_PRESENT);
        redisService.deleteTokensOfEmployeesOfDepartment(companyId,departmentId);
        caching.deleteDepartmentsOfCompany(companyId);
        //employeeRepository.deleteEmployeesOfDepartment(MyEnum.none,-1L,-1L,companyId,departmentId,true,false);
        //employeeRepository.deleteEmployeeDepartmentMapping(companyId,departmentId,true,false);
        //companyDepartmentMappingEntity.setActive(false);
        //companyDepartmentMappingRepository.save(companyDepartmentMappingEntity);
        employeeMappingRepository.deleteDepartmentQuery(companyId,departmentId,false,"none",-1L,-1L);
        return new Response(200,SUCCESS);
    }

    public List<EmployeeResponse> getAllEmployeeOfDepartment(Long companyId,Long departmentId) throws NotPresentException {
        List<EmployeeEntity>employeeEntityList= employeeRepository.findAllEmployeeQuery(companyId,departmentId,true);
         //System.out.println(employeeEntityList);
         if(employeeEntityList==null || employeeEntityList.isEmpty())
             throw new NotPresentException(NOT_PRESENT);
        return  Arrays.asList(modelMapper.map(employeeEntityList,EmployeeResponse[].class));
    }


}
