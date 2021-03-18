package edu.neu.csye7125.webapp.Dao;

import edu.neu.csye7125.webapp.Entity.Alert.Alert;
import edu.neu.csye7125.webapp.Entity.User.User;
import edu.neu.csye7125.webapp.Service.UserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AlertDaoImpl implements AlertDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private UserService userService;

    @Override
    public void post(Alert alert) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(alert);
    }

    @Override
    public void delete(Alert alert) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(alert);
    }

    @Override
    public Alert findByKeywordAndCategory(String keyword, String category) {
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
        return alert;
    }
}
