package work1.project1.package1.services;

import work1.project1.package1.dto.request.UpdateSalaryRequestDto;
import work1.project1.package1.dto.response.EmployeeCompleteResponseDto;
import work1.project1.package1.dto.response.GetCompanyResponseDto;
import work1.project1.package1.dto.response.Response;
import work1.project1.package1.entity.CompanyEntity;
import work1.project1.package1.entity.DepartmentEntity;
import work1.project1.package1.exception.CustomException;

import work1.project1.package1.mapper.MyMapper;
import work1.project1.package1.repository.DepartmentRepository;
import work1.project1.package1.dto.request.CompanyAddRequestDto;
import work1.project1.package1.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    MyMapper myMapper;

    @Autowired
    GetCompanyResponseDto responseGetCompanyDto;


    @Autowired
    Caching caching;

    public Object getAll() throws CustomException {
       List<CompanyEntity>companyEntityList= companyRepository.findAllByIsActive(true);
       if(!companyEntityList.isEmpty()) {
           List<GetCompanyResponseDto> responseCompanyList = responseGetCompanyDto.convertList(companyEntityList);
           return responseCompanyList;
       }
       throw new CustomException(NOT_PRESENT);
    }

    public Object getCompanyById(Long id) throws CustomException {
        CompanyEntity companyEntity= companyRepository.findByIdAndIsActive(id, true);
        if(companyEntity!=null) {
            return  responseGetCompanyDto.convert(companyEntity);
        }
        throw   new CustomException();
    }


    public Object addCompanyDetail(CompanyAddRequestDto companyDto) {
        String companyName= companyDto.getCompanyName().toLowerCase();
        String ceoName=null;
       if(companyDto.getCeoName()!=null) {
          ceoName=companyDto.getCeoName().toLowerCase();
       }

        if(companyRepository.existsByCompanyName(companyName)) //company name is unique
          {
               //company name unique so in lower-case
           CompanyEntity companyEntity=   companyRepository.findByCompanyName(companyName);
           if(companyEntity.getIsActive())
               return   new Response(404, DUPLICATE_NAME_ERROR);
           companyEntity.setActive(true);
           companyRepository.save(companyEntity);
           return    myMapper.convert(companyEntity,ADD_SUCCESS);
          }
        else {
           CompanyEntity companyEntity=new CompanyEntity(companyName,ceoName,-1,-1,true);
               this.companyRepository.save(companyEntity);
               return     myMapper.convert(companyEntity,ADD_SUCCESS);
        }
    }


   // @Cacheable(value = "company_cache",key="#id")



    public Object deleteCompanyDetails(Long companyId) throws CustomException {
           CompanyEntity companyEntity=companyRepository.findByIdAndIsActive(companyId,true);
           if(companyEntity!=null) {
               companyEntity.setActive(false);
               companyRepository.save(companyEntity);
               // caching.deleteCompany(company_id);
               List<DepartmentEntity> departmentEntityList=departmentRepository.findAllByCompanyId(companyId);
               departmentEntityList.forEach((d)->{
                           try {
                               departmentService.deleteDepartmentDetails(d.getId());
                           } catch (Exception e) {
                               e.printStackTrace();
                           }
                       });
                return  myMapper.convert(companyEntity,DELETE_SUCCESS);
                     }
                throw new CustomException(DELETE_FAILED);
    }

    public Object updateDetails(long  companyId, String company, String ceo)throws CustomException{
        String companyName=null,ceoName=null;
        if(company!=null)
        companyName=company.toLowerCase();
        if(ceo!=null)
        ceoName=ceo.toLowerCase();

        CompanyEntity updateCompanyEntity =new CompanyEntity(companyId,companyName,ceoName,-1,-1,true);
        // long companyId=updateCompanyEntity.getCompanyId();
        Optional<CompanyEntity> companyEntity= Optional.ofNullable(companyRepository.findByIdAndIsActive(companyId, true));
        if(companyEntity.isPresent()) {
            CompanyEntity companyEntity1= companyEntity.get();
         //   String companyName=updateCompanyEntity.getCompanyName();
            if (companyName==null ){
                updateCompanyEntity.setCompanyName(companyEntity1.getCompanyName());
            }
            else if(!companyName.equals(companyEntity1.getCompanyName()))//check for unique company name , if not same as present means want to update
            {
                if(companyRepository.existsByCompanyName(companyName))
                    throw new CustomException(DUPLICATE_NAME_ERROR);
            }                  //companyEntity.setCeoName(Optional.ofNullable(updateCompanyEntity.getCeoName()).orElseGet(companyEntity.getCeoName())
            if (ceoName==null) {
                updateCompanyEntity.setCeoName(companyEntity1.getCeoName());
            }
         //   caching.update(companyId,updateCompanyEntity);
            companyRepository.save(updateCompanyEntity);
           return   myMapper.convert(updateCompanyEntity,UPDATE_SUCCESS) ;//new Response(500 , Update_Success);
        }
        throw  new CustomException(FAILED);
    }

    public Object getallEmployeesOfCompany(Long companyId) {
        List<DepartmentEntity>departmentEntityList= departmentRepository.findAllByCompanyIdAndIsActive(companyId,true);
        if(departmentEntityList.isEmpty())
        {
            return new Response(200,DEPARTMENT_NOT_PRESENT);
        }
        HashMap<Long,List<EmployeeCompleteResponseDto>> deptIdToEmpMapp=new HashMap<>();
        departmentEntityList.forEach((d)->{
            List<EmployeeCompleteResponseDto> employeeCompleteDtoList=departmentService.getAllEmployeeOfDepartment(d.getId());
            deptIdToEmpMapp.put(d.getId(),employeeCompleteDtoList);
        });
        return deptIdToEmpMapp;
    }

    public Object updateSalary(UpdateSalaryRequestDto requestSalaryDto) {
        Long companyId=requestSalaryDto.getCompanyId();
        Long departmentId=requestSalaryDto.getDepartmentId();
        Long employeeId=requestSalaryDto.getEmployeeId();
        Long salary_increment=requestSalaryDto.getSalary();
        boolean flag=requestSalaryDto.isFlag();

        if(companyId!=-1)
        {  //update salary for all employees of company
           if( updateCompanySalary(companyId,salary_increment,flag))
               return new Response(200,UPDATE_SUCCESS);
           else
               return new Response(400,FAILED);
        }
        else if(departmentId!=-1)
        {
            if(departmentService.updateDepartmentSalary(departmentId,salary_increment,flag))
                return new Response(200,UPDATE_SUCCESS);
            else
                return new Response(400,FAILED);
        }
        else if(employeeId!=-1)
        {
             if(employeeService.updateEmployeeSalary(employeeId,salary_increment,flag))
                 return new Response(200,UPDATE_SUCCESS);
             else
                 return new Response(400,FAILED);
        }
        return new Response(400,FAILED);
    }

    public boolean updateCompanySalary(Long companyId,Long salary_increment,boolean flag){
        if(!companyRepository.existsByIdAndIsActive(companyId,true))
            return false;
        List<DepartmentEntity>departmentEntityList=departmentRepository.findAllByCompanyIdAndIsActive(companyId,true);
        departmentEntityList.forEach((d)->{
            departmentService.updateDepartmentSalary(d.getId(),salary_increment,flag);
        });
        return true;
    }

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
                 List<DepartmentResponseDto>responseDepartmentList= new ArrayList<>();
                 return departmentEntityListToResponseDeptList.convert(departmentEntityList);
              }
      }
     return new Response(200 , NOT_PRESENT);
    }
*/
}
