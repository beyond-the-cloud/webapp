package edu.neu.csye7125.webapp.Dao;

import edu.neu.csye7125.webapp.Entity.Alert.Alert;
import edu.neu.csye7125.webapp.Entity.User.User;
import edu.neu.csye7125.webapp.Service.UserService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class AlertDaoImpl implements AlertDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private UserService userService;

    @Autowired
    private MeterRegistry meterRegistry;

    @Override
    public void post(Alert alert) {
        Timer timer = meterRegistry.timer("alert.post");
        Long start = System.currentTimeMillis();

        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(alert);

        Long end = System.currentTimeMillis();
        timer.record(end - start, TimeUnit.MILLISECONDS);
    }

    @Override
    public void delete(Alert alert) {
        Timer timer = meterRegistry.timer("alert.delete");
        Long start = System.currentTimeMillis();

        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(alert);

        Long end = System.currentTimeMillis();
        timer.record(end - start, TimeUnit.MILLISECONDS);
    }

    @Override
    public Alert findByKeywordAndCategory(String keyword, String category) {
        Timer timer = meterRegistry.timer("alert.findByKeywordAndCategory");
        Long start = System.currentTimeMillis();

        User currentUser = userService.getCurrentUser();
        Session currentSession = sessionFactory.getCurrentSession();
        Query<Alert> theQuery = currentSession.createQuery(
                "from Alert where keyword=:uKeyword AND category=:uCategory AND userId=:uUserId", Alert.class);
        theQuery.setParameter("uKeyword", keyword);
        theQuery.setParameter("uCategory", category);
        theQuery.setParameter("uUserId", currentUser.getId());
        Alert alert;
        try {
            alert = theQuery.getSingleResult();
        } catch (Exception e) {
            alert = null;
        }

        Long end = System.currentTimeMillis();
        timer.record(end - start, TimeUnit.MILLISECONDS);
        return alert;
    }
}
