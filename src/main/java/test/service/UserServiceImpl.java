package test.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.dao.RoleDAOImp;
import test.dao.UserDAOImpl;
import test.model.Role;
import test.model.User;


import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {


    private final UserDAOImpl userDAO;

    private final RoleDAOImp roleDAO;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserServiceImpl(UserDAOImpl userDAO, RoleDAOImp roleDAO, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public List<User> allUsers() {
        return userDAO.findAll();
    }

    @Override
    public void delete(User user) {
        userDAO.delete(user);
    }

    @Override
    public User getById(int id) {
        return userDAO.getById(id);
    }


    @Override
    public void addFromRegistration(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleDAO.getOne(1L));
        user.setRoles(roles);
        userDAO.save(user);
    }

    private void addOrEdit(User user, String role) {
        if (role.equalsIgnoreCase("admin")) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleDAO.getOne(2L));
            user.setRoles(roles);
        }
        if (role.equalsIgnoreCase("user")) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleDAO.getOne(1L));
            user.setRoles(roles);
        }
        if (role.equalsIgnoreCase("admin,user")) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleDAO.getOne(1L));
            roles.add(roleDAO.getOne(2L));
            user.setRoles(roles);
        }
    }


    @Override
    public void add(User user, String role) {
        addOrEdit(user, role);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDAO.save(user);
    }

    @Override
    public void edit(User user, String role, String password) {
        addOrEdit(user, role);
        if (password == null || password.isEmpty()) {
            user.setPassword(userDAO.getById(user.getId()).getPassword());
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userDAO.saveAndFlush(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByName(username);
        if (user != null) {
            return user;
        } else throw new UsernameNotFoundException("User not found");
    }

}