package com.mssecurity.mssecurity.Controllers;

import com.mssecurity.mssecurity.Models.Role;
import com.mssecurity.mssecurity.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleRepository theRoleRepository;

    @GetMapping("")
    public List<Role> index(){
        return  this.theRoleRepository.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Role store(@RequestBody Role newRole){
        return this.theRoleRepository.save(newRole);
    }

    @GetMapping("{id}")
    public Role show(@PathVariable String id){
        return this.theRoleRepository.findById(id).orElse(null);
    }

    @PutMapping("{id}")
    public Role update(@PathVariable String id, @RequestBody Role newRole){
        Role theActualRole = this.theRoleRepository.findById(id).orElse(null);
        if (theActualRole != null){
            theActualRole.setName(newRole.getName());
            theActualRole.setDescription(newRole.getDescription());
            return this.theRoleRepository.save(theActualRole);
        }
        return null;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void destroy(@PathVariable String id){
        Role theRole = this.theRoleRepository.findById(id).orElse(null);
        if (theRole != null) {
            this.theRoleRepository.delete(theRole);
        }
    }
}
