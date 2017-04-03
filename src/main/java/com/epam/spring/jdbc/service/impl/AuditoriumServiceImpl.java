package com.epam.spring.jdbc.service.impl;

import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.epam.spring.jdbc.dao.AuditoriumDao;
import com.epam.spring.jdbc.domain.Auditorium;
import com.epam.spring.jdbc.service.AuditoriumService;

/**
 * @author Aliaksandr_Liahushau
 */
@Service
public class AuditoriumServiceImpl implements AuditoriumService {

	@Inject
	private AuditoriumDao auditoriumDao;
	
	@Override
	public Set<Auditorium> getAll() {
		return auditoriumDao.getAll();
	}

	@Override
	public Auditorium getByName(String name) {
		return auditoriumDao.getByName(name);
	}

}
