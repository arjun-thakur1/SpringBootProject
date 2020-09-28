package work1.project1.package1.test.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import work1.project1.package1.dto.request.DepartmentAddRequest;
import work1.project1.package1.dto.request.DepartmentCompanyAddRequest;
import work1.project1.package1.dto.request.DepartmentUpdateRequest;
import work1.project1.package1.dto.response.DepartmentResponse;
import work1.project1.package1.dto.response.Response;
import work1.project1.package1.entity.DepartmentEntity;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.exception.DuplicateDataException;
import work1.project1.package1.exception.NotPresentException;
import work1.project1.package1.repository.CompanyDepartmentMappingRepository;
import work1.project1.package1.repository.CompanyRepository;
import work1.project1.package1.repository.DepartmentRepository;
import work1.project1.package1.repository.EmployeeRepository;
import work1.project1.package1.services.CachingService;
import work1.project1.package1.services.DepartmentService;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static work1.project1.package1.constants.ApplicationConstants.*;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentServiceTest {

    @InjectMocks
    DepartmentService departmentService;
    @InjectMocks
    ModelMapper objectMapper;

    @Mock
    DepartmentRepository departmentRepository;
    @Mock
    CompanyRepository companyRepository;
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    CompanyDepartmentMappingRepository companyDepartmentMappingRepository;
    @Mock
    ModelMapper modelMapper;
    @Mock
    CachingService caching;
    @Mock
    Arrays arrays;

    @Test
    public void test_AddDepartment_Success() throws DuplicateDataException {
        String departmentName = "hr";
        DepartmentAddRequest addRequest = new DepartmentAddRequest(departmentName);
        DepartmentResponse expectedResponse = new DepartmentResponse(null, departmentName);

        when(departmentRepository.existsByDepartmentName(departmentName)).thenReturn(false);

        DepartmentResponse actualResponse = departmentService.addDepartment(addRequest, 1L);
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test//(expected = DuplicateDataException.class)
    public void test_AddDepartment_Fails() throws DuplicateDataException {
        String departmentName = "hr";
        DepartmentAddRequest addRequest = new DepartmentAddRequest(departmentName);
        Throwable exception= Assert.assertThrows(DuplicateDataException.class,()->{
            when(departmentRepository.existsByDepartmentName(anyString())).thenReturn(true);
            departmentService.addDepartment(addRequest, 1L);
        });
        Assert.assertEquals(" Department already present!! " ,exception.getMessage());
    }


    @Test
    public void test_GetDepartmentById_Success() throws NotPresentException {
        Long departmentId=1L;
        DepartmentEntity departmentEntity=new DepartmentEntity(departmentId,"hr");
        DepartmentResponse expectedResponse=new DepartmentResponse(departmentId,"hr");
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(departmentEntity));
        when(modelMapper.map(departmentEntity,DepartmentResponse.class)).thenReturn(expectedResponse);

        DepartmentResponse actualResponse=departmentService.getDepartmentDetail(departmentId);
        Assert.assertEquals(expectedResponse,actualResponse);
    }

    @Test //(expected = NotPresentException.class)
    public void test_GetDepartmentById_Fails() throws CustomException, NotPresentException {
        Long departmentId = 1L;
        Throwable exception=Assert.assertThrows(NotPresentException.class,()->{
            when(departmentRepository.findById(departmentId)).thenReturn(null);
            departmentService.getDepartmentDetail(departmentId);
        });
        Assert.assertEquals("Department"+NOT_PRESENT,exception.getMessage());
    }

    @Test //(expected = NotPresentException.class)
    public void test_UpdateDepartmentDetails_NotPresent_Fails() throws DuplicateDataException, NotPresentException, CustomException {
        Long departmentId=1L;
        String departmentName="hr";
        DepartmentUpdateRequest updateRequest=new DepartmentUpdateRequest(departmentId.doubleValue(),departmentName);
        Throwable exception=Assert.assertThrows(NotPresentException.class,()->{
            when(departmentRepository.findById(departmentId)).thenReturn(null);
            departmentService.updateDetails(updateRequest,1L);
        });
        Assert.assertEquals("Department"+NOT_PRESENT,exception.getMessage());
    }


    @Test //(expected = DuplicateDataException.class)
    public void test_UpdateDepartmentDetails_DepartmentNameAlreadyPresent_Fails() throws DuplicateDataException, NotPresentException, CustomException {
        Long departmentId=1L;
        String departmentName="hr";
        DepartmentUpdateRequest updateRequest=new DepartmentUpdateRequest(departmentId.doubleValue(),departmentName);
        DepartmentEntity departmentEntity=new DepartmentEntity(departmentId,departmentName);
        Throwable exception=Assert.assertThrows(DuplicateDataException.class,()->{
            when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(departmentEntity));
            when(departmentRepository.existsByDepartmentName(departmentName)).thenReturn(true);

            departmentService.updateDetails(updateRequest,1L);
        });
               Assert.assertEquals("Department "+DUPLICATE_NAME_ERROR,exception.getMessage());
    }

    @Test
    public void test_UpdateDepartmentDetails_Success() throws DuplicateDataException, NotPresentException, CustomException {
        Long departmentId=1L;
        String departmentName="hr";
        DepartmentUpdateRequest updateRequest=new DepartmentUpdateRequest(departmentId.doubleValue(),departmentName);
        DepartmentEntity departmentEntity=new DepartmentEntity(departmentId,departmentName);
        DepartmentResponse expectedResponse=new DepartmentResponse(departmentId,departmentName,SUCCESS);

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(departmentEntity));
        when(departmentRepository.existsByDepartmentName(departmentName)).thenReturn(false);
        //doNothing().when(caching).cacheDepartmentDeleteForCompanies(anyLong());
        when(departmentRepository.save(departmentEntity)).thenReturn(departmentEntity);
        when(modelMapper.map(departmentEntity,DepartmentResponse.class)).thenReturn(expectedResponse);

        DepartmentResponse actualResponse=departmentService.updateDetails(updateRequest,1L);
        Assert.assertEquals(expectedResponse,actualResponse);
    }

    @Test//(expected = NotPresentException.class)
    public void test_AddDepartmentToCompany_CompanyNotExists_Fails() throws NotPresentException, CustomException {
        Long companyId=1L;
        DepartmentCompanyAddRequest addRequest=new DepartmentCompanyAddRequest(1D,1D);
        Throwable exception= Assert.assertThrows(NotPresentException.class,()->{
            when(companyRepository.existsByIdAndIsActive(companyId,true)).thenReturn(false);
            departmentService.addDepartmentToCompany(addRequest,1L);
        });
        Assert.assertEquals(" Company not present!! ",exception.getMessage());
    }

    @Test //(expected = NotPresentException.class)
    public void test_AddDepartmentToCompany_DepartmentNotExists_Fails() throws NotPresentException, CustomException {
        Long companyId=1L;
        Long departmentId=1L;
        DepartmentCompanyAddRequest addRequest=new DepartmentCompanyAddRequest(1D,1D);
        Throwable exception= Assert.assertThrows(NotPresentException.class,()->{
            when(companyRepository.existsByIdAndIsActive(companyId,true)).thenReturn(true);
            when(departmentRepository.existsById(departmentId)).thenReturn(false);
            departmentService.addDepartmentToCompany(addRequest,1L);
        });
        Assert.assertEquals(" Department not present!! ",exception.getMessage());
    }

    @Test
    public void test_AddDepartmentToCompany_Success() throws NotPresentException, CustomException {
        Long companyId=1L;
        Long departmentId=1L;
        DepartmentCompanyAddRequest addRequest=new DepartmentCompanyAddRequest(1D,1D);
        Response expectedResponse=new Response(200L,SUCCESS);

        when(companyRepository.existsByIdAndIsActive(companyId,true)).thenReturn(true);
        when(departmentRepository.existsById(departmentId)).thenReturn(true);
        when(companyDepartmentMappingRepository.findByCompanyIdAndDepartmentId(companyId,departmentId)).thenReturn(null);
        doNothing().when(caching).deleteDepartmentsOfCompany(anyLong());
        when(companyDepartmentMappingRepository.save(any())).thenReturn(null);

        Response actualResponse=departmentService.addDepartmentToCompany(addRequest,1L);
        Assert.assertEquals(expectedResponse,actualResponse);
    }


 /*   @Test
    public void test_GetAllEmployeeOfDepartment_Success() throws NotPresentException {
        Long companyId=1L;
        Long departmentId=1L;
        List<EmployeeEntity> employeeEntityList=new ArrayList<>();
        employeeEntityList.add(new EmployeeEntity(1L,"A","1234512345",1000L,1L,
                MyEnum.employee));
        when(employeeRepository.findAllEmployeeQuery(companyId,departmentId,true)).thenReturn(employeeEntityList);

       //error in this line
        when(arrays.asList()).thenCallRealMethod();
        List<EmployeeResponse>actualResponse=departmentService.getAllEmployeeOfDepartment(companyId,departmentId);
    }
*/

}
