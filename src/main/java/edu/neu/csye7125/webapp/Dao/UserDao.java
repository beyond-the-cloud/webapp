package edu.neu.csye7125.webapp.Dao;

import edu.neu.csye7125.webapp.Entity.User.User;

import java.util.List;

public interface UserDao {

    List<User> findAll();

    User findByUsername(String username);
    
    void save(User user);

}
