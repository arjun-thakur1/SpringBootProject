package work1.project1.package1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work1.project1.package1.entity.CompanyDepartmentMappingEntity;
import work1.project1.package1.repository.CompanyDepartmentMappingRepository;

import java.util.List;

@Service
public class CompanyDepartmentMappingService {


    @Autowired
    CompanyDepartmentMappingRepository mappingRepository;

    public boolean add(Long companyId, Long departmentId) {
         CompanyDepartmentMappingEntity mappingEntity=mappingRepository.findByCompanyIdAndDepartmentId(companyId,departmentId);
         if(mappingEntity!=null) {
             if(mappingEntity.isActive()){
                 return false;
             }
             mappingEntity.setActive(true);
             mappingRepository.save(mappingEntity);
             return true;
         }
         mappingRepository.save(new CompanyDepartmentMappingEntity(companyId,departmentId,(long)-1,(long)-1,true));
         return true;
    }

   public List<CompanyDepartmentMappingEntity> getAllDepartmentOfCompany(Long companyId){
        return mappingRepository.findByCompanyIdAndIsActive(companyId,true);
    }


}
