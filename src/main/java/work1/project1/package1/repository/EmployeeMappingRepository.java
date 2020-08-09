package work1.project1.package1.repository;

import org.springframework.data.jpa.repository.Query;
import work1.project1.package1.entity.EmployeeMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeMappingRepository extends JpaRepository<EmployeeMappingEntity, Long>{


    EmployeeMappingEntity findByEmployeeIdAndIsActive(Long employeeId, boolean b);

    EmployeeMappingEntity findByEmployeeIdAndMappingId(Long employeeId, Long companyDepartmentMappingId);

    List<EmployeeMappingEntity> findByMappingIdAndIsActive(Long id, boolean b);


    @Query(nativeQuery = true,value = "SELECT * FROM department_employee_mapping m  INNER JOIN employee e  on " +
            "m.employee_id=e.id")
    public List<EmployeeMappingEntity> FindAllWithDescriptionQuery();

    boolean existsByEmployeeIdAndIsActive(Long employeeId, boolean b);
}

