package work1.project1.package1.controller;

import work1.project1.package1.dto.request.CompanyUpdateRequest;
import work1.project1.package1.dto.request.UpdateSalaryRequestDto;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.dto.request.CompanyAddRequest;
import work1.project1.package1.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import work1.project1.package1.services.DepartmentService;
import work1.project1.package1.services.EmployeeService;

import javax.validation.Valid;

@RestController
@RequestMapping("/company")
@Validated
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/all" , produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getAllCompanies() throws CustomException {
        return   companyService.getAll();
    }

    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getCompanyDetailsById(@Valid @PathVariable("id") Long companyId) throws CustomException {
       return  companyService.getCompanyById(companyId);
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addCompany( @Valid @RequestBody CompanyAddRequest companyDto ) {
        return new ResponseEntity<> (companyService.addCompanyDetail(companyDto), HttpStatus.OK);
    }

    @DeleteMapping("/{companyId}")
    public Object deleteCompanyById(@PathVariable("companyId") Long companyId) throws CustomException {
        return companyService.deleteCompanyDetails(companyId);
    }

    @PutMapping("")
    public Object updateCompanyDetail(@Valid @RequestBody CompanyUpdateRequest companyUpdateRequestDto) throws CustomException {
        return companyService.updateDetails(companyUpdateRequestDto.getId(),companyUpdateRequestDto.getCompanyName(),
                companyUpdateRequestDto.getCeoName());
    }


    //response of this api must be change....
    @GetMapping (value="/complete-details/{companyId}")
    public Object getEmployeesOfCompany(@PathVariable("companyId") Long companyId) {
        return  companyService.getallEmployeesOfCompany(companyId);
    }


}











