package work1.project1.package1.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import work1.project1.package1.dto.request.EmployeeAddRequest;
import work1.project1.package1.dto.request.EmployeeUpdateRequest;
import work1.project1.package1.dto.request.UpdateSalaryRequest;
import work1.project1.package1.dto.response.EmployeeAddResponse;
import work1.project1.package1.dto.response.Response;
import work1.project1.package1.exception.NotPresentException;
import work1.project1.package1.exception.UnAuthorizedUser;
import work1.project1.package1.myenum.MyEnum;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeControllerTesting {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    ObjectMapper om=new ObjectMapper();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                 .build();
    }

    @Test(expected = UnAuthorizedUser.class)
    public void test_Authentication_Failed() throws Exception {  //
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.delete("/employee/{id}",479)).andReturn();
        // .andExpect(MockMvcResultMatchers.status().isUnauthorized())
       //.andReturn();
      //  String stringResponse=result.getResponse().getContentAsString();
       // Response response=om.readValue(stringResponse,Response.class);
       // Assert.assertTrue(response.getStatus()==(4023));
    }

    @Test
    public void test_DeleteEmployee_Success() throws Exception {  //
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.delete("/employee/{id}",479) // getClass().getName()).andExpect(status().(200L));
                .header("token","admin"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
       // String stringResponse=result.getResponse().getContentAsString();
        //Response response=om.readValue(stringResponse,Response.class);
       // Assert.assertTrue(response.getStatus()==(200));
    }

    @Test
    public void test_DeleteEmployee_Failed() throws Exception {  //
       MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/employee/{id}", 47943) // getClass().getName()).andExpect(status().(200L));
                .header("token","admin"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
       //njj
        }

    @Test
    public void test_UpdateSalary_Success() throws Exception {
        UpdateSalaryRequest salaryRequest=new UpdateSalaryRequest();
        salaryRequest.setEmployeeId(464L);
        salaryRequest.setFlag(1L);
        salaryRequest.setSalary_increment(1000L);
        String jsonRequest=om.writeValueAsString(salaryRequest);
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.put("/employee/update-salary").contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest).header("token","admin"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        }

    @Test
    public void test_UpdateSalary_Failed() throws Exception {
        UpdateSalaryRequest salaryRequest=new UpdateSalaryRequest();
        salaryRequest.setEmployeeId(479L);
        salaryRequest.setFlag(1L);
        salaryRequest.setSalary_increment(1000L);
        String jsonRequest=om.writeValueAsString(salaryRequest);
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.put("/employee/update-salary").contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest).header("token","admin"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
          }

    @Test
    public void test_UpdateEmployee_Success() throws Exception {
        EmployeeUpdateRequest updateRequest=new EmployeeUpdateRequest();
        updateRequest.setId(476L);
        updateRequest.setSalary(23000L);
        updateRequest.setDesignation(MyEnum.employee);
        String jsonRequest=om.writeValueAsString(updateRequest);
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.put("/employee").contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest).header("token","admin"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
         }

    @Test
    public void test_UpdateEmployee_Failed() throws Exception {
        EmployeeUpdateRequest updateRequest=new EmployeeUpdateRequest();
        updateRequest.setId(479L);
        updateRequest.setSalary(23000L);
        updateRequest.setDesignation(MyEnum.employee);
        String jsonRequest=om.writeValueAsString(updateRequest);
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.put("/employee").contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest).header("token","admin"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void test_AddFreelancerEmployee_Success() throws Exception {  //add without company-department
        EmployeeAddRequest employeeRequest=new EmployeeAddRequest();
        employeeRequest.setName("ARJUN");
        employeeRequest.setPhone("9993390859");
        String jsonRequest=om.writeValueAsString(employeeRequest);
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.post("/employee").contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest).header("token","admin"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String stringResponse=result.getResponse().getContentAsString();
        EmployeeAddResponse response=om.readValue(stringResponse,EmployeeAddResponse.class);
        Assert.assertEquals(response.getName(),employeeRequest.getName());
        Assert.assertEquals(response.getPhone(),employeeRequest.getPhone());
    }

    @Test
    public void test_AddEmployee_Success() throws Exception {  //add without company-department
        EmployeeAddRequest employeeRequest=new EmployeeAddRequest();
        employeeRequest.setName("ARJUN");
        employeeRequest.setPhone("9993390859");
        employeeRequest.setCompanyId(1L);
        employeeRequest.setDepartmentId(1L);
        employeeRequest.setDesignation(MyEnum.employee);
        String jsonRequest=om.writeValueAsString(employeeRequest);
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.post("/employee").contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest).header("token","admin"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String stringResponse=result.getResponse().getContentAsString();
        EmployeeAddResponse response=om.readValue(stringResponse,EmployeeAddResponse.class);
        Assert.assertEquals(response.getName(),employeeRequest.getName());
        Assert.assertEquals(response.getPhone(),employeeRequest.getPhone());
    }


}
