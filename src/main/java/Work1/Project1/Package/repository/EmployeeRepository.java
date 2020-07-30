package Work1.Project1.Package.repository;

import Work1.Project1.Package.entity.CompanyEntity;
import Work1.Project1.Package.entity.EmployeeEntity;
import Work1.Project1.Package.entity.EmployeePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, EmployeePK>{


    public List<EmployeeEntity> findAllByEmployeePKCompanyId(Long companyId);

    //@Query("select me from EmployeeEntity me where me.EmployeePK.CompanyId = ?1 AND me.EmployeePK.DepartmentId=?2")
      public Optional<List<EmployeeEntity>> findByEmployeePKCompanyIdAndEmployeePKDepartmentIdAndIsActive(Long companyId, Long departmentId, boolean isActive);

   public List<EmployeeEntity> findByEmployeePK(EmployeePK employeePK);
    public List<EmployeeEntity> findByEmployeePKCompanyId(long companyId);

    public void deleteByEmployeePK(EmployeePK employeePK);

    EmployeeEntity findByEmployeePKAndIsActive(EmployeePK employeePK,boolean isActive);
    long countByEmployeePKCompanyIdAndEmployeePKDepartmentId(Long companyId, Long departmentId);

    List<EmployeeEntity> findByEmployeePKCompanyIdAndEmployeePKDepartmentId(Long companyId, Long departmentId);


    boolean existsByPhoneAndIsActive(String phone, boolean b);

    boolean existsByPhone(String phone);

    EmployeeEntity findByPhone(String phone);

    List<EmployeeEntity> findAllByEmployeePKCompanyIdAndEmployeePKDepartmentId(Long companyId, Long departmentId);

    boolean existsByPhoneAndEmployeePKCompanyIdAndEmployeePKDepartmentId(String phone, long companyId, long departmentId);

    EmployeeEntity findByPhoneAndEmployeePKCompanyIdAndEmployeePKDepartmentId(String phone, long companyId, long departmentId);
}
