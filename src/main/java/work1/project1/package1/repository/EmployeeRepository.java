package work1.project1.package1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import work1.project1.package1.dto.response.EmployeeCompleteGetResponse;
import work1.project1.package1.entity.EmployeeEntity;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {


    boolean existsByPhone(String phone);

    boolean existsById(Long managerId);

    @Modifying
    @Query(nativeQuery = true, value = "update department_employee_mapping set is_active=?4 from department_employee_mapping as de where " +
                     " de.company_department_id in  ( select cd.id from company_department_mapping_entity as cd " +
            " where cd.company_id=?1 and cd.department_id=?2 and cd.is_active=?3 ) " )
     void deleteEmployeeDepartmentMapping(Long companyId, Long departmentId,boolean tru,boolean fal);


    @Modifying
    @Query(nativeQuery = true, value = "update employee set  designation=?1 , manager_id=?2 , salary=?3 " +
            " from employee as e  where e.id in (  select de.employee_id from department_employee_mapping as de where de.is_active=?6 and de.company_department_id in " +
            " ( select cd.id from company_department_mapping_entity as cd " +
            " where cd.company_id=?4 and cd.department_id=?5 and cd.is_active=?6 ) )" )
    void deleteEmployeesOfDepartment(String NONE,Long managerId,Long salary,Long companyId, Long departmentId,boolean tru,boolean fal);



    @Query(nativeQuery = true, value = " select * from employee as e where e.id in " +
            " ( select de.employee_id from department_employee_mapping as de where de.is_active=?3 " +
            " and de.company_department_id in  ( select cd.id from company_department_mapping_entity as cd " +
            "where cd.company_id=?1 and cd.department_id=?2 and cd.is_active=?3 ) )")
    List<EmployeeEntity> findAllEmployeeQuery(Long companyId, Long departmentId,boolean b);

}
