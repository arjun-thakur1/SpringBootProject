package work1.project1.package1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work1.project1.package1.dto.request.EmployeeAddRequest;
import work1.project1.package1.dto.request.EmployeeUpdateRequest;
import work1.project1.package1.dto.request.UpdateSalaryRequestDto;
import work1.project1.package1.dto.response.Response;
import work1.project1.package1.entity.EmployeeMappingEntity;
import work1.project1.package1.services.EmployeeService;

import javax.validation.Valid;
import java.util.List;

import static work1.project1.package1.constants.ApplicationConstants.FAILED;
import static work1.project1.package1.constants.ApplicationConstants.UPDATE_SUCCESS;

@RestController
@RequestMapping("/employee")
@Validated
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object addEmployeeDetails(@Valid @RequestBody EmployeeAddRequest employeeAddRequestDto ) {
        return employeeService.addEmployee(employeeAddRequestDto);
    }


    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getEmployeeById(@PathVariable("id") Long id)  {
        return employeeService.getEmployee(id);
    }

    @DeleteMapping("/{id}")
    public Object deleteEmployeeDetails(@PathVariable("id") Long id)  {
        return employeeService.deleteEmployee(id);
    }

    @PutMapping("")
    public Object updateEmployeeDetails(@Valid @RequestBody EmployeeUpdateRequest requestDto)  {
        return employeeService.updateDetails(requestDto);
    }


    @PutMapping(value = "/update-salary")
    public Object updateSalary(@Valid @RequestBody UpdateSalaryRequestDto updateSalaryRequestDto ){

        if(employeeService.updateSalary(updateSalaryRequestDto))
            return new Response(200,UPDATE_SUCCESS);
        return new Response(404,FAILED);

    }

}
