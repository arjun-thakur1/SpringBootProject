package work1.project1.package1.test.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;
import static work1.project1.package1.constants.ApplicationConstants.*;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import work1.project1.package1.dto.request.CompanyAddRequest;
import work1.project1.package1.dto.response.CompanyResponse;
import work1.project1.package1.dto.response.Response;
import work1.project1.package1.entity.CompanyEntity;
import work1.project1.package1.exception.*;
import work1.project1.package1.repository.CompanyRepository;
import work1.project1.package1.services.CachingService;
import work1.project1.package1.services.CompanyService;


@RunWith(MockitoJUnitRunner.class)
public class CompanyServiceTest {

    @InjectMocks
    CompanyService companyService;
    @Mock
    CachingService caching;
    @Mock
    CompanyRepository companyRepository;
    @Mock
    ModelMapper modelMapper;


 /*   @Test
    public void test_GetAllCompany_Success() throws NotPresentException {
        int page=0,size=1;
        Pageable pageable= PageRequest.of(page,size);
        List<CompanyEntity>companyEntityList=new ArrayList<>();
        companyEntityList.add(new CompanyEntity(1L,"companyName","ceoName"));
        Page<CompanyEntity>companyEntityPage=new PageImpl<>(companyEntityList);
        List<CompanyResponse> companyResponseList=new ArrayList<>();
        companyResponseList.add(new CompanyResponse(1L,"companyName","ceoName"));
        when(companyRepository.findAllByIsActive(true,pageable)).thenReturn(companyEntityPage);
     //   when(actualModelMapper.map(any(),any())).thenCallRealMethod();
        when(companyResponseList.add(any())).thenReturn(companyResponseList);
        companyService.getAll(page,size);
    }
  */
    @Test
    public void test_GetAllCompany_CompaniesNotPresent_Fails() throws NotPresentException {
        int page=0,size=1;
        Pageable pageable= PageRequest.of(page,size);
        Throwable exception = Assert.assertThrows(SuccessException.class, () -> {
            when(companyRepository.findAllByIsActive(true, pageable)).thenReturn(null);
            companyService.getAll(page, size);
        });
        Assert.assertEquals("no more companies present in the system!! ",exception.getMessage());
    }

    @Test
    public void test_GetCompanyById_NotPresent_Fails() throws NotPresentException {
        Long companyId=1L;
        Throwable exception = Assert.assertThrows(NotPresentException.class, () -> {
                    when(companyRepository.findByIdAndIsActive(companyId, true)).thenReturn(null);
                    companyService.getCompanyById(companyId);
                });
        Assert.assertEquals("company Not Present!! ",exception.getMessage());
    }

    @Test
    public void test_GetCompanyById_Success() throws NotPresentException {
        Long companyId=1L;
        CompanyEntity companyEntity= new CompanyEntity(1L,"companyName","ceoName");
        CompanyResponse expectedResponse=new CompanyResponse(companyId,"companyName","ceoName");

        when(companyRepository.findByIdAndIsActive(companyId,true)).thenReturn(companyEntity);
        when(modelMapper.map(companyEntity,CompanyResponse.class)).thenReturn(expectedResponse);

        CompanyResponse actualResponse=companyService.getCompanyById(companyId);
        Assert.assertEquals(expectedResponse,actualResponse);
    }



    @Test
    public void test_AddCompany_Success() throws DuplicateDataException {
        Long companyId=1L;
        CompanyEntity companyEntity= new CompanyEntity(companyId,"company","ceo");
        CompanyResponse expectedResponse=new CompanyResponse(companyId,"company","ceo");
        CompanyAddRequest addRequest=new CompanyAddRequest("company","ceo");

        when(companyRepository.existsByCompanyName("company")).thenReturn(false);
        when(companyRepository.save(companyEntity)).thenReturn(companyEntity);
        when(modelMapper.map(addRequest,CompanyEntity.class)).thenReturn(companyEntity);
        when(modelMapper.map(companyEntity,CompanyResponse.class)).thenReturn(expectedResponse);

        CompanyResponse actualResponse=companyService.addCompanyDetail(addRequest,1L);
        Assert.assertEquals(expectedResponse,actualResponse);
    }



    @Test
    public void test_AddCompany_AlreadyPresent_Fails() throws DuplicateDataException {
        String companyName="companyName";
        String  ceoName="ceoName";
        CompanyAddRequest addRequest=new CompanyAddRequest(companyName,ceoName);
        CompanyEntity companyEntity= new CompanyEntity(1L,companyName,ceoName);
        companyEntity.setActive(true);
        Throwable exception = Assert.assertThrows(DuplicateDataException.class, () -> {
            when(companyRepository.existsByCompanyName(anyString())).thenReturn(true);
            when(companyRepository.findByCompanyName(anyString())).thenReturn(companyEntity);
            companyService.addCompanyDetail(addRequest,1L);
        });
       Assert.assertEquals(DUPLICATE_NAME_ERROR,exception.getMessage());
    }

    @Test
    public void test_AddCompany_AlreadyExists_ButIsActiveFalse_Success() throws DuplicateDataException {
        String companyName="company";
        String  ceoName="ceo";
        Long companyId=1L;
        CompanyAddRequest addRequest=new CompanyAddRequest(companyName,ceoName);
        CompanyEntity companyEntity= new CompanyEntity(companyId,companyName,ceoName);
        companyEntity.setActive(false);
        CompanyResponse expectedResponse=new CompanyResponse(companyId,companyName,ceoName);

        when(companyRepository.existsByCompanyName(companyName)).thenReturn(true);
        when(companyRepository.save(companyEntity)).thenReturn(companyEntity);
        when(companyRepository.findByCompanyName(companyName)).thenReturn(companyEntity);
        when(modelMapper.map(companyEntity,CompanyResponse.class)).thenReturn(expectedResponse);

        CompanyResponse actualResponse=companyService.addCompanyDetail(addRequest,1L);
        Assert.assertEquals(expectedResponse,actualResponse);
    }


    @Test
    public  void test_DeleteCompany_Success() throws NotPresentException, CustomException {
        Long companyId=1L;
        Response expectedResponse=new Response(200L,DELETE_SUCCESS);

        when(companyRepository.existsByIdAndIsActive(anyLong(),anyBoolean())).thenReturn(true);
        doNothing().when(caching).deleteDepartmentsOfCompany(companyId);

        Response  actualResponse=companyService.deleteCompanyDetails(companyId);
        Assert.assertEquals(expectedResponse,actualResponse);
    }

  @Test //(expected = NotPresentException.class)
    public void test_DeleteCompany_NotPresent_Fails() throws NotPresentException {
        Long companyId=1L;
        Throwable exception=Assert.assertThrows(NotPresentException.class, ()->{
            when(companyRepository.existsByIdAndIsActive(anyLong(),anyBoolean())).thenReturn(false);
            companyService.deleteCompanyDetails(companyId);
        });
        Assert.assertEquals("company"+NOT_PRESENT,exception.getMessage());
    }

  @Test
    public void test_UpdateCompany_Success() throws DuplicateDataException, NotPresentException, InsufficientDataException, CustomException {
        Double companyId=1D;
        String companyName="companyname";
        String ceoName="ceoname";
        CompanyResponse expectedResponse=new CompanyResponse(companyId.longValue(),companyName,ceoName,UPDATE_SUCCESS) ;
        CompanyEntity companyEntity=new CompanyEntity(companyId.longValue(),companyName,ceoName,true);

        when(companyRepository.findByIdAndIsActive(1L,true)).thenReturn(companyEntity);
        when(companyRepository.save(companyEntity)).thenReturn(companyEntity);

        CompanyResponse actualResponse=companyService.updateDetails(companyId,companyName,ceoName,1L);
        Assert.assertEquals(expectedResponse,actualResponse);
  }

    @Test//(expected = NotPresentException.class)
    public void test_UpdateCompany_Fails() throws DuplicateDataException, NotPresentException, InsufficientDataException, CustomException {

        Throwable exception=Assert.assertThrows(NotPresentException.class, ()->{
            when(companyRepository.findByIdAndIsActive(1,true)).thenReturn(null);
            companyService.updateDetails(1D,"q","a",1L);
        });
        Assert.assertEquals("company"+NOT_PRESENT,exception.getMessage());
    }


}
