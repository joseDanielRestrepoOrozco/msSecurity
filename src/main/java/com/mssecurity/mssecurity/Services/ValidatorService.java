package com.mssecurity.mssecurity.Services;

import com.mssecurity.mssecurity.Models.Permission;
import com.mssecurity.mssecurity.Models.Role;
import com.mssecurity.mssecurity.Models.RolePermission;
import com.mssecurity.mssecurity.Models.User;
import com.mssecurity.mssecurity.Repositories.PermissionRepository;
import com.mssecurity.mssecurity.Repositories.RolePermissionRepository;
import com.mssecurity.mssecurity.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidatorService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PermissionRepository thePermissionRepository;

    @Autowired
    private UserRepository theUserRepository;

    @Autowired
    private RolePermissionRepository theRolePermissionRepository;

    private static final String BEARER_PREFIX = "Bearer ";

    /** Valida si un usuario tiene permiso para acceder a una URL y método específicos en función de su rol y permisos asignados
     * @param request La solicitud HTTP
     * @param url La URL que se desea validar
     * @param method El método HTTP que se desea validar
     * @return Devuelve el valor de success, que indica si la validación de roles y permisos fue exitosa o no lo fue.
     */
    public boolean validationRolePermission(HttpServletRequest request, String url, String method) {
        boolean success = false;
                User theUser = this.getUser(request);
                if (theUser != null) {
            Role theRole = theUser.getRole();
            System.out.println("holaaaaaaaaaaaaaaaaaaaaaaaaaa");
            System.out.println("Antes URL " + url + " metodo " + method);
            url = url.replaceAll("[0-9a-fA-F]{24}", "?");
            Permission thePermission = this.thePermissionRepository.getPermission(url, method);
            if (theRole != null && thePermission != null) {
                System.out.println("Rol " + theRole.getName() + " Permission " + thePermission.getUrl());
                RolePermission theRolePermission = this.theRolePermissionRepository.getRolePermission(theRole.get_id(),
                        thePermission.get_id());
                System.out.println(theRolePermission.getPermission().getMethod() + theRolePermission.getRole().getName());
                System.out.println(theRolePermission != null);
                if (theRolePermission != null) {
                    success = true;
                }
            } else {
                success = false;
            }
        }
        System.out.println(success);
        return success;
    }

    /** Extrae información del usuario a partir de un token JWT (JSON Web Token) 
     * que se encuentra en la cabecera de autorización de una solicitud HTTP
     * @param request La solicitud HTTP
     * @return Devuelve el objeto theUser, que contiene la información del usuario obtenida a partir del token JWT.
     * Si no se encontró un token válido en la cabecera de autorización o si hubo 
     * algún problema en la extracción del usuario a partir del token, el método devolverá null.
     */
    public User getUser(final HttpServletRequest request) {
        User theUser = null;
        String authorizationHeader = request.getHeader("Authorization");
        
        System.out.println("Header " + authorizationHeader);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
            System.out.println("Bearer Token: " + token);
            User theUserFromToken = jwtService.getUserFromToken(token);
            
            if (theUserFromToken != null) {
                theUser = this.theUserRepository.findById(theUserFromToken.get_id())
                        .orElse(null);
                theUser.setPassword("");
            }
        }
        return theUser;
    }

}
