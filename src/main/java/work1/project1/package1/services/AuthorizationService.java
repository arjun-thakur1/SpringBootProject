package work1.project1.package1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work1.project1.package1.entity.CompanyDepartmentMappingEntity;
import work1.project1.package1.entity.EmployeeEntity;
import work1.project1.package1.entity.UserEntity;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.exception.UnAuthorizedUser;
import work1.project1.package1.myenum.MyEnum;
import work1.project1.package1.repository.CompanyDepartmentMappingRepository;
import work1.project1.package1.repository.EmployeeRepository;
import work1.project1.package1.repository.UserRepository;

import java.util.Optional;

import static work1.project1.package1.constants.ApplicationConstants.ADMIN;
import static work1.project1.package1.constants.ApplicationConstants.FAILED;

@Service
public class AuthorizationService {

       @Autowired
       UserRepository userRepository;
       @Autowired
       EmployeeRepository employeeRepository;
       @Autowired
       EmployeeMappingService employeeMappingService;

       public void isAccessOfAll(Long userId, String password) throws CustomException, UnAuthorizedUser {
          if(userId==1 && password.equalsIgnoreCase(ADMIN))
             return ;
          throw  new UnAuthorizedUser(" Authorization Failed!! ");
       }


        public void isAccessOfCompany(Long userId,String password, Long companyId ) throws UnAuthorizedUser {
            if(userId==1 && password.equalsIgnoreCase(ADMIN))
                return ;
            CompanyDepartmentMappingEntity mappingEntity=employeeMappingService.getIds(userId);
            if((mappingEntity==null) || !(mappingEntity.getCompanyId().equals(companyId)))
                throw  new UnAuthorizedUser(" Authorization Failed !!. ");
            UserEntity userEntity=userRepository.findByUserId(userId);
            if(userEntity==null )
                throw  new UnAuthorizedUser(" Authorization Failed!! unauthorized user!! ");
            MyEnum saved_role=getRole(userId);
            if(userEntity.get_password().equals(password) && (saved_role==MyEnum.ceo || saved_role==MyEnum.CEO) )
                return ;
            throw  new UnAuthorizedUser(" Authorization Failed!! ");
        }


      public void isAccessOfDepartment(Long userId, String password,Long companyId,Long departmentId) throws UnAuthorizedUser {
        if(userId==1 && password.equalsIgnoreCase(ADMIN))
            return ;
        UserEntity userEntity=userRepository.findByUserId(userId);
        if(userEntity==null )
            throw  new UnAuthorizedUser(" Authorization Failed!! unauthorized user!! ");
        MyEnum saved_role=getRole(userId);
        CompanyDepartmentMappingEntity mappingEntity=employeeMappingService.getIds(userId);
        if(mappingEntity==null ||  (!mappingEntity.getCompanyId().equals(companyId)) )  //if ceo then only company match
            throw  new UnAuthorizedUser(" Authorization Failed!!. ");
        if((saved_role==MyEnum.ceo || saved_role==MyEnum.CEO || ( (saved_role==MyEnum.HOD || saved_role==MyEnum.hod) &&
                mappingEntity.getDepartmentId().equals(departmentId))) && userEntity.get_password().equals(password))
            return ;
        throw  new UnAuthorizedUser(" Authorization Failed!! ");
      }

      public void isAccessOfDepartment(Long userId,String password , Long employeeId) throws UnAuthorizedUser {
          if(userId==1 && password.equalsIgnoreCase(ADMIN))
              return ;
          CompanyDepartmentMappingEntity mappingEntity=employeeMappingService.getIds(employeeId);
          if(mappingEntity!=null){
               isAccessOfDepartment(userId,password,mappingEntity.getCompanyId(),mappingEntity.getDepartmentId());
               return;
          }
          throw new UnAuthorizedUser(FAILED + " user is not part of any company!! ");
      }

      public void isAccessOfUpdateEmployee(Long userId,String password,Long employeeId) throws UnAuthorizedUser {
           if(userId==1 && password.equalsIgnoreCase(ADMIN))
               return ;
           UserEntity userEntity=userRepository.findByUserId(userId);
           if(userEntity==null )
               throw  new UnAuthorizedUser(" Authorization Failed!! unauthorized user!! ");
           Optional<EmployeeEntity> fetchedEmployeeEntity=employeeRepository.findById(employeeId);
           if(fetchedEmployeeEntity.isPresent()) {
               EmployeeEntity employeeEntity = fetchedEmployeeEntity.get();
               MyEnum saved_role=employeeEntity.getDesignation();
               CompanyDepartmentMappingEntity mappingEntity = employeeMappingService.getIds(employeeId);
               if (saved_role==MyEnum.ceo || saved_role==MyEnum.CEO)
                   isAccessOfCompany(userId, password, mappingEntity.getCompanyId());
               if (saved_role==MyEnum.HOD || saved_role==MyEnum.hod)
                   isAccessOfDepartment(userId, password, mappingEntity.getCompanyId(), mappingEntity.getDepartmentId());
               if (userEntity.get_password().equals(password) && (saved_role==MyEnum.employee || saved_role==MyEnum.employee)
                       && userId.equals(employeeId)) //emp can update only itself info
                   return ;
           }
           throw  new UnAuthorizedUser(" Authorization Failed!! ");
      }

       public void isAccessOfGetEmployee(Long userId, String password) throws UnAuthorizedUser {
           if(userId==1 && password.equalsIgnoreCase(ADMIN))
               return ;
           UserEntity userEntity=userRepository.findByUserId(userId);
           if(userEntity==null)
               throw  new UnAuthorizedUser(" Authorization Failed!! unauthorized user!! ");
           Optional<EmployeeEntity> fetchedEmployeeEntity=employeeRepository.findById(userId);
           if(fetchedEmployeeEntity.isPresent()) {
               EmployeeEntity employeeEntity = fetchedEmployeeEntity.get();
               MyEnum saved_role=employeeEntity.getDesignation();
               if((saved_role==MyEnum.ceo || saved_role==MyEnum.CEO||saved_role==MyEnum.HOD || saved_role==MyEnum.hod||
                       saved_role==MyEnum.employee || saved_role==MyEnum.employee) && userEntity.get_password().equals(password))
                   return;
           }
           throw  new UnAuthorizedUser(" Authorization Failed!! ");
       }

    private MyEnum getRole(Long employeeId) {
        Optional<EmployeeEntity> fetchedEmployeeEntity=employeeRepository.findById(employeeId);
        if(fetchedEmployeeEntity.isPresent()) {
            EmployeeEntity employeeEntity= fetchedEmployeeEntity.get();
            return employeeEntity.getDesignation();
        }
        return null;
    }

    public void isAccessOfAnyDepartment(Long userId, String password) throws UnAuthorizedUser {
        if(userId==1 && password.equalsIgnoreCase(ADMIN))
            return ;
        UserEntity userEntity=userRepository.findByUserId(userId);
        if(userEntity==null )
            throw  new UnAuthorizedUser(" Authorization Failed!! unauthorized user!! ");
        MyEnum saved_role=getRole(userId);
        if((saved_role==MyEnum.ceo || saved_role==MyEnum.CEO || saved_role==MyEnum.HOD || saved_role==MyEnum.hod)  &&
                userEntity.get_password().equals(password))
            return ;
        throw new UnAuthorizedUser(" Authorization Failed!! ");
    }

    public void isAccessOfCompany(Long userId, String password) throws UnAuthorizedUser {
        if(userId==1 && password.equalsIgnoreCase(ADMIN))
            return ;
        CompanyDepartmentMappingEntity mappingEntity=employeeMappingService.getIds(userId);
        if(mappingEntity==null)
            throw  new UnAuthorizedUser(" Authorization Failed!! ");
        isAccessOfCompany(userId,password,mappingEntity.getCompanyId());
        return;
    }

    public void isAccessOfAnyCompany(Long userId , String password) throws  UnAuthorizedUser {
        if(userId==1 && password.equalsIgnoreCase(ADMIN))
            return ;
        UserEntity userEntity=userRepository.findByUserId(userId);
        if(userEntity==null )
            throw  new UnAuthorizedUser(" Authorization Failed!! unauthorized user!! ");
        MyEnum saved_role=getRole(userId);
        if((saved_role==MyEnum.ceo || saved_role==MyEnum.CEO) && userEntity.get_password().equals(password))
        return ;
        throw new UnAuthorizedUser(" Authorization Failed!! ");
    }

    public void isAccessOfUserData(Long userId, String password, Long id) throws UnAuthorizedUser {
        if(userId==1 && password.equalsIgnoreCase(ADMIN))
            return ;
        UserEntity userEntity=userRepository.findByUserId(userId);
        if(!userId.equals(id) || userEntity==null)
            throw  new UnAuthorizedUser(" Authorization Failed!! ");
        if(userEntity.get_password().equalsIgnoreCase(password))
            return;
        throw  new UnAuthorizedUser(" Authorization Failed!! Password Incorrect!!");
    }


}
