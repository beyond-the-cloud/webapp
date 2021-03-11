package edu.neu.csye7125.webapp.Dao;

import edu.neu.csye7125.webapp.Entity.Alert.Alert;

public interface AlertDao {

    void post(Alert alert);

    void delete(Alert alert);

    Alert findByKeywordAndCategory(String keyword, String category);

}
