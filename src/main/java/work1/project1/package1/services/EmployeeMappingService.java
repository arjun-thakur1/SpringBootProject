package work1.project1.package1.services;

import org.springframework.http.HttpStatus;
import work1.project1.package1.entity.CompanyDepartmentMappingEntity;
import work1.project1.package1.entity.EmployeeMappingEntity;
import work1.project1.package1.exception.ResponseHttp;
import work1.project1.package1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Component
@Transactional
public class EmployeeMappingService {

    @Autowired
    EmployeeMappingRepository employeeMappingRepository;
    @Autowired
    CompanyDepartmentMappingRepository companyDepartmentMappingRepository;

    public void add(Long mappingId, Long employeeId) {
        EmployeeMappingEntity employeeMapping=new EmployeeMappingEntity(mappingId,employeeId,-1,-1,true);
        employeeMappingRepository.save(employeeMapping);
    }

    public CompanyDepartmentMappingEntity getIds(Long employeeId) {
        EmployeeMappingEntity employeeMapping= employeeMappingRepository.findByEmployeeIdAndIsActive(employeeId,true);
        if(employeeMapping!=null) {
           return companyDepartmentMappingRepository.findByIdAndIsActive(employeeMapping.getMappingId(),true);
        }
        return null;
    }

    public void updateDetails(Long companyId, Long departmentId, Long employeeId,Long userId) throws ResponseHttp {
       CompanyDepartmentMappingEntity companyDepartmentMappingEntity= companyDepartmentMappingRepository.
               findByCompanyIdAndDepartmentIdAndIsActive(companyId,departmentId,true);
        if(companyDepartmentMappingEntity==null)
            throw new ResponseHttp(HttpStatus.NOT_FOUND," company-department not present!! ");
        Long companyDepartmentMappingId=companyDepartmentMappingEntity.getId();
        EmployeeMappingEntity activeEmployeeMappingEntity= employeeMappingRepository.findByEmployeeIdAndIsActive(employeeId,true);
        if(activeEmployeeMappingEntity!=null)
        {
            throw new ResponseHttp(HttpStatus.BAD_REQUEST," Employee already part of another company-department !! ");
        }
         EmployeeMappingEntity employeeMappingEntity=employeeMappingRepository.findByEmployeeIdAndMappingId(employeeId,
                companyDepartmentMappingId);
        if(employeeMappingEntity!=null) //want to add in same dept
        {
            employeeMappingEntity.setActive(true);
            employeeMappingEntity.setUpdatedBy(userId); //...........
            employeeMappingRepository.save(employeeMappingEntity);
            return ;
        }
        EmployeeMappingEntity newEmployeeMappingEntity=new EmployeeMappingEntity(companyDepartmentMappingId,employeeId,-1,-1,true);
        employeeMappingRepository.save(newEmployeeMappingEntity);
        return;
    }

}



