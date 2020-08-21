package work1.project1.package1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work1.project1.package1.dto.request.*;
import work1.project1.package1.dto.response.EmployeeGetResponse;
import work1.project1.package1.dto.response.EmployeeUpdateResponse;
import work1.project1.package1.dto.response.Response;
import work1.project1.package1.exception.*;
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
                                     @RequestHeader(value="password",defaultValue = "0")String password) throws CustomException, NotPresentException, UnAuthorizedUser {
        authorizationService.isAccessOfDepartment(userId,password,employeeAddRequestDto.getCompanyId(), employeeAddRequestDto.getDepartmentId());
        return new ResponseEntity<>(employeeService.addEmployee(employeeAddRequestDto,userId), HttpStatus.OK);
    }


    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getEmployeeById(@Valid @PathVariable("id") Long id,@RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                  @RequestHeader(value="password",defaultValue = "0")String password ) throws CustomException, UnAuthorizedUser {
         authorizationService.isAccessOfGetEmployee(userId,password); //can access by all employee
         return new ResponseEntity<>(employeeService.getEmployee(id), HttpStatus.OK);
    }

    @GetMapping(value="/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeGetResponse>> getAllEmployees(@RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                                                     @RequestHeader(value="password",defaultValue = "0")String password ,
                                                                     @RequestParam(value = "page",defaultValue = "0")int page,
                                                                     @RequestParam(value = "size",defaultValue = "10")int size) throws CustomException, NotPresentException, UnAuthorizedUser {
        authorizationService.isAccessOfAll(userId,password);
        return new ResponseEntity<>(employeeService.getAllEmployees(page,size) , HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteEmployeeDetails(@PathVariable("id") Long id,@RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                        @RequestHeader(value="password",defaultValue = "0")  String password) throws CustomException, NotPresentException, UnAuthorizedUser {
        authorizationService.isAccessOfDepartment(userId,password,id);
        return new ResponseEntity<>(employeeService.deleteEmployee(id), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<EmployeeUpdateResponse> updateEmployeeDetails(@Valid @RequestBody EmployeeUpdateRequest requestDto, @RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                                                        @RequestHeader(value="password",defaultValue = "0")  String password)
            throws CustomException, NotPresentException, ResponseHttp, SuccessException, UnAuthorizedUser {
        authorizationService.isAccessOfDepartment(userId,password,requestDto.getId());
        return new ResponseEntity<>(employeeService.updateDetails(requestDto,userId), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateEmployeeGeneralInfo(@Valid @RequestBody EmployeePersonalInfoUpdateRequest updateRequest ,@RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                            @RequestHeader(value="password",defaultValue = "0")  String password) throws ResponseHttp, CustomException, UnAuthorizedUser {
        authorizationService.isAccessOfUpdateEmployee(userId,password,updateRequest.getId());
        return new ResponseEntity<>(employeeService.updatePersonalInfo(updateRequest,userId), HttpStatus.OK);
    }


    @PutMapping(value = "/update-salary")
    public Object updateSalary(@Valid @RequestBody UpdateSalaryRequest request , @RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                               @RequestHeader(value="password",defaultValue = "0")  String password) throws CustomException, UnAuthorizedUser {
        if(request.getEmployeeId()!=-1)//want to update emp salary
            authorizationService.isAccessOfDepartment(userId,password,request.getEmployeeId());
        else if(request.getDepartmentId()!=-1 && request.getCompanyId()!=-1)
            authorizationService.isAccessOfDepartment(userId,password,request.getCompanyId(),request.getDepartmentId());
        else if(request.getCompanyId()!=-1)
            authorizationService.isAccessOfCompany(userId,password,request.getCompanyId());
        else
            throw new CustomException(FAILED + " please enter valid data " );

        if(employeeService.updateSalary(request))
            return new ResponseEntity<>(new Response(200,UPDATE_SUCCESS), HttpStatus.OK);
        throw new CustomException(FAILED);
    }

   @PutMapping(value = "/change-password")
    public ResponseEntity<Response> changePassword( @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest ,
                                                    @RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                @RequestHeader(value="password",defaultValue = "0")  String password) throws CustomException, UnAuthorizedUser {
       authorizationService.isAccessOfUserData(userId,password,updatePasswordRequest.getId());
       return new ResponseEntity<>(userService.changePassword(updatePasswordRequest.getId(),updatePasswordRequest.getNewPassword()),
               HttpStatus.OK);
   }

}
