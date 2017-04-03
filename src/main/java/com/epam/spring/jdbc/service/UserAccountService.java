package com.epam.spring.jdbc.service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.epam.spring.jdbc.domain.UserAccount;

/**
 * @author Aliaksandr_Liahushau
 */
public interface UserAccountService extends AbstractDomainObjectService<UserAccount> {

	/**
     * Finding user account by user id
     * 
     * @param id
     *            Id of the user
     * @return found user account or <code>null</code>
     */
    public @Nullable UserAccount getUserAccountByUserId(@Nonnull Long id);
    
    /**
     * Remove user account by account id
     * 
     * @param id
     *            Id of the user account
     */
    public @Nullable void remove(@Nonnull Long id);

}
