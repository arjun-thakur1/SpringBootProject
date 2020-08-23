package work1.project1.package1.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work1.project1.package1.dto.request.*;
import work1.project1.package1.dto.response.*;
import work1.project1.package1.entity.CompanyDepartmentMappingEntity;
import work1.project1.package1.exception.*;
import work1.project1.package1.repository.EmployeeRepository;
import work1.project1.package1.services.AuthorizationService;
import work1.project1.package1.services.EmployeeMappingService;
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
    EmployeeRepository employeeRepository;
    @Autowired
    EmployeeMappingService employeeMappingService;
    @Autowired
    AuthorizationService authorizationService;
    @Autowired
    UserService userService;
    @Autowired
    ModelMapper modelMapper;
    Long userId;

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addEmployeeDetails(@Valid @RequestBody EmployeeAddRequest employeeAddRequestDto ,
                                                     @RequestHeader(value = "token",defaultValue = "0")String token) throws CustomException,
            NotPresentException, UnAuthorizedUser {
        if(!token.equals(ADMIN))
            userId=  authorizationService.isAccessOfCompanyDepartment(token, employeeAddRequestDto.getCompanyId(), employeeAddRequestDto.getDepartmentId());
        else
            userId=1L;
        return new ResponseEntity<>(employeeService.addEmployee(employeeAddRequestDto,userId), HttpStatus.OK);
    }


    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getEmployeeById(@PathVariable("id") Long id,
          @RequestHeader(value = "token",defaultValue = "0")String token) throws CustomException {
        boolean isSameEmployee;
        if(!token.equals(ADMIN))
           isSameEmployee = authorizationService.isAccessOfGetEmployee(token, id); //can access by all employee
        else
        isSameEmployee=true;
        Object employee=employeeService.getEmployee(id);
          if(isSameEmployee)
          return new ResponseEntity<>(employee, HttpStatus.OK);
          else
              return new ResponseEntity<>(modelMapper.map(employee,EmployeeGetResponse.class),HttpStatus.OK);
    }

    @GetMapping(value="/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeGetResponse>> getAllEmployees(@RequestHeader(value = "token",defaultValue = "0")String token,
                                                                     @RequestParam(value = "page",defaultValue = "0")int page,
                                                                     @RequestParam(value = "size",defaultValue = "10")int size) throws  NotPresentException, UnAuthorizedUser {
        //authorizationService.isAccessOfAll(userId,password);
        if(!token.equals(ADMIN))
            throw new UnAuthorizedUser(" user is not authorized!! ");
        return new ResponseEntity<>(employeeService.getAllEmployees(page,size) , HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteEmployeeDetails(@PathVariable("id") Long id,@RequestHeader(value = "token",defaultValue = "0")String token)
            throws CustomException, NotPresentException, UnAuthorizedUser {
        //authorizationService.isAccessOfDepartment(userId,password,id);
        if(!token.equals(ADMIN))
            userId=  authorizationService.isAccessOfCompanyDepartment(token,id);
        else
            userId=1L;
        return new ResponseEntity<>(employeeService.deleteEmployee(id,userId), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<EmployeeUpdateResponse> updateEmployeeDetails(@Valid @RequestBody EmployeeUpdateRequest requestDto,
                                                                        @RequestHeader(value = "token",defaultValue = "0")String token)
            throws CustomException, NotPresentException, ResponseHttp, SuccessException, UnAuthorizedUser {
       // authorizationService.isAccessOfDepartment(userId,password,requestDto.getId());
        //-1,-1 also companyId,departmentId
        if(!token.equals(ADMIN)) {
            Long companyId=requestDto.getCompanyId(),departmentId=requestDto.getDepartmentId();
             if(requestDto.getCompanyId()==-1 && requestDto.getDepartmentId()==-1){ //if not add and only update...
                 CompanyDepartmentMappingEntity cdMappingEntity=employeeMappingService.getIds(requestDto.getId());
                 if(cdMappingEntity==null){
                     if(employeeRepository.findById(requestDto.getId()).orElse(null) == null)
                         throw new CustomException(" employee is not present!! ");
                     throw new CustomException(" employee is not part of any company-department!! ");
                 }
                 companyId=cdMappingEntity.getCompanyId();
                 departmentId=cdMappingEntity.getDepartmentId();
             }
             userId = authorizationService.isAccessOfCompanyDepartment(token,companyId ,departmentId );
        }
        else
            userId=1L;
        return new ResponseEntity<>(employeeService.updateDetails(requestDto,userId), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateEmployeeGeneralInfo(@Valid @RequestBody EmployeePersonalInfoUpdateRequest updateRequest,
                                                              @RequestHeader(value = "token",defaultValue = "0")String token)
            throws ResponseHttp, CustomException, UnAuthorizedUser {
        if(!token.equals(ADMIN) && !authorizationService.isAccessOfGetEmployee(token, updateRequest.getId()))
                 throw new UnAuthorizedUser(" user is not Authorized!! ");
        return new ResponseEntity<>(employeeService.updatePersonalInfo(updateRequest,updateRequest.getId()), HttpStatus.OK);
    }


    @PutMapping(value = "/update-salary")
    public Object updateSalary(@Valid @RequestBody UpdateSalaryRequest request , @RequestHeader(value = "token",
            defaultValue = "0")String token) throws CustomException, UnAuthorizedUser, NotPresentException {
        if (!token.equals(ADMIN)) {
            if (request.getEmployeeId() != -1)//want to update only emp salary
                userId=authorizationService.isAccessOfCompanyDepartment( token,request.getEmployeeId());
            else if (request.getDepartmentId() != -1 && request.getCompanyId() != -1)
                userId=authorizationService.isAccessOfCompanyDepartment( token,request.getCompanyId(), request.getDepartmentId());
            else if (request.getCompanyId() != -1)
                userId=authorizationService.isAccessOfCompanyDepartment(token, request.getCompanyId(),-1L);
            else
                throw new CustomException(FAILED + " please enter valid data ");
        }
        else
            userId=1L;
            if (employeeService.updateSalary(request))
                return new ResponseEntity<>(new Response(200, UPDATE_SUCCESS), HttpStatus.OK);
            throw new CustomException(FAILED);

    }
    @PutMapping(value = "/change-password")
    public ResponseEntity<Response> changePassword( @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest ,
                                                    @RequestHeader(value = "token",defaultValue = "0")String token) throws CustomException, UnAuthorizedUser {
       if(!token.equals(ADMIN) && !authorizationService.isAccessOfGetEmployee(token, updatePasswordRequest.getId())) //can access by all employee
               throw new UnAuthorizedUser(" user is not Authorized!! ");
       return new ResponseEntity<>(userService.changePassword(updatePasswordRequest.getId(),updatePasswordRequest.getNewPassword()),
               HttpStatus.OK);
    }
    @PostMapping(value = "/token-generate")
    public ResponseEntity<TokenGenerateResponse> tokenGeneration(@RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                 @RequestHeader(value="password",defaultValue = "0")  String password ) throws UnAuthorizedUser {
       return  new ResponseEntity<>(new TokenGenerateResponse(200L,employeeService.tokenGenerate(userId,password)),HttpStatus.OK);
    }

}
