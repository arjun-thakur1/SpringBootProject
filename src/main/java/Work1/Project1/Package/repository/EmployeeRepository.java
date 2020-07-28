package Work1.Project1.Package.repository;

import Work1.Project1.Package.entity.EmployeeEntity;
import Work1.Project1.Package.entity.EmployeePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, EmployeePK>{


    public List<EmployeeEntity> findAllByEmployeePKCompanyId(Long companyId);

    //@Query("select me from EmployeeEntity me where me.EmployeePK.CompanyId = ?1 AND me.EmployeePK.DepartmentId=?2")
      public List<EmployeeEntity> findByEmployeePKCompanyIdAndEmployeePKDepartmentIdAndIsActive(Long companyId,Long departmentId,boolean isActive);

   public List<EmployeeEntity> findByEmployeePK(EmployeePK employeePK);
    public List<EmployeeEntity> findByEmployeePKCompanyId(long companyId);

    public void deleteByEmployeePK(EmployeePK employeePK);

    EmployeeEntity findByEmployeePKAndIsActive(EmployeePK employeePK,boolean isActive);
    long countByEmployeePKCompanyIdAndEmployeePKDepartmentId(Long companyId, Long departmentId);

    List<EmployeeEntity> findByEmployeePKCompanyIdAndEmployeePKDepartmentId(Long companyId, Long departmentId);


    boolean existsByPhoneAndIsActive(String phone, boolean b);

    boolean existsByPhone(String phone);

    EmployeeEntity findByPhone(String phone);
}
