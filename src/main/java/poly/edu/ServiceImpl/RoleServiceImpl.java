package poly.edu.ServiceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.edu.Service.RoleService;
import poly.edu.entity.Role;
import poly.edu.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Optional<Role> findByRolename(String rolename) {
        // TODO Auto-generated method stub
        return roleRepository.findByRoleName(rolename);
    }
    
}
