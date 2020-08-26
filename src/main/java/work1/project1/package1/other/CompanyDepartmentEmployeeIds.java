package work1.project1.package1.other;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDepartmentEmployeeIds {

    private Long companyId;
    private Long departmentId;
    private Long employeeId;
}
