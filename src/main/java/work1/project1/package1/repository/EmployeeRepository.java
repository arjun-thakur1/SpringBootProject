package work1.project1.package1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import work1.project1.package1.entity.EmployeeEntity;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {


    boolean existsByPhone(String phone);

    boolean existsById(Long managerId);
}
