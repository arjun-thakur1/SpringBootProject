package work1.project1.package1.services;

import work1.project1.package1.entity.CompanyDepartmentMappingEntity;
import work1.project1.package1.entity.EmployeeMappingEntity;
import work1.project1.package1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Component
@Transactional   //( Propagation.REQUIRES_NEW) //for cache put update in redis
public class EmployeeMappingService {

    @Autowired
    EmployeeMappingRepository employeeMappingRepository;
    @Autowired
    CompanyDepartmentMappingRepository companyDepartmentMappingRepository;



    public void add(Long mappingId, Long employeeId) {
        EmployeeMappingEntity employeeMapping=new EmployeeMappingEntity(mappingId,employeeId,-1,-1,true);
        employeeMappingRepository.save(employeeMapping);
    }

    public CompanyDepartmentMappingEntity getIds(Long employeeId) {
        EmployeeMappingEntity employeeMapping= employeeMappingRepository.findByEmployeeIdAndIsActive(employeeId,true);

        if(employeeMapping!=null)
        {
           return companyDepartmentMappingRepository.findByIdAndIsActive(employeeMapping.getMappingId(),true);
        }
        return null;
    }

    public boolean updateDetails(Long companyId, Long departmentId, Long employeeId) {
        if(companyId==-1) //only want to change department within a company
        {
            EmployeeMappingEntity activeEmployeeMappingEntity= employeeMappingRepository.findByEmployeeIdAndIsActive(employeeId,true);
            if(activeEmployeeMappingEntity==null) {
                return false;
            }
               Optional<CompanyDepartmentMappingEntity> fetchedEntity=companyDepartmentMappingRepository.findById(activeEmployeeMappingEntity.getMappingId());//mapping id is PK in ComapnyDept_repo
               CompanyDepartmentMappingEntity entity=fetchedEntity.get();
               companyId=entity.getCompanyId(); //finds companyID for given dept
        }
       CompanyDepartmentMappingEntity companyDepartmentMappingEntity= companyDepartmentMappingRepository.
               findByCompanyIdAndDepartmentIdAndIsActive(companyId,departmentId,true);
        if(companyDepartmentMappingEntity==null)
            return false;
        Long companyDepartmentMappingId=companyDepartmentMappingEntity.getId();
        EmployeeMappingEntity activeEmployeeMappingEntity= employeeMappingRepository.findByEmployeeIdAndIsActive(employeeId,true);
        if(activeEmployeeMappingEntity!=null)
        {
            activeEmployeeMappingEntity.setActive(false);
            employeeMappingRepository.save(activeEmployeeMappingEntity);
        }
         EmployeeMappingEntity employeeMappingEntity=employeeMappingRepository.findByEmployeeIdAndMappingId(employeeId,
                companyDepartmentMappingId);
        if(employeeMappingEntity!=null) //want to add in same dept
        {
            employeeMappingEntity.setActive(true);
            employeeMappingRepository.save(employeeMappingEntity);
            return true;
        }
        EmployeeMappingEntity newEmployeeMappingEntity=new EmployeeMappingEntity(companyDepartmentMappingId,employeeId,-1,-1,true);
        employeeMappingRepository.save(newEmployeeMappingEntity);
        return true;
    }
/*
  //  @Cacheable(value = "employee_cache",key="#employeePK")
    public Object getEmployeeDetails(Long employeeId) {
        EmployeeMappingEntity employeeEntity= employeeMappingRepository.findByIdAndIsActive(employeeId, true);    //HttpStatus.OK
        if(employeeEntity!=null){
        return responseGetEmployeeDto.convert(employeeEntity,SUCCESS);
        }
        return new Response(204,NOT_PRESENT);
    }

public boolean addEmployee(EmployeeAddRequest addRequestDto,Long employeeId) {
        Long departmentId=addRequestDto.getDepartmentId();
        Long managerId=addRequestDto.getManagerId();
        Long salary=addRequestDto.getSalary();
        String designation=addRequestDto.getDesignation();
        //if dept id given then must present in dept table
        if (departmentId!=-1 && !departmentRepository.existsByIdAndIsActive(departmentId,true) ){
              return false;
        }
        EmployeeMappingEntity employeeEntity=new EmployeeMappingEntity(departmentId,employeeId,managerId,designation,-1
                ,-1,true);

        employeeMappingRepository.save(employeeEntity);
        return true;
    }
    //  @CacheEvict(value = "employee_cache",key="#employeePK", allEntries = true)
    public Object deleteEmployee(Long employeeId) throws Exception {
            EmployeeMappingEntity employeeEntity= employeeMappingRepository.findByIdAndIsActive(employeeId, true);
            if(employeeEntity!=null) {
                employeeEntity.setActive(false);
                employeeMappingRepository.save(employeeEntity);
                return new Response(200,DELETE_SUCCESS);
            }
        return new Response(404, DELETE_FAILED);
    }

    //  @CachePut(value = "employee_cache",key="#employeePK")//,key = "#employeePK")
    public boolean updateDetails(Long employeeId,Long departmentId,Long managerId,String designation) {
        if(departmentId!=-1) //want to add in dept or switch dept,then dept must present in dept table
        {  //dept present or not
            if(!departmentRepository.existsByIdAndIsActive(departmentId,true))
            return false;
            //emp want to update in same dept
            if(employeeMappingRepository.existsByDepartmentIdAndEmployeeId(departmentId,employeeId)) {
                EmployeeMappingEntity employeeMapping = employeeMappingRepository.findByDepartmentIdAndEmployeeId(departmentId
                        , employeeId);
            employeeMapping.setActive(true);
            if(!designation.equals("NOT_CHANGE"))
                employeeMapping.setDesignation(designation);
            if(managerId!=-1 && employeeRepository.existsById(managerId))
                employeeMapping.setManagerId(managerId);
            employeeMappingRepository.save(employeeMapping);
            return true;
            }
            //if dept_id not given but already part of dept ,then update
            if(employeeMappingRepository.existsByEmployeeIdAndIsActive(employeeId,true))
            {
                EmployeeMappingEntity employeeMapping = employeeMappingRepository.findByEmployeeIdAndIsActive
                        (employeeId,true);
                if(employeeMapping.getDepartmentId()!=departmentId)
                    return  false;//emp at a time cant be part of two dept
                employeeMapping.setActive(true);
                if(!designation.equals("NOT_CHANGE"))
                    employeeMapping.setDesignation(designation);
                if(managerId!=-1 && employeeRepository.existsById(managerId))
                    employeeMapping.setManagerId(managerId);
                employeeMappingRepository.save(employeeMapping);
                return true;
            }
                //if new entry
            EmployeeMappingEntity employeeMapping=new EmployeeMappingEntity(departmentId,employeeId,managerId,
                    designation,-1,-1,true);
            employeeMappingRepository.save(employeeMapping);
        }
        //if dept_id not given then check for emp ,present in mapping table or not
        EmployeeMappingEntity fetchedEmployeeEntity =employeeMappingRepository.findByEmployeeIdAndIsActive
                (employeeId,true);
        if(fetchedEmployeeEntity==null) {
            return false;
        }
        if(managerId!=-1 && !employeeMappingRepository.existsByManagerIdAndIsActive(managerId,true)) {
            return false;
        }
        if(managerId!=-1)
        fetchedEmployeeEntity.setManagerId(managerId);
        if(!designation.equals("NOT_CHANGE"))
        fetchedEmployeeEntity.setDesignation(designation);

      //  fetchedEmployeeEntity.setSalary(employeeDto.getSalary());
      //  return responseGetEmployeeDto.convert(fetchedEmployeeEntity,UPDATE_SUCCESS);
       return true;

    }


/*
    public Object getAllDetails(Integer pageNo, Integer pageSize){
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<CompanyEntity> allCompanyEntity= companyRepository.findAllByIsActive(true,paging);

        HashMap<Long,HashMap <Long,List<ResponseEmployee>>> companyToDeptToEmpMapp= new HashMap<Long,HashMap<Long,List<ResponseEmployee>>>();  //l.getCompanyId()>
        allCompanyEntity.forEach((c)->{
            List<DepartmentEntity> allDepartmentEntities= departmentRepository.findAllByDepartmentPKCompanyIdAndIsActive(c.getCompanyId(),true);
            HashMap <Long,List<ResponseEmployee>> deptToEmployeemap=new HashMap<Long,List<ResponseEmployee>>();



            allDepartmentEntities.forEach((d)-> {
                Optional<List<EmployeeEntity>> employeeEntities = employeeRepository.findByEmployeePKCompanyIdAndEmployeePKDepartmentIdAndIsActive(c.getCompanyId(), d.getDepartmentPK().getDepartmentId(),true);
                List<EmployeeEntity>employeeEntityList=new ArrayList<>();
              if(employeeEntities.isPresent())
                 employeeEntityList=employeeEntities.get();
                List<ResponseEmployee> responseEmployeeEntities = employeeEntityListToResponseEmpList.convert(employeeEntityList);
                deptToEmployeemap.put(d.departmentPK.getDepartmentId(), responseEmployeeEntities);
            });
          companyToDeptToEmpMapp.put(c.getCompanyId(),deptToEmployeemap);
        } );
        if(companyToDeptToEmpMapp.isEmpty())
            return new Response(204,NOT_PRESENT);
        return  companyToDeptToEmpMapp;
    }

*/
}



