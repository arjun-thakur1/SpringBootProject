package work1.project1.package1.services;

import org.modelmapper.ModelMapper;
import work1.project1.package1.dto.request.DepartmentAddRequest;
import work1.project1.package1.dto.request.DepartmentUpdateRequestDto;
import work1.project1.package1.dto.response.*;
import work1.project1.package1.entity.*;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.mapper.MyMapper;
import work1.project1.package1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static work1.project1.package1.constants.ApplicationConstants.*;

@Service
@Transactional      //delete request is not performed without this annotation
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private EmployeeMappingRepository departmentEmployeeMappingRepository;

    @Autowired
    CompanyDepartmentMappingRepository companyDepartmentMappingRepository;
    @Autowired
    EmployeeMappingRepository employeeMappingRepository;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    MyMapper myMapper;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CompanyDepartmentMappingService mappingService;

    @Autowired
    GetDepartmentResponseDto responseGetDepartmentDto;

   // @Autowired
    //DepartmentEntityListToResponseDeptList departmentEntityListToResponseDeptList;
    @Autowired
    EmployeeMappingService employeeMappingService;
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CompanyDepartmentMappingRepository mappingRepository;

    public Object getAll(Long companyId) {
        boolean isPresent= companyRepository.existsByIdAndIsActive(companyId,true);
        if(isPresent) {
          List<CompanyDepartmentMappingEntity> mappingEntityList= mappingRepository.findAllByCompanyIdAndIsActive(companyId,true);
           if(mappingEntityList.isEmpty())
               return new Response(200,DEPARTMENT_NOT_PRESENT);
           List<DepartmentResponse> getResponseList=new ArrayList<>();
           mappingEntityList.forEach(mapp->{
               try {
                   getResponseList.add(getDepartmentDetail(mapp.getDepartmentId()));
               } catch (CustomException e) {
                   e.printStackTrace();
               }
           });
           return getResponseList;
        }
        return new Response(409,DEPARTMENT_NOT_PRESENT);
    }


    public Object addDepartment(DepartmentAddRequest requestDepartment) {
        Long companyId=requestDepartment.getCompanyId();
        Long departmentId=requestDepartment.getDepartmentId();
        String departmentName=requestDepartment.getDepartmentName();
        if(departmentId==-1 && departmentName==null)
            return new Response(400,FAILED);
        if(companyId==-1) {
            if(departmentName==null)
                return new Response(400,FAILED);
            departmentName=departmentName.toLowerCase();
            if(departmentRepository.existsByDepartmentName(departmentName)) {
                DepartmentEntity departmentEntity=departmentRepository.findByDepartmentName(departmentName);
                return new DepartmentResponse(departmentEntity.getId(),departmentName,ALREADY_PRESENT);
            }
            DepartmentEntity departmentEntity=new DepartmentEntity(departmentName.toLowerCase(),-1,-1,true);
            departmentRepository.save(departmentEntity);
            return modelMapper.map(departmentEntity, DepartmentResponse.class);
        }
        boolean isCompanyPresent=companyRepository.existsByIdAndIsActive(companyId,true);

        if( !isCompanyPresent ||(departmentId==-1 && departmentName==null )) {
            return new Response(404, ADD_FAILED);
        }
       if(departmentId!=-1) {  //dept id given
           Optional<DepartmentEntity> fetchedDepartmentEntity=departmentRepository.findById(departmentId);
           if(fetchedDepartmentEntity.isPresent()) {
               DepartmentEntity departmentEntity = fetchedDepartmentEntity.get();
               if(mappingService.add(companyId , departmentId)) {
                   return new DepartmentCompanyResponse(companyId, departmentId, departmentEntity.getDepartmentName(), SUCCESS);
               }
               return new DepartmentCompanyResponse(companyId,departmentEntity.getId(),departmentEntity.getDepartmentName(),ALREADY_PRESENT);
           }
           return new Response(404, ADD_FAILED);
       }
       else{   //companyId and departmentName given
           Optional<DepartmentEntity>fetchedDepartmentEntity= Optional.ofNullable(departmentRepository.findByDepartmentName(departmentName));
           departmentName=departmentName.toLowerCase();
           if(fetchedDepartmentEntity.isPresent()){
               DepartmentEntity departmentEntity=fetchedDepartmentEntity.get();
               departmentId=departmentEntity.getId();
               if(mappingService.add(companyId , departmentId)) {
                   return new DepartmentCompanyResponse(companyId, departmentId, departmentEntity.getDepartmentName(), SUCCESS);
               }
               return new DepartmentCompanyResponse(companyId,departmentEntity.getId(),departmentName,ALREADY_PRESENT);
           }
           DepartmentEntity departmentEntity=new DepartmentEntity(departmentName,-1,-1,true);
           departmentRepository.save(departmentEntity);
           if(mappingService.add(companyId , departmentEntity.getId())) {
               return new DepartmentCompanyResponse(companyId, departmentEntity.getId(), departmentName, SUCCESS);
           }
           return new Response(404, ADD_FAILED);
       }
}



    public DepartmentResponse getDepartmentDetail(Long departmentId) throws CustomException {
            Optional<DepartmentEntity> fetchedDepartmentEntity=departmentRepository.findById(departmentId);
            if(!fetchedDepartmentEntity.isPresent())
               throw new CustomException(FAILED);
            else
            {
                DepartmentEntity departmentEntity=fetchedDepartmentEntity.get();
                return    modelMapper.map(departmentEntity, DepartmentResponse.class);//responseGetDepartmentDto.convert(departmentEntity1);
            }
   }

    public Object getDepartmentOfCompany(Long companyId, Long departmentId) {
            Optional<DepartmentEntity> fetchedDepartmentEntity=departmentRepository.findById(departmentId);
            if(fetchedDepartmentEntity.isPresent()){
                if(mappingRepository.existsByCompanyIdAndDepartmentIdAndIsActive(companyId,departmentId,true))
                 {
                    DepartmentEntity departmentEntity=fetchedDepartmentEntity.get();
                    return new DepartmentCompanyResponse(companyId,departmentId,departmentEntity.getDepartmentName(),SUCCESS);
                 }
                return new Response(404,NOT_PRESENT);
            }
            return new Response(404,NOT_PRESENT);
    }




    public Object updateDetails(DepartmentUpdateRequestDto departmentRequestDto) {
            Long departmentId=departmentRequestDto.getDepartmentId();
            Optional<DepartmentEntity> fetchedDepartmentEntity=departmentRepository.findById(departmentId);
            if(fetchedDepartmentEntity.isPresent()){
                DepartmentEntity departmentEntity=fetchedDepartmentEntity.get();
                departmentEntity.setDepartmentName(departmentRequestDto.getDepartmentName());
                departmentRepository.save(departmentEntity);
                return modelMapper.map(departmentEntity,DepartmentResponse.class);
            }
        return new Response(204 , FAILED);
    }


    public Object deleteDepartmentDetails(Long companyId,Long departmentId) {
        CompanyDepartmentMappingEntity companyDepartmentMappingEntity=companyDepartmentMappingRepository.
             findByCompanyIdAndDepartmentIdAndIsActive(companyId,departmentId,true);
     if(companyDepartmentMappingEntity==null)
         return new Response(409,DEPARTMENT_NOT_PRESENT);

     List<EmployeeMappingEntity> employeeMappingEntityList= employeeMappingRepository.findByMappingIdAndIsActive
             (companyDepartmentMappingEntity.getId(),true);

     employeeMappingEntityList.forEach(e->{
         employeeService.deleteEmployee(e.getEmployeeId());
     });
     companyDepartmentMappingEntity.setActive(false);
     companyDepartmentMappingRepository.save(companyDepartmentMappingEntity);
     return  new DepartmentDeleteResponse(companyId,departmentId,DELETE_SUCCESS);
}


    public List<EmployeeCompleteResponse> getAllEmployeeOfDepartment(Long companyId,Long departmentId) {
        CompanyDepartmentMappingEntity companyDepartmentMappingEntity=companyDepartmentMappingRepository.
                findByCompanyIdAndDepartmentIdAndIsActive(companyId,departmentId,true);
        if(companyDepartmentMappingEntity==null)
            return null;

        List<EmployeeMappingEntity> employeeMappingEntityList= employeeMappingRepository.findByMappingIdAndIsActive
                (companyDepartmentMappingEntity.getId(),true);
        List<EmployeeCompleteResponse> employeeCompleteResponseList=new ArrayList<>();
        employeeMappingEntityList.forEach(e->{
            Optional<EmployeeEntity> fetchedEmployeeEntity=employeeRepository.findById(e.getEmployeeId());
            EmployeeEntity employeeEntity=fetchedEmployeeEntity.get();

            EmployeeCompleteResponse completeResponseDto=new EmployeeCompleteResponse();
            employeeCompleteResponseList.add(completeResponseDto.convert(employeeEntity,companyId,departmentId));
        });
        return employeeCompleteResponseList;

    }

/*

    public List<DepartmentEntity> getAllDepartmentsOfCompany(long companyId) {
        return departmentRepository.findAllByCompanyIdAndIsActive(companyId,true);
    }



    public boolean updateDepartmentSalary(Long departmentId, Long salary_increment, boolean flag) {
        List<EmployeeMappingEntity> departmentEmployeeMappingList=new ArrayList<>();
        if(!departmentRepository.existsByIdAndIsActive(departmentId,true))
            return false;
        departmentEmployeeMappingList=departmentEmployeeMappingRepository.findAllByDepartmentIdAndIsActive(departmentId,true);
        departmentEmployeeMappingList.forEach((departmentEmployeeMapping -> {
            Long employeeId=departmentEmployeeMapping.getEmployeeId();
            employeeService.updateEmployeeSalary(employeeId, salary_increment,flag);
        }));
        return true;
    }
    */
}
