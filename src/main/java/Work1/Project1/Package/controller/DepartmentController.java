package Work1.Project1.Package.controller;

import Work1.Project1.Package.entity.*;
import Work1.Project1.Package.response.Response;
import Work1.Project1.Package.request.RequestDepartment;
import Work1.Project1.Package.services.DepartmentServices;
import Work1.Project1.Package.services.EmployeeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentServices departmentService;

    @Autowired
    private EmployeeServices employeeServices;

    @GetMapping("/all")
    public Object getAllDepartmentsDetail() {

         return departmentService.getAllDetails();
    }

    @RequestMapping(value = "/company/{companyId}/department/{departmentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getDepartmentDetails(@PathVariable("departmentId") Long departmentId,
                                       @PathVariable("companyId") Long companyId){
        return departmentService.getDepartmentDetail(departmentId, companyId);
    }

    @GetMapping(value="/all-employee-details")
     public Object getAllEmployeeeOfDepartment(@RequestBody DepartmentPK departmentPK)
    {
        return employeeServices.getAllEmployeeOfDepartment(departmentPK);
    }

    @DeleteMapping("")
    public Object deleteDepartment(@RequestParam("departmentId") Long departmentId,
                                 @RequestParam("companyId") Long companyId) throws Exception {
        DepartmentPK departmentPK = new DepartmentPK(companyId,departmentId);
        return departmentService.deleteDepartmentDetails(departmentPK);

    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response addDepartmentDetails(@RequestBody RequestDepartment requestDepartment) {

       return departmentService.addDepartment(requestDepartment);
    }


    @RequestMapping(value = "/update-details", method = RequestMethod.PUT)
    public Response updateDepartmentDetails(@RequestParam(value = "companyId") Long companyId,
                                            @RequestParam("departmentId") Long departmentId,
                                            @RequestParam("departmentName") String departmentName,
                                            @RequestParam(value="managerId",defaultValue = "-1") Long managerId) { // RequestUpdateDepartment requestUpdateDepartment) {

        return  departmentService.updateDetails(companyId,departmentId,departmentName,managerId);
    }




    //completeOrginazation()

}
