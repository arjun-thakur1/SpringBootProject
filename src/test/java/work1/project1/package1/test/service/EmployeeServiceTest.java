package work1.project1.package1.test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import work1.project1.package1.dto.request.EmployeeAddRequest;
import work1.project1.package1.dto.request.EmployeePersonalInfoUpdateRequest;
import work1.project1.package1.dto.request.UpdateSalaryRequest;
import work1.project1.package1.dto.response.EmployeeCompleteResponse;
import work1.project1.package1.dto.response.Response;
import work1.project1.package1.entity.CompanyDepartmentMappingEntity;
import work1.project1.package1.entity.EmployeeEntity;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.exception.InsufficientDataException;
import work1.project1.package1.exception.NotPresentException;
import work1.project1.package1.exception.ResponseHttp;
import work1.project1.package1.myenum.MyEnum;
import work1.project1.package1.repository.CompanyDepartmentMappingRepository;
import work1.project1.package1.repository.EmployeeMappingRepository;
import work1.project1.package1.repository.EmployeeRepository;
import work1.project1.package1.services.*;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static work1.project1.package1.constants.ApplicationConstants.*;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

    @InjectMocks
    EmployeeService employeeService;
    @InjectMocks
    CachingService cachingObject;
    @InjectMocks
    ModelMapper actualModelMapper;

    @Mock
    ModelMapper mockModelMapper;
    @Mock
    CompanyDepartmentMappingRepository companyDepartmentMappingRepository;
    @Mock
    EmployeeMappingRepository employeeMappingRepository;
    @Mock
    CachingService caching;
    @Mock
    RedisService redisService;
    @Mock
    EmployeeMappingService mappingService;
    @Mock
    EmployeeRepository employeeRepository;

    ObjectMapper om=new ObjectMapper();


    @Test //(expected = NotPresentException.class)
    public void test_getEmployeeById_Fails() throws NotPresentException, CustomException {
        //employee not exist
        Long employeeId=1234L;
        Throwable exception= Assert.assertThrows(NotPresentException.class,()->{
            when(cachingObject.getEmployeeById(employeeId)).thenCallRealMethod();
            when(employeeRepository.findById(employeeId)).thenReturn(null);
            employeeService.getEmployee(employeeId);
        });
        Assert.assertEquals("Employee"+NOT_PRESENT,exception.getMessage());
    }

    @Test
    public void test_getEmployeeById_Success() throws CustomException, NotPresentException {
        final Long id=11L;
        EmployeeEntity employee=new EmployeeEntity();
        employee.setId(id);
        employee.setName("ARJUN");
        employee.setPhone("1234567894");
        //       when(cachingObject.getEmployeeById(id)).thenCallRealMethod();
        //   when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(caching.getEmployeeById(id)).thenReturn(employee);
        Object actualResponse=employeeService.getEmployee(id);
        Assert.assertEquals(employee,actualResponse);
    }

    @Test //(expected = NotPresentException.class)
    public void test_getAllEmployee_Fails() throws NotPresentException {
        int page=0,size=1;
        Pageable pageable= PageRequest.of(page,size);
        Throwable exception=Assert.assertThrows(NotPresentException.class,()->{
            when(employeeRepository.findAll(pageable)).thenReturn(null);
            employeeService.getAllEmployees(page,size);
        });
        Assert.assertEquals("Employee"+NOT_PRESENT,exception.getMessage());
    }
   /* @Test
    public void test_getAllEmployee_Success() throws NotPresentException {
        int page=0,size=1;
        Pageable pageable= PageRequest.of(page,size);
        List<EmployeeEntity> employeeEntityList=new ArrayList<>();
          employeeEntityList.add( new EmployeeEntity("q","1234567890",1000L,-1L,-1L));
        List<EmployeeGetResponse>employeeResponseList  = Arrays.asList(actualModelMapper.map(employeeEntityList,EmployeeGetResponse[].class));
        Page<EmployeeEntity> pageEmployeeEntityList=new PageImpl<EmployeeEntity>(employeeEntityList);
        when(employeeRepository.findAll(pageable)).thenReturn(pageEmployeeEntityList);
        when(Arrays.asList(mockModelMapper.map(employeeEntityList,EmployeeGetResponse[].class))).thenReturn(employeeResponseList);

        List<EmployeeGetResponse>actualResult=employeeService.getAllEmployees(page,size);
        Assert.assertEquals(employeeEntityList.size(),actualResult.size());
    } */
    @Test //(expected = NotPresentException.class)
    public void test_updatePersonalInfo_Fails() throws ResponseHttp, CustomException, NotPresentException, InsufficientDataException {
        Double doubleEmployeeId=1244D;
        Long employeeId=doubleEmployeeId.longValue();
        EmployeePersonalInfoUpdateRequest request=new EmployeePersonalInfoUpdateRequest(doubleEmployeeId,"AQ","1234512345");
        Throwable exception=Assert.assertThrows(NotPresentException.class,()->{
            when(employeeRepository.findById(employeeId)).thenReturn(null);
            employeeService.updatePersonalInfo(request,employeeId);
        });
        Assert.assertEquals("Employee"+NOT_PRESENT,exception.getMessage());
    }
    @Test
    public void test_updatePersonalInfo_Success() throws ResponseHttp, CustomException, NotPresentException, InsufficientDataException {
        Double doubleEmployeeId=1244D;
        Long employeeId=doubleEmployeeId.longValue();
        EmployeePersonalInfoUpdateRequest request=new EmployeePersonalInfoUpdateRequest(doubleEmployeeId,"AQ","1234512345");
        EmployeeEntity employee=new EmployeeEntity();
        employee.setId(478L);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(caching.updateEmployeeCache(employeeId,employee)).thenReturn(employee);
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        Response response=employeeService.updatePersonalInfo(request,employeeId);
        Assert.assertEquals(new Response(200L,UPDATE_SUCCESS),response);
    }
    @Test
    public void test_UpdateEmployeeSalary_Fails() throws CustomException, NotPresentException, InsufficientDataException {
       UpdateSalaryRequest salaryRequest=new UpdateSalaryRequest(1000D,2L,1234D);

       //when(mappingService.getIds(1234L)).thenReturn(null);
       Throwable exception= Assert.assertThrows(CustomException.class,()->{
           employeeService.updateSalary(salaryRequest);
       });

       Assert.assertEquals(" type can only be 0 or 1. 0 for % increment & 1 for absolute increment ",exception.getMessage());
    }
    @Test
    public void test_UpdateEmployeeSalary_Success() throws CustomException, NotPresentException, InsufficientDataException {
       UpdateSalaryRequest updateSalaryRequest=new UpdateSalaryRequest(1000D,1L,461D);
        CompanyDepartmentMappingEntity cdMappingEntity=new CompanyDepartmentMappingEntity();
        cdMappingEntity.setCompanyId(11L);
        cdMappingEntity.setDepartmentId(22L);
        EmployeeEntity employee=new EmployeeEntity();
        employee.setId(461L);
        employee.setName("ARJUN");
        employee.setPhone("1234512345");
        employee.setSalary(1000D);

        //when(mappingService.getIds(461L)).thenReturn(cdMappingEntity);
        when(employeeRepository.findById(461L)).thenReturn(Optional.of(employee));
        when(employeeMappingRepository.existsByEmployeeIdAndIsActive(461L,true)).thenReturn(true);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(caching.updateEmployeeCache(461L,employee)).thenReturn(employee);

        boolean isUpdated=employeeService.updateSalary(updateSalaryRequest);
        Assert.assertEquals(true,isUpdated);
        Assert.assertTrue(2000D==employee.getSalary());
    }

    @Test //(expected = NotPresentException.class)
    public void test_AddEmployee_CompanyNotExist_Fails() throws CustomException, NotPresentException, NoSuchAlgorithmException,
            InsufficientDataException {
        //no company-department exists for this ids
        EmployeeAddRequest addRequest =new EmployeeAddRequest("A","1234512345",1000D,1D,
                1D,-1D, MyEnum.employee);
        Throwable exception=Assert.assertThrows(NotPresentException.class,()->{
            when(companyDepartmentMappingRepository.findByCompanyIdAndDepartmentIdAndIsActive(1L,1L,true))
                    .thenReturn(null);
            employeeService.addEmployee(addRequest,1L);
        });
        Assert.assertEquals(" Department-Company "+NOT_PRESENT,exception.getMessage());
   }

    @Test //(expected = InsufficientDataException.class)
    public void test_AddEmployee_InsufficientData_Fails() throws CustomException, NotPresentException,
            NoSuchAlgorithmException, InsufficientDataException {
        EmployeeAddRequest addRequest=new EmployeeAddRequest();
        //companyId,departmentId not given but salary,designation given
        addRequest.setName("A");
        addRequest.setPhone("1234512345");
        addRequest.setSalary(1000D);
        addRequest.setDesignation(MyEnum.employee);
        Throwable exception=Assert.assertThrows(InsufficientDataException.class,()->{
            employeeService.addEmployee(addRequest,1L);
        });
        Assert.assertEquals(" insufficient data , please provide all required data!! ",exception.getMessage());

          }

    @Test
    public void test_AddEmployee_Success() throws CustomException, NotPresentException, NoSuchAlgorithmException, InsufficientDataException {
        EmployeeAddRequest addRequest =new EmployeeAddRequest("A","1234512345",1000D,1D,
                1D,null, MyEnum.employee);
        EmployeeEntity employee=new EmployeeEntity(1L,"A","1234512345",1000D,1L,
                MyEnum.employee);
        EmployeeCompleteResponse expectedResponse=actualModelMapper.map(employee,EmployeeCompleteResponse.class);
        expectedResponse.setCompanyId(1L);
        expectedResponse.setDepartmentId(1L);
        expectedResponse.setStatus(200L);
        expectedResponse.setMessage(SUCCESS);
        CompanyDepartmentMappingEntity cdMappingEntity=new CompanyDepartmentMappingEntity();
        cdMappingEntity.setId(1L);
        cdMappingEntity.setDepartmentId(1L);
        cdMappingEntity.setCompanyId(1L);

        when(companyDepartmentMappingRepository.findByCompanyIdAndDepartmentIdAndIsActive(1L,1L,true))
        .thenReturn(cdMappingEntity);
        when(mockModelMapper.map(addRequest,EmployeeEntity.class)).thenReturn(employee);
        //doNothing().when(userService).userAdd(anyLong(),anyString());
        doNothing().when(mappingService).add(anyLong(),anyLong());

        Object actualResponse=employeeService.addEmployee(addRequest,1L);
        //System.out.println(expectedResponse);
        //System.out.println(actualResponse);
        Assert.assertEquals(expectedResponse,actualResponse);
    }


    @Test
    public void test_deleteEmployee_Success() throws CustomException, NotPresentException {
        Long employeeId=478L;
        Response expectedResponse=new Response(200L,DELETE_SUCCESS);

        when(redisService.deleteTokenOfEmployeeFromCache(anyLong())).thenReturn("jnwjf");
        when(caching.deleteEmployeeById(employeeId,1L)).thenReturn(expectedResponse);

        Response actualResponse=employeeService.deleteEmployee(employeeId,1L);
        Assert.assertEquals(expectedResponse,actualResponse);
    }


    @Test  //here basically main logic is in another method that is caching.deleteEmployee()
    public void test_deleteEmployee_Fails() throws CustomException, NotPresentException {
        Long employeeId=478L;
        Response expectedResponse=new Response(404L,FAILED);

        when(redisService.deleteTokenOfEmployeeFromCache(anyLong())).thenReturn("jnwjf");
        when(caching.deleteEmployeeById(employeeId,1L)).thenReturn(expectedResponse);

        Response actualResponse=employeeService.deleteEmployee(employeeId,1L);
        Assert.assertEquals(expectedResponse,actualResponse);
    }


}
