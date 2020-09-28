package work1.project1.package1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import work1.project1.package1.entity.CompanyDepartmentMappingEntity;

import java.util.List;

@Repository
public interface CompanyDepartmentMappingRepository extends JpaRepository<CompanyDepartmentMappingEntity,Long> {

    boolean existsByCompanyIdAndDepartmentIdAndIsActive(Long companyId, Long departmentId, boolean b);

    CompanyDepartmentMappingEntity findByCompanyIdAndDepartmentId(Long companyId, Long departmentId);

    List<CompanyDepartmentMappingEntity> findAllByCompanyIdAndIsActive(Long companyId, boolean b);

    CompanyDepartmentMappingEntity findByCompanyIdAndDepartmentIdAndIsActive(Long companyId, Long departmentId, boolean b);

    CompanyDepartmentMappingEntity findByIdAndIsActive(Long mappingId, boolean b);

    List<CompanyDepartmentMappingEntity> findByDepartmentId(Long departmentId);

    @Query(nativeQuery = true , value = "select * from company_department_mapping_entity as cd where is_active=?2 and id in" +
            " (select company_department_id from department_employee_mapping where employee_id=?1 and is_active=?2 )")
    CompanyDepartmentMappingEntity findCompanyDepartmentQuery(Long userId, boolean b);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = " UPDATE employee SET salary=salary+((salary*?4)/100) where id in " +
            " (SELECT employee_id FROM department_employee_mapping as de  WHERE is_active=?3 AND company_department_id in " +
            " (SELECT id FROM company_department_mapping_entity WHERE company_id=?1 and department_id=?2 and is_active=?3) ) " )
    void queryForChangeSalaryOfDepartmentOfCompanyByPercentage(Long companyId,Long departmentId,boolean truu,
                                                               Double percentageForSalaryChange);


    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "BEGIN TRANSACTION; " +
            "UPDATE employee SET salary=salary+?4 where id in " +
            " (SELECT employee_id FROM department_employee_mapping as de  WHERE is_active=?3 AND company_department_id in " +
            " (SELECT id FROM company_department_mapping_entity WHERE company_id=?1 and department_id=?2 and is_active=?3) );" +
            "UPDATE employee SET salary=0 where salary<0 and id in " +
            " (SELECT employee_id FROM department_employee_mapping as de  WHERE is_active=?3 AND company_department_id in " +
            " (SELECT id FROM company_department_mapping_entity WHERE company_id=?1 and department_id=?2 and is_active=?3) );" +
            " COMMIT TRANSACTION; ")
    void queryForChangeSalaryOfDepartmentOfCompanyByAbsValue(Long companyId,Long departmentId,boolean b,Double salary_change);

    @Query(nativeQuery = true,value = " SELECT  company_id  FROM company_department_mapping_entity WHERE department_id=?1 AND is_active=?2 ")
    List<Long> findAllCompaniesThatHasDepartment(Long departmentId , boolean truu);
}
