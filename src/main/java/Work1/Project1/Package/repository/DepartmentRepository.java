package Work1.Project1.Package.repository;

import Work1.Project1.Package.entity.DepartmentEntity;
import Work1.Project1.Package.entity.DepartmentPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, DepartmentPK> {
   // public List<DepartmentEntity> findByCompanyId(String companyId);
    public List<DepartmentEntity> findAllByDepartmentPKCompanyId(Long companyId);

        public void deleteByDepartmentPK(DepartmentPK departmentPK);

   long countByDepartmentPKCompanyId(Long companyId);

    Optional<DepartmentEntity> findByDepartmentPKAndIsActive(DepartmentPK departmentPK, boolean b);

    boolean existsByDepartmentPKCompanyIdAndDepartmentName(long companyId, String departmentName);

    DepartmentEntity findByDepartmentPKCompanyIdAndDepartmentName(long companyId, String departmentName);

    List<DepartmentEntity> findAllByDepartmentPKCompanyIdAndIsActive(long companyId, boolean b);

    boolean existsByDepartmentPKAndIsActive(DepartmentPK departmentPK, boolean b);

    Optional<DepartmentEntity> findByDepartmentPKAndManagerIdAndIsActive(DepartmentPK departmentPK, long employeeId, boolean b);
}
