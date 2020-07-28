package Work1.Project1.Package.services;

import Work1.Project1.Package.converter.EmployeeEntityListToResponseEmpList;
import Work1.Project1.Package.entity.*;
import Work1.Project1.Package.exception.NotFoundException;
import Work1.Project1.Package.repository.CompanyRepository;
import Work1.Project1.Package.repository.DepartmentRepository;
import Work1.Project1.Package.response.ResponseEmployee;
import Work1.Project1.Package.request.RequestEmployee;
import Work1.Project1.Package.repository.EmployeeRepository;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static Work1.Project1.Package.constants.ApplicationConstants.*;

@Service
@CacheConfig(cacheNames={"employee_cache"})
@Component
@Transactional
public class EmployeeServices  {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    EmployeeEntityListToResponseEmpList employeeEntityListToResponseEmpList;

    public Object getAllDetails(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<CompanyEntity> allCompanyEntity= companyRepository.findAllByIsActive(true,paging);
        HashMap<Long,HashMap <Long,List<ResponseEmployee>>> companyToDeptToEmpMapp= new HashMap<Long,HashMap<Long,List<ResponseEmployee>>>();  //l.getCompanyId()>
        allCompanyEntity.forEach((c)->{
            List<DepartmentEntity> allDepartmentEntities= departmentRepository.findAllByDepartmentPKCompanyIdAndIsActive(c.getCompanyId(),true);
            HashMap <Long,List<ResponseEmployee>> deptToEmployeemap=new HashMap<Long,List<ResponseEmployee>>();
            allDepartmentEntities.forEach((d)-> {
                List<EmployeeEntity> employeeEntities = employeeRepository.findByEmployeePKCompanyIdAndEmployeePKDepartmentIdAndIsActive(c.getCompanyId(), d.getDepartmentPK().getDepartmentId(),true);
                List<ResponseEmployee> responseEmployeeEntities = employeeEntityListToResponseEmpList.convert(employeeEntities);
                deptToEmployeemap.put(d.departmentPK.getDepartmentId(), responseEmployeeEntities);
            });
            companyToDeptToEmpMapp.put(c.getCompanyId(),deptToEmployeemap);
        } );
        if(companyToDeptToEmpMapp.isEmpty())
            return null;
        return  companyToDeptToEmpMapp;
    }

    @Cacheable(value = "employee_cache")
    public Object getEmployeeDetails(EmployeePK employeePK) {

            Optional<EmployeeEntity> employeeEntity= Optional.ofNullable(employeeRepository.findByEmployeePKAndIsActive(employeePK, true));    //HttpStatus.OK
           if(employeeEntity.isPresent()){
            EmployeeEntity employeeEntity1= employeeEntity.get();
            return( new ResponseEmployee(employeeEntity1.getEmployeePK().getCompanyId(),employeeEntity1.getEmployeePK().getDepartmentId(),
                    employeeEntity1.getEmployeePK().getEmployeeId(),employeeEntity1.getEmpName(),employeeEntity1.getPhone(),employeeEntity1.getSalary()) );
       }
        else {
            return null;
        }
}

public String addEmployee(RequestEmployee requestEmployee) {
        long companyId = requestEmployee.getCompanyId();
        long departmentId = requestEmployee.getDepartmentId();
        DepartmentPK departmentPK = new DepartmentPK(companyId, departmentId);
        String phone=requestEmployee.getPhone();
         if(employeeRepository.existsByPhone(phone))  //AndIsActive(requestEmployee.getPhone(),true))
         {
            EmployeeEntity employeeEntity= employeeRepository.findByPhone(phone);
            if(employeeEntity.getIsActive())
             return Already_Present;
            else
            {
                employeeEntity.setActive(true);
                employeeRepository.save(employeeEntity);
                return  "Employee With Id " + employeeEntity.getEmployeePK().getEmployeeId() + Add_Success;
            }
         }
        if (departmentRepository.existsByDepartmentPKAndIsActive(departmentPK,true)) {
           long employeeId = employeeRepository.countByEmployeePKCompanyIdAndEmployeePKDepartmentId(companyId, departmentId);
            EmployeePK employeePK = new EmployeePK(companyId, departmentId, employeeId);//employeeEntity.getEmployeePK();
            EmployeeEntity employeeEntity = new EmployeeEntity(employeePK, requestEmployee.getEmpName(), requestEmployee.getPhone(),
                    requestEmployee.getSalary(), true);
            // DepartmentPK departmentPK=new DepartmentPK(requestEmployeeEntity.getCompanyId(),)
            if (!employeeRepository.existsById(employeePK)) {
                this.employeeRepository.saveAndFlush(employeeEntity);
                return "Employee With Id " + employeeEntity.getEmployeePK().getEmployeeId() + Add_Success;
            }
            else {
                return Add_Failed;
            }
      }
        return Add_Failed;
    }

    @CacheEvict(value = "employee_cache", allEntries = true)
    public String deleteEmployeeDetails(EmployeePK employeePK) throws Exception {
         try {
                Optional<EmployeeEntity> employeeEntity= Optional.ofNullable(employeeRepository.findByEmployeePKAndIsActive(employeePK, true));
                if(employeeEntity.isPresent()) {
                    EmployeeEntity employeeEntity1 = employeeEntity.get();
                    employeeEntity1.setActive(false);
                 employeeRepository.save(employeeEntity1);
          //   this.employeeRepository.deleteByEmployeePK(employeePK);
                    return Delete_Success;
               }
            }catch(Exception e)
            {
                return Failed;
            }
     return Failed;
    }

    public List<EmployeeEntity> getAllEmployeeOfDepartment(DepartmentPK departmentPK) {
        long companyId = departmentPK.getCompanyId();
        long departmentId = departmentPK.getDepartmentId();
        return employeeRepository.findByEmployeePKCompanyIdAndEmployeePKDepartmentIdAndIsActive(companyId, departmentId,true);
    }

    @CachePut(value = "employee_cache")
    public String updateDetails(long  employeeId,long departmentId,long companyId,String  phone,String empName,long salary) { //throws NotFoundException {
        EmployeePK employeePK = new EmployeePK(companyId, departmentId, employeeId);
        Optional<EmployeeEntity> fetchedEmployeeEntity = Optional.ofNullable(employeeRepository.findByEmployeePKAndIsActive
                (employeePK, true));
        if(fetchedEmployeeEntity==null) {
          //   throw   new NotFoundException();
             return Failed;
         }
        if (fetchedEmployeeEntity.isPresent()) {
            EmployeeEntity employeeEntity = fetchedEmployeeEntity.get();
            if (empName != null) {
                employeeEntity.setEmpName(empName);
            }
            if (phone != null) {
                employeeEntity.setPhone(phone);
                if (salary != 0) {
                    employeeEntity.setSalary(salary);
                }
            }
                this.employeeRepository.save(employeeEntity);
                return Update_Success;

        }
        return Failed;
}




























    public String updateSalaryByAbsoluteValue(Long companyId, Long departmentId, Long employeeId, Long increment) {
        if (departmentId == -1) {
            //increment for all emp in the org
           return updateAllCompanyEmployeeSalary(companyId, increment);
        } else if (employeeId == -1) {
            //inc for all emp in dept in particular org
           return updateCompanyAllDepartmentEmployeeSalary(companyId, departmentId, increment);
        } else {
           return updateEmployeeSalary(companyId, departmentId, employeeId, increment);
        }
    }

    public String updateSalaryByPercentage(long companyId, long departmentId, long employeeId, long increment) {
        if (departmentId == -1) {
            //increment for all emp in the org
           return updateAllCompanyEmployeeSalaryByPercentage(companyId, increment);
        } else if (employeeId == -1) {
            //inc for all emp in dept in particular org
            return updateAllDepartmentEmployeeSalarybyPercentage(companyId, departmentId, increment);
        } else {
           return updateEmployeeSalaryByPercentage(companyId, departmentId, employeeId, increment);
        }
    }

    public String updateAllCompanyEmployeeSalary(Long companyId, Long increment) {
        List<EmployeeEntity> employeeEntityList = employeeRepository.findAllByEmployeePKCompanyId(companyId);
        if(employeeEntityList.isEmpty())
            return Failed;
        employeeEntityList.forEach((l) -> {
            l.setSalary(l.getSalary() + increment);
            updateDetails(l.getEmployeePK().getCompanyId(),l.getEmployeePK().getDepartmentId(),l.getEmployeePK().getCompanyId(),
                    l.getEmpName(),l.getPhone(),l.getSalary());
        });
        return Update_Success;
    }

    public String updateCompanyAllDepartmentEmployeeSalary(Long companyId, Long departmentId, Long increment) {

        List<EmployeeEntity> employeeEntityList = employeeRepository.findByEmployeePKCompanyIdAndEmployeePKDepartmentId(companyId, departmentId);
         if(employeeEntityList.isEmpty())
             return Failed;
        employeeEntityList.forEach((l) -> {
            // int salary=l.getSalary();
            l.setSalary(l.getSalary() + increment);
            updateDetails(l.getEmployeePK().getCompanyId(),l.getEmployeePK().getDepartmentId(),l.getEmployeePK().getCompanyId(),
                    l.getEmpName(),l.getPhone(),l.getSalary());
        });
        return Update_Success;
    }


    public String updateEmployeeSalary(Long companyId, Long departmentId, Long employeeId, Long increment) {
        EmployeePK employeePK = new EmployeePK(companyId,departmentId,employeeId);
        List<EmployeeEntity> employeeEntityList = employeeRepository.findByEmployeePK(employeePK);
        System.out.println(employeeEntityList);
        if(employeeEntityList.isEmpty())
            return Failed;
        employeeEntityList.forEach((l) -> {
            l.setSalary(l.getSalary() + increment);
            updateDetails(l.getEmployeePK().getCompanyId(),l.getEmployeePK().getDepartmentId(),l.getEmployeePK().getCompanyId(),
                    l.getEmpName(),l.getPhone(),l.getSalary());
        });
        return Update_Success;
    }


    public String updateAllCompanyEmployeeSalaryByPercentage(long companyId, long increment) {
        List<EmployeeEntity> employeeEntityList = employeeRepository.findAllByEmployeePKCompanyId(companyId);
        if(employeeEntityList.isEmpty())
            return Failed;
        employeeEntityList.forEach((l) -> {
            Long salary = l.getSalary();
            l.setSalary(salary + ((increment * salary) / 100));
            updateDetails(l.getEmployeePK().getCompanyId(),l.getEmployeePK().getDepartmentId(),l.getEmployeePK().getCompanyId(),
                    l.getEmpName(),l.getPhone(),l.getSalary());
        });
        return Update_Success;
    }

    public String updateAllDepartmentEmployeeSalarybyPercentage(long companyId, long departmentId, long increment) {
        List<EmployeeEntity> employeeEntityList = employeeRepository.findByEmployeePKCompanyIdAndEmployeePKDepartmentId(companyId, departmentId);
        if(employeeEntityList.isEmpty())
            return Failed;
        employeeEntityList.forEach((l) -> {
            long salary = l.getSalary();
            l.setSalary(salary + ((increment * salary) / 100));
            updateDetails(l.getEmployeePK().getCompanyId(),l.getEmployeePK().getDepartmentId(),l.getEmployeePK().getCompanyId(),
                    l.getEmpName(),l.getPhone(),l.getSalary());
        });
        return Update_Success;
    }

    public String updateEmployeeSalaryByPercentage(long companyId, long departmentId, long employeeId, long increment) {
        EmployeePK employeePK = new EmployeePK(companyId,departmentId,employeeId);
        List<EmployeeEntity> employeeEntityList = employeeRepository.findByEmployeePK(employeePK);
        if(employeeEntityList.isEmpty())
            return Failed;
        employeeEntityList.forEach((l) -> {
            long salary = l.getSalary();
            l.setSalary(salary + ((increment * salary) / 100));
            updateDetails(l.getEmployeePK().getCompanyId(),l.getEmployeePK().getDepartmentId(),l.getEmployeePK().getCompanyId(),
                    l.getEmpName(),l.getPhone(),l.getSalary());
        });
        return Update_Success;
    }
}



