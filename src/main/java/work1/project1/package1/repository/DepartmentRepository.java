package work1.project1.package1.repository;

import work1.project1.package1.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {


    boolean existsByDepartmentName(String departmentName);

    Optional<DepartmentEntity> findByIdAndIsActive(Long departmentId, boolean b);

    DepartmentEntity findByDepartmentName(String departmentName);
}
