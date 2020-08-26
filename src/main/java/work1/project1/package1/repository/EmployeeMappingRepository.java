package work1.project1.package1.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import work1.project1.package1.entity.EmployeeMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import work1.project1.package1.myenum.MyEnum;

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
    @Transactional
    @Query(nativeQuery = true,value = " BEGIN TRANSACTION; " +
            " UPDATE employee SET designation=?4, manager_id=?5, salary=?6 " +
            " WHERE id in ( SELECT employee_id FROM department_employee_mapping as de " +
            " WHERE company_department_id in (SELECT id FROM company_department_mapping_entity as cd WHERE cd.company_id=?1 AND cd.department_id=?2) ); " +
            " UPDATE department_employee_mapping SET is_active=?3 " +
            " WHERE company_department_id in (SELECT id FROM company_department_mapping_entity as cd WHERE cd.company_id=?1 AND cd.department_id=?2); " +
            " UPDATE company_department_mapping_entity SET is_active =?3  " +
            " WHERE company_department_mapping_entity.company_id=?1 AND company_department_mapping_entity.department_id=?2 ; " +
            " COMMIT; ")
    public void deleteDepartmentQuery(Long companyId  , Long departmentId , boolean faalse, String val,Long managerId,Long salary);

  //  @Query(nativeQuery = true,value = "(SELECT id FROM company_department_mapping_entity as cd WHERE cd.company_id=?1 AND cd.department_id=?2)" )
   // public List<Long> deleteDepartmentQuery(Long companyId  ,Long departmentId , boolean faalse);

}

