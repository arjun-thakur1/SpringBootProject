package work1.project1.package1.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import work1.project1.package1.entity.DepartmentEntity;

@Service
public class GetDepartmentResponseDto {

    @Getter @Setter private Long id;
    @Getter @Setter private Long companyId;
    @Getter @Setter private String departmentName;


    public GetDepartmentResponseDto convert(DepartmentEntity departmentEntity) {

        GetDepartmentResponseDto responseGetDepartmentDto=new GetDepartmentResponseDto();
        responseGetDepartmentDto.setId(departmentEntity.getId());
        responseGetDepartmentDto.setCompanyId(departmentEntity.getCompanyId());
        responseGetDepartmentDto.setDepartmentName(departmentEntity.getDepartmentName());

        return responseGetDepartmentDto;
    }
}
