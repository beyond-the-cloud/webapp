package edu.neu.csye7125.webapp.Service;

import edu.neu.csye7125.webapp.Dao.AlertDao;
import edu.neu.csye7125.webapp.Entity.Alert.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlertServiceImpl implements AlertService {

    @Autowired
    private AlertDao alertDao;

    @Override
    @Transactional
    public void post(Alert alert) {
        alertDao.post(alert);
    }

    @Override
    @Transactional
    public void delete(Alert alert) {
        alertDao.delete(alert);
    }

    @Override
    @Transactional
    public Alert findByKeywordAndCategory(String keyword, String category) {
        return alertDao.findByKeywordAndCategory(keyword, category);
    }

}
