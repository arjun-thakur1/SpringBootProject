package Work1.Project1.Package.converter;

import Work1.Project1.Package.entity.CompanyEntity;
import Work1.Project1.Package.entity.DepartmentEntity;
import Work1.Project1.Package.response.ResponseCompany;
import Work1.Project1.Package.response.ResponseDepartment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class CompanyEntityListToResponseCompanyList {

    public List<ResponseCompany> convert(List<CompanyEntity> companyEntities)
    {
        List<ResponseCompany> responseCompanies = new ArrayList<ResponseCompany>();
        companyEntities.forEach((l)->{
            responseCompanies.add(new ResponseCompany(l.getCompanyId(),
                    l.getCompanyName(), l.getCeoName()));
        });

        return responseCompanies;
    }
}
