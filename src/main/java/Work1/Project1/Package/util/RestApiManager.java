package Work1.Project1.Package.util;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//@ConfigurationProperties("auth")
@Component
@Service
@Getter @Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestApiManager {

    @Autowired
    RestTemplate restTemplate;

    private String url = "https://e6707348-a2a6-4a80-9d7a-c89850abe893.mock.pstmn.io";

    public boolean  authorization(String token) {

        String result = restTemplate.getForObject(this.url +"/"+token, String.class); //getForObject(uri, String.class);
         if(result.equals("1"))
               return true;
           else
               return false;
   }
}
