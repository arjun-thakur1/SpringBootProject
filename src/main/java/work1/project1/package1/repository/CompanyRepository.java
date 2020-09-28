package work1.project1.package1.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import work1.project1.package1.entity.CompanyDepartmentMappingEntity;
import work1.project1.package1.entity.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import work1.project1.package1.entity.DepartmentEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    boolean existsByCompanyName(String companyName);

    CompanyEntity findByIdAndIsActive(long companyId,boolean isActive);

    Page<CompanyEntity> findAllByIsActive(boolean isActive, Pageable paging);

    CompanyEntity findByCompanyName(String companyName);

    boolean existsByIdAndIsActive(Long companyId, boolean b);

    @Query(nativeQuery = true,value = "select * from company_entity as c where c.id=?1 and c.is_active=?2 ")
    CompanyEntity findQuery(Long id, boolean b);

    @Modifying
    @Transactional
    @Query( nativeQuery = true,value = " BEGIN TRANSACTION; " +
           " UPDATE employee SET designation=?4, manager_id=null, salary=null " +
           " WHERE id in ( SELECT employee_id FROM department_employee_mapping as de  WHERE is_active=?2 AND company_department_id in ( " +
            " (SELECT id FROM company_department_mapping_entity as cd WHERE cd.company_id=?1 AND cd.is_active=?2) )); " +
            " UPDATE department_employee_mapping SET is_active=?3 " +
            " WHERE company_department_id in (SELECT id FROM company_department_mapping_entity as cd WHERE cd.company_id=?1 AND cd.is_active=?2);" +
            " UPDATE company_department_mapping_entity SET is_active =?3  " +
            " WHERE company_department_mapping_entity.company_id=?1 AND company_department_mapping_entity.is_active=?2 ; " +
            " UPDATE company_entity SET is_active=?3 WHERE company_entity.id=?1 ; " +
            " COMMIT; ")
    public void deleteCompanyQuery(Long companyId,boolean truu, boolean faals , String val,Long managerId , Double salary);


    @Modifying
    @Transactional
    @Query( nativeQuery = true,value =" UPDATE employee SET salary=salary+((salary*?3)/100) WHERE id in " +
            "( SELECT employee_id FROM department_employee_mapping WHERE is_active=?2 AND company_department_id in " +
            "  (SELECT id FROM company_department_mapping_entity as cd WHERE cd.company_id=?1 AND cd.is_active=?2) ) ")
    void queryForChangeSalaryOfCompanyByPercentage(Long companyId, boolean b, Double salary_change);


    @Modifying
    @Transactional
    @Query( nativeQuery = true,value =" COMMIT TRANSACTION;" +
            " UPDATE employee SET salary=salary+?3 WHERE id in " +
            "( SELECT employee_id FROM department_employee_mapping WHERE is_active=?2 AND company_department_id in " +
            "  (SELECT id FROM company_department_mapping_entity as cd WHERE cd.company_id=?1 AND cd.is_active=?2) ); " +
            " UPDATE employee SET salary=0 where salary<0 and id in" +
            "( SELECT employee_id FROM department_employee_mapping WHERE is_active=?2 AND company_department_id in " +
            "      (SELECT id FROM company_department_mapping_entity as cd WHERE cd.company_id=?1 AND cd.is_active=?2) )")
    void queryForChangeSalaryOfCompanyByAbsValue(Long companyId, boolean b, Double salary_change);


    @Query(nativeQuery = true,value = "SELECT employee_id FROM department_employee_mapping WHERE is_active=?2 AND company_department_id in " +
            "      (SELECT id FROM company_department_mapping_entity as cd WHERE cd.company_id=?1 AND cd.is_active=?2) " )
    List<Long> findIdsOfAllEmployeesOfCompany(Long companyId, boolean b);
}
