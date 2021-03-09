package edu.neu.csye7125.webapp.Service;

import edu.neu.csye7125.webapp.Entity.User.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> findAll();

    User findByUsername(String username);

    User save(User user);

    User getCurrentUser();

    @Override
    UserDetails loadUserByUsername(String username);
}
