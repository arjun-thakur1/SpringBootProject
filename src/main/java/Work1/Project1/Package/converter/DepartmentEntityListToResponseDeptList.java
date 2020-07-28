package Work1.Project1.Package.converter;

import Work1.Project1.Package.entity.DepartmentEntity;
import Work1.Project1.Package.response.ResponseDepartment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class DepartmentEntityListToResponseDeptList {

    public List<ResponseDepartment> convert(List<DepartmentEntity> departmentEntityList)
    {
        List<ResponseDepartment> responseDepartmentEntities = new ArrayList<ResponseDepartment>();
       departmentEntityList.forEach((l)->{
          responseDepartmentEntities.add(new ResponseDepartment(l.getDepartmentPK().getCompanyId(),
                   l.getDepartmentPK().getDepartmentId(), l.getDepartmentName(),l.getManagerId()));
       });

        return responseDepartmentEntities;
    }
}
