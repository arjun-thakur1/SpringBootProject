package work1.project1.package1.repository;

import work1.project1.package1.entity.DepartmentEmployeeMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentEmployeeMappingRepository extends JpaRepository<DepartmentEmployeeMapping, Long>{

    DepartmentEmployeeMapping findByIdAndIsActive(Long employeeId, boolean b);

    boolean existsByEmployeeIdAndIsActive(Long employeeId, boolean b);

    boolean existsByDepartmentIdAndEmployeeId(Long departmentId, Long employeeId);

    DepartmentEmployeeMapping findByDepartmentIdAndEmployeeId(Long departmentId, Long employeeId);

    boolean existsByIdAndIsActive(Long managerId, boolean b);

    DepartmentEmployeeMapping findByEmployeeIdAndIsActive(Long id, boolean b);

    List<DepartmentEmployeeMapping> findAllByDepartmentIdAndIsActive(Long departmentId, boolean b);

    boolean existsByManagerIdAndIsActive(Long managerId, boolean b);
}
