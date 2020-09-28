package work1.project1.package1.services;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import work1.project1.package1.aop.MethodExecutionTime;
import work1.project1.package1.dto.response.*;
import work1.project1.package1.entity.CompanyEntity;

import work1.project1.package1.entity.EmployeeEntity;
import work1.project1.package1.exception.*;
import work1.project1.package1.repository.CompanyDepartmentMappingRepository;
import work1.project1.package1.repository.DepartmentRepository;
import work1.project1.package1.dto.request.CompanyAddRequest;
import work1.project1.package1.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work1.project1.package1.repository.EmployeeRepository;

import java.util.*;

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
    ModelMapper modelMapper;

    @Autowired
    CompanyDepartmentMappingRepository mappingRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    CachingService caching;

    Logger logger= LoggerFactory.getLogger(this.getClass());

    public List<AllCompaniesResponse> getAll(int page,int size) throws NotPresentException, SuccessException {
       Pageable pageable= PageRequest.of(page,size);
       Page<CompanyEntity> pageCompanyEntityList= companyRepository.findAllByIsActive(true,pageable);
       if(pageCompanyEntityList!=null) {
           List<CompanyEntity> companyEntityList = pageCompanyEntityList.getContent();
           if (!companyEntityList.isEmpty()) {
               List<AllCompaniesResponse> companyResponseList = new ArrayList<>();
               companyEntityList.forEach((c) -> {
                   companyResponseList.add(modelMapper.map(c, AllCompaniesResponse.class));
               });
               return companyResponseList;
           }
       }
       throw new SuccessException("no more companies present in the system!! ");
    }
    @MethodExecutionTime
    public CompanyResponse getCompanyById(Long id) throws NotPresentException {
        CompanyEntity companyEntity=companyRepository.findByIdAndIsActive(id,true)  ; //findQuery(id,true);
             if(companyEntity!=null) {

                return  modelMapper.map(companyEntity,CompanyResponse.class);
        }
        throw new NotPresentException("company"+NOT_PRESENT);
    }


    public CompanyResponse addCompanyDetail(CompanyAddRequest companyAddRequest,Long userId) throws DuplicateDataException {
        String companyName= companyAddRequest.getCompanyName().toLowerCase();
        String ceoName=companyAddRequest.getCeoName();     //=null; by default already build null
        if(companyAddRequest.getCeoName()!=null) {
           ceoName=companyAddRequest.getCeoName().toLowerCase();
        }
        if(companyRepository.existsByCompanyName(companyName)) {
            //company name unique so in lower-case
            CompanyEntity companyEntity = companyRepository.findByCompanyName(companyName);
            if (companyEntity.getIsActive())
                throw new DuplicateDataException(DUPLICATE_NAME_ERROR);
            companyEntity.setActive(true);
            companyEntity.setCompanyName(companyName);
            companyRepository.save(companyEntity);
            CompanyResponse companyResponse = modelMapper.map(companyEntity, CompanyResponse.class);
            return companyResponse;
        }
        CompanyEntity companyEntity= modelMapper.map(companyAddRequest,CompanyEntity.class);
        companyEntity.setCreatedBy(userId);
        companyEntity.setCompanyName(companyName);
        companyEntity.setActive(true);
        companyRepository.save(companyEntity);
        CompanyResponse companyResponse= modelMapper.map(companyEntity, CompanyResponse.class);
        return companyResponse;
    }

    public Response deleteCompanyDetails(Long companyId) throws NotPresentException, CustomException {
        if(companyRepository.existsByIdAndIsActive(companyId,true)) {
            caching.deleteDepartmentsOfCompany(companyId);
            List<Long> employeeIds = companyRepository.findIdsOfAllEmployeesOfCompany(companyId,true);
            for (Long employeeId:employeeIds) {
                caching.deleteEmployeeById(employeeId,null);
            }
            companyRepository.deleteCompanyQuery(companyId , true,false ,"none" ,-1L,-1D);
            return  new Response((long) 200,DELETE_SUCCESS);
        }
       throw new NotPresentException("company"+NOT_PRESENT);
    }

    public CompanyResponse updateDetails(Double  doubleCompanyId, String company, String ceo,Long userId)
            throws DuplicateDataException, NotPresentException, InsufficientDataException, CustomException {
        if("".equals(company) )
            throw new InsufficientDataException("please provide valid company name!!");
        if("".equals(ceo))
            throw new InsufficientDataException("please provide valid ceo name!!");
        String companyName=null,ceoName=null;
        if(company!=null)
            companyName=company.toLowerCase();
        if(ceo!=null)
            ceoName=ceo.toLowerCase();
        Long companyId=doubleCompanyId.longValue();
        Optional<CompanyEntity> fetchedCompanyEntity= Optional.ofNullable(companyRepository.findByIdAndIsActive(companyId, true));
        if(fetchedCompanyEntity.isPresent()) {
            CompanyEntity companyEntity= fetchedCompanyEntity.get();
            if(companyName!=null && !companyName.equals(companyEntity.getCompanyName())) { //check for unique company name , if not same as present means want to update
                if(companyRepository.existsByCompanyName(companyName))
                    throw  new DuplicateDataException(DUPLICATE_NAME_ERROR);
                companyEntity.setCompanyName(companyName);
            }
            if (ceoName!=null) {
                companyEntity.setCeoName(ceoName);
            }
            companyEntity.setUpdatedBy(userId);
            companyRepository.save(companyEntity);
            return new CompanyResponse(companyId,companyEntity.getCompanyName(),companyEntity.getCeoName(),UPDATE_SUCCESS) ;
        }
        throw  new NotPresentException("company"+NOT_PRESENT);
    }


    public HashMap<Long,List<EmployeeResponse>> getCompanyCompleteDetails(Long companyId) throws CustomException, NotPresentException {
        List<DepartmentResponse> departmentResponseList=departmentService.getAllDepartmentsOfCompany(companyId);
        HashMap<Long,List<EmployeeResponse>> departmentToEmployeesMap=new HashMap<>();
        departmentResponseList.forEach(d->{
           List<EmployeeEntity> employeeEntityList= employeeRepository.findAllEmployeeQuery(companyId,d.getId(),true);
           List<EmployeeResponse>employeeResponseList= Arrays.asList(modelMapper.map(employeeEntityList,EmployeeResponse[].class));
            departmentToEmployeesMap.put(d.getId(),employeeResponseList);
        });
        if( departmentToEmployeesMap.isEmpty())
            throw new NotPresentException(NOT_PRESENT);
        return departmentToEmployeesMap;
    }


}
