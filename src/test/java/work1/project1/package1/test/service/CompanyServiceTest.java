package work1.project1.package1.test.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;
import static work1.project1.package1.constants.ApplicationConstants.DELETE_SUCCESS;
import static work1.project1.package1.constants.ApplicationConstants.UPDATE_SUCCESS;

import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import work1.project1.package1.dto.request.CompanyAddRequest;
import work1.project1.package1.dto.response.CompanyResponse;
import work1.project1.package1.dto.response.Response;
import work1.project1.package1.entity.CompanyEntity;
import work1.project1.package1.exception.DuplicateDataException;
import work1.project1.package1.exception.NotPresentException;
import work1.project1.package1.repository.CompanyRepository;
import work1.project1.package1.services.Caching;
import work1.project1.package1.services.CompanyService;
import java.util.Arrays;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class CompanyServiceTest {

    @InjectMocks
    CompanyService companyService;
    @InjectMocks
    ModelMapper actualModelMapper;
    @Mock
    Caching  caching;

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


    @Test(expected = NotPresentException.class)
    public void test_GetAllCompany_Fails() throws NotPresentException {
        int page=0,size=1;
        Pageable pageable= PageRequest.of(page,size);

        when(companyRepository.findAllByIsActive(true,pageable)).thenReturn(null);
        companyService.getAll(page,size);
    }

    @Test(expected=NotPresentException.class)
    public void test_GetCompanyById_NotPresent_Fails() throws NotPresentException {
        Long companyId=1L;
        //CompanyEntity companyEntity= new CompanyEntity(1L,"companyName","ceoName");
       // CompanyResponse expectedResponse=new CompanyResponse(companyId,"companyName","ceoName");
        when(companyRepository.findByIdAndIsActive(companyId,true)).thenReturn(null);
        companyService.getCompanyById(companyId);
       // Assert.assertEquals(expectedResponse.toString(),actualResponse.toString());CompanyResponse actualResponse=
    }

    @Test
    public void test_GetCompanyById_Success() throws NotPresentException {
        Long companyId=1L;
        CompanyEntity companyEntity= new CompanyEntity(1L,"companyName","ceoName");
        CompanyResponse expectedResponse=new CompanyResponse(companyId,"companyName","ceoName");

        when(companyRepository.findByIdAndIsActive(companyId,true)).thenReturn(companyEntity);
        when(modelMapper.map(any(),any())).thenReturn(expectedResponse);

        CompanyResponse actualResponse=companyService.getCompanyById(companyId);
        Assert.assertEquals(expectedResponse.toString(),actualResponse.toString());
    }

    @Test
    public void test_AddCompany_Success() throws DuplicateDataException {
        Long companyId=1L;
        CompanyEntity companyEntity= new CompanyEntity(1L,"companyName","ceoName");
        CompanyResponse expectedResponse=new CompanyResponse(companyId,"companyName","ceoName");
        CompanyAddRequest addRequest=new CompanyAddRequest("companyName","ceoName");

        when(companyRepository.existsByCompanyName(anyString())).thenReturn(false);
        when(companyRepository.save(any())).thenReturn(companyEntity);
        when(modelMapper.map(any(),any())).thenReturn(expectedResponse);

        CompanyResponse actualResponse=companyService.addCompanyDetail(addRequest,1L);
        Assert.assertEquals(expectedResponse.toString(),actualResponse.toString());
    }

    @Test(expected = DuplicateDataException.class)
    public void test_AddCompany_AlreadyPresent_Fails() throws DuplicateDataException {
        String companyName="companyName";
        String  ceoName="ceoName";
        CompanyAddRequest addRequest=new CompanyAddRequest(companyName,ceoName);
        CompanyEntity companyEntity= new CompanyEntity(1L,companyName,ceoName);
        companyEntity.setActive(true);

        when(companyRepository.existsByCompanyName(anyString())).thenReturn(true);
        when(companyRepository.findByCompanyName(anyString())).thenReturn(companyEntity);

        companyService.addCompanyDetail(addRequest,1L);
    }

    @Test
    public void test_AddCompany_AlreadyExists_ButIsActiveFalse_Fails() throws DuplicateDataException {
        String companyName="companyName";
        String  ceoName="ceoName";
        Long companyId=1L;
        CompanyAddRequest addRequest=new CompanyAddRequest(companyName,ceoName);
        CompanyEntity companyEntity= new CompanyEntity(companyId,companyName,ceoName);
        companyEntity.setActive(false);
        CompanyResponse expectedResponse=new CompanyResponse(companyId,"companyName","ceoName");

        when(companyRepository.existsByCompanyName(anyString())).thenReturn(true);
        when(companyRepository.save(any())).thenReturn(companyEntity);
        when(companyRepository.findByCompanyName(anyString())).thenReturn(companyEntity);
        when(modelMapper.map(any(),any())).thenReturn(expectedResponse);

        CompanyResponse actualResponse=companyService.addCompanyDetail(addRequest,1L);
        Assert.assertEquals(expectedResponse.toString(),actualResponse.toString());
    }


    @Test
    public  void test_DeleteCompany_Success() throws NotPresentException {
        Long companyId=1L;
        Response expectedResponse=new Response(200L,DELETE_SUCCESS);

        when(companyRepository.existsByIdAndIsActive(anyLong(),anyBoolean())).thenReturn(true);
        doNothing().when(companyRepository).deleteCompanyQuery(anyLong(),anyBoolean(),anyBoolean(),anyString(),anyLong(),anyLong());
        doNothing().when(caching).deleteDepartmentsOfCompany(companyId);

        Response  actualResponse=companyService.deleteCompanyDetails(companyId);
        Assert.assertEquals(expectedResponse,actualResponse);
    }

  @Test(expected = NotPresentException.class)
    public void test_DeleteCompany_NotPresent_Fails() throws NotPresentException {
        Long companyId=1L;
        when(companyRepository.existsByIdAndIsActive(anyLong(),anyBoolean())).thenReturn(false);
        companyService.deleteCompanyDetails(companyId);
  }

  @Test
    public void test_UpdateCompany_Success() throws DuplicateDataException, NotPresentException {
        Long companyId=1L;
        String companyName="companyname";
        String ceoName="ceoname";
        CompanyResponse expectedResponse=new CompanyResponse(companyId,companyName,ceoName,UPDATE_SUCCESS) ;
        CompanyEntity companyEntity=new CompanyEntity(companyId,companyName,ceoName,true);

        when(companyRepository.findByIdAndIsActive(anyLong(),anyBoolean())).thenReturn(companyEntity);
        when(companyRepository.save(companyEntity)).thenReturn(companyEntity);

        CompanyResponse actualResponse=companyService.updateDetails(companyId,companyName,ceoName,1L);
        Assert.assertEquals(expectedResponse.toString(),actualResponse.toString());
  }

    @Test(expected = NotPresentException.class)
    public void test_UpdateCompany_Fails() throws DuplicateDataException, NotPresentException {
        when(companyRepository.findByIdAndIsActive(anyLong(),anyBoolean())).thenReturn(null);
        companyService.updateDetails(1L,"q","a",1L);
    }


}
