package Work1.Project1.Package.services;

import Work1.Project1.Package.converter.CompanyEntityListToResponseCompanyList;
import Work1.Project1.Package.converter.DepartmentEntityListToResponseDeptList;
import Work1.Project1.Package.entity.CompanyEntity;
import Work1.Project1.Package.entity.DepartmentEntity;
import Work1.Project1.Package.entity.DepartmentPK;
import Work1.Project1.Package.exception.CustomException;
import Work1.Project1.Package.repository.DepartmentRepository;
import Work1.Project1.Package.response.ResponseCompany;
import Work1.Project1.Package.response.ResponseDepartment;
import Work1.Project1.Package.response.Response;
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

    public Object getAllDetails() throws CustomException {
       List<CompanyEntity>companyEntities= companyRepository.findAllByIsActive(true);
        List<ResponseCompany>responseCompanyList = companyEntityListToResponseCompanyList.convert(companyEntities);
        if(responseCompanyList.isEmpty())
           {
               throw new CustomException(Not_Present);
           }
       return responseCompanyList;
    }

    public Object addCompany(RequestCompany requestCompany) {
        if(companyRepository.existsByCompanyName(requestCompany.getCompanyName()))
          {
           CompanyEntity companyEntity=   companyRepository.findByCompanyName(requestCompany.getCompanyName());
           if(companyEntity.getIsActive())
               return   new Response(404, Duplicate_Name_Error);
           companyEntity.setActive(true);
           companyRepository.save(companyEntity);
           return new Response(201, Add_Success +" "+Company_ID +companyEntity.getCompanyId());
          }
       else {
           CompanyEntity companyEntity=new CompanyEntity(requestCompany.getCompanyName(),requestCompany.getCeoName(),true);
               this.companyRepository.save(companyEntity);
               return new Response( 201, Add_Success +" "+Company_ID +companyEntity.getCompanyId());
       }
    }



    public Object getCompanyDetails(Long id) throws CustomException {
       Optional<CompanyEntity>  companyEntity= Optional.ofNullable(companyRepository.findByCompanyIdAndIsActive(id, true));//.orElseThrow(() -> new NotFoundException(id));
         if(companyEntity.isPresent()) {
             CompanyEntity companyEntity1 = companyEntity.get();
             return new ResponseCompany(companyEntity1.getCompanyId(), companyEntity1.getCompanyName(), companyEntity1.getCeoName());
         }
        throw   new CustomException();
    }



    public Response deleteCompanyDetails(Long company_id) throws CustomException {
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
                return new Response(200, Delete_Success);
                     }
                throw new CustomException(Failed);
    }

    public Response updateDetails(CompanyEntity updateCompanyEntity) throws CustomException {
       long companyId=updateCompanyEntity.getCompanyId();
       Optional<CompanyEntity> companyEntity= Optional.ofNullable(companyRepository.findByCompanyIdAndIsActive(companyId, true));
        if(companyEntity.isPresent()) {
            CompanyEntity companyEntity1= companyEntity.get();
            String companyName=updateCompanyEntity.getCompanyName();
            if (companyName == null) {
                updateCompanyEntity.setCompanyName(companyEntity1.getCompanyName());
            }
            else if(!companyName.equals(companyEntity1.getCompanyName()))//check for unique company name , if not same as present means want to update
            {     //System.out.println(companyName+"              "+companyEntity1.getCompanyName());
                if(companyRepository.existsByCompanyName(companyName))
                    throw new CustomException(Duplicate_Name_Error);
            }                  //companyEntity.setCeoName(Optional.ofNullable(updateCompanyEntity.getCeoName()).orElseGet(companyEntity.getCeoName())
            if (updateCompanyEntity.getCeoName() == null) {
                updateCompanyEntity.setCeoName(companyEntity1.getCeoName());
            }
            companyRepository.save(updateCompanyEntity);
           return new Response(500 , Update_Success);
        }
        throw  new CustomException(Failed);
    }



    public Object getCompanyCompleteDetails(long companyId){
      Optional<CompanyEntity> companyEntity= Optional.ofNullable(companyRepository.findByCompanyIdAndIsActive(companyId, true));
      if(companyEntity.isPresent())
      {
         List<DepartmentEntity> departmentEntityList= departmentServices.getAllDepartmentsOfCompany(companyId);
              if(departmentEntityList.isEmpty()) {
                 return new Response(204, Not_Dept_Present);
          }
          else {
              List<ResponseDepartment>responseDepartmentList= new ArrayList<>();
              return departmentEntityListToResponseDeptList.convert(departmentEntityList);
          }
      }
     return new Response(200 , Not_Present);
    }
}
