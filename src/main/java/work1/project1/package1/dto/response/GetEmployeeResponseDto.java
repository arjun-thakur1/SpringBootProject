package work1.project1.package1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import work1.project1.package1.entity.DepartmentEmployeeMapping;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class GetEmployeeResponseDto {  //get response

    @Getter @Setter private Long id;
    @Getter @Setter private  Long departmemtId;
    @Getter @Setter private Long personId;
    @Getter @Setter private Long managerId;
    @Getter @Setter private  String designation;
    @Getter @Setter private Long salary;
    @Getter @Setter private Long status;
    @Getter @Setter private String message;

    public GetEmployeeResponseDto convert(DepartmentEmployeeMapping employeeEntity, String message)
    {
        GetEmployeeResponseDto responseGetEmployeeDto=new GetEmployeeResponseDto();

        responseGetEmployeeDto.setId(employeeEntity.getId());
        responseGetEmployeeDto.setDepartmemtId(employeeEntity.getDepartmentId());
        responseGetEmployeeDto.setPersonId(employeeEntity.getEmployeeId());
        responseGetEmployeeDto.setDesignation(employeeEntity.getDesignation());
        responseGetEmployeeDto.setManagerId(employeeEntity.getManagerId());
      //  responseGetEmployeeDto.setSalary(employeeEntity.getSalary());
        responseGetEmployeeDto.setStatus((long) 200);
        responseGetEmployeeDto.setMessage(message);

        return responseGetEmployeeDto;
    }



}
