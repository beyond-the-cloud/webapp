package edu.neu.csye7125.webapp.Dao;

import edu.neu.csye7125.webapp.Entity.User.Authorities;
import edu.neu.csye7125.webapp.Entity.User.User;
import edu.neu.csye7125.webapp.Entity.User.Users;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<User> findAll() {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<User> theQuery =
				currentSession.createQuery("from User", User.class);
		List<User> users = theQuery.getResultList();
		return users;
	}

	@Override
	public User findByUsername(String theUsername) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<User> theQuery = currentSession.createQuery("from User where emailAddress=:uEmail", User.class);
		theQuery.setParameter("uEmail", theUsername);
		User theUser;
		try {
			theUser = theQuery.getSingleResult();
		} catch (Exception e) {
			theUser = null;
		}
		return theUser;
	}

	@Override
	public void save(User theUser) {
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
	}

}
