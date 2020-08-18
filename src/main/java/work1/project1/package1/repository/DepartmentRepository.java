package work1.project1.package1.repository;

import org.springframework.data.jpa.repository.Query;
import work1.project1.package1.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {


    boolean existsByDepartmentName(String departmentName);


    DepartmentEntity findByDepartmentName(String departmentName);

    @Query(nativeQuery = true,value ="select * from department_entity as d where  d.id in" +
            " (select cd.department_id from company_department_mapping_entity as cd where cd.company_id=?1 and cd.is_active=?2)")
    List<DepartmentEntity> findAllDepartmentsOfCompany(Long companyId,boolean b);
}
