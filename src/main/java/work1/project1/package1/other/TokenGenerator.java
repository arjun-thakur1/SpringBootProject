package work1.project1.package1.other;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class TokenGenerator {
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    private static final SecureRandom random = new SecureRandom();

    public String tokenGenerate(Long companyId,Long departmentId,Long userId) {
        byte[] randomBytes = new byte[24];
        random.nextBytes(randomBytes);
        String token= base64Encoder.encodeToString(randomBytes);

        return token+"-"+(companyId.toString())+"-"+(departmentId.toString())+"-"+(userId.toString());
        //  return  UUID.randomUUID().toString()+"-"+(companyId.toString())+"-"+(departmentId.toString())+"-"+(employeeId.toString());
    }
}
