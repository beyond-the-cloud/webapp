package edu.neu.csye7125.webapp.Controller;

import edu.neu.csye7125.webapp.Entity.Alert.Alert;
import edu.neu.csye7125.webapp.Entity.User.User;
import edu.neu.csye7125.webapp.Service.AlertService;
import edu.neu.csye7125.webapp.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@Slf4j
public class AlertController {

    @Autowired
    private final AlertService alertService;

    @Autowired
    private final UserService userService;

    public AlertController(AlertService alertService, UserService userService) {
        this.alertService = alertService;
        this.userService = userService;
    }

    @PostMapping("/v1/alert")
    public ResponseEntity post(@RequestBody Alert alert) {
        log.info("Request Received. GET /v1/alert");

        String keyword = alert.getKeyword();
        String category = alert.getCategory();
        Timestamp expiry = alert.getExpiry();
        if (keyword == null || category == null || expiry == null) {
            log.error("No Keyword or Category or Expiry Specified");
            return new ResponseEntity(
                    "{\"message\": \"No Keyword or Category or Expiry Specified. All Fields Are Required\"}",
                    HttpStatus.BAD_REQUEST);
        }
        System.out.println(keyword.toLowerCase());
        if (!Alert.CATEGORY.contains(category.toLowerCase())) {
            log.error("Category Not Allowed");
            return new ResponseEntity("{\"message\": \"Category Not Allowed. Choose from [new, top, best]\"}",
                    HttpStatus.BAD_REQUEST);
        }
        Alert theAlert = alertService.findByKeywordAndCategory(keyword, category);
        if (theAlert != null) {
            log.error("Alert Already Exist");
            return new ResponseEntity("{\"message\": \"Alert Already Exist.\"}", HttpStatus.BAD_REQUEST);
        }
        User currentUser = userService.getCurrentUser();
        alert.setUser(currentUser);
        alertService.post(alert);
        log.info("Alert Created. Alert Id: " + alert.getId());
        return new ResponseEntity<>("{\"message\": \"Alert Created.\"}", HttpStatus.CREATED);
    }

    @DeleteMapping("/v1/alert")
    public ResponseEntity delete(@RequestBody Alert alert) {
        log.info("Request Received. DELETE /v1/alert");

        String keyword = alert.getKeyword();
        String category = alert.getCategory();
        if (keyword == null || category == null) {
            log.error("No Keyword or Category Specified");
            return new ResponseEntity("{\"message\": \"No Keyword or Category Specified.\"}", HttpStatus.BAD_REQUEST);
        }
        Alert theAlert = alertService.findByKeywordAndCategory(keyword, category);
        if (theAlert == null) {
            log.error("Alert Not Found");
            return new ResponseEntity("{\"message\": \"Alert Not Found.\"}", HttpStatus.NOT_FOUND);
        }
        alert.setUser(theAlert.getUser());
        alert.setId(theAlert.getId());
        alert.setCategory(theAlert.getCategory());
        alert.setExpiry(theAlert.getExpiry());
        alertService.delete(alert);
        log.info("Alert Deleted. Alert Id: " + alert.getId());
        return new ResponseEntity<>("{\"message\": \"Alert Deleted.\"}", HttpStatus.OK);
    }

}
