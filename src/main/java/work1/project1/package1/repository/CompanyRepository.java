package work1.project1.package1.repository;

import work1.project1.package1.entity.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    boolean existsByCompanyName(String companyName);

    CompanyEntity findByIdAndIsActive(long companyId,boolean isActive);

    List<CompanyEntity> findAllByIsActive(boolean isActive);
    Page<CompanyEntity> findAllByIsActive(boolean isActive, Pageable paging);

    CompanyEntity findByCompanyName(String companyName);

    boolean existsByIdAndIsActive(Long companyId, boolean b);

    }
