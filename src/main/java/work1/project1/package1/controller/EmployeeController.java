package work1.project1.package1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work1.project1.package1.dto.request.*;
import work1.project1.package1.dto.response.EmployeeCompleteGetResponse;
import work1.project1.package1.dto.response.EmployeeGetResponse;
import work1.project1.package1.dto.response.EmployeeUpdateResponse;
import work1.project1.package1.dto.response.Response;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.exception.NotPresentException;
import work1.project1.package1.exception.ResponseHttp;
import work1.project1.package1.services.AuthorizationService;
import work1.project1.package1.services.EmployeeService;
import work1.project1.package1.services.UserService;

import javax.validation.Valid;

import java.util.List;

import static work1.project1.package1.constants.ApplicationConstants.*;

@RestController
@RequestMapping("/employee")
@Validated
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    AuthorizationService authorizationService;
    @Autowired
    UserService userService;

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addEmployeeDetails(@Valid @RequestBody EmployeeAddRequest employeeAddRequestDto ,
                                     @RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                     @RequestHeader(value="password",defaultValue = "0")String password) throws CustomException, NotPresentException {
        authorizationService.isAccessOfDepartment(userId,password,employeeAddRequestDto.getCompanyId(),
                employeeAddRequestDto.getDepartmentId());
        return new ResponseEntity<>(employeeService.addEmployee(employeeAddRequestDto,userId), HttpStatus.OK);
    }


    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getEmployeeById(@Valid @PathVariable("id") Long id,@RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                  @RequestHeader(value="password",defaultValue = "0")String password ) throws CustomException {
         authorizationService.isAccessOfEmployee(userId,password); //can access by all employee
         return new ResponseEntity<>(employeeService.getEmployee(id), HttpStatus.OK);
    }

    @GetMapping(value="/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeGetResponse>> getAllEmployees(@RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                                                     @RequestHeader(value="password",defaultValue = "0")String password ) throws CustomException {
        authorizationService.isAccessOfAll(userId,password);
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteEmployeeDetails(@PathVariable("id") Long id,@RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                        @RequestHeader(value="password",defaultValue = "0")  String password) throws CustomException, NotPresentException {
        authorizationService.isAccessOfDepartment(userId,password);
        return new ResponseEntity<>(employeeService.deleteEmployee(id), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<EmployeeUpdateResponse> updateEmployeeDetails(@Valid @RequestBody EmployeeUpdateRequest requestDto, @RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                                                        @RequestHeader(value="password",defaultValue = "0")  String password) throws CustomException, NotPresentException, ResponseHttp {
        authorizationService.isAccessOfDepartment(userId,password);
        return new ResponseEntity<>(employeeService.updateDetails(requestDto,userId), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateEmployeeGeneralInfo(@Valid @RequestBody EmployeePersonalInfoUpdateRequest updateRequest ,@RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                            @RequestHeader(value="password",defaultValue = "0")  String password) throws ResponseHttp, CustomException {
        authorizationService.isAccessOfEmployee(userId,password,updateRequest.getId());
        return new ResponseEntity<>(employeeService.updatePersonalInfo(updateRequest,userId), HttpStatus.OK);
    }


    @PutMapping(value = "/update-salary")
    public Object updateSalary(@Valid @RequestBody UpdateSalaryRequestDto updateSalaryRequestDto ,@RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                               @RequestHeader(value="password",defaultValue = "0")  String password) throws CustomException {
        authorizationService.isAccessOfCompany(userId,password);
        if(employeeService.updateSalary(updateSalaryRequestDto))
            return new ResponseEntity<>(new Response(200,UPDATE_SUCCESS), HttpStatus.OK);
        throw new CustomException(FAILED);
    }

   @PutMapping(value = "/change-password")
    public ResponseEntity<Response> changePassword( @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest ,
                                                    @RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                @RequestHeader(value="password",defaultValue = "0")  String password) throws CustomException {
       authorizationService.isAccessOfUserData(userId,password,updatePasswordRequest.getId());
       return new ResponseEntity<>(userService.changePassword(updatePasswordRequest.getId(),updatePasswordRequest.getNewPassword()),
               HttpStatus.OK);
   }

}
