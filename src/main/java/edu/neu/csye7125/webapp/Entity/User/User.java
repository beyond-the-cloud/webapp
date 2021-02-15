package edu.neu.csye7125.webapp.Entity.User;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="user")
@JsonFilter("UserFilter")
public class User {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "password")
    private String password;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "emailAddress")
    private String emailAddress;

    @Column(name = "accountCreated")
    private String accountCreated;

    @Column(name = "accountUpdated")
    private String accountUpdated;

    public User() {
    }

}
