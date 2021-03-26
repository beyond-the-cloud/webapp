package edu.neu.csye7125.webapp.Dao;

import edu.neu.csye7125.webapp.Entity.User.Authorities;
import edu.neu.csye7125.webapp.Entity.User.User;
import edu.neu.csye7125.webapp.Entity.User.Users;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private MeterRegistry meterRegistry;

	@Override
	public List<User> findAll() {
		Timer timer = meterRegistry.timer("user.findAll");
		Long start = System.currentTimeMillis();

		Session currentSession = sessionFactory.getCurrentSession();
		Query<User> theQuery =
				currentSession.createQuery("from User", User.class);
		List<User> users = theQuery.getResultList();

		Long end = System.currentTimeMillis();
		timer.record(end - start, TimeUnit.MILLISECONDS);
		return users;
	}

	@Override
	public User findByUsername(String theUsername) {
		Timer timer = meterRegistry.timer("user.findByUsername");
		Long start = System.currentTimeMillis();

		Session currentSession = sessionFactory.getCurrentSession();
		Query<User> theQuery = currentSession.createQuery("from User where emailAddress=:uEmail", User.class);
		theQuery.setParameter("uEmail", theUsername);
		User theUser;
		try {
			theUser = theQuery.getSingleResult();
		} catch (Exception e) {
			theUser = null;
		}

		Long end = System.currentTimeMillis();
		timer.record(end - start, TimeUnit.MILLISECONDS);
		return theUser;
	}

	@Override
	public void save(User theUser) {
		Timer timer = meterRegistry.timer("user.save");
		Long start = System.currentTimeMillis();

		Session currentSession = sessionFactory.getCurrentSession();
		// create the user
		currentSession.saveOrUpdate(theUser);

		// save user to the table that usd to auth
		Users users = new Users();
		users.setUsername(theUser.getEmailAddress());
		users.setPassword(theUser.getPassword());
		users.setEnabled(1);
		currentSession.saveOrUpdate(users);

		// save the authority
		Authorities authorities = new Authorities();
		authorities.setUsername(theUser.getEmailAddress());
		authorities.setAuthority("ROLE_USER");
		currentSession.saveOrUpdate(authorities);

		Long end = System.currentTimeMillis();
		timer.record(end - start, TimeUnit.MILLISECONDS);
	}

}
