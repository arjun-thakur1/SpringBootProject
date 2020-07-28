package Work1.Project1.Package.converter;

import Work1.Project1.Package.entity.EmployeeEntity;
import Work1.Project1.Package.response.ResponseEmployee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class EmployeeEntityListToResponseEmpList {
    public List<ResponseEmployee> convert(List<EmployeeEntity> employeeEntities)
    {
        List<ResponseEmployee> responseEmployeeEntities = new ArrayList<ResponseEmployee>();
        employeeEntities.forEach((l)->{
            responseEmployeeEntities.add(new ResponseEmployee(l.getEmployeePK().getCompanyId(),l.getEmployeePK().getDepartmentId(),
                    l.getEmployeePK().getEmployeeId(), l.getEmpName(),l.getPhone(),l.getSalary())); });

        return responseEmployeeEntities;
    }
}
