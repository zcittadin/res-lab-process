package com.servicos.estatica.resicolor.lab.dao;

import org.hibernate.Session;

import com.servicos.estatica.resicolor.lab.model.Leitura;
import com.servicos.estatica.resicolor.lab.util.HibernateUtil;

public class LeituraDAO {

	public void saveLeitura(Leitura leitura) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.save(leitura);
		session.getTransaction().commit();
		session.clear();
		session.close();
	}
}
