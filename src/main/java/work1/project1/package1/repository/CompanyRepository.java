package work1.project1.package1.repository;

import org.springframework.data.jpa.repository.Query;
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

//somthing in master forn testing purpose
    //some changes in remote master
}
