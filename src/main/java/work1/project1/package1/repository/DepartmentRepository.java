package work1.project1.package1.repository;

import work1.project1.package1.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

    Optional<DepartmentEntity> findByIdAndIsActive(Long departmentId, boolean b);

    boolean existsByCompanyIdAndDepartmentName(long companyId, String departmentName);

    DepartmentEntity findByCompanyIdAndDepartmentName(long companyId, String departmentName);

    List<DepartmentEntity> findAllByCompanyId(Long companyId);


    List<DepartmentEntity> findAllByCompanyIdAndIsActive(long companyId, boolean b);

    boolean existsByIdAndIsActive(Long departmentId, boolean b);
}
