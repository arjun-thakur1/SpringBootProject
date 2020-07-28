package Work1.Project1.Package.controller;


import Work1.Project1.Package.entity.EmployeeEntity;
import Work1.Project1.Package.entity.EmployeePK;
import Work1.Project1.Package.request.RequestEmployee;
import Work1.Project1.Package.response.ResponseError;
import Work1.Project1.Package.services.EmployeeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static Work1.Project1.Package.constants.ApplicationConstants.Not_Present;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeServices employeeService;

    @GetMapping("")
    public Object getAllEmployeesDetail(@RequestParam(defaultValue = "0") Integer pageNo,
                                        @RequestParam(defaultValue = "2") Integer pageSize) {

        return employeeService.getAllDetails(pageNo,pageSize);
    }

    @GetMapping("/one")
    public Object getEmployeeDetail(@RequestBody EmployeePK employeePK) {
        Object empObject= employeeService.getEmployeeDetails(employeePK);
        if(empObject==null){
            ResponseError responseError =new ResponseError(200 , Not_Present);
            return responseError;
        }
        else{
            return empObject;
        }
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String addEmployeeDetails(@RequestBody RequestEmployee requestEmployee){

        return employeeService.addEmployee(requestEmployee);
    }

    @DeleteMapping("")
    public String deleteEmployee(@RequestParam("employeeId") Long employeeId,
                                 @RequestParam("departmentId") Long departmentId,
                                 @RequestParam("companyId") Long companyId) throws Exception {
        EmployeePK employeePK = new EmployeePK( companyId,departmentId,employeeId);
        return employeeService.deleteEmployeeDetails(employeePK);

    }

     //.....................
    @RequestMapping(value = "/update-details", method = RequestMethod.PUT)
    public String updateEmployeeDetails( @RequestParam("employeeId") Long employeeId, @RequestParam("departmentId") Long departmentId,
                                       @RequestParam("companyId") Long companyId , @RequestParam("phone") String phone,
                                       @RequestParam("empName") String empName, @RequestParam("salary") Long salary){
     return  employeeService.updateDetails(employeeId,departmentId,companyId,phone,empName,salary);
    }

    @RequestMapping(value = "/update-salary", method = RequestMethod.PUT)
    public String updateSalary(@RequestParam("companyId") Long companyId,
                             @RequestParam(value = "departmentId" ,defaultValue ="-1") Long departmentId,
                             @RequestParam(value = "employeeId",defaultValue ="-1") Long employeeId,
                             @RequestParam("increment") Long increment,
                             @RequestParam(value="flag",defaultValue ="1") boolean flag ) {
        if(flag)
        {
           return employeeService.updateSalaryByAbsoluteValue(companyId,departmentId,employeeId,increment);

        }
        else
        {
            return employeeService.updateSalaryByPercentage(companyId,departmentId,employeeId,increment);
        }
    }






}