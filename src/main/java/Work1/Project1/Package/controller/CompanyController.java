package Work1.Project1.Package.controller;

import Work1.Project1.Package.entity.CompanyEntity;
import Work1.Project1.Package.exception.NotFoundException;
import Work1.Project1.Package.response.ResponseError;
import Work1.Project1.Package.request.RequestCompany;
import Work1.Project1.Package.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static Work1.Project1.Package.constants.ApplicationConstants.*;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;



    @RequestMapping(value = "/all" , produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getAllCompaniesDetails() throws NotFoundException {
        return   companyService.getAllDetails();
    }

    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getCompanyDetailsById(@PathVariable("id") Long companyId) throws NotFoundException {
        return  companyService.getCompanyDetails(companyId);
    }

    @DeleteMapping("/{companyId}")
    public ResponseError deleteCompany(@PathVariable("companyId") Long companyId) {
        return companyService.deleteCompanyDetails(companyId);
    }

    @PutMapping("")
     public ResponseError updateCompanyDetails(@RequestParam("companyId") long companyId,
                                               @RequestParam("companyName") String companyName,
                                               @RequestParam("ceoName") String ceoName) throws NotFoundException {   //CompanyEntity companyEntity ) {
        return companyService.updateDetails(new CompanyEntity(companyId,companyName,ceoName,true));
    }


    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addCompanyDetails(@RequestBody RequestCompany requestCompany) {
        return new ResponseEntity<> (companyService.addCompany(requestCompany), HttpStatus.OK);

    }

    @GetMapping (value="/complete-details")
    public Object getCompanyAllDepartmentDetails(@RequestParam("companyId") long companyId) throws NotFoundException {

        return  companyService.getCompanyCompleteDetails(companyId);
    }

}











