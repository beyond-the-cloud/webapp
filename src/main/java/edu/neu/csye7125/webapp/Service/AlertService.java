package edu.neu.csye7125.webapp.Service;

import edu.neu.csye7125.webapp.Entity.Alert.Alert;

public interface AlertService {

    void post(Alert alert);

    void delete(Alert alert);

    Alert findByKeywordAndCategory(String keyword, String category);

}
