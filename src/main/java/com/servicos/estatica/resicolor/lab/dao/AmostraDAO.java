package com.servicos.estatica.resicolor.lab.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import com.servicos.estatica.resicolor.lab.model.Amostra;
import com.servicos.estatica.resicolor.lab.model.Prova;
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

	@SuppressWarnings("unchecked")
	public List<Amostra> findByProva(Prova prova) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		Query query = session.createQuery("SELECT a FROM Amostra a WHERE provaAmostras = :idProva");
		query.setParameter("idProva", prova);
		List<Amostra> list = new ArrayList<>();
		list = query.getResultList();
		session.close();
		return list;
	}
}
