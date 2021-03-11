package edu.neu.csye7125.webapp.Controller;

import edu.neu.csye7125.webapp.Entity.Alert.Alert;
import edu.neu.csye7125.webapp.Entity.User.User;
import edu.neu.csye7125.webapp.Service.AlertService;
import edu.neu.csye7125.webapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
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
        String keyword = alert.getKeyword();
        String category = alert.getCategory();
        Timestamp expiry = alert.getExpiry();
        if (keyword == null || category == null || expiry == null) {
            return new ResponseEntity(
                    "{\"message\": \"No Keyword or Category or Expiry Specified. All Fields Are Required\"}",
                    HttpStatus.BAD_REQUEST);
        }
        System.out.println(keyword.toLowerCase());
        if (!Alert.CATEGORY.contains(category.toLowerCase())) {
            return new ResponseEntity("{\"message\": \"Category Not Allowed. Choose from [new, top, best]\"}", HttpStatus.BAD_REQUEST);
        }
        Alert theAlert = alertService.findByKeywordAndCategory(keyword, category);
        if (theAlert != null) {
            return new ResponseEntity("{\"message\": \"Alert Already Exist.\"}", HttpStatus.BAD_REQUEST);
        }
        User currentUser = userService.getCurrentUser();
        alert.setUser(currentUser);
        alertService.post(alert);
        return new ResponseEntity<>("{\"message\": \"Alert Created.\"}", HttpStatus.CREATED);
    }

    @DeleteMapping("/v1/alert")
    public ResponseEntity delete(@RequestBody Alert alert) {
        String keyword = alert.getKeyword();
        String category = alert.getCategory();
        if (keyword == null || category == null) {
            return new ResponseEntity("{\"message\": \"No Keyword or Category Specified.\"}", HttpStatus.BAD_REQUEST);
        }
        Alert theAlert = alertService.findByKeywordAndCategory(keyword, category);
        if (theAlert == null) {
            return new ResponseEntity("{\"message\": \"Alert Not Found.\"}", HttpStatus.NOT_FOUND);
        }
        alert.setUser(theAlert.getUser());
        alert.setId(theAlert.getId());
        alert.setCategory(theAlert.getCategory());
        alert.setExpiry(theAlert.getExpiry());
        alertService.delete(alert);
        return new ResponseEntity<>("{\"message\": \"Alert Deleted.\"}", HttpStatus.OK);
    }

}
