package work1.project1.package1.mapper;

import org.springframework.stereotype.Service;
import work1.project1.package1.dto.response.*;
import work1.project1.package1.entity.CompanyEntity;
import work1.project1.package1.entity.DepartmentEmployeeMapping;
import work1.project1.package1.entity.DepartmentEntity;
import work1.project1.package1.entity.EmployeeEntity;

import java.util.ArrayList;
import java.util.List;

import static work1.project1.package1.constants.ApplicationConstants.SUCCESS;

@Service
public class MyMapper {

    public CompanyResponseDto convert(CompanyEntity companyEntity, String message)
        {
            CompanyResponseDto responseCompanyDto=new CompanyResponseDto();

            responseCompanyDto.setId(companyEntity.getId());
            responseCompanyDto.setCompanyName(companyEntity.getCompanyName());
            responseCompanyDto.setCeoName(companyEntity.getCeoName());
            responseCompanyDto.setStatus((long) 200);
            responseCompanyDto.setMessage(message);
            return responseCompanyDto;
        }

    public DepartmentResponseDto convert(DepartmentEntity departmentEntity, String message)
    {
        DepartmentResponseDto responseDepartmentDto=new DepartmentResponseDto();

        responseDepartmentDto.setId(departmentEntity.getId());
        responseDepartmentDto.setCompanyId(departmentEntity.getCompanyId());
        responseDepartmentDto.setDepartmentName(departmentEntity.getDepartmentName());
        responseDepartmentDto.setStatus((long) 200);
        responseDepartmentDto.setMessage(message);
        return responseDepartmentDto;
    }

    public List<DepartmentResponseDto> convert(List<DepartmentEntity> departmentEntityList, String message) {

        List<DepartmentResponseDto> responseDepartmentDtoList=new ArrayList<>();
        departmentEntityList.forEach((d)->
                responseDepartmentDtoList.add(convert(d,message))
        );
        return responseDepartmentDtoList;
    }



        public EmployeeResponseDto convert(EmployeeEntity personEntity, String message)
    {
        EmployeeResponseDto responseEmployeeDto=new EmployeeResponseDto();

        responseEmployeeDto.setId(personEntity.getId());
        responseEmployeeDto.setName(personEntity.getName());
        responseEmployeeDto.setPhone(personEntity.getPhone());
        responseEmployeeDto.setStatus((long) 200);
        responseEmployeeDto.setMessage(message);

        return responseEmployeeDto;
    }

    public EmployeeCompleteResponseDto convert(EmployeeEntity employeeEntity1, DepartmentEmployeeMapping d) {
        EmployeeCompleteResponseDto completeDto=new EmployeeCompleteResponseDto();

        completeDto.setId(employeeEntity1.getId());
        completeDto.setName(employeeEntity1.getName());
        completeDto.setPhone(employeeEntity1.getPhone());
        completeDto.setSalary(employeeEntity1.getSalary());
        completeDto.setManagerId(d.getManagerId());
        completeDto.setDesignation(d.getDesignation());
        completeDto.setStatus((long) 200);
        completeDto.setMessage(SUCCESS);

        return completeDto;
    }

    /* created for delete emp response but not need
    public EmployeeResponseDto convert(EmployeeEntity employeeEntity,long status,String message)
    {
        EmployeeResponseDto responseEmployeeDto=new EmployeeResponseDto();

        responseEmployeeDto.setId(employeeEntity.getId());
        responseEmployeeDto.setStatus(status);
        responseEmployeeDto.setMessage(message);

        return responseEmployeeDto;
    }
  */
}

