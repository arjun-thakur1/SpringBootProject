package work1.project1.package1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work1.project1.package1.dto.request.EmployeeAddRequestDto;
import work1.project1.package1.dto.request.EmployeeUpdateRequestDto;
import work1.project1.package1.dto.response.Response;
import work1.project1.package1.entity.DepartmentEmployeeMapping;
import work1.project1.package1.entity.EmployeeEntity;
import work1.project1.package1.mapper.MyMapper;
import work1.project1.package1.repository.DepartmentEmployeeMappingRepository;
import work1.project1.package1.repository.EmployeeRepository;

import java.util.Optional;

import static work1.project1.package1.constants.ApplicationConstants.*;

@Service
public class EmployeeService {


    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    DepartmentEmployeeMappingRepository mappingRepository;

    @Autowired
    EmployeeMappingService mappingService;

    @Autowired
    MyMapper myMapper;

    public Object addEmployee(EmployeeAddRequestDto employeeAddRequestDto) {
     if(employeeRepository.existsByPhone(employeeAddRequestDto.getPhone()))
             return new Response(404,DUPLICATE_ERROR);
     EmployeeEntity employeeEntity=   new EmployeeEntity(employeeAddRequestDto.getName(),employeeAddRequestDto.getPhone(),
                employeeAddRequestDto.getSalary(),-1,-1);
     employeeRepository.save(employeeEntity);
     //if dept info given than also mapp if incorrect info given than ADD FAILED
     if(! mappingService.addEmployee(employeeAddRequestDto,employeeEntity.getId())) { //if given department not present
         employeeRepository.deleteById(employeeEntity.getId());
         return new Response(404, FAILED);
     }
     return myMapper.convert(employeeEntity,ADD_SUCCESS);
    }


    public Object getEmployee(Long id) {
        Optional<EmployeeEntity> employeeEntity= employeeRepository.findById(id);
        if(employeeEntity.isPresent())
        {
            EmployeeEntity employeeEntity1=employeeEntity.get();
            if(mappingRepository.existsByEmployeeIdAndIsActive(id,true))
            {
                return myMapper.convert(employeeEntity1,mappingRepository.findByEmployeeIdAndIsActive(id,true));
            }
            return    myMapper.convert(employeeEntity1," Not a part of any department!! ");//employeeEntity1; //getDepartmentEmployeeMappingList();
        }
        return new Response(400,NOT_PRESENT);
    }




    public Object deleteEmployee(Long id) {
        Optional<EmployeeEntity> fetchedEmployeeEntity=employeeRepository.findById(id);
        if(fetchedEmployeeEntity.isPresent())
        {
            DepartmentEmployeeMapping employeeMapping=mappingRepository.findByEmployeeIdAndIsActive(id,true);
            if(employeeMapping!=null) {
                employeeMapping.setActive(false);
                mappingRepository.save(employeeMapping);
            }
            return new Response(200, DELETE_SUCCESS);
        }
        return new Response(200,DELETE_FAILED);
    }

    public Object updateDetails(EmployeeUpdateRequestDto requestDto) {
        long departmentId=requestDto.getDepartmentId();
        if(!employeeRepository.existsById(requestDto.getId()))
            return new Response(400,NOT_PRESENT);

       boolean flag= mappingService.updateDetails(requestDto.getId(),requestDto.getDepartmentId(),requestDto.getManagerId(),requestDto.getDesignation());
       if(!flag)
           return new Response(400,FAILED);
        if(requestDto.getSalary()!=-1)//salary  update
        {
          Optional<EmployeeEntity> employeeEntity=employeeRepository.findById(requestDto.getId());
          if(employeeEntity.isPresent()) {
              EmployeeEntity employeeEntity1 = employeeEntity.get();
              employeeEntity1.setSalary(requestDto.getSalary());
              employeeRepository.save(employeeEntity1);
          }
        }
       return new Response(200,SUCCESS);
    }

    public boolean updateEmployeeSalary(Long employeeId, Long salary_increment, boolean flag) {
        if(flag)  //increment by absolute value
        {
            Optional<EmployeeEntity> employeeEntity=employeeRepository.findById(employeeId);
            if(employeeEntity.isPresent())
            {
                EmployeeEntity employeeEntity1=employeeEntity.get();
                employeeEntity1.setSalary(employeeEntity1.getSalary()+salary_increment);
                employeeRepository.save(employeeEntity1);
                return true;
            }
            return false;
        }
        else
        {
            Optional<EmployeeEntity> employeeEntity=employeeRepository.findById(employeeId);
            if(employeeEntity.isPresent())
            {
                EmployeeEntity employeeEntity1=employeeEntity.get();
                Long salary=employeeEntity1.getSalary();
                employeeEntity1.setSalary(salary+((salary*salary_increment)/100));
                employeeRepository.save(employeeEntity1);
                return true;
            }
            return false;
        }
    }


    //update....................


}
