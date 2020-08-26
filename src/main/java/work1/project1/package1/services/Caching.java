package work1.project1.package1.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import work1.project1.package1.dto.response.DepartmentResponse;
import work1.project1.package1.dto.response.EmployeeCompleteGetResponse;
import work1.project1.package1.dto.response.EmployeeGetResponse;
import work1.project1.package1.dto.response.Response;
import work1.project1.package1.entity.CompanyDepartmentMappingEntity;
import work1.project1.package1.entity.DepartmentEntity;
import work1.project1.package1.entity.EmployeeEntity;
import work1.project1.package1.entity.EmployeeMappingEntity;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.exception.NotPresentException;
import work1.project1.package1.myenum.MyEnum;
import work1.project1.package1.repository.*;

import java.util.*;

import static work1.project1.package1.constants.ApplicationConstants.*;
import static work1.project1.package1.constants.ApplicationConstants.FAILED;

@Component
@CacheConfig(cacheNames={"company_cache"})
public class Caching {
     @Autowired
     CompanyRepository companyRepository;
     @Autowired
     ModelMapper modelMapper;
     @Autowired
     EmployeeRepository employeeRepository;
     @Autowired
     DepartmentRepository departmentRepository;
     @Autowired
     EmployeeMappingService mappingService;
     @Autowired
     EmployeeMappingRepository employeeMappingRepository;
     @Autowired
    CompanyDepartmentMappingRepository companyDepartmentMappingRepository;

    @Cacheable(key = "#id",value = "company_departments")
    public Object getAllDepartmentsOfCompany(Long id) throws CustomException, NotPresentException {
        List<DepartmentEntity>  departmentEntityList= departmentRepository.findAllDepartmentsOfCompany(id,true);
        if(departmentEntityList==null)
            throw new CustomException(FAILED);
        if(departmentEntityList.isEmpty())
            throw new NotPresentException("Departments "+NOT_PRESENT);
        List<DepartmentResponse> departmentResponses= Arrays.asList(modelMapper.map(departmentEntityList,DepartmentResponse[].class));
        return (Object)departmentResponses;
    }

    public void cacheDepartmentDeleteForCompanies(Long departmentId){
        List<CompanyDepartmentMappingEntity> mappingEntityList= companyDepartmentMappingRepository.findByDepartmentId(departmentId);
        Set<Long > companyIds=new HashSet<>();
        mappingEntityList.forEach(m->{
            companyIds.add(m.getCompanyId());
        });
        for( Long companyId : companyIds){
            deleteDepartmentsOfCompany(companyId);
        }
    }
    @CacheEvict(key="#id", value = "company_departments", allEntries = true)
    public void deleteDepartmentsOfCompany(Long id){
        return;
    }


    @Cacheable(key = "#id",value = "employee_cache")
    public Object getEmployeeById(Long id) throws CustomException, NotPresentException {
        Optional<EmployeeEntity> fetchedEmployeeEntity= employeeRepository.findById(id);
        if(fetchedEmployeeEntity!=null && fetchedEmployeeEntity.isPresent()) {
            EmployeeEntity employeeEntity=fetchedEmployeeEntity.get();
            CompanyDepartmentMappingEntity companyDepartmentMappingEntity= mappingService.getIds(id);
            if(companyDepartmentMappingEntity==null)
                return  (Object)(modelMapper.map(employeeEntity, EmployeeGetResponse.class));
            EmployeeCompleteGetResponse completeResponse=new EmployeeCompleteGetResponse();
            return  (Object)(completeResponse.convert(employeeEntity,companyDepartmentMappingEntity.getCompanyId(),
                    companyDepartmentMappingEntity.getDepartmentId()));
        }
        throw new NotPresentException(NOT_PRESENT);
    }

    @CachePut(cacheNames = "employee_cache" , key = "#id")
    public Object updateEmployeeCache(Long id,EmployeeEntity employeeEntity) {
         CompanyDepartmentMappingEntity companyDepartmentMappingEntity= mappingService.getIds(id);
         if(companyDepartmentMappingEntity==null)
             return  (Object)(modelMapper.map(employeeEntity, EmployeeGetResponse.class));
         EmployeeCompleteGetResponse completeResponse=new EmployeeCompleteGetResponse();
         return  (Object)(completeResponse.convert(employeeEntity,companyDepartmentMappingEntity.getCompanyId(),
                 companyDepartmentMappingEntity.getDepartmentId()));
    }


    @CacheEvict(value = "employee_cache",key="#id", allEntries = true)
    public Response deleteEmployeebyId(Long id , Long userId) throws NotPresentException, CustomException {
        EmployeeEntity employeeEntity=employeeRepository.findById(id).orElse(null);
        if(employeeEntity!=null) {
           // EmployeeEntity employeeEntity=fetchedEmployeeEntity.get();
            employeeEntity.setSalary(-1L);
            employeeEntity.setDesignation(MyEnum.NONE);
            employeeEntity.setManagerId(-1L);
            employeeEntity.setUpdatedBy(userId);
            employeeRepository.save(employeeEntity);

            EmployeeMappingEntity employeeMapping= employeeMappingRepository.findByEmployeeIdAndIsActive(id,true);
            if(employeeMapping!=null) {
                employeeMapping.setActive(false);
                employeeMappingRepository.save(employeeMapping);
                return new Response(200,  DELETE_SUCCESS);
            }
            throw new NotPresentException( " Not part of any company-department!! ");
        }
        throw  new NotPresentException(FAILED);
    }


    @Cacheable(value="access_token",key = "#token")
    public String  tokenCaching(String token,String value){

        return token;
    }



}
