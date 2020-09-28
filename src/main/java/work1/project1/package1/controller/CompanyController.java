package work1.project1.package1.controller;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work1.project1.package1.dto.request.CompanyUpdateRequest;
import work1.project1.package1.dto.response.AllCompaniesResponse;
import work1.project1.package1.dto.response.CompanyResponse;
import work1.project1.package1.dto.response.EmployeeResponse;
import work1.project1.package1.dto.response.Response;
import work1.project1.package1.exception.*;
import work1.project1.package1.dto.request.CompanyAddRequest;
import work1.project1.package1.services.AuthorizationService;
import work1.project1.package1.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work1.project1.package1.services.DepartmentService;
import work1.project1.package1.services.EmployeeService;

import javax.validation.Valid;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static work1.project1.package1.constants.ApplicationConstants.ADMIN;

@RestController
@RequestMapping(value = "/company")
@Validated
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AuthorizationService authorizationService;
    Long updatedBy =1L;

Logger logger=LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/all" )
    public ResponseEntity<List<AllCompaniesResponse>> getAllCompanies(@RequestHeader(value = "token")String token,
                                                                      @RequestParam(value = "page",defaultValue = "0")int page,
                                                                      @RequestParam(value = "size",defaultValue = "10")int size,
                                                                      @RequestParam(value = "b",required = false)boolean b) throws
            CustomException, NotPresentException, UnAuthorizedUser, SuccessException {
        System.out.println(b);
        if(!ADMIN.equals(token))
            throw new UnAuthorizedUser(" user is not Authorized!! ");
        return new ResponseEntity<> (companyService.getAll(page,size),HttpStatus.OK);
    }


    @GetMapping(value="/{id}")
    public ResponseEntity<CompanyResponse> getCompanyDetailsById(@NonNull @PathVariable("id") Long companyId,
                                                                 @RequestHeader(value = "token")String token)
            throws  NotPresentException, UnAuthorizedUser {
        if(!ADMIN.equals(token))
            authorizationService.isAccessOfCompanyDepartment(token,companyId,-1L);
        return new ResponseEntity<> (companyService.getCompanyById(companyId),HttpStatus.OK);
    }

    @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompanyResponse> addCompany(  @RequestBody @Valid CompanyAddRequest companyDto ,
                                                       @RequestHeader(value = "token")String token)
            throws DuplicateDataException, UnAuthorizedUser {
        if(!ADMIN.equals(token))
            throw new UnAuthorizedUser(" user is not Authorized!! ");
        return new ResponseEntity<> (companyService.addCompanyDetail(companyDto,1L), HttpStatus.OK);
    }

    @DeleteMapping("/{companyId}")
    public  ResponseEntity<Response> deleteCompanyById(@Valid @PathVariable("companyId") Long companyId,
                                                       @RequestHeader(value = "token")String token) throws CustomException, NotPresentException, UnAuthorizedUser {
        if(!ADMIN.equals(token))
            throw new UnAuthorizedUser(" user is not Authorized!! ");
        return new ResponseEntity<> (companyService.deleteCompanyDetails(companyId),HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<CompanyResponse> updateCompanyDetail(@Valid @RequestBody CompanyUpdateRequest companyUpdateRequestDto ,
                                                               @RequestHeader(value = "token")String token)
            throws DuplicateDataException, NotPresentException, UnAuthorizedUser, InsufficientDataException, CustomException {
        if(!ADMIN.equals(token))
           updatedBy =authorizationService.isAccessOfCompanyDepartment(token,companyUpdateRequestDto.getId().longValue(),-1L);
        return new ResponseEntity<> (companyService.updateDetails(companyUpdateRequestDto.getId(),companyUpdateRequestDto.getCompanyName(),
                companyUpdateRequestDto.getCeoName(), updatedBy),HttpStatus.OK);
    }


    @GetMapping (value="/complete-details/{companyId}")
    public ResponseEntity<HashMap<Long,List<EmployeeResponse>>> getCompanyCompleteDetails(@PathVariable("companyId") Long companyId,
                                                                                      @RequestHeader(value = "token")String token)
            throws CustomException, NotPresentException, UnAuthorizedUser {
        if(!ADMIN.equals(token))
            authorizationService.isAccessOfCompanyDepartment(token,companyId,-1L);
        return  new ResponseEntity<> (companyService.getCompanyCompleteDetails(companyId),HttpStatus.OK);
    }

}











