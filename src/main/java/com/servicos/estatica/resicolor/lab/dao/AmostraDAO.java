package com.servicos.estatica.resicolor.lab.dao;

import org.hibernate.Session;

import com.servicos.estatica.resicolor.lab.model.Amostra;
import com.servicos.estatica.resicolor.lab.util.HibernateUtil;

public class AmostraDAO {

	public void saveAmostra(Amostra amostra) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.save(amostra);
		session.getTransaction().commit();
		session.clear();
		session.close();
	}
}
