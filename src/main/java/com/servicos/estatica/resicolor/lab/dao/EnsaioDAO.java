package com.servicos.estatica.resicolor.lab.dao;

import org.hibernate.Session;

import com.servicos.estatica.resicolor.lab.model.Prova;
import com.servicos.estatica.resicolor.lab.util.HibernateUtil;

public class EnsaioDAO {

	public void saveEnsaio(Prova ensaio) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.save(ensaio);
		session.getTransaction().commit();
		session.clear();
		session.close();
	}
}
