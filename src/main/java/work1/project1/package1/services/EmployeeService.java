package work1.project1.package1.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work1.project1.package1.dto.request.EmployeeAddRequest;
import work1.project1.package1.dto.request.EmployeeUpdateRequest;
import work1.project1.package1.dto.request.UpdateSalaryRequestDto;
import work1.project1.package1.dto.response.EmployeeAddResponseDto;
import work1.project1.package1.dto.response.EmployeeCompleteResponse;
import work1.project1.package1.dto.response.Response;
import work1.project1.package1.entity.CompanyDepartmentMappingEntity;
import work1.project1.package1.entity.EmployeeEntity;
import work1.project1.package1.entity.EmployeeMappingEntity;
import work1.project1.package1.entity.UserEntity;
import work1.project1.package1.mapper.MyMapper;
import work1.project1.package1.repository.CompanyDepartmentMappingRepository;
import work1.project1.package1.repository.EmployeeMappingRepository;
import work1.project1.package1.repository.EmployeeRepository;
import work1.project1.package1.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static work1.project1.package1.constants.ApplicationConstants.*;

@Service
public class EmployeeService {


    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeMappingRepository employeeMappingRepository;

    @Autowired
    CompanyDepartmentMappingRepository companyDepartmentMappingRepository;

    @Autowired
    EmployeeMappingService mappingService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MyMapper myMapper;
    @Autowired
    ModelMapper modelMapper;
     //@Autowired
     //EmployeeCompleteResponse completeResponseDto;

    public Object addEmployee(EmployeeAddRequest employeeAddRequest) {
        if (employeeRepository.existsByPhone(employeeAddRequest.getPhone())) {
            return new Response(404, DUPLICATE_ERROR);
        }
        Long companyId = employeeAddRequest.getCompanyId();
        Long departmentId = employeeAddRequest.getDepartmentId();
        Long managerId = employeeAddRequest.getManagerId();

        if (companyId == -1 || departmentId == -1) {
            if(managerId!=-1 || employeeAddRequest.getDesignation()!=_NONE || employeeAddRequest.getSalary()!=-1)
                return new Response(404,FAILED);
            EmployeeEntity employeeEntity =new EmployeeEntity(employeeAddRequest.getName(),employeeAddRequest.getPhone(),
                    (long)-1,(long)-1,_NONE, (long)-1,(long)-1,true);
            employeeRepository.save(employeeEntity);
            UserEntity userEntity=new UserEntity(employeeEntity.getId(),employeeAddRequest.getPhone());
            userRepository.save(userEntity);
            return modelMapper.map(employeeEntity, EmployeeAddResponseDto.class);
        }
        CompanyDepartmentMappingEntity companyDepartmentMappingEntity = companyDepartmentMappingRepository.
                findByCompanyIdAndDepartmentIdAndIsActive(companyId, departmentId, true);
        if (companyDepartmentMappingEntity != null) {
            Long mappingId = companyDepartmentMappingEntity.getId();
            if (managerId != -1 && !isManager(managerId)) { //if managerId given then designation must be manage
                return new Response(400, FAILED);
            }
            if(employeeAddRequest.getSalary()==-1)
                return new Response(400,FAILED+" Salary Required!!");
            EmployeeEntity employeeEntity =modelMapper.map(employeeAddRequest, EmployeeEntity.class);
            employeeRepository.save(employeeEntity);
            UserEntity userEntity=new UserEntity(employeeEntity.getId(),employeeAddRequest.getPhone());
            userRepository.save(userEntity);
            mappingService.add(mappingId, employeeEntity.getId());
            EmployeeCompleteResponse completeResponseDto=new EmployeeCompleteResponse();
            return  completeResponseDto.convert(employeeEntity,companyId,departmentId);
        }
        else {
            return new Response(409, FAILED);
        }
    }







    public Object getEmployee(Long id) {
        Optional<EmployeeEntity> fetchedEmployeeEntity= employeeRepository.findById(id);
        if(fetchedEmployeeEntity.isPresent())
        {
            EmployeeEntity employeeEntity=fetchedEmployeeEntity.get();
            CompanyDepartmentMappingEntity companyDepartmentMappingEntity= mappingService.getIds(id);
            System.out.println(companyDepartmentMappingEntity);
            if(companyDepartmentMappingEntity==null)
            return modelMapper.map(employeeEntity, EmployeeAddResponseDto.class);

            EmployeeCompleteResponse completeResponse=new EmployeeCompleteResponse();
            return completeResponse.convert(employeeEntity,companyDepartmentMappingEntity.getCompanyId(),
                    companyDepartmentMappingEntity.getDepartmentId());
        }
        return new Response(400,NOT_PRESENT);
    }



    boolean isManager(Long managerId) {
        Optional<EmployeeEntity> fetchedEmployeeEntity=employeeRepository.findById(managerId);
        if(fetchedEmployeeEntity.isPresent()) {
            EmployeeEntity employeeEntity = fetchedEmployeeEntity.get();
            if (employeeEntity.getDesignation().equals(MANAGER))  //can be removed...{
                return true;
            return false;
        }
        return false;
    }

    public Object deleteEmployee(Long id) {
        Optional<EmployeeEntity> fetchedEmployeeEntity=employeeRepository.findById(id);
        if(fetchedEmployeeEntity.isPresent())
        {
            EmployeeEntity employeeEntity=fetchedEmployeeEntity.get();
            employeeEntity.setSalary((long) -1);
            employeeEntity.setDesignation(_NONE);
            employeeEntity.setManagerId((long) -1);
            employeeRepository.save(employeeEntity);

            EmployeeMappingEntity employeeMapping= employeeMappingRepository.findByEmployeeIdAndIsActive(id,true);
            if(employeeMapping!=null) {
                employeeMapping.setActive(false);
                employeeMappingRepository.save(employeeMapping);
                return new Response(200,  DELETE_SUCCESS);
            }
            return new Response(200,  " Not part of any company!! ");
        }
        return new Response(200,DELETE_FAILED);
    }




    public Object updateDetails(EmployeeUpdateRequest requestDto) {
        Long employeeId=requestDto.getId();
        Optional<EmployeeEntity> fetchedEmployeeEntity= employeeRepository.findById(employeeId);
        if(fetchedEmployeeEntity.isPresent()) {
            EmployeeEntity employeeEntity=fetchedEmployeeEntity.get();
            Long companyId=requestDto.getCompanyId();
            Long departmentId=requestDto.getDepartmentId();
            String name=requestDto.getName();
            String phone=requestDto.getPhone();
            Long salary=requestDto.getSalary();
            Long managerId=requestDto.getManagerId();
            String designation=requestDto.getDesignation();

            if(( companyId!=-1 && departmentId==-1) ) //only company id given then error
                return new Response(400,FAILED);

            if(departmentId!=-1){ //want to change only dept or may be company also
              boolean  flag= mappingService.updateDetails(companyId,departmentId,employeeId);
               if(!flag){ //cid,did given not valid
                   return new Response(400,FAILED);
               }
            }
            if(name!=null)
                employeeEntity.setName(name);
            if(phone!=null)
                employeeEntity.setPhone(phone);
            //check emp is part of any dept before updating salary,designation,managerId
            boolean flag=employeeMappingRepository.existsByEmployeeIdAndIsActive(employeeId,true);
            if(!flag){
                employeeRepository.save(employeeEntity);
                return new Response(200,SUCCESS);
            }
            if(salary<=0) {
                return new Response(400,FAILED+" , Salary Required!! ");
            }
            else
                employeeEntity.setSalary(salary);
            if(designation!=null)
                employeeEntity.setDesignation(designation);
            if(managerId!=-1){
                if(isManager(managerId))
                employeeEntity.setManagerId(managerId);
                else{ //wrong managerId given then failed
                    return new Response(400,FAILED);
                }
            }
            employeeRepository.save(employeeEntity);
            return new Response(200,SUCCESS);

        }
        return new Response(400,NOT_PRESENT);
    }


    public boolean updateSalary(UpdateSalaryRequestDto updateSalaryRequestDto) {
        Long employeeId=updateSalaryRequestDto.getEmployeeId();
        Long departmentId=updateSalaryRequestDto.getDepartmentId();
        Long companyId=updateSalaryRequestDto.getCompanyId();
        Long salary_increment=updateSalaryRequestDto.getSalary_increment();
        Long flag=updateSalaryRequestDto.getFlag();

        if(employeeId!=-1)
           return updateEmployeeSalary(employeeId,salary_increment,flag);
        if(departmentId!=-1 && companyId!=-1){
              CompanyDepartmentMappingEntity companyDepartmentMappingEntity=companyDepartmentMappingRepository.
                      findByCompanyIdAndDepartmentIdAndIsActive(companyId,departmentId,true);
              if(companyDepartmentMappingEntity==null)
                  return false;
             return updateDepartmentSalary(companyDepartmentMappingEntity.getId(),salary_increment,flag);
        }
        if(companyId!=-1){
            List<CompanyDepartmentMappingEntity> companyDepartmentMappingEntityList=companyDepartmentMappingRepository.
                    findAllByCompanyIdAndIsActive(companyId,true);
            companyDepartmentMappingEntityList.forEach(d->{
                updateDepartmentSalary(d.getId(),salary_increment,flag);
            });
            return true;
        }
        return false;
    }

    private boolean updateDepartmentSalary(Long cd_mappingId,Long salary_increment,Long flag) {

        List<EmployeeMappingEntity> employeeMappingEntityList=employeeMappingRepository.findByMappingIdAndIsActive
                (cd_mappingId,true);
        if(employeeMappingEntityList==null )
             return false;
        employeeMappingEntityList.forEach(m->{
            updateEmployeeSalary(m.getEmployeeId(),salary_increment,flag);
        });
        return true;
    }

    public boolean updateEmployeeSalary(Long employeeId, Long salary_increment,Long flag) {
        if(mappingService.getIds(employeeId)==null)
            return  false;   //new Response(409,NOT_PRESENT);
        if(flag==1)  //increment by absolute value
        {
            Optional<EmployeeEntity> fetchedEmployeeEntity=employeeRepository.findById(employeeId);
            if(fetchedEmployeeEntity.isPresent())
            {
                EmployeeEntity employeeEntity=fetchedEmployeeEntity.get();
                employeeEntity.setSalary(employeeEntity.getSalary()+salary_increment);
                employeeRepository.save(employeeEntity);
                return true;
            }
            return false;
        }
        else if(flag==0)
        {
            Optional<EmployeeEntity> fetchedEmployeeEntity=employeeRepository.findById(employeeId);
            if(fetchedEmployeeEntity.isPresent())
            {
                EmployeeEntity employeeEntity=fetchedEmployeeEntity.get();
                Long salary=employeeEntity.getSalary();
                employeeEntity.setSalary(salary+((salary*salary_increment)/100));
                employeeRepository.save(employeeEntity);
                return true;
            }
            return false;
        }
        else
        {
            return false;
        }
    }




    //update....................


}
