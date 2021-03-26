package edu.neu.csye7125.webapp.Controller;

import edu.neu.csye7125.webapp.Entity.User.User;
import edu.neu.csye7125.webapp.Exception.*;
import edu.neu.csye7125.webapp.Service.UserService;
import edu.neu.csye7125.webapp.Validation.EmailValidator;
import edu.neu.csye7125.webapp.Validation.PasswordValidator;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class ApplicationController {

    private final UserService userService;

    private EmailValidator emailValidator = new EmailValidator();

    private PasswordValidator passwordValidator = new PasswordValidator();

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ApplicationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * HelloWorld GET Controller
     * Used for verify server is running, can also be used for health check or readiness check
     * @return
     */
    @GetMapping("/v1/helloworld")
    public String helloWorld() {
        return "Hello World";
    }

    /**
     * GET /v1/users
     *
     * Return a list of users in json, which only contain certain fields
     * @return
     */
    @GetMapping("/v1/users")
    public MappingJacksonValue findAll() {
        log.info("Request Received. GET /v1/users");

        List<User> users = userService.findAll();
        SimpleBeanPropertyFilter filter =
                SimpleBeanPropertyFilter.filterOutAllExcept("id", "firstName", "lastName",
                        "emailAddress", "accountCreated", "accountUpdated");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserFilter", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filters);
        return mapping;
    }

    /**
     * POST /v1/user
     *
     * @param user
     * @return
     */
    @PostMapping("/v1/user")
    @ResponseStatus(HttpStatus.CREATED)
    public MappingJacksonValue createUser(@RequestBody User user) {
        log.info("Request Received. GET /v1/user");

        String username = user.getEmailAddress();

        // check whether the username is valid
        if (!emailValidator.isValid(username, null)) {
            String msg = "Invalid email!";
            log.error(msg);
            throw new EmailInvalidException(msg);
        }

        // check whether the password is strong
        String password = user.getPassword();
        if (password.length() < 8 || !passwordValidator.isValid(password)) {
            String msg = "Password too weak";
            log.error(msg);
            throw new WeakPasswordException(msg);
        }

        User existing = userService.findByUsername(username);
        if (existing != null) {
            String msg = "Username already exist";
            log.error(msg);
            throw new UserAlreadyExist(msg);
        }

        // create new user
        user.setId(UUID.randomUUID().toString());
        user.setAccountCreated(dateFormat.format(new Date()));
        User savedUser = userService.save(user);

        log.info("User Created. User Id: " + savedUser.getId());

        SimpleBeanPropertyFilter filter =
                SimpleBeanPropertyFilter.filterOutAllExcept("id", "firstName", "lastName",
                        "emailAddress", "accountCreated", "accountUpdated");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserFilter", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(savedUser);
        mapping.setFilters(filters);
        return mapping;
    }

    /**
     * GET /v1/user/self
     *
     * Return current authenticated user
     * @return
     */
    @GetMapping("/v1/user/self")
    public MappingJacksonValue findOne() {
        log.info("Request Received. GET /v1/user/self");

        User user = userService.getCurrentUser();

        log.info("User Retrieved. User Id: " + user.getId());

        SimpleBeanPropertyFilter filter =
                SimpleBeanPropertyFilter.filterOutAllExcept("id", "firstName", "lastName",
                        "emailAddress", "accountCreated", "accountUpdated");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserFilter", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);
        return mapping;
    }

    /**
     * PUT /v1/user/self
     *
     * Update current authenticated user
     * @param user
     */
    @PutMapping("/v1/user/self")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user) {
        log.info("Request Received. PUT /v1/user/self");

        if (user.getEmailAddress() != null
                || user.getAccountCreated() != null
                || user.getAccountUpdated() != null) {
            String msg = "Updating user: Fields are not allowed to modify";
            log.error(msg);
            throw new FieldRestrictedException(msg);
        }

        String password = user.getPassword();
        if (password != null && (password.length() < 8 || !passwordValidator.isValid(password))) {
            String msg = "Password too weak";
            log.warn(msg);
            throw new WeakPasswordException(msg);
        }

        User old = userService.getCurrentUser();
        // pass those static attributes
        user.setId(old.getId());
        user.setEmailAddress(old.getEmailAddress());
        user.setAccountCreated(old.getAccountCreated());

        userService.save(user);
        log.info("User updated. User Id: " + old.getId());
    }

}
