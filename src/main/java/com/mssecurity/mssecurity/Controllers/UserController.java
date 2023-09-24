package com.mssecurity.mssecurity.Controllers;

import com.mssecurity.mssecurity.Models.Role;
import com.mssecurity.mssecurity.Models.User;
import com.mssecurity.mssecurity.Repositories.RoleRepository;
import com.mssecurity.mssecurity.Repositories.UserRepository;
import com.mssecurity.mssecurity.Services.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository theUserRepository;

    @Autowired
    private RoleRepository theRoleRepository;

    @Autowired
    private EncryptionService encryptionService;

    @GetMapping("")
    public List<User> index(){
        return  this.theUserRepository.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User store(@RequestBody User newUser){
        newUser.setPassword(encryptionService.convertirSHA256(newUser.getPassword()));
        return this.theUserRepository.save(newUser);
    }

    @GetMapping("{id}")
    public User show(@PathVariable String id){
        return this.theUserRepository.findById(id).orElse(null);
    }

    @PutMapping("{id}")
    public User update(@PathVariable String id, @RequestBody User theNewUser){
        User theActualUser = this.theUserRepository.findById(id).orElse(null);
        if (theActualUser != null){
            theActualUser.setName(theNewUser.getName());
            theActualUser.setEmail(theNewUser.getEmail());
            theActualUser
                    .setPassword(encryptionService.convertirSHA256(theNewUser.getPassword()));
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

    /**
     * asocia el role con el usuario
     * @param user_id identificador del usuario
     * @param role_id identificador del role
     * @return guarda los cambios del usuario en la base de datos
     */
    @PutMapping("{user_id}/role/{role_id}")
    public User matchUserRole(@PathVariable String user_id, @PathVariable String role_id){
        User theActualUser = this.theUserRepository.findById(user_id).orElse(null);
        Role theActualRole = this.theRoleRepository.findById(role_id).orElse(null);
        if (theActualUser != null && theActualRole != null){
            theActualUser.setRole(theActualRole);
            return this.theUserRepository.save(theActualUser);
        } else {
            return null;
        }
    }

    /**
     * desasocia el role con el usuario
     * @param user_id identificador del usuario
     * @return guarda los cambios en la base de datos
     */
    @DeleteMapping("{user_id}/role")
    public User unMatchUserRole(@PathVariable String user_id){
        User theActualUser = this.theUserRepository.findById(user_id).orElse(null);
        if (theActualUser != null){
            theActualUser.setRole(null);
            return this.theUserRepository.save(theActualUser);
        } else {
            return null;
        }
    }
}
