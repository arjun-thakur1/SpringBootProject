package work1.project1.package1.services;

import org.modelmapper.ModelMapper;
import work1.project1.package1.dto.response.CompanyDeleteResponse;
import work1.project1.package1.dto.response.CompanyResponse;
import work1.project1.package1.dto.response.EmployeeCompleteResponse;
import work1.project1.package1.dto.response.Response;
import work1.project1.package1.entity.CompanyDepartmentMappingEntity;
import work1.project1.package1.entity.CompanyEntity;

import work1.project1.package1.entity.EmployeeEntity;
import work1.project1.package1.mapper.MyMapper;
import work1.project1.package1.repository.CompanyDepartmentMappingRepository;
import work1.project1.package1.repository.DepartmentRepository;
import work1.project1.package1.dto.request.CompanyAddRequest;
import work1.project1.package1.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static work1.project1.package1.constants.ApplicationConstants.*;

@Service
//@CacheConfig(cacheNames={"company_cache"})
public class CompanyService {

    private static final long serialVersionUID = 7156526077883281623L;
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentRepository departmentRepository;

   @Autowired
   private EmployeeService employeeService;

    @Autowired
    MyMapper myMapper; //................................................................remove

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CompanyDepartmentMappingService companyDepartmentMappingService;

   @Autowired
    CompanyDepartmentMappingRepository mappingRepository;

    @Autowired
    Caching caching;

    public Object getAll()  {
       List<CompanyEntity>companyEntityList= companyRepository.findAllByIsActive(true);
       if(!companyEntityList.isEmpty()) {
           List<CompanyResponse> companyResponseList=new ArrayList<>();
           companyEntityList.forEach((c)->{
               companyResponseList.add(modelMapper.map(c,CompanyResponse.class));
           });
           return  companyResponseList;
       }
        return new Response(404,NOT_PRESENT);
    }

    public Object getCompanyById(Long id) {
        CompanyEntity companyEntity= companyRepository.findByIdAndIsActive(id, true);
        if(companyEntity!=null) {
            return      modelMapper.map(companyEntity,CompanyResponse.class);//responseGetCompanyDto.convert(companyEntity);
        }
        return new Response(404,NOT_PRESENT);
    }


    public Object addCompanyDetail(CompanyAddRequest companyAddRequest) {
        String companyName= companyAddRequest.getCompanyName().toLowerCase();
        String ceoName=companyAddRequest.getCeoName();     //=null; by default already build null
        if(companyAddRequest.getCeoName()!=null)
        {
           ceoName=companyAddRequest.getCeoName().toLowerCase();
        }
        if(companyRepository.existsByCompanyName(companyName)) //company name is unique
        {    //company name unique so in lower-case
           CompanyEntity companyEntity=   companyRepository.findByCompanyName(companyName);
           if(companyEntity.getIsActive())
               return new Response(409, DUPLICATE_NAME_ERROR);
           companyEntity.setActive(true);
           companyRepository.save(companyEntity);
           return    modelMapper.map(companyEntity, CompanyResponse.class);
        }
        else
        {
           CompanyEntity companyEntity=new CompanyEntity(companyName,ceoName,-1,-1,true);
           this.companyRepository.save(companyEntity);
           return modelMapper.map(companyEntity, CompanyResponse.class);
        }
    }

    public Object deleteCompanyDetails(Long companyId)  {
        CompanyEntity companyEntity=companyRepository.findByIdAndIsActive(companyId,true);
        if(companyEntity!=null) {
            companyEntity.setActive(false);
            companyRepository.save(companyEntity);
            // caching.deleteCompany(company_id);
            List<CompanyDepartmentMappingEntity> companyDepartmentMappingEntityList=mappingRepository.
                    findAllByCompanyIdAndIsActive(companyId,true);
            companyDepartmentMappingEntityList.forEach((d)->{
                    departmentService.deleteDepartmentDetails(companyId,d.getDepartmentId());

            });
            return  new CompanyDeleteResponse((long) 200,DELETE_SUCCESS);
        }
       return new Response(409,NOT_PRESENT);
    }

    public Object updateDetails(long  companyId, String company, String ceo){
        String companyName=null,ceoName=null;
        if(company!=null)
            companyName=company.toLowerCase();
        if(ceo!=null)
            ceoName=ceo.toLowerCase();
        Optional<CompanyEntity> fetchedCompanyEntity= Optional.ofNullable(companyRepository.findByIdAndIsActive(companyId, true));
        if(fetchedCompanyEntity.isPresent()) {
            CompanyEntity companyEntity= fetchedCompanyEntity.get();
            if(companyName!=null && !companyName.equals(companyEntity.getCompanyName()))//check for unique company name , if not same as present means want to update
            {
                if(companyRepository.existsByCompanyName(companyName))
                    return new Response(409,DUPLICATE_NAME_ERROR);
                companyEntity.setCompanyName(companyName);
            }
            if (ceoName!=null) {
                companyEntity.setCeoName(ceoName);
            }    //   caching.update(companyId,updateCompanyEntity);
            companyRepository.save(companyEntity);
            return   new CompanyResponse(companyId,companyEntity.getCompanyName(),companyEntity.getCeoName(),UPDATE_SUCCESS) ;//new Response(500 , Update_Success);
        }
        return new Response(404,FAILED);
    }

   // @Cacheable(value = "company_cache",key="#id")






    public Object getallEmployeesOfCompany(Long companyId) {
        List<CompanyDepartmentMappingEntity> cdMappingEntity=companyDepartmentMappingService.getAllDepartmentOfCompany
                (companyId);
        if(cdMappingEntity==null )
            return new Response(404,FAILED);
        if( cdMappingEntity.isEmpty())
            return new Response(200,NOT_PRESENT);
        HashMap<Long,List<EmployeeCompleteResponse>> mapp= new HashMap<>();
        cdMappingEntity.forEach(d->{
          List<EmployeeCompleteResponse> employeeEntityList =departmentService.getAllEmployeeOfDepartment(d.getCompanyId(),d.getDepartmentId());
          mapp.put(d.getDepartmentId(),employeeEntityList);
        });
       return mapp;
    }
/*

/*
    public Object getCompanyCompleteDetails(Long companyId){
      Optional<CompanyEntity> companyEntity= Optional.ofNullable(companyRepository.findByCompanyIdAndIsActive(companyId, true));
      if(companyEntity.isPresent())
      {
         List<DepartmentEntity> departmentEntityList= departmentServices.getAllDepartmentsOfCompany(companyId);
              if(departmentEntityList.isEmpty()) {
                 return new Response(204, Not_Dept_Present);
              }
              else {
                 List<DepartmentCompanyResponse>responseDepartmentList= new ArrayList<>();
                 return departmentEntityListToResponseDeptList.convert(departmentEntityList);
              }
      }
     return new Response(200 , NOT_PRESENT);
    }
*/
}
