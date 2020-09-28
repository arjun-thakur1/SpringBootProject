package work1.project1.package1.dto.response;

import lombok.*;
import work1.project1.package1.entity.EmployeeEntity;
import work1.project1.package1.myenum.MyEnum;

import java.io.Serializable;

import static work1.project1.package1.constants.ApplicationConstants.SUCCESS;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCompleteGetResponse implements Serializable {
    private static final long serialVersionUID = 1L;
     private Long   id;
     private String name;
     private String phone;

    private Long companyId;
    private Long departmentId;

     private Double salary;
     private Long managerId;
     private MyEnum designation;

     private Long status;
     private String message;



    public EmployeeCompleteGetResponse convert(EmployeeEntity employeeEntity, Long companyId, Long departmentId) {
        EmployeeCompleteGetResponse completeResponseDto=new EmployeeCompleteGetResponse();

        completeResponseDto.setCompanyId(companyId);
        completeResponseDto.setDepartmentId(departmentId);

        completeResponseDto.setId(employeeEntity.getId());
        completeResponseDto.setName(employeeEntity.getName());
        completeResponseDto.setPhone(employeeEntity.getPhone());
        completeResponseDto.setSalary(employeeEntity.getSalary());
        completeResponseDto.setManagerId(employeeEntity.getManagerId());
        completeResponseDto.setDesignation(employeeEntity.getDesignation());
        completeResponseDto.setStatus(Long.valueOf(200));
        completeResponseDto.setMessage(SUCCESS);

        return completeResponseDto;
    }
}
