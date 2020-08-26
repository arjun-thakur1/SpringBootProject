package work1.project1.package1.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import work1.project1.package1.dto.request.DepartmentAddRequest;
import work1.project1.package1.dto.request.DepartmentCompanyAddRequest;
import work1.project1.package1.dto.request.DepartmentUpdateRequest;
import work1.project1.package1.dto.response.*;
import work1.project1.package1.exception.*;
import work1.project1.package1.services.AuthorizationService;
import work1.project1.package1.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static work1.project1.package1.constants.ApplicationConstants.ADMIN;

@RestController
//@RequestMapping("/department")
@Validated
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private AuthorizationService authorizationService;

    private Long updatedBy =1L;
    @GetMapping(value = "/department/{departmentId}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable("departmentId") Long departmentId,
                                                                @RequestHeader(value = "token")String token)
            throws CustomException, UnAuthorizedUser, NotPresentException {
        if(!ADMIN.equals(token))
        authorizationService.isAccessOfDepartmentTable(token,departmentId); // hod,ceo can access
        return new ResponseEntity<> (departmentService.getDepartmentDetail(departmentId),HttpStatus.OK);
    }
    @GetMapping(value = "/department/all")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments(@RequestHeader(value = "token")
                                                                      String token) throws CustomException, UnAuthorizedUser {
        if(!ADMIN.equals(token))
            authorizationService.isAccessOfDepartmentTable(token,-1L);
        return new ResponseEntity<> (departmentService.getAllDepartments(),HttpStatus.OK);
    }
    @PostMapping(value = "/department", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepartmentResponse> addDepartment(@Valid @RequestBody DepartmentAddRequest requestDepartment,
                                                            @RequestHeader(value = "token")String token)
            throws CustomException, UnAuthorizedUser, DuplicateDataException {
        if(!ADMIN.equals(token))
        updatedBy =authorizationService.isAccessOfDepartmentTable(token,-1L);
        return new ResponseEntity<> (departmentService.addDepartment(requestDepartment, updatedBy),HttpStatus.OK);
    }
    @PutMapping(value = "/department/update-details")
    public ResponseEntity<DepartmentResponse> updateDepartmentDetails(@Valid @RequestBody DepartmentUpdateRequest departmentRequestDto,
                                                                      @RequestHeader(value = "token")String token  )
            throws CustomException, NotPresentException, UnAuthorizedUser, DuplicateDataException {
        //authorizationService.isAccessOfAll(userId,password);  //only admin can update
        if(!ADMIN.equals(token))
            throw new UnAuthorizedUser(" user is not authorized!! ");
        return new ResponseEntity<>(departmentService.updateDetails(departmentRequestDto, updatedBy),HttpStatus.OK);
    }



    @GetMapping(value = "/company/{companyId}/department/{departmentId}")
    public ResponseEntity<DepartmentCompanyResponse> getDepartmentOfCompany(@PathVariable("companyId") Long companyId,
                                                                            @PathVariable("departmentId") Long departmentId,
                                                                            @RequestHeader(value = "token")String token )
            throws NotPresentException, UnAuthorizedUser {
        if(!ADMIN.equals(token))
        authorizationService.isAccessOfCompanyDepartment(token,companyId,departmentId);
        return new ResponseEntity<> (departmentService.getDepartmentOfCompany(companyId,departmentId),HttpStatus.OK);
    }
    @PostMapping(value = "/add-dept-to-company", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> addDepartmentToCompany(@Valid @RequestBody DepartmentCompanyAddRequest requestDepartment,
                                                           @RequestHeader(value = "token")String token)
            throws CustomException, NotPresentException, UnAuthorizedUser { //only ceo so departmentId:0L
        if(!ADMIN.equals(token))
        updatedBy =authorizationService.isAccessOfCompanyDepartment(token,requestDepartment.getCompanyId(),-1L);
        return new ResponseEntity<> (departmentService.addDepartmentToCompany(requestDepartment, updatedBy),HttpStatus.OK);
    }
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartmentOfCompany(@PathVariable("companyId")Long companyId ,
                                                                              @RequestHeader(value = "token")String token)
            throws CustomException, NotPresentException, UnAuthorizedUser {
        if(!ADMIN.equals(token))
        authorizationService.isAccessOfCompanyDepartment(token,companyId,-1L);
        return new ResponseEntity<> (departmentService.getAllDepartmentsOfCompany(companyId),HttpStatus.OK);
    }


    @DeleteMapping("/company/{companyId}/department/{departmentId}")
    public ResponseEntity<Response> deleteDepartment(@PathVariable("companyId") Long companyId, @PathVariable("departmentId") Long departmentId,
                                                     @RequestHeader(value = "token")String token) throws ResponseHttp, UnAuthorizedUser {
        if(!ADMIN.equals(token))
        updatedBy =authorizationService.isAccessOfCompanyDepartment(token,companyId,-1L);
        return new ResponseEntity<>(departmentService.deleteDepartmentDetails(companyId,departmentId),HttpStatus.OK);
    }
    @GetMapping(value="/company/{companyId}/department/{departmentId}/all-employee", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeResponse>> getEmployeesOfDepartment(@PathVariable("companyId") Long companyId, @PathVariable("departmentId") Long departmentId,
                                                         @RequestHeader(value = "token")String token) throws CustomException, NotPresentException, UnAuthorizedUser {
        if(!ADMIN.equals(token))
        authorizationService.isAccessOfCompanyDepartment(token,companyId,departmentId);
        return new ResponseEntity<>(departmentService.getAllEmployeeOfDepartment(companyId,departmentId),HttpStatus.OK);
    }

}
