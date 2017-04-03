package com.epam.spring.jdbc.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

/**
 * @author Yuriy_Tkach
 */
public class User extends DomainObject {

    public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String firstName;

    private String lastName;

    private String email;
    
    private Set<String> roles;
    
    private String password;

    private NavigableSet<Ticket> tickets = new TreeSet<>();
    
    @JsonDeserialize(using=LocalDateDeserializer.class)
    private LocalDate birthday;
    
    private Set<String> systemMessages = new HashSet<>();
    
    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(final LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(final Set<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
    
    public NavigableSet<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(final NavigableSet<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Set<String> getSystemMessages() {
        return systemMessages;
    }

    public void setSystemMessages(final Set<String> systemMessages) {
        this.systemMessages = systemMessages;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }
        if (firstName == null) {
            if (other.firstName != null) {
                return false;
            }
        } else if (!firstName.equals(other.firstName)) {
            return false;
        }
        if (lastName == null) {
            if (other.lastName != null) {
                return false;
            }
        } else if (!lastName.equals(other.lastName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User[firstName:" + firstName + ", lastName:" + lastName + ", email:" + email + ", tickets:" + tickets
                + ", birthday:" + birthday + "]";
    }

}
