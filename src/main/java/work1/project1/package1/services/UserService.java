package work1.project1.package1.services;

import work1.project1.package1.dto.response.Response;
import work1.project1.package1.entity.UserEntity;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.other.TokenGenerator;
import work1.project1.package1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static work1.project1.package1.constants.ApplicationConstants.NOT_PRESENT;
import static work1.project1.package1.constants.ApplicationConstants.SUCCESS;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    public UserEntity get(long id){

        return  userRepository.findByUserId(id);
    }

    public void userAdd(Long userId,String password){
        UserEntity userEntity=new UserEntity(userId,password);
        userRepository.save(userEntity);
        return ;
    }

    public Response changePassword(Long userId, String newPassword) throws CustomException {
        UserEntity userEntity=userRepository.findByUserId(userId);
        if(userEntity==null)
            throw new CustomException(NOT_PRESENT);
        userEntity.set_password(newPassword);
        userRepository.save(userEntity);
        return new Response(200,SUCCESS);
    }

}
