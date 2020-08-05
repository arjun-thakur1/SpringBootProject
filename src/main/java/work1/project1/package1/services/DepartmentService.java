package work1.project1.package1.services;

import work1.project1.package1.dto.request.DepartmentAddRequestDto;
import work1.project1.package1.dto.request.DepartmentUpdateRequestDto;
import work1.project1.package1.dto.response.EmployeeCompleteResponseDto;
import work1.project1.package1.dto.response.GetDepartmentResponseDto;
import work1.project1.package1.dto.response.Response;
import work1.project1.package1.entity.*;
import work1.project1.package1.mapper.MyMapper;
import work1.project1.package1.repository.CompanyRepository;
import work1.project1.package1.repository.DepartmentRepository;
import work1.project1.package1.repository.DepartmentEmployeeMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work1.project1.package1.repository.EmployeeRepository;

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
    private DepartmentEmployeeMappingRepository departmentEmployeeMappingRepository;

    @Autowired
    MyMapper myMapper;
    @Autowired
    GetDepartmentResponseDto responseGetDepartmentDto;

   // @Autowired
    //DepartmentEntityListToResponseDeptList departmentEntityListToResponseDeptList;
    @Autowired
    EmployeeMappingService employeeMappingService;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    EmployeeService employeeService;

    public Object getAll(Long companyId) {
        CompanyEntity companyEntity= companyRepository.findByIdAndIsActive(companyId,true);
        if(companyEntity!=null) {
         //  List<DepartmentResponseDto> departmentDtoArrayList = new ArrayList<DepartmentResponseDto>();
           List<DepartmentEntity>departmentEntityList= departmentRepository.findAllByCompanyIdAndIsActive(companyId,true);
           if(departmentEntityList.isEmpty())
               return new Response(200,DEPARTMENT_NOT_PRESENT);
           return myMapper.convert(departmentEntityList,SUCCESS);
        }
        return new Response(400,FAILED);
    }


    public Object addDepartment(DepartmentAddRequestDto requestDepartment) {
        Long companyId=requestDepartment.getCompanyId();
        boolean companyPresent=companyRepository.existsByIdAndIsActive(companyId,true);
        if(!companyPresent)
             return new Response(404,ADD_FAILED);
       // String departmentName=null;
        //if(requestDepartment.getDepartmentName()!=null)
        String departmentName=requestDepartment.getDepartmentName().toLowerCase();
        if(departmentRepository.existsByCompanyIdAndDepartmentName(companyId ,departmentName))
        {
            DepartmentEntity departmentEntity= departmentRepository.findByCompanyIdAndDepartmentName(companyId,departmentName);
             if(departmentEntity.getIsActive())
                 return new Response(404,ADD_FAILED);
             departmentEntity.setActive(true);
             departmentRepository.save(departmentEntity);
             return  myMapper.convert(departmentEntity,ADD_SUCCESS);
        }
           DepartmentEntity departmentEntity = new DepartmentEntity(departmentName,companyId,-1,-1,true);
           this.departmentRepository.save(departmentEntity);
           return  myMapper.convert(departmentEntity,ADD_SUCCESS);
}



    public Object getDepartmentDetail(Long departmentId) {
            Optional<DepartmentEntity> departmentEntity=departmentRepository.findByIdAndIsActive(departmentId,true);
            if(!departmentEntity.isPresent())
               return  new Response(404,FAILED) ;
            else
            {
                DepartmentEntity departmentEntity1=departmentEntity.get();
                return responseGetDepartmentDto.convert(departmentEntity1);
            }
   }

    public Object deleteDepartmentDetails(Long departmentId) throws Exception{
        Optional<DepartmentEntity> departmentEntity=departmentRepository.findByIdAndIsActive(departmentId,true);
        if(departmentEntity.isPresent()) {
            DepartmentEntity departmentEntity1 = departmentEntity.get();
            departmentEntity1.setActive(false);
            departmentRepository.save(departmentEntity1);
          /*  List<EmployeeEntity> employeeEntityList=employeeRepository.findAllByEmployeePKCompanyIdAndEmployeePKDepartmentId
                    (departmentPK.getCompanyId(),departmentPK.getDepartmentId());
            employeeEntityList.forEach((e)->{
                try {
                    employeeServices.deleteEmployeeDetails(e.getEmployeePK().getCompanyId(),e.getEmployeePK().getDepartmentId(),
                            e.employeePK.getEmployeeId());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });*/
            return myMapper.convert(departmentEntity1,DELETE_SUCCESS);//new Response(200, Delete_Success);
        }
      return new Response(404, FAILED);
}

    public Object updateDetails(DepartmentUpdateRequestDto departmentRequestDto) {
        Long departmentId=departmentRequestDto.getDepartmentId();
        String departmentName=departmentRequestDto.getDepartmentName();
        Long companyId=departmentRequestDto.getCompanyId();
        if(departmentName!=null)
            departmentName=departmentName.toLowerCase();
        Optional<DepartmentEntity> fetchedDepartmentEntity=departmentRepository.findByIdAndIsActive(departmentId,true);
        if(fetchedDepartmentEntity.isPresent())
        {
            DepartmentEntity departmentEntity=fetchedDepartmentEntity.get();
            if(departmentName!=null)
            {
                departmentEntity.setDepartmentName(departmentName);
            }
            if(companyId!=-1) {
                if (!companyRepository.existsByIdAndIsActive(companyId, true))   //if transfer of department in another company,update
                {
                    return new Response(204, FAILED);
                }
                departmentEntity.setCompanyId(companyId);
            }
            departmentRepository.save(departmentEntity);
            return    myMapper.convert(departmentEntity,UPDATE_SUCCESS);//new Response(200 , UPDATE_SUCCESS);
        }
        return new Response(204 , FAILED);
    }

    public List<DepartmentEntity> getAllDepartmentsOfCompany(long companyId) {
        return departmentRepository.findAllByCompanyIdAndIsActive(companyId,true);
    }

    public List<EmployeeCompleteResponseDto> getAllEmployeeOfDepartment(Long departmentId) {
        List<DepartmentEmployeeMapping> departmentEmployeeMappingList=departmentEmployeeMappingRepository.
                findAllByDepartmentIdAndIsActive(departmentId,true);
        List<EmployeeCompleteResponseDto> employeeCompleteDto=new ArrayList<>();
        departmentEmployeeMappingList.forEach((d)-> {
                    Optional<EmployeeEntity> employeeEntity=employeeRepository.findById(d.getEmployeeId());

                    if(employeeEntity.isPresent())
                    {
                        EmployeeEntity employeeEntity1=employeeEntity.get();
                        employeeCompleteDto.add(myMapper.convert(employeeEntity1,d));
                    }
                }
        );
        return employeeCompleteDto;
    }

    public boolean updateDepartmentSalary(Long departmentId, Long salary_increment, boolean flag) {
        List<DepartmentEmployeeMapping> departmentEmployeeMappingList=new ArrayList<>();
        if(!departmentRepository.existsByIdAndIsActive(departmentId,true))
            return false;
        departmentEmployeeMappingList=departmentEmployeeMappingRepository.findAllByDepartmentIdAndIsActive(departmentId,true);
        departmentEmployeeMappingList.forEach((departmentEmployeeMapping -> {
            Long employeeId=departmentEmployeeMapping.getEmployeeId();
            employeeService.updateEmployeeSalary(employeeId, salary_increment,flag);
        }));
        return true;
    }
}
