package com.epam.spring.jdbc.service.impl;

import java.util.Collection;
import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.epam.spring.jdbc.dao.UserAccountDao;
import com.epam.spring.jdbc.domain.UserAccount;
import com.epam.spring.jdbc.service.UserAccountService;

/**
 * @author Aliaksandr_Liahushau
 */
@Service
public class UserAccountServiceImpl implements UserAccountService {

	@Inject
    UserAccountDao userAccountDao;
	
	@Override
	public UserAccount save(final UserAccount account) {
		final UserAccount newUserAccount = userAccountDao.save(account);
		return newUserAccount;
	}

	@Override
	public void remove(final UserAccount account) {
		//userAccountDao.remove(account);
	}
	
	@Override
	public void remove(final Long id) {
		//userAccountDao.remove(id);
	}

	@Override
	public UserAccount getById(final Long id) {
		return null;//userAccountDao.getById(id);
	}

	@Override
	public Collection<UserAccount> getAll() {
		return userAccountDao.getAll();
	}

	@Override
	public UserAccount getUserAccountByUserId(final Long id) {
		return userAccountDao.getById(id);
	}

}
