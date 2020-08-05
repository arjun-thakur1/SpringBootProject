package work1.project1.package1.controller;

import org.springframework.validation.annotation.Validated;
import work1.project1.package1.dto.request.DepartmentAddRequestDto;
import work1.project1.package1.dto.request.DepartmentUpdateRequestDto;
import work1.project1.package1.dto.response.EmployeeCompleteResponseDto;
import work1.project1.package1.dto.response.Response;
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

    @GetMapping("/company/{companyId}")
    public Object getAllDepartmentOfCompany(@PathVariable("companyId")Long companyId) {

         return departmentService.getAll(companyId);
    }

    @GetMapping(value = "/{departmentId}")
    public Object getDepartmentById(@PathVariable("departmentId") Long departmentId){
        return departmentService.getDepartmentDetail(departmentId);
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object addDepartmentDetails(@Valid @RequestBody DepartmentAddRequestDto requestDepartment) {
        return departmentService.addDepartment(requestDepartment);
    }

    @DeleteMapping("/{departmentId}")
    public Object deleteDepartment( @PathVariable("departmentId") Long departmentId) throws Exception {
        return departmentService.deleteDepartmentDetails(departmentId);

    }

    @PutMapping(value = "/update-details")
    public Object updateDepartmentDetails(@Valid @RequestBody DepartmentUpdateRequestDto departmentRequestDto) {

        return  departmentService.updateDetails(departmentRequestDto);
    }

    @GetMapping(value="/{departmentId}/all-employee", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getEmployeesOfDepartment(@PathVariable("departmentId")Long departmentId){
         List<EmployeeCompleteResponseDto>responseEmployeeList=departmentService.getAllEmployeeOfDepartment(departmentId);
         if(responseEmployeeList.isEmpty())
             return  new Response(204,NOT_PRESENT);
         return responseEmployeeList;
    }

    //completeOrginazation()

}
