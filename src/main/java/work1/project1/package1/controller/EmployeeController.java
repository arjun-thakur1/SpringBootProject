package work1.project1.package1.controller;

import lombok.NonNull;
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

import javax.validation.Valid;

import java.security.NoSuchAlgorithmException;
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
    ModelMapper modelMapper;
    Long updatedBy=1L;

    @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addEmployeeDetails(@Valid @RequestBody EmployeeAddRequest employeeAddRequestDto ,
                                                     @RequestHeader(value = "token")String token) throws CustomException,
            NotPresentException, UnAuthorizedUser, NoSuchAlgorithmException, InsufficientDataException {
        if(!ADMIN.equals(token))
            updatedBy =  authorizationService.isAccessOfCompanyDepartment(token, employeeAddRequestDto.getCompanyId().
                    longValue(), employeeAddRequestDto.getDepartmentId().longValue());
        return new ResponseEntity<>(employeeService.addEmployee(employeeAddRequestDto, updatedBy), HttpStatus.OK);
    }


    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getEmployeeById(@NonNull @PathVariable("id") Long id,
          @RequestHeader(value = "token")String token) throws CustomException, NotPresentException {
        boolean isSameEmployee;
        if(!ADMIN.equals(token))
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
    public ResponseEntity<List<EmployeeGetResponse>> getAllEmployees(@RequestHeader(value = "token")String token,
                                                                     @RequestParam(value = "page",defaultValue = "0")int page,
                                                                     @RequestParam(value = "size",defaultValue = "10")int size)
            throws  NotPresentException, UnAuthorizedUser {
        //authorizationService.isAccessOfAll(userId,password);
        if(!ADMIN.equals(token))
            throw new UnAuthorizedUser(" user is not authorized!! ");
        return new ResponseEntity<>(employeeService.getAllEmployees(page,size) , HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteEmployeeDetails( @PathVariable("id") Long id, @RequestHeader(value = "token")String token)
            throws CustomException, NotPresentException, UnAuthorizedUser {
        //authorizationService.isAccessOfDepartment(userId,password,id);
        if(!ADMIN.equals(token))
            updatedBy =  authorizationService.isAccessOfCompanyDepartment(token,id);
        return new ResponseEntity<>(employeeService.deleteEmployee(id, updatedBy), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Response> updateEmployeeDetails(@Valid @RequestBody EmployeeUpdateRequest requestDto,
                                                                        @RequestHeader(value = "token")String token)
            throws CustomException, NotPresentException, ResponseHttp, SuccessException, UnAuthorizedUser {
       // authorizationService.isAccessOfDepartment(userId,password,requestDto.getId());
        //-1,-1 also companyId,departmentId
        if(!ADMIN.equals(token)) {
             Long companyId=null,departmentId=null;
             if(requestDto.getCompanyId()==null && requestDto.getDepartmentId()==null){ //if not add and only update...
                 CompanyDepartmentMappingEntity cdMappingEntity=employeeMappingService.getIds(requestDto.getId().longValue());
                 if(cdMappingEntity==null){
                     if(employeeRepository.findById(requestDto.getId().longValue()).orElse(null) == null)
                         throw new CustomException(" employee is not present!! ");
                     throw new CustomException(" employee is not part of any company-department!! ");
                 }
                 companyId=cdMappingEntity.getCompanyId();
                 departmentId=cdMappingEntity.getDepartmentId();
             }
             updatedBy = authorizationService.isAccessOfCompanyDepartment(token,companyId ,departmentId );
        }
        else
            updatedBy =1L;
        return new ResponseEntity<>(employeeService.updateDetails(requestDto, updatedBy), HttpStatus.OK);
    }

    @PutMapping("/update-personal")
    public ResponseEntity<Response> updateEmployeeGeneralInfo(@Valid @RequestBody EmployeePersonalInfoUpdateRequest updateRequest,
                                                              @RequestHeader(value = "token")String token)
            throws ResponseHttp, CustomException, UnAuthorizedUser, NotPresentException, InsufficientDataException {
        if(!ADMIN.equals(token) && !authorizationService.isAccessOfGetEmployee(token, updateRequest.getId().longValue()))
                 throw new UnAuthorizedUser(" user is not Authorized!! ");
        return new ResponseEntity<>(employeeService.updatePersonalInfo(updateRequest,updateRequest.getId().longValue()), HttpStatus.OK);
    }


    @PutMapping(value = "/update-salary")
    public ResponseEntity<Response> updateSalary(@Valid @RequestBody UpdateSalaryRequest request,@RequestHeader(value="token")
            String token) throws CustomException, UnAuthorizedUser, NotPresentException, InsufficientDataException {
        if (!token.equals(ADMIN)) {
            if (request.getEmployeeId() != null)//want to update only emp salary
                updatedBy =authorizationService.isAccessOfCompanyDepartment( token,request.getEmployeeId().longValue());
            else if (request.getDepartmentId() != null && request.getCompanyId() != null)
                updatedBy =authorizationService.isAccessOfCompanyDepartment( token,request.getCompanyId().longValue(),
                        request.getDepartmentId().longValue());
            else if (request.getCompanyId() != null)
                updatedBy =authorizationService.isAccessOfCompanyDepartment(token, request.getCompanyId().longValue(),-1L);
            else
                throw new CustomException(" please enter valid data!!");
        }
        if (employeeService.updateSalary(request))
                return new ResponseEntity<>(new Response(200, UPDATE_SUCCESS), HttpStatus.OK);
            throw new CustomException(FAILED);
    }
    @PutMapping(value = "/change-password")
    public ResponseEntity<Response> changePassword( @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest ,
                                                    @RequestHeader(value = "token")String token) throws CustomException,
            UnAuthorizedUser, NoSuchAlgorithmException {
       if(!ADMIN.equals(token) && !authorizationService.isAccessOfGetEmployee(token, updatePasswordRequest.getId())) //can access by all employee
               throw new UnAuthorizedUser(" user is not Authorized!! ");
       return new ResponseEntity<>(employeeService.changePassword(updatePasswordRequest.getId(),updatePasswordRequest.getNewPassword()),
               HttpStatus.OK);
    }
    @PostMapping(value = "/token-generate")
    public ResponseEntity<TokenGenerateResponse> tokenGeneration(@RequestHeader(value = "user_id",defaultValue = "0")Long userId,
                                 @RequestHeader(value="password",defaultValue = "0")  String password ) throws
            UnAuthorizedUser, NoSuchAlgorithmException {
       return  new ResponseEntity<>(new TokenGenerateResponse(200L,employeeService.tokenGenerate(userId,password)),HttpStatus.OK);
    }

}











