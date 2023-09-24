package com.mssecurity.mssecurity.Controllers;

import com.mssecurity.mssecurity.Models.Permission;
import com.mssecurity.mssecurity.Repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/permission")
public class PermissionsController {

    @Autowired
    private PermissionRepository thePermissionRepository;

    @GetMapping("")
    public List<Permission> index(){
        return  this.thePermissionRepository.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Permission store(@RequestBody Permission newPermission){
        return this.thePermissionRepository.save(newPermission);
    }

    @GetMapping("{id}")
    public Permission show(@PathVariable String id){
        return this.thePermissionRepository.findById(id).orElse(null);
    }

    @PutMapping("{id}")
    public Permission update(@PathVariable String id, @RequestBody Permission newPermission){
        Permission theActualPermission = this.thePermissionRepository.findById(id).orElse(null);
        if (theActualPermission != null){
            theActualPermission.setUrl(newPermission.getUrl());
            theActualPermission.setMethod(newPermission.getMethod());
            theActualPermission.setMenuItem(newPermission.getMenuItem());
            return this.thePermissionRepository.save(theActualPermission);
        }
        return null;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void destroy(@PathVariable String id){
        Permission thePermission = this.thePermissionRepository.findById(id).orElse(null);
        if (thePermission != null) {
            this.thePermissionRepository.delete(thePermission);
        }
    }
}
