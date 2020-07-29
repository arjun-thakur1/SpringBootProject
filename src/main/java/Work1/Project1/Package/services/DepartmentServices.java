package Work1.Project1.Package.services;

import Work1.Project1.Package.converter.DepartmentEntityListToResponseDeptList;
import Work1.Project1.Package.entity.*;
import Work1.Project1.Package.exception.CustomException;
import Work1.Project1.Package.repository.CompanyRepository;
import Work1.Project1.Package.repository.DepartmentRepository;
import Work1.Project1.Package.repository.EmployeeRepository;
import Work1.Project1.Package.response.Response;
import Work1.Project1.Package.request.RequestDepartment;
import Work1.Project1.Package.response.ResponseDepartment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static Work1.Project1.Package.constants.ApplicationConstants.*;

@Service
@Transactional      //delete request is not performed without this annotation
public class DepartmentServices {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private EmployeeRepository employeeRepository;


    @Autowired
    DepartmentEntityListToResponseDeptList departmentEntityListToResponseDeptList;
    @Autowired
    EmployeeServices employeeServices;

    public Object getAllDetails() {
            List<CompanyEntity> allCompanyEntity= companyRepository.findAllByIsActive(true);
            HashMap<Long,List<ResponseDepartment>> mapp= new HashMap<Long, List<ResponseDepartment> >();
            allCompanyEntity.forEach((l)->{
               List<DepartmentEntity> departmentEntities= departmentRepository.findAllByDepartmentPKCompanyIdAndIsActive(l.getCompanyId(),true);
               List<ResponseDepartment> responseDepartmentEntities= departmentEntityListToResponseDeptList.convert(departmentEntities);
                        mapp.put(l.getCompanyId() , responseDepartmentEntities);
                    }
                    );
            if(mapp.isEmpty())
              return new Response(204 , "No content!!");

           return  mapp;
    }


    public Response addDepartment(RequestDepartment requestDepartment) {
//
        long companyId=requestDepartment.getCompanyId();
        boolean companyPresent=companyRepository.existsById(companyId);
         if(!companyPresent)
             return new Response(404,Add_Failed);
        String departmentName=requestDepartment.getDepartmentName();
        if(departmentRepository.existsByDepartmentPKCompanyIdAndDepartmentName(companyId ,departmentName))
        {
            DepartmentEntity departmentEntity= departmentRepository.findByDepartmentPKCompanyIdAndDepartmentName(companyId,departmentName);
             if(departmentEntity.getIsActive())
                 return new Response(404,Add_Failed);
             departmentEntity.setActive(true);
             departmentEntity.setManagerId(-1);
             departmentRepository.save(departmentEntity);
             return new Response(201,"Department with ID " + departmentEntity.getDepartmentPK().getDepartmentId() + Add_Success);
        }
           long departmentId= departmentRepository.countByDepartmentPKCompanyId(requestDepartment.getCompanyId());
           DepartmentPK departmentPK = new DepartmentPK(companyId, departmentId);
           DepartmentEntity departmentEntity = new DepartmentEntity(departmentPK, departmentName,true,-1);
           this.departmentRepository.save(departmentEntity);
           return new Response(201,"Department with ID " + departmentEntity.getDepartmentPK().getDepartmentId() + Add_Success);
    }



    public Object getDepartmentDetail(Long departmentId, Long companyId) {
        DepartmentPK departmentPK = new  DepartmentPK(companyId,departmentId);
            Optional<DepartmentEntity> departmentEntity=departmentRepository.findByDepartmentPKAndIsActive(departmentPK,true);
            if(!departmentEntity.isPresent())
               return  new Response(404,Failed) ;
            else
            {
                DepartmentEntity departmentEntity1=departmentEntity.get();
                return (new ResponseDepartment(departmentEntity1.getDepartmentPK().getCompanyId(),
                        departmentEntity1.getDepartmentPK().getDepartmentId(),departmentEntity1.getDepartmentName(),
                        departmentEntity1.getManagerId()));
            }

    }

    public Object deleteDepartmentDetails(DepartmentPK departmentPK) throws Exception{
        Optional<DepartmentEntity> departmentEntity=departmentRepository.findByDepartmentPKAndIsActive(departmentPK,true);
        if(departmentEntity.isPresent()) {
            DepartmentEntity departmentEntity1 = departmentEntity.get();
            departmentEntity1.setActive(false);
            departmentEntity1.setManagerId(-1);
            departmentRepository.save(departmentEntity1);
            List<EmployeeEntity> employeeEntityList=employeeRepository.findAllByEmployeePKCompanyIdAndEmployeePKDepartmentId
                    (departmentPK.getCompanyId(),departmentPK.getDepartmentId());
            employeeEntityList.forEach((e)->{
                try {
                    employeeServices.deleteEmployeeDetails(e.getEmployeePK().getCompanyId(),e.getEmployeePK().getDepartmentId(),
                            e.employeePK.getEmployeeId());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
            return new Response(200, Delete_Success);
        }
      return new Response(404, Failed);
}

    public Response updateDetails(long companyId, long departmentId, String departmentName, long managerId) {
       DepartmentPK departmentPK= new DepartmentPK(companyId,departmentId);
        DepartmentEntity updateDepartmentEntity=new DepartmentEntity( departmentPK, departmentName,true,managerId);
        if(!departmentRepository.existsByDepartmentPKAndIsActive(departmentPK,true)) {
            return new Response(204, Failed);
        }
        Optional<DepartmentEntity> fetchedDepartmentEntity=departmentRepository.findById(updateDepartmentEntity.getDepartmentPK());
        if(fetchedDepartmentEntity.isPresent())
        {
            DepartmentEntity departmentEntity=fetchedDepartmentEntity.get();
            if(updateDepartmentEntity.getDepartmentName()==null)    //if update dept name not given so existing name
            {
                updateDepartmentEntity.setDepartmentName(departmentEntity.getDepartmentName());
            }
            if(updateDepartmentEntity.getManagerId()==-1)   //if updated manager id not given then existing
            {
                updateDepartmentEntity.setManagerId(departmentEntity.getManagerId());
            }
            departmentRepository.save(updateDepartmentEntity);
            return new Response(200 , Update_Success);
        }

        return new Response(204 , Failed);
    }

    public List<DepartmentEntity> getAllDepartmentsOfCompany(long companyId) {
        return departmentRepository.findAllByDepartmentPKCompanyIdAndIsActive(companyId,true);
    }

}
