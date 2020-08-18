package work1.project1.package1.dto.response;

import lombok.Getter;
import lombok.Setter;
import work1.project1.package1.entity.EmployeeEntity;

import java.io.Serializable;

import static work1.project1.package1.constants.ApplicationConstants.SUCCESS;

public class EmployeeCompleteGetResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter @Setter private Long   id;
    @Getter @Setter private String name;
    @Getter @Setter private String phone;

    @Getter @Setter private Long companyId;
    @Getter @Setter private Long departmentId;

    @Getter @Setter private Long salary;
    @Getter @Setter private Long managerId;
    @Getter @Setter private String designation;

    @Getter @Setter private Long status;
    @Getter @Setter private String message;



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
