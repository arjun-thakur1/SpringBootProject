package work1.project1.package1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work1.project1.package1.dto.request.EmployeeAddRequestDto;
import work1.project1.package1.dto.request.EmployeeUpdateRequestDto;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.services.EmployeeService;

import javax.validation.Valid;

@RestController
@RequestMapping("/employee")
@Validated
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;


    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getEmployeeById(@PathVariable("id") Long id) throws CustomException {
        return employeeService.getEmployee(id);
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object addEmployeeDetails(@Valid @RequestBody EmployeeAddRequestDto employeeAddRequestDto ) {
        return employeeService.addEmployee(employeeAddRequestDto);
    }


    @DeleteMapping("/{id}")
    public Object deleteEmployeeDetails(@PathVariable("id") Long id) throws CustomException {
        return employeeService.deleteEmployee(id);
    }

    @PutMapping("")
    public Object updateEmployeeDetails(@Valid @RequestBody EmployeeUpdateRequestDto requestDto) throws CustomException {
        return employeeService.updateDetails(requestDto);
    }

}
