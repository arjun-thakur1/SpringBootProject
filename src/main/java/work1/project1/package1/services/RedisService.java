package work1.project1.package1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static work1.project1.package1.constants.ApplicationConstants.SUCCESS;

@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    public String getKeyValue(String token) {

        Jedis jedis = jedisPool.getResource();
        String keyValue=jedis.get(token);
        return keyValue;
    }

    public void deleteTokenFromCache(String token){
        Jedis jedis = jedisPool.getResource();
        jedis.del(token);
        return ;
    }

 /*   public boolean isTokenAlreadyPresent(Long userId){
        Jedis jedis=jedisPool.getResource();
        String cursor = "0";
        ScanParams scanParams=new ScanParams();
        scanParams.match("*"+"-"+(userId.toString()));
        do{
            ScanResult<String> scan =jedis.scan(cursor,scanParams);
            List<String> result = scan.getResult();
            if (result!=null && result.size()>0)
                return true;
            cursor=scan.getCursor();
        }while(!cursor.equals("0"));
        return false;
    }
  */
    public String deleteTokenOfEmployeeFromCache(Long employeeId){ //todo improve logic by using an alt cache
            Jedis jedis = jedisPool.getResource();
           // jedis = jedisPool.getResource();
            String cursor = "0";
            ScanParams sp = new ScanParams();
            sp.match("*"+"-"+(employeeId.toString()));
             do{
                 ScanResult<String> scan = jedis.scan(cursor, sp);
                 List<String> result = scan.getResult();
                 if(result!=null && result.size()>0){
                    deleteTokenFromCache(result.get(0));
                 }
                cursor = scan.getCursor();
            }while(!cursor.equals("0"));
            return SUCCESS;
        }

    public void deleteListOfTokensFromCache(List<String> token){
        Jedis jedis = jedisPool.getResource();
        token.forEach(t->{
            jedis.del(t);
        });

        return ;
    }
    public void deleteTokensOfEmployeesOfDepartment(Long companyId, Long departmentId) {
        Jedis jedis = jedisPool.getResource();
        jedis = jedisPool.getResource();
        String cursor = "0";
        ScanParams sp = new ScanParams();
        sp.match("*"+"-"+(companyId.toString())+"-"+(departmentId.toString())+"*" );
        do{
            ScanResult<String> scan = jedis.scan(cursor, sp);
            List<String> result = scan.getResult();
            if(result!=null && result.size()>0){
                System.out.println(result+" ....  "+result.get(0));
                deleteListOfTokensFromCache(result);
            }
            cursor = scan.getCursor();
        }while(!cursor.equals("0"));
        return ;
    }

  /*  public String findValueFromCacheById(String token) {
        Jedis jedis = jedisPool.getResource();
        jedis = jedisPool.getResource();
        return jedis.get(token);
    }

   */
}
