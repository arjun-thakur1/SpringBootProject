package work1.project1.package1.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import work1.project1.package1.dto.request.DepartmentAddRequest;
import work1.project1.package1.dto.request.DepartmentCompanyAddRequest;
import work1.project1.package1.dto.request.DepartmentUpdateRequestDto;
import work1.project1.package1.dto.response.*;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.exception.NotPresentException;
import work1.project1.package1.exception.ResponseHttp;
import work1.project1.package1.exception.UnAuthorizedUser;
import work1.project1.package1.services.AuthorizationService;
import work1.project1.package1.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/department")
@Validated
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping(value = "/{departmentId}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable("departmentId") Long departmentId,@RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                    @RequestHeader(value="password",defaultValue = "0")String password) throws CustomException, UnAuthorizedUser {
        authorizationService.isAccessOfAnyDepartment(userId,password); //any hod , ceo can access
        return new ResponseEntity<> (departmentService.getDepartmentDetail(departmentId),HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments(@RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                                                    @RequestHeader(value="password",defaultValue = "0")String password) throws CustomException, UnAuthorizedUser {
        authorizationService.isAccessOfAnyCompany(userId,password);
        return new ResponseEntity<> (departmentService.getAllDepartments(),HttpStatus.OK);
    }
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepartmentResponse> addDepartment(@Valid @RequestBody DepartmentAddRequest requestDepartment, @RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                @RequestHeader(value="password",defaultValue = "0")String password) throws CustomException, UnAuthorizedUser {
        authorizationService.isAccessOfAnyCompany(userId,password);
        return new ResponseEntity<> (departmentService.addDepartment(requestDepartment,userId),HttpStatus.OK);
    }

    @PutMapping(value = "/update-details")
    public ResponseEntity<DepartmentResponse> updateDepartmentDetails(@Valid @RequestBody DepartmentUpdateRequestDto departmentRequestDto, @RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                                                      @RequestHeader(value="password",defaultValue = "0")String password) throws CustomException, NotPresentException, UnAuthorizedUser {
        authorizationService.isAccessOfAll(userId,password); //or ceo...
        return new ResponseEntity<>(departmentService.updateDetails(departmentRequestDto,userId),HttpStatus.OK);
    }



    @GetMapping(value = "/company/{companyId}/department/{departmentId}")
    public ResponseEntity<DepartmentCompanyResponse> getDepartmentOfCompany(@PathVariable("companyId") Long companyId, @PathVariable("departmentId") Long departmentId,
                                         @RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                         @RequestHeader(value="password",defaultValue = "0")String password) throws CustomException, NotPresentException, UnAuthorizedUser {
        authorizationService.isAccessOfDepartment(userId,password,companyId,departmentId);
        return new ResponseEntity<> (departmentService.getDepartmentOfCompany(companyId,departmentId),HttpStatus.OK);
    }

    @PostMapping(value = "/add-dept-to-company", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> addDepartmentToCompany(@Valid @RequestBody DepartmentCompanyAddRequest requestDepartment, @RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                       @RequestHeader(value="password",defaultValue = "0")String password) throws CustomException, NotPresentException, UnAuthorizedUser {
        authorizationService.isAccessOfCompany(userId,password,requestDepartment.getCompanyId());
        return new ResponseEntity<> (departmentService.addDepartmentToCompany(requestDepartment,userId),HttpStatus.OK);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartmentOfCompany(@PathVariable("companyId")Long companyId , @RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                                              @RequestHeader(value="password",defaultValue = "0")String password) throws CustomException, NotPresentException, UnAuthorizedUser {
        authorizationService.isAccessOfCompany(userId,password,companyId);
        return new ResponseEntity<> (departmentService.getAllDepartmentsOfCompany(companyId),HttpStatus.OK);
    }




    @DeleteMapping("/company/{companyId}/department/{departmentId}")
    public ResponseEntity<Response> deleteDepartment(@PathVariable("companyId") Long companyId, @PathVariable("departmentId") Long departmentId,
                                                     @RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                                     @RequestHeader(value="password",defaultValue = "0")String password) throws CustomException, ResponseHttp, UnAuthorizedUser {
        authorizationService.isAccessOfCompany(userId,password,companyId);
        return new ResponseEntity<>(departmentService.deleteDepartmentDetails(companyId,departmentId),HttpStatus.OK);
    }

    @GetMapping(value="/company/{companyId}/department/{departmentId}/all-employee", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeResponse>> getEmployeesOfDepartment(@PathVariable("companyId") Long companyId, @PathVariable("departmentId") Long departmentId,
                                                         @RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                                         @RequestHeader(value="password",defaultValue = "0")String password) throws CustomException, NotPresentException, UnAuthorizedUser {
        authorizationService.isAccessOfDepartment(userId,password,companyId,departmentId);
        return new ResponseEntity<>(departmentService.getAllEmployeeOfDepartment(companyId,departmentId),HttpStatus.OK);
    }

}
