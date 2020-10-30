package work1.project1.package1.controller;

import work1.project1.package1.dto.request.CompanyUpdateRequest;
import work1.project1.package1.dto.response.CompanyDeleteResponse;
import work1.project1.package1.dto.response.CompanyResponse;
import work1.project1.package1.dto.response.EmployeeResponse;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.dto.request.CompanyAddRequest;
import work1.project1.package1.exception.DuplicateDataException;
import work1.project1.package1.exception.NotPresentException;
import work1.project1.package1.exception.UnAuthorizedUser;
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
import javax.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/company")
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


    @GetMapping(value = "/all" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CompanyResponse>> getAllCompanies(@RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                                 @RequestHeader(value="password",defaultValue = "0")String password,
                                                 @RequestParam(value = "page",defaultValue = "0")int page,
                                                 @RequestParam(value = "size",defaultValue = "10")int size) throws CustomException, NotPresentException, UnAuthorizedUser {
       // authorizationService.isAccessOfAll(userId,password);
        return new ResponseEntity<> (companyService.getAll(page,size),HttpStatus.OK);
    }

    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompanyResponse> getCompanyDetailsById(@Valid @PathVariable("id") @NotNull  Long companyId, @RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                                 @RequestHeader(value="password",defaultValue = "0")String password) throws CustomException, NotPresentException, UnAuthorizedUser {
        authorizationService.isAccessOfCompany(userId,password,companyId);
        return new ResponseEntity<> (companyService.getCompanyById(companyId),HttpStatus.OK);
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompanyResponse> addCompany( @Valid @RequestBody CompanyAddRequest companyDto ,@RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                              @RequestHeader(value="password",defaultValue = "0")String password) throws CustomException, DuplicateDataException, UnAuthorizedUser {
        authorizationService.isAccessOfAll(userId,password);
        return new ResponseEntity<> (companyService.addCompanyDetail(companyDto,userId), HttpStatus.OK);
    }

    @DeleteMapping("/{companyId}")
    public  ResponseEntity<CompanyDeleteResponse> deleteCompanyById(@PathVariable("companyId") Long companyId, @RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                                   @RequestHeader(value="password",defaultValue = "0")String password) throws CustomException, NotPresentException, UnAuthorizedUser {
        authorizationService.isAccessOfAll(userId,password);
        return new ResponseEntity<> (companyService.deleteCompanyDetails(companyId),HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<CompanyResponse> updateCompanyDetail(@Valid @RequestBody CompanyUpdateRequest companyUpdateRequestDto , @RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                               @RequestHeader(value="password",defaultValue = "0")String password) throws CustomException, DuplicateDataException, NotPresentException, UnAuthorizedUser {
        authorizationService.isAccessOfCompany(userId,password,companyUpdateRequestDto.getId());
        return new ResponseEntity<> (companyService.updateDetails(companyUpdateRequestDto.getId(),companyUpdateRequestDto.getCompanyName(),
                companyUpdateRequestDto.getCeoName(),userId),HttpStatus.OK);
    }

    @GetMapping (value="/complete-details/{companyId}")
    public ResponseEntity<HashMap<Long,List<EmployeeResponse>>> getEmployeesOfCompany(@PathVariable("companyId") Long companyId, @RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                                                      @RequestHeader(value="password",defaultValue = "0")String password) throws CustomException, NotPresentException, UnAuthorizedUser {
        authorizationService.isAccessOfCompany(userId,password,companyId);
        return  new ResponseEntity<> (companyService.getallEmployeesOfCompany(companyId),HttpStatus.OK);
    }

}











