package work1.project1.package1.other;

import java.util.UUID;

public class TokenGenerator {

  public String tokenGenerate(Long companyId,Long departmentId,Long employeeId) {

   return  UUID.randomUUID().toString()+"-"+(companyId.toString())+"-"+(departmentId.toString())+"-"+(employeeId.toString());
  }
}
