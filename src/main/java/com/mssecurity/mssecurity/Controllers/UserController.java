package com.mssecurity.mssecurity.Controllers;

import com.mssecurity.mssecurity.Models.User;
import com.mssecurity.mssecurity.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository theUserRepository;

    @GetMapping("")
    public List<User> index(){
        return  this.theUserRepository.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User store(@RequestBody User newUser){
        return this.theUserRepository.save(newUser);
    }

    @GetMapping("{id}")
    public User show(@PathVariable String id){
        return this.theUserRepository.findById(id).orElse(null);
    }

    @PutMapping("{id}")
    public User update(@PathVariable String id, @RequestBody User newUser){
        User theActualUser = this.theUserRepository.findById(id).orElse(null);
        if (theActualUser != null){
            theActualUser.setName(newUser.getName());
            theActualUser.setEmail(newUser.getEmail());
            theActualUser.setPassword(newUser.getPassword());
            return this.theUserRepository.save(theActualUser);
        }
        return null;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void destroy(@PathVariable String id){
        User theUser = this.theUserRepository.findById(id).orElse(null);
        if (theUser != null) {
            this.theUserRepository.delete(theUser);
        }
    }

}
