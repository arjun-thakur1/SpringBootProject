package work1.project1.package1.other;

public class TokenIds {

    public CompanyDepartmentEmployeeIds findCompanyDepartmentEmployeeIdFromToken(String token) {

        CompanyDepartmentEmployeeIds ids=new CompanyDepartmentEmployeeIds();
        int lastIndex=token.lastIndexOf('-');
        ids.setEmployeeId(Long.valueOf(token.substring(lastIndex+1)));

        String str="";
        int i;
        for( i=lastIndex-1;token.charAt(i)!='-';i--){
            str= token.charAt(i)+str;
        }
        ids.setDepartmentId(Long.valueOf(str));

        str="";
        i--;
        for(;token.charAt(i)!='-';i--){
            str= token.charAt(i)+str;
        }
        ids.setCompanyId(Long.valueOf(str));

        return ids;
    }
}
