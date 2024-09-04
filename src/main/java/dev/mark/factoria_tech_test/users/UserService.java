package dev.mark.factoria_tech_test.users;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository repository;

    public List<User> getAll(){
        List<User> users = repository.findAll();

        return users;
    }
    
    public User delete(Long id)throws Exception{

        User userToDelete = repository.findById(id).orElseThrow(() -> new UserNotFoundException("user not found"));
        repository.deleteById(id);

        return userToDelete;
    }

}
