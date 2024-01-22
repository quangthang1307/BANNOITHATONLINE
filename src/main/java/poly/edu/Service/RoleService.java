package poly.edu.Service;

import java.util.Optional;

import poly.edu.entity.Role;

public interface RoleService {
    Optional<Role> findByRolename(String rolename);
}
