package work1.project1.package1.repository;

import org.springframework.data.jpa.repository.Modifying;
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



    @Modifying
    @Query(nativeQuery = true,value = " BEGIN TRANSACTION; " +
            " UPDATE company_department_mapping_entity SET is_active =?3  " +
            " WHERE company_department_mapping_entity.company_id=?1 AND company_department_mapping_entity.department_id=?2 ; " +
            "COMMIT; ")
    public void deleteDepartmentQuery(Long companyId  ,Long departmentId , boolean faalse);
}

