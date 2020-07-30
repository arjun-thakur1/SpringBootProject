package Work1.Project1.Package.services;

import Work1.Project1.Package.converter.EmployeeEntityListToResponseEmpList;
import Work1.Project1.Package.entity.*;
import Work1.Project1.Package.exception.CustomException;
import Work1.Project1.Package.repository.CompanyRepository;
import Work1.Project1.Package.repository.DepartmentRepository;
import Work1.Project1.Package.response.Response;
import Work1.Project1.Package.response.ResponseEmployee;
import Work1.Project1.Package.request.RequestEmployee;
import Work1.Project1.Package.repository.EmployeeRepository;
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
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static Work1.Project1.Package.constants.ApplicationConstants.*;

@Service
@Component
@Transactional   //( Propagation.REQUIRES_NEW) //for cache put update in redis
public class EmployeeServices  {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    EmployeeEntityListToResponseEmpList employeeEntityListToResponseEmpList;

    public Object getAllDetails(Integer pageNo, Integer pageSize){
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<CompanyEntity> allCompanyEntity= companyRepository.findAllByIsActive(true,paging);

        HashMap<Long,HashMap <Long,List<ResponseEmployee>>> companyToDeptToEmpMapp= new HashMap<Long,HashMap<Long,List<ResponseEmployee>>>();  //l.getCompanyId()>
        allCompanyEntity.forEach((c)->{
            List<DepartmentEntity> allDepartmentEntities= departmentRepository.findAllByDepartmentPKCompanyIdAndIsActive(c.getCompanyId(),true);
            HashMap <Long,List<ResponseEmployee>> deptToEmployeemap=new HashMap<Long,List<ResponseEmployee>>();



            allDepartmentEntities.forEach((d)-> {
                Optional<List<EmployeeEntity>> employeeEntities = employeeRepository.findByEmployeePKCompanyIdAndEmployeePKDepartmentIdAndIsActive(c.getCompanyId(), d.getDepartmentPK().getDepartmentId(),true);
                List<EmployeeEntity>employeeEntityList=new ArrayList<>();
              if(employeeEntities.isPresent())
                 employeeEntityList=employeeEntities.get();
                List<ResponseEmployee> responseEmployeeEntities = employeeEntityListToResponseEmpList.convert(employeeEntityList);
                deptToEmployeemap.put(d.departmentPK.getDepartmentId(), responseEmployeeEntities);
            });



            companyToDeptToEmpMapp.put(c.getCompanyId(),deptToEmployeemap);
        } );
        if(companyToDeptToEmpMapp.isEmpty())
            return new Response(204,Not_Present);
        return  companyToDeptToEmpMapp;
    }

  //  @Cacheable(value = "employee_cache",key="#employeePK")
    public Object getEmployeeDetails(EmployeePK employeePK) {
           Optional<EmployeeEntity> employeeEntity= Optional.ofNullable(employeeRepository.findByEmployeePKAndIsActive(employeePK, true));    //HttpStatus.OK
           if(employeeEntity.isPresent()){
              EmployeeEntity employeeEntity1= employeeEntity.get();
            return( new ResponseEmployee(employeeEntity1.getEmployeePK().getCompanyId(),employeeEntity1.getEmployeePK().getDepartmentId(),
                    employeeEntity1.getEmployeePK().getEmployeeId(),employeeEntity1.getEmpName(),employeeEntity1.getPhone(),employeeEntity1.getSalary()) );
            }
         return new Response(204,Not_Present);
}

public Response addEmployee(RequestEmployee requestEmployee) throws CustomException {
        if(requestEmployee.getSalary()==null)
            throw new CustomException(Failed);

        long companyId = requestEmployee.getCompanyId();
        long departmentId = requestEmployee.getDepartmentId();

        DepartmentPK departmentPK = new DepartmentPK(companyId, departmentId);
        if (! departmentRepository.existsByDepartmentPKAndIsActive(departmentPK,true)) { //dept not present
              return new Response(404,Add_Failed);
        }

        String phone=requestEmployee.getPhone();
        if(employeeRepository.existsByPhoneAndIsActive(phone,true))  //AndIsActive(requestEmployee.getPhone(),true))
         {
              return new Response(404, Failed);
         }
         if(employeeRepository.existsByPhoneAndIsActive(phone,false))  //if employee is not active
          {    //check for,if want same company join....
                boolean isPresentInSameOrg= employeeRepository.existsByPhoneAndEmployeePKCompanyIdAndEmployeePKDepartmentId
                                                                (phone,companyId,departmentId);
                if(isPresentInSameOrg) {
                    EmployeeEntity employeeEntity=employeeRepository.findByPhoneAndEmployeePKCompanyIdAndEmployeePKDepartmentId
                    (phone,companyId,departmentId);
                    employeeEntity.setActive(true);    //want to add in same org
                    employeeEntity.setSalary(requestEmployee.getSalary());
                    employeeRepository.save(employeeEntity);
                    return new Response(201, "Employee With Id " + employeeEntity.getEmployeePK().getEmployeeId() + Add_Success);
                }
          }

           //id generation
           long employeeId = employeeRepository.countByEmployeePKCompanyIdAndEmployeePKDepartmentId(companyId, departmentId);
            EmployeePK employeePK = new EmployeePK(companyId, departmentId, employeeId);//employeeEntity.getEmployeePK();
            EmployeeEntity employeeEntity = new EmployeeEntity(employeePK, requestEmployee.getEmpName(), requestEmployee.getPhone(),
                    requestEmployee.getSalary(), true);
             this.employeeRepository.saveAndFlush(employeeEntity);
                return new Response(201,"Employee With Id " + employeeEntity.getEmployeePK().getEmployeeId() + Add_Success);

    }

  //  @CacheEvict(value = "employee_cache",key="#employeePK", allEntries = true)
    public Response deleteEmployeeDetails(long companyId,long departmentId,long employeeId) throws Exception {
        EmployeePK employeePK = new EmployeePK( companyId,departmentId,employeeId);
         try {
                Optional<EmployeeEntity> employeeEntity= Optional.ofNullable(employeeRepository.findByEmployeePKAndIsActive(employeePK, true));
                if(employeeEntity.isPresent()) {
                    EmployeeEntity employeeEntity1 = employeeEntity.get();
                 //if employee is manager then also set manager id -1, no manager of dept exits..
                    DepartmentPK departmentPK= new DepartmentPK(companyId,departmentId);
                     Optional<DepartmentEntity> departmentEntity=departmentRepository.findByDepartmentPKAndManagerIdAndIsActive(departmentPK,employeeId,true);
                       if(departmentEntity.isPresent())
                       {
                           DepartmentEntity departmentEntity1=departmentEntity.get();
                           departmentEntity1.setManagerId(-1);
                           departmentRepository.save(departmentEntity1);
                       }

                    employeeEntity1.setActive(false);
                 employeeRepository.save(employeeEntity1);
          //   this.employeeRepository.deleteByEmployeePK(employeePK);
                    return new Response(404,Delete_Success);
               }
            }catch(Exception e)
            {
                return new Response(404, Failed);
            }
         return new Response(404, Failed);
    }

    //called from department controller
    public Object getAllEmployeeOfDepartment(DepartmentPK departmentPK) {
        long companyId = departmentPK.getCompanyId();
        long departmentId = departmentPK.getDepartmentId();
        Optional<List<EmployeeEntity>>employeeEntityList= employeeRepository.findByEmployeePKCompanyIdAndEmployeePKDepartmentIdAndIsActive(companyId, departmentId,true);
        if(employeeEntityList.isPresent())
            return employeeEntityList;
        else
            return new Response(204,Not_Present);
    }

  //  @CachePut(value = "employee_cache",key="#employeePK")//,key = "#employeePK")
    public Response updateDetails(long  employeeId,long departmentId,long companyId,String  phone,String empName,long salary) { //throws NotFoundException {
        EmployeePK employeePK = new EmployeePK(companyId, departmentId, employeeId);
        Optional<EmployeeEntity> fetchedEmployeeEntity = Optional.ofNullable(employeeRepository.findByEmployeePKAndIsActive
                (employeePK, true));
        if(fetchedEmployeeEntity==null) {
             return new Response(404 , Failed);
         }
        if (fetchedEmployeeEntity.isPresent()) {
               EmployeeEntity employeeEntity = fetchedEmployeeEntity.get();
               if(!empName.equals("o"))
                employeeEntity.setEmpName(empName);
               if (!phone.equals("o")) {
                   employeeEntity.setPhone(phone);
               }
               if (salary!=0)
                 employeeEntity.setSalary(salary);
               this.employeeRepository.save(employeeEntity);
           return new Response(200 , Update_Success);
        }
       return new Response(404 , Failed);
    }





    public Response updateSalaryByAbsoluteValue(Long companyId, Long departmentId, Long employeeId, Long increment) {
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

    public Response updateSalaryByPercentage(long companyId, long departmentId, long employeeId, long increment) {
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

    public Response updateAllCompanyEmployeeSalary(Long companyId, Long increment) {
        List<EmployeeEntity> employeeEntityList = employeeRepository.findAllByEmployeePKCompanyId(companyId);
        if(employeeEntityList.isEmpty())
            return new Response(404 , Failed);
        employeeEntityList.forEach((l) -> {
            l.setSalary(l.getSalary() + increment);
            updateDetails(l.getEmployeePK().getCompanyId(),l.getEmployeePK().getDepartmentId(),l.getEmployeePK().getCompanyId(),
                    l.getEmpName(),l.getPhone(),l.getSalary());
        });
        return new Response(200 , Update_Success);
    }

    public Response updateCompanyAllDepartmentEmployeeSalary(Long companyId, Long departmentId, Long increment) {

        List<EmployeeEntity> employeeEntityList = employeeRepository.findByEmployeePKCompanyIdAndEmployeePKDepartmentId(companyId, departmentId);
         if(employeeEntityList.isEmpty())
             return new Response(404 , Failed);
        employeeEntityList.forEach((l) -> {
            // int salary=l.getSalary();
            l.setSalary(l.getSalary() + increment);
            updateDetails(l.getEmployeePK().getCompanyId(),l.getEmployeePK().getDepartmentId(),l.getEmployeePK().getCompanyId(),
                    l.getEmpName(),l.getPhone(),l.getSalary());
        });
        return new Response(200 , Update_Success);
    }


    public Response updateEmployeeSalary(Long companyId, Long departmentId, Long employeeId, Long increment) {
        EmployeePK employeePK = new EmployeePK(companyId,departmentId,employeeId);
        List<EmployeeEntity> employeeEntityList = employeeRepository.findByEmployeePK(employeePK);
        System.out.println(employeeEntityList);
        if(employeeEntityList.isEmpty())
            return new Response(404 , Failed);
        employeeEntityList.forEach((l) -> {
            l.setSalary(l.getSalary() + increment);
            updateDetails(l.getEmployeePK().getCompanyId(),l.getEmployeePK().getDepartmentId(),l.getEmployeePK().getCompanyId(),
                    l.getEmpName(),l.getPhone(),l.getSalary());
        });
        return new Response(200 , Update_Success);
    }


    public Response updateAllCompanyEmployeeSalaryByPercentage(long companyId, long increment) {
        List<EmployeeEntity> employeeEntityList = employeeRepository.findAllByEmployeePKCompanyId(companyId);
        if(employeeEntityList.isEmpty())
            return new Response(404 , Failed);
        employeeEntityList.forEach((l) -> {
            Long salary = l.getSalary();
            l.setSalary(salary + ((increment * salary) / 100));
            updateDetails(l.getEmployeePK().getCompanyId(),l.getEmployeePK().getDepartmentId(),l.getEmployeePK().getCompanyId(),
                    l.getEmpName(),l.getPhone(),l.getSalary());
        });
        return new Response(200 , Update_Success);
    }

    public Response updateAllDepartmentEmployeeSalarybyPercentage(long companyId, long departmentId, long increment) {
        List<EmployeeEntity> employeeEntityList = employeeRepository.findByEmployeePKCompanyIdAndEmployeePKDepartmentId(companyId, departmentId);
        if(employeeEntityList.isEmpty())
            return new Response(404 , Failed);
        employeeEntityList.forEach((l) -> {
            long salary = l.getSalary();
            l.setSalary(salary + ((increment * salary) / 100));
            updateDetails(l.getEmployeePK().getCompanyId(),l.getEmployeePK().getDepartmentId(),l.getEmployeePK().getCompanyId(),
                    l.getEmpName(),l.getPhone(),l.getSalary());
        });
        return new Response(200 , Update_Success);
    }

    public Response updateEmployeeSalaryByPercentage(long companyId, long departmentId, long employeeId, long increment) {
        EmployeePK employeePK = new EmployeePK(companyId,departmentId,employeeId);
        List<EmployeeEntity> employeeEntityList = employeeRepository.findByEmployeePK(employeePK);
        if(employeeEntityList.isEmpty())
            return new Response(404 , Failed);
        employeeEntityList.forEach((l) -> {
            long salary = l.getSalary();
            l.setSalary(salary + ((increment * salary) / 100));
            updateDetails(l.getEmployeePK().getCompanyId(),l.getEmployeePK().getDepartmentId(),l.getEmployeePK().getCompanyId(),
                    l.getEmpName(),l.getPhone(),l.getSalary());
        });
        return new Response(200 , Update_Success);
    }
}



