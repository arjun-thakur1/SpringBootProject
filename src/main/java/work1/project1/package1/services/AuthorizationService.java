package work1.project1.package1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work1.project1.package1.entity.CompanyDepartmentMappingEntity;
import work1.project1.package1.entity.EmployeeEntity;
import work1.project1.package1.exception.NotPresentException;
import work1.project1.package1.exception.UnAuthorizedUser;
import work1.project1.package1.myenum.MyEnum;
import work1.project1.package1.other.TokenIds;
import work1.project1.package1.other.CompanyDepartmentEmployeeIds;
import work1.project1.package1.repository.CompanyDepartmentMappingRepository;
import work1.project1.package1.repository.EmployeeRepository;

@Service
public class AuthorizationService {

       @Autowired
       EmployeeRepository employeeRepository;
       @Autowired
       CompanyDepartmentMappingRepository cdMappingRepository;
       @Autowired
       EmployeeMappingService employeeMappingService;
       @Autowired
       RedisService redisService;

       TokenIds findIdsFromToken=new TokenIds();

    //please save enum in database that r lowercase - none,employee,ceo,hod ,, addEmployee(),updateEmployee()
    public Long isAccessOfCompanyDepartment(String token , Long companyId , Long departmentId) throws UnAuthorizedUser {
        CompanyDepartmentEmployeeIds ids=findIdsFromToken.findCompanyDepartmentEmployeeIdFromToken(token);
        EmployeeEntity employeeEntity=employeeRepository.findById(ids.getEmployeeId()).orElse(null);
        if((employeeEntity.getDesignation().equals(MyEnum.ceo) && ids.getCompanyId().equals(companyId)) || (employeeEntity
                .getDesignation().equals(MyEnum.hod) && ids.getCompanyId().equals(companyId) && ids.getDepartmentId()
                .equals(departmentId)))
            return (ids.getEmployeeId());
        throw new UnAuthorizedUser(" user is not authorized!! ");
    }
    //deleteEmployee
    public Long isAccessOfCompanyDepartment(String token , Long employeeId) throws UnAuthorizedUser, NotPresentException {
        CompanyDepartmentEmployeeIds ids=findIdsFromToken.findCompanyDepartmentEmployeeIdFromToken(token);
        CompanyDepartmentMappingEntity cdMappingEntity=employeeMappingService.getIds(employeeId);
        if(cdMappingEntity==null)
            throw new NotPresentException(" employee is not part of any company-department!! ");
        EmployeeEntity employeeEntity=employeeRepository.findById(ids.getEmployeeId()).orElse(null);
        if((employeeEntity.getDesignation().equals(MyEnum.ceo) && ids.getCompanyId().equals(cdMappingEntity.getCompanyId()))
                || (employeeEntity.getDesignation().equals(MyEnum.hod) && ids.getCompanyId().equals(cdMappingEntity.
                getCompanyId()) && ids.getDepartmentId().equals(cdMappingEntity.getDepartmentId())))
            return ids.getEmployeeId();
        throw new UnAuthorizedUser(" user is not authorized!! ");
    }

    public boolean isAccessOfGetEmployee(String token , Long employeeId){
        CompanyDepartmentEmployeeIds ids=findIdsFromToken.findCompanyDepartmentEmployeeIdFromToken(token);
        if(employeeId.equals(ids.getEmployeeId()))
            return true;
        else
            return false;
    }
    //department_entity_table
    public Long isAccessOfDepartmentTable(String token,Long departmentId) throws UnAuthorizedUser {
        CompanyDepartmentEmployeeIds ids=findIdsFromToken.findCompanyDepartmentEmployeeIdFromToken(token);
        EmployeeEntity employeeEntity=employeeRepository.findById(ids.getEmployeeId()).orElse(null);
        if(employeeEntity!=null && (employeeEntity.getDesignation().equals(MyEnum.ceo) || (employeeEntity.getDesignation().equals(MyEnum.hod)
           && ids.getDepartmentId().equals(departmentId)) ))
                return ids.getEmployeeId();
        throw new UnAuthorizedUser(" user is not authorized!! ");
    }


}
