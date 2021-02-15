package edu.neu.csye7125.webapp.Controller;

import edu.neu.csye7125.webapp.Entity.User.User;
import edu.neu.csye7125.webapp.Exception.*;
import edu.neu.csye7125.webapp.Service.UserService;
import edu.neu.csye7125.webapp.Validation.EmailValidator;
import edu.neu.csye7125.webapp.Validation.PasswordValidator;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class ApplicationController {

    private final UserService userService;

    private EmailValidator emailValidator = new EmailValidator();

    private PasswordValidator passwordValidator = new PasswordValidator();

    private Logger logger = LogManager.getLogger(getClass());

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
        logger.info("Retrieving all users");

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
        String username = user.getEmailAddress();

        logger.info(username);

        // check whether the username is valid
        if (!emailValidator.isValid(username, null)) {
            logger.warn("Invalid email!");
            throw new EmailInvalidException("Invalid email!");
        }

        // check whether the password is strong
        String password = user.getPassword();
        if (password.length() < 8 || !passwordValidator.isValid(password)) {
            logger.warn("Password too weak");
            throw new WeakPasswordException("Password Too Weak!");
        }

        logger.info("Processing registration form for: " + username);

        User existing = userService.findByUsername(username);
        if (existing != null){
            logger.warn("Username already exist!");
            throw new UserAlreadyExist("Username already exist!");
        }

        user.setId(UUID.randomUUID().toString());
        user.setAccountCreated(dateFormat.format(new Date()));
        User savedUser = userService.save(user);

        logger.info("Creating user... ID: " + savedUser.getId());

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
        logger.info("Retrieving current user");

        User user = getCurrentUser();
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
        logger.info("Updating user...");

        if (user.getEmailAddress() != null
                || user.getAccountCreated() != null
                || user.getAccountUpdated() != null) {
            logger.warn("Updating user: Fields are not allowed to modify");
            throw new FieldRestrictedException("Fields not allowed to modify; " +
                    "Only can change FirstName, LastName and Password.");
        }

        String password = user.getPassword();
        if (password != null && (password.length() < 8 || !passwordValidator.isValid(password))) {
            logger.warn("Password too weak");
            throw new WeakPasswordException("Password Too Weak!");
        }

        User old = getCurrentUser();
        // pass those static attributes
        user.setId(old.getId());
        user.setEmailAddress(old.getEmailAddress());
        user.setAccountCreated(old.getAccountCreated());

        userService.save(user);
        logger.info("User updated. ID: " + old.getId());
    }

    /**
     * Helper function
     * Return current authenticated user
     * @return
     */
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);

        logger.info("Successfully obtained user: " + username);

        if (user == null) {
            throw new UserNotFoundException("User Not Found!");
        }
        return user;
    }

}
