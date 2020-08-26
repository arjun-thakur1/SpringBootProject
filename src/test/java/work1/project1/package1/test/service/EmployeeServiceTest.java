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
import work1.project1.package1.dto.response.EmployeeCompleteResponse;
import work1.project1.package1.dto.response.EmployeeGetResponse;
import work1.project1.package1.dto.response.Response;
import work1.project1.package1.entity.CompanyDepartmentMappingEntity;
import work1.project1.package1.entity.EmployeeEntity;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.exception.NotPresentException;
import work1.project1.package1.exception.ResponseHttp;
import work1.project1.package1.myenum.MyEnum;
import work1.project1.package1.repository.CompanyDepartmentMappingRepository;
import work1.project1.package1.repository.EmployeeRepository;
import work1.project1.package1.services.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static work1.project1.package1.constants.ApplicationConstants.*;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

    @InjectMocks
    EmployeeService employeeService;
    @InjectMocks
    Caching cachingObject;
    @InjectMocks
    ModelMapper actualModelMapper;

    @Mock
    ModelMapper mockModelMapper;
    @Mock
    CompanyDepartmentMappingRepository companyDepartmentMappingRepository;
    @Mock
    Caching caching;
    @Mock
    RedisService redisService;
    @Mock
    EmployeeMappingService mappingService;
    @Mock
    UserService userService;
    @Mock
    EmployeeRepository employeeRepository;

    ObjectMapper om=new ObjectMapper();


    @Test(expected = NotPresentException.class)
    public void test_getEmployeeById_Fails() throws NotPresentException, CustomException {
        //employee not exist
        Long employeeId=1234L;
        when(cachingObject.getEmployeeById(employeeId)).thenCallRealMethod();
        when(employeeRepository.findById(employeeId)).thenReturn(null);
        employeeService.getEmployee(employeeId);
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
        Assert.assertEquals(employee.toString(),actualResponse.toString());
    }

    @Test(expected = NotPresentException.class)
    public void test_getAllEmployee_Fails() throws NotPresentException {
        int page=0,size=1;
        Pageable pageable= PageRequest.of(page,size);
        when(employeeRepository.findAll(pageable)).thenReturn(null);
        employeeService.getAllEmployees(page,size);
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
    @Test(expected = NotPresentException.class)
    public void test_updatePersonalInfo_Fails() throws ResponseHttp, CustomException, NotPresentException {
        Long employeeId=1244L;
        EmployeePersonalInfoUpdateRequest request=new EmployeePersonalInfoUpdateRequest(employeeId,"AQ","1234512345");
        when(employeeRepository.findById(employeeId)).thenReturn(null);
        employeeService.updatePersonalInfo(request,employeeId);
    }
    @Test
    public void test_updatePersonalInfo_Success() throws ResponseHttp, CustomException, NotPresentException {
        Long employeeId=478L;
        EmployeePersonalInfoUpdateRequest request=new EmployeePersonalInfoUpdateRequest(employeeId,"AQ","1234512345");
        EmployeeEntity employee=new EmployeeEntity();
        employee.setId(478L);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(caching.updateEmployeeCache(employeeId,employee)).thenReturn(employee);
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        Response response=employeeService.updatePersonalInfo(request,employeeId);
        Assert.assertEquals(200L,response.getStatus());
        verify(caching).updateEmployeeCache(employeeId,employee);
    }
    @Test
    public void test_UpdateEmployeeSalary_Fails(){
       Long  employeeId=461L;
       Long salary_change=1000L;
       Long flag=1L ;
       when(mappingService.getIds(employeeId)).thenReturn(null);
       boolean isUpdated=employeeService.updateEmployeeSalary(employeeId,salary_change,flag);
       Assert.assertEquals(false,isUpdated);
    }
    @Test
    public void test_UpdateEmployeeSalary_Success(){
        Long  employeeId=461L;
        Long salary_change=1000L;
        Long flag=1L ;
        CompanyDepartmentMappingEntity cdMappingEntity=new CompanyDepartmentMappingEntity();
        cdMappingEntity.setCompanyId(11L);
        cdMappingEntity.setDepartmentId(22L);
        EmployeeEntity employee=new EmployeeEntity();
        employee.setId(employeeId);
        employee.setName("ARJUN");
        employee.setPhone("1234512345");
        employee.setSalary(1000L);

        when(mappingService.getIds(employeeId)).thenReturn(cdMappingEntity);
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(caching.updateEmployeeCache(employeeId,employee)).thenReturn(employee);

        boolean isUpdated=employeeService.updateEmployeeSalary(employeeId,salary_change,flag);
        Assert.assertEquals(true,isUpdated);
        Assert.assertTrue(2000L==employee.getSalary());
    }
    @Test(expected = NotPresentException.class)
    public void test_AddEmployee_CompanyNotExist_Fails() throws CustomException, NotPresentException {
        //no company-department exists for this ids
        EmployeeAddRequest addRequest =new EmployeeAddRequest("A","1234512345",1000L,1L,
                1L,-1L, MyEnum.employee);
        when(companyDepartmentMappingRepository.findByCompanyIdAndDepartmentIdAndIsActive(1L,1L,true))
                .thenReturn(null);
        //action
        employeeService.addEmployee(addRequest,1L);
        //result
    }

    @Test(expected = CustomException.class)
    public void test_AddEmployee_InsufficientData_Fails() throws CustomException, NotPresentException {
        EmployeeAddRequest addRequest=new EmployeeAddRequest();
        //companyId,departmentId not given but salary,designation given
        addRequest.setName("A");
        addRequest.setPhone("1234512345");
        addRequest.setSalary(1000L);
        addRequest.setDesignation(MyEnum.employee);

        employeeService.addEmployee(addRequest,1L);
    }

    @Test
    public void test_AddEmployee_Success() throws CustomException, NotPresentException {
        EmployeeAddRequest addRequest =new EmployeeAddRequest("A","1234512345",1000L,1L,
                1L,-1L, MyEnum.employee);
        EmployeeEntity employee=new EmployeeEntity(1L,"A","1234512345",1000L,1L,
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
        doNothing().when(userService).userAdd(anyLong(),anyString());
        doNothing().when(mappingService).add(anyLong(),anyLong());

        Object actualResponse=employeeService.addEmployee(addRequest,1L);
        Assert.assertEquals(expectedResponse.toString(),actualResponse.toString());
    }


    @Test
    public void test_deleteEmployee_Success() throws CustomException, NotPresentException {
        Long employeeId=478L;
      //  EmployeeEntity employee=new EmployeeEntity(1L,"A","1234512345",1000L,1L,
       //         MyEnum.employee);
        Response expectedResponse=new Response(200L,DELETE_SUCCESS);

        when(redisService.deleteTokenOfEmployeeFromCache(anyLong())).thenReturn("jnwjf");
        when(caching.deleteEmployeebyId(employeeId,1L)).thenReturn(expectedResponse);
        //  when(cachingObject.deleteEmployeebyId(employeeId,1L)).thenCallRealMethod();
        //when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        Response actualResponse=employeeService.deleteEmployee(employeeId,1L);
        Assert.assertEquals(expectedResponse.toString(),actualResponse.toString());
    }


    @Test  //here basically main logic is in another method that is caching.deleteEmployee()
    public void test_deleteEmployee_Fails() throws CustomException, NotPresentException {
        Long employeeId=478L;
        Response expectedResponse=new Response(404L,FAILED);

        when(redisService.deleteTokenOfEmployeeFromCache(anyLong())).thenReturn("jnwjf");
        when(caching.deleteEmployeebyId(employeeId,1L)).thenReturn(expectedResponse);

        Response actualResponse=employeeService.deleteEmployee(employeeId,1L);
        Assert.assertEquals(expectedResponse.toString(),actualResponse.toString());
    }


}
