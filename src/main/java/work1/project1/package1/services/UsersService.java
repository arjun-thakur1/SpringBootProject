package work1.project1.package1.services;

import work1.project1.package1.entity.Users;
import work1.project1.package1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    UserRepository userRepository;


    public Optional<Users> get(long id){
      return  userRepository.findById(id);
    }
}
