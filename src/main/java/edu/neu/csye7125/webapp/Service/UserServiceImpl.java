package edu.neu.csye7125.webapp.Service;

import edu.neu.csye7125.webapp.Dao.UserDao;
import edu.neu.csye7125.webapp.Entity.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public List<User> findAll() {
		return userDao.findAll();
	}

	@Override
	@Transactional
	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	@Override
	@Transactional
	public User save(User user) {
		User savedUser = new User();

		savedUser.setId(user.getId());
		savedUser.setPassword(passwordEncoder.encode(user.getPassword()));
		savedUser.setFirstName(user.getFirstName());
		savedUser.setLastName(user.getLastName());
		savedUser.setEmailAddress(user.getEmailAddress());

		DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		if (user.getAccountCreated() == null) {
			savedUser.setAccountCreated(dateFormat.format(new Date()));
		} else {
			savedUser.setAccountCreated(user.getAccountCreated());
		}
		savedUser.setAccountUpdated(dateFormat.format(new Date()));

		// save user in the database
		userDao.save(savedUser);
		return savedUser;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userDao.findByUsername(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(
				user.getEmailAddress(),
				user.getPassword(),
				AuthorityUtils.createAuthorityList("USER")
		);
	}

}
