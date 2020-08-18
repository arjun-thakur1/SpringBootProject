package work1.project1.package1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work1.project1.package1.entity.CompanyDepartmentMappingEntity;
import work1.project1.package1.entity.EmployeeEntity;
import work1.project1.package1.entity.UserEntity;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.repository.CompanyDepartmentMappingRepository;
import work1.project1.package1.repository.EmployeeRepository;
import work1.project1.package1.repository.UserRepository;

import java.util.Optional;

@Service
public class AuthorizationService {

       @Autowired
       UserRepository userRepository;
       @Autowired
       EmployeeRepository employeeRepository;
       @Autowired
       EmployeeMappingService employeeMappingService;

       public void isAccessOfAll(Long userId, String password) throws CustomException {
          if(userId==1 && password.equalsIgnoreCase("admin"))
             return ;
         throw  new CustomException(" Authorization Failed!! ");
       }


        public void isAccessOfCompany(Long userId,String password, Long companyId ) throws CustomException {
            if(userId==1 && password.equalsIgnoreCase("admin"))
                return ;

            CompanyDepartmentMappingEntity mappingEntity=employeeMappingService.getIds(userId);
            if(mappingEntity==null || mappingEntity.getCompanyId()!=companyId)
                throw  new CustomException(" Authorization Failed !!. ");

            UserEntity userEntity=userRepository.findByUserId(userId);
            if(userEntity==null )
                throw  new CustomException(" Authorization Failed!! unauthorized user!! ");
            String role=getRole(userId);
            if(userEntity.get_password().equals(password) && role.equalsIgnoreCase("ceo") )
                return ;
            throw  new CustomException(" Authorization Failed!! ");

        }


      public void isAccessOfDepartment(Long userId, String password,Long companyId,Long departmentId) throws CustomException {
        if(userId==1 && password.equalsIgnoreCase("admin"))
            return ;
        UserEntity userEntity=userRepository.findByUserId(userId);
        if(userEntity==null )
            throw  new CustomException(" Authorization Failed!! unauthorized user!! ");
        String role=getRole(userId);
        CompanyDepartmentMappingEntity mappingEntity=employeeMappingService.getIds(userId);
        if(mappingEntity==null ||  mappingEntity.getCompanyId()!=companyId )  //if ceo then only company match
            throw  new CustomException(" Authorization Failed!!. ");
        if((role.equalsIgnoreCase("ceo") || ( role.equalsIgnoreCase("hod") &&
                mappingEntity.getDepartmentId()!=departmentId)) && userEntity.get_password().equals(password))
            return ;
        throw  new CustomException(" Authorization Failed!! ");
      }


       public void isAccessOfEmployee(Long userId,String password,Long employeeId) throws CustomException {
           if(userId==1 && password.equalsIgnoreCase("admin"))
               return ;
           UserEntity userEntity=userRepository.findByUserId(userId);
           if(userEntity==null || !userId.equals(employeeId))
               throw  new CustomException(" Authorization Failed!! unauthorized user!! ");
           Optional<EmployeeEntity> fetchedEmployeeEntity=employeeRepository.findById(employeeId);
           if(fetchedEmployeeEntity.isPresent()) {
               EmployeeEntity employeeEntity = fetchedEmployeeEntity.get();

               String saved_role=employeeEntity.getDesignation();
               CompanyDepartmentMappingEntity mappingEntity = employeeMappingService.getIds(userId);
               if (saved_role.equalsIgnoreCase("ceo"))
                   isAccessOfCompany(userId, password, mappingEntity.getCompanyId());
               if (saved_role.equalsIgnoreCase("hod"))
                   isAccessOfDepartment(userId, password, mappingEntity.getCompanyId(), mappingEntity.getDepartmentId());
               if (userEntity.get_password().equals(password) && "employee".equalsIgnoreCase(saved_role))
                   return ;
           }
           throw  new CustomException(" Authorization Failed!! ");
        }

       public void isAccessOfEmployee(Long userId, String password) throws CustomException {
           if(userId==1 && password.equalsIgnoreCase("admin"))
               return ;
           UserEntity userEntity=userRepository.findByUserId(userId);
           if(userEntity==null)
               throw  new CustomException(" Authorization Failed!! unauthorized user!! ");
           Optional<EmployeeEntity> fetchedEmployeeEntity=employeeRepository.findById(userId);
           if(fetchedEmployeeEntity.isPresent()) {
               EmployeeEntity employeeEntity = fetchedEmployeeEntity.get();
               String saved_role=employeeEntity.getDesignation();
               CompanyDepartmentMappingEntity mappingEntity = employeeMappingService.getIds(userId);
               if (saved_role.equalsIgnoreCase("ceo"))
                   isAccessOfCompany(userId, password, mappingEntity.getCompanyId());
               if (saved_role.equalsIgnoreCase("hod"))
                   isAccessOfDepartment(userId, password, mappingEntity.getCompanyId(), mappingEntity.getDepartmentId());
               if (userEntity.get_password().equals(password) && "employee".equalsIgnoreCase(saved_role))
                   return ;
           }
           throw  new CustomException(" Authorization Failed!! ");
       }

    private String getRole(Long employeeId) {
        Optional<EmployeeEntity> fetchedEmployeeEntity=employeeRepository.findById(employeeId);
        if(fetchedEmployeeEntity.isPresent()) {
            EmployeeEntity employeeEntity= fetchedEmployeeEntity.get();
            return employeeEntity.getDesignation();
        }
        return null;
    }

    public void isAccessOfDepartment(Long userId, String password) throws CustomException {
        if(userId==1 && password.equalsIgnoreCase("admin"))
            return ;
        CompanyDepartmentMappingEntity mappingEntity=employeeMappingService.getIds(userId);
        if(mappingEntity==null)
            throw  new CustomException(" Authorization Failed!! ");
        isAccessOfDepartment(userId,password,mappingEntity.getCompanyId(),mappingEntity.getDepartmentId());
        return;
    }

    public void isAccessOfCompany(Long userId, String password) throws CustomException {
        if(userId==1 && password.equalsIgnoreCase("admin"))
            return ;
        CompanyDepartmentMappingEntity mappingEntity=employeeMappingService.getIds(userId);
        if(mappingEntity==null)
            throw  new CustomException(" Authorization Failed!! ");
        isAccessOfCompany(userId,password,mappingEntity.getCompanyId());
        return;
    }

    public void isAccessOfUserData(Long userId, String password, Long id) throws CustomException {
        if(userId==1 && password.equalsIgnoreCase("admin"))
            return ;
        UserEntity userEntity=userRepository.findByUserId(userId);
        if(userId!=id || userEntity==null)
            throw  new CustomException(" Authorization Failed!! ");
        if(userEntity.get_password().equalsIgnoreCase(password))
            return;
        throw  new CustomException(" Authorization Failed!! Password Incorrect!!");
    }


}
