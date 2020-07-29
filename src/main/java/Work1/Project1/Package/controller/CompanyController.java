package Work1.Project1.Package.controller;

import Work1.Project1.Package.entity.CompanyEntity;
import Work1.Project1.Package.exception.CustomException;
import Work1.Project1.Package.response.Response;
import Work1.Project1.Package.request.RequestCompany;
import Work1.Project1.Package.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;



    @RequestMapping(value = "/all" , produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getAllCompaniesDetails() throws CustomException {
        return   companyService.getAllDetails();
    }

    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getCompanyDetailsById(@PathVariable("id") Long companyId) throws CustomException {
        return  companyService.getCompanyDetails(companyId);
    }

    @DeleteMapping("/{companyId}")
    public Response deleteCompany(@PathVariable("companyId") Long companyId) throws CustomException {
        return companyService.deleteCompanyDetails(companyId);
    }

    @PutMapping("")
     public Response updateCompanyDetails(@RequestParam("companyId") long companyId,
                                          @RequestParam("companyName") String companyName,
                                          @RequestParam("ceoName") String ceoName) throws CustomException {   //CompanyEntity companyEntity ) {
        return companyService.updateDetails(new CompanyEntity(companyId,companyName,ceoName,true));
    }


    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addCompanyDetails(@RequestBody RequestCompany requestCompany) {
        return new ResponseEntity<> (companyService.addCompany(requestCompany), HttpStatus.OK);

    }

    @GetMapping (value="/complete-details")
    public Object getCompanyAllDepartmentDetails(@RequestParam("companyId") long companyId) throws CustomException {

        return  companyService.getCompanyCompleteDetails(companyId);
    }

}











