package Work1.Project1.Package.services;

import Work1.Project1.Package.converter.CompanyEntityListToResponseCompanyList;
import Work1.Project1.Package.converter.DepartmentEntityListToResponseDeptList;
import Work1.Project1.Package.entity.CompanyEntity;
import Work1.Project1.Package.entity.DepartmentEntity;
import Work1.Project1.Package.entity.DepartmentPK;
import Work1.Project1.Package.exception.NotFoundException;
import Work1.Project1.Package.repository.DepartmentRepository;
import Work1.Project1.Package.response.ResponseCompany;
import Work1.Project1.Package.response.ResponseDepartment;
import Work1.Project1.Package.response.ResponseError;
import Work1.Project1.Package.request.RequestCompany;
import Work1.Project1.Package.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static Work1.Project1.Package.constants.ApplicationConstants.*;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DepartmentServices departmentServices;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CompanyEntityListToResponseCompanyList companyEntityListToResponseCompanyList;
    @Autowired
    private DepartmentEntityListToResponseDeptList departmentEntityListToResponseDeptList;

    public Object getAllDetails() throws NotFoundException{
       List<CompanyEntity>companyEntities= companyRepository.findAllByIsActive(true);
        List<ResponseCompany>responseCompanyList = companyEntityListToResponseCompanyList.convert(companyEntities);
       if(responseCompanyList.isEmpty())
           {
               ResponseError responseError =new ResponseError(200 , "No content!!");
             return responseError;
           }
       return responseCompanyList;
    }

    public Object addCompany(RequestCompany requestCompany) {
    //    long companyId =requestCompany.g
        if(companyRepository.existsByCompanyName(requestCompany.getCompanyName()))
          {
         //   if(companyRepository.findById())
           CompanyEntity companyEntity=   companyRepository.findByCompanyName(requestCompany.getCompanyName());
            if(companyEntity.getIsActive())
            return Duplicate_Name_Error;
            companyEntity.setActive(true);
            companyRepository.save(companyEntity);
            return Add_Success +" "+Company_ID +companyEntity.getCompanyId();
          }
       else {
           CompanyEntity companyEntity=new CompanyEntity(requestCompany.getCompanyName(), requestCompany.getCeoName(),true);
           try {
               this.companyRepository.save(companyEntity);
               return Add_Success +" "+Company_ID +companyEntity.getCompanyId();
           }catch(Exception e)
           {
               return Add_Failed;
           }
       }

    }



    public Object getCompanyDetails(Long id) throws NotFoundException{
       Optional<CompanyEntity>  companyEntity= Optional.ofNullable(companyRepository.findByCompanyIdAndIsActive(id, true));//.orElseThrow(() -> new NotFoundException(id));
         if(companyEntity.isPresent()) {
             CompanyEntity companyEntity1 = companyEntity.get();
             return new ResponseCompany(companyEntity1.getCompanyId(), companyEntity1.getCompanyName(), companyEntity1.getCeoName());
         }
         throw   new NotFoundException();
    }



    public ResponseError deleteCompanyDetails(Long company_id) {
             Optional<CompanyEntity> companyEntity=companyRepository.findById(company_id);
                if(companyEntity.isPresent()) {
                       CompanyEntity companyEntity1 = companyEntity.get();
                       companyEntity1.setActive(false);
                       companyRepository.save(companyEntity1);
                       List<DepartmentEntity> departmentEntityList=departmentRepository.findAllByDepartmentPKCompanyId(company_id);
                       departmentEntityList.forEach((d)->{
                           DepartmentPK departmentPK=new DepartmentPK(d.getDepartmentPK().getCompanyId(),d.getDepartmentPK().getDepartmentId());
                           try {
                               departmentServices.deleteDepartmentDetails(departmentPK);
                           } catch (Exception e) {
                               e.printStackTrace();
                           }
                       });
                         ResponseError responseError = new ResponseError(200, Delete_Success);
                         return responseError;
                     }
                else{
                    ResponseError responseError = new ResponseError(204, Failed);
                    return responseError;
                }

    }

    public ResponseError updateDetails(CompanyEntity updateCompanyEntity) throws  NotFoundException{
       long companyId=updateCompanyEntity.getCompanyId();
       Optional<CompanyEntity> companyEntity= Optional.ofNullable(companyRepository.findByCompanyIdAndIsActive(companyId, true));
        if(companyEntity.isPresent()) {
            CompanyEntity companyEntity1= companyEntity.get();
            if (updateCompanyEntity.getCompanyName() == null) {
                updateCompanyEntity.setCompanyName(companyEntity1.getCompanyName());
            }
            if (updateCompanyEntity.getCeoName() == null) {
                updateCompanyEntity.setCeoName(companyEntity1.getCeoName());
            }try {
                companyRepository.save(updateCompanyEntity);
            } catch (Exception e) {
                ResponseError responseError =new ResponseError(500 , Failed);
                return responseError;
            }
            ResponseError responseError =new ResponseError(500 , Update_Success);
            return responseError;
        }
        ResponseError responseError =new ResponseError(500 , Failed);
        return responseError;
    }

    public Object getCompanyCompleteDetails(long companyId) {
      Optional<CompanyEntity> companyEntity= Optional.ofNullable(companyRepository.findByCompanyIdAndIsActive(companyId, true));
      if(companyEntity.isPresent())
      {
         List<DepartmentEntity> departmentEntityList= departmentServices.getAllDepartmentsOfCompany(companyId);
          if(departmentEntityList.isEmpty()) {
              ResponseError responseError = new ResponseError(200, "No Department Present with given company Id!!");
              return responseError;
          }
          else {
              List<ResponseDepartment>responseDepartmentList= new ArrayList<>();
              return departmentEntityListToResponseDeptList.convert(departmentEntityList);
          }
      }
        ResponseError responseError =new ResponseError(200 , "Company not found!!");
        return responseError;
    }


}
