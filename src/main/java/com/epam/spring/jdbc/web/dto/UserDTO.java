package com.epam.spring.jdbc.web.dto;

import com.epam.spring.jdbc.domain.User;
import com.epam.spring.jdbc.domain.UserAccount;

public class UserDTO {
	
	private User user;
	
	private UserAccount userAccount;
	
	public UserDTO(final User user, final UserAccount userAccount) {
		super();
		this.user = user;
		this.userAccount = userAccount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}
	
}
