package work1.project1.package1.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import work1.project1.package1.entity.EmployeeEntity;
import work1.project1.package1.myenum.MyEnum;

import static work1.project1.package1.constants.ApplicationConstants.SUCCESS;
@Data
public class EmployeeCompleteResponse {

     private Long   id;
     private String name;
     private String phone;

     private Long companyId;
     private Long departmentId;

     private Long salary;
     private Long managerId;
     private MyEnum designation;

  //  @Getter @Setter private  String accessToken;

     private Long status;
     private String message;

//multiple sources and one destination mapping..
    public EmployeeCompleteResponse convert(EmployeeEntity employeeEntity, Long companyId, Long departmentId) {
        EmployeeCompleteResponse completeResponseDto=new EmployeeCompleteResponse();

        completeResponseDto.setCompanyId(companyId);
        completeResponseDto.setDepartmentId(departmentId);

        completeResponseDto.setId(employeeEntity.getId());
        completeResponseDto.setName(employeeEntity.getName());
        completeResponseDto.setPhone(employeeEntity.getPhone());
        completeResponseDto.setSalary(employeeEntity.getSalary());
        completeResponseDto.setManagerId(employeeEntity.getManagerId());
        completeResponseDto.setDesignation(employeeEntity.getDesignation());
        completeResponseDto.setStatus(Long.valueOf(200));
       // completeResponseDto.setAccessToken(token);

        completeResponseDto.setMessage(SUCCESS);
        return completeResponseDto;
    }




}
