package com.mssecurity.mssecurity.Repositories;

import com.mssecurity.mssecurity.Models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PermissionRepository extends MongoRepository<Role, String> {
}
