package Work1.Project1.Package.repository;

import Work1.Project1.Package.entity.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository   //so that this is scanned during classpath
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    boolean existsByCompanyName(String companyName);
    CompanyEntity findByCompanyIdAndIsActive(long companyId,boolean isActive);

    List<CompanyEntity> findAllByIsActive(boolean isActive);
    Page<CompanyEntity> findAllByIsActive(boolean isActive, Pageable paging);

    CompanyEntity findByCompanyName(String companyName);
}
