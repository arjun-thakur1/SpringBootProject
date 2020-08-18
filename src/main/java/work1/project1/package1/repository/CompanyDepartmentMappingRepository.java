package work1.project1.package1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import work1.project1.package1.entity.CompanyDepartmentMappingEntity;

import java.util.List;

@Repository
public interface CompanyDepartmentMappingRepository extends JpaRepository<CompanyDepartmentMappingEntity,Long> {

    boolean existsByCompanyIdAndDepartmentIdAndIsActive(Long companyId, Long departmentId, boolean b);

    CompanyDepartmentMappingEntity findByCompanyIdAndDepartmentId(Long companyId, Long departmentId);

    List<CompanyDepartmentMappingEntity> findAllByCompanyIdAndIsActive(Long companyId, boolean b);

    CompanyDepartmentMappingEntity findByCompanyIdAndDepartmentIdAndIsActive(Long companyId, Long departmentId, boolean b);

    CompanyDepartmentMappingEntity findByIdAndIsActive(Long mappingId, boolean b);

}
