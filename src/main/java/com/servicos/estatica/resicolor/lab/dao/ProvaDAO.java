package com.servicos.estatica.resicolor.lab.dao;

import org.hibernate.Session;

import com.servicos.estatica.resicolor.lab.model.Prova;
import com.servicos.estatica.resicolor.lab.util.HibernateUtil;

public class ProvaDAO {

	public void saveProva(Prova prova) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.save(prova);
		session.getTransaction().commit();
		session.clear();
		session.close();
	}
}
