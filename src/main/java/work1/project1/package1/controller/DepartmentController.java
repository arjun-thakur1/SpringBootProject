package work1.project1.package1.controller;

import org.springframework.validation.annotation.Validated;
import work1.project1.package1.dto.request.DepartmentAddRequest;
import work1.project1.package1.dto.request.DepartmentUpdateRequestDto;
import work1.project1.package1.dto.response.EmployeeCompleteResponse;
import work1.project1.package1.dto.response.Response;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.services.DepartmentService;
import work1.project1.package1.services.EmployeeMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static work1.project1.package1.constants.ApplicationConstants.NOT_PRESENT;

@RestController
@RequestMapping("/department")
@Validated
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeMappingService employeeServices;



    @GetMapping(value = "/{departmentId}")
    public Object getDepartmentById(@PathVariable("departmentId") Long departmentId) throws CustomException {
        return departmentService.getDepartmentDetail(departmentId);
    }
    @GetMapping(value = "/company/{companyId}/department/{departmentId}")
    public Object getDepartmentOfCompany(@PathVariable("companyId") Long companyId,
                                         @PathVariable("departmentId") Long departmentId){
        return departmentService.getDepartmentOfCompany(companyId,departmentId);
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object addDepartmentDetails(@Valid @RequestBody DepartmentAddRequest requestDepartment) {
        return departmentService.addDepartment(requestDepartment);
    }

    @GetMapping("/company/{companyId}")
    public Object getAllDepartmentOfCompany(@PathVariable("companyId")Long companyId) {

        return departmentService.getAll(companyId);
    }

    @PutMapping(value = "/update-details")
    public Object updateDepartmentDetails(@Valid @RequestBody DepartmentUpdateRequestDto departmentRequestDto) {

        return  departmentService.updateDetails(departmentRequestDto);
    }


    @DeleteMapping("/company/{companyId}/department/{departmentId}")
    public Object deleteDepartment(@PathVariable("companyId") Long companyId,
                                   @PathVariable("departmentId") Long departmentId) throws Exception {
        return departmentService.deleteDepartmentDetails(companyId,departmentId);

    }



    @GetMapping(value="/company/{companyId}/department/{departmentId}/all-employee", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getEmployeesOfDepartment(@PathVariable("companyId") Long companyId,
                                           @PathVariable("departmentId") Long departmentId){
         List<EmployeeCompleteResponse> responseEmployeeList=departmentService.getAllEmployeeOfDepartment(companyId,departmentId);
         if(responseEmployeeList==null || responseEmployeeList.isEmpty())
             return  new Response(204,NOT_PRESENT);
         return responseEmployeeList;
    }

    //completeOrginazation()

}
