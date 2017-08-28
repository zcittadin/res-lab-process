package com.servicos.estatica.resicolor.lab.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.servicos.estatica.resicolor.lab.model.Amostra;
import com.servicos.estatica.resicolor.lab.model.Projeto;
import com.servicos.estatica.resicolor.lab.model.Prova;
import com.servicos.estatica.resicolor.lab.util.HibernateUtil;

public class ProvaDAO {

	private static AmostraDAO amostraDao = new AmostraDAO();

	public void saveProva(Prova prova) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.save(prova);
		session.getTransaction().commit();
		session.clear();
		session.close();
	}

	public void removeProva(Prova prova) {
		removeChildren(prova);
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.remove(prova);
		session.getTransaction().commit();
		session.close();

	}

	private void removeChildren(Prova prova) {
		List<Amostra> amostras = amostraDao.findByProva(prova);
		if (amostras != null && !amostras.isEmpty()) {
			for (Amostra amostra : amostras) {
				amostraDao.removeAmostra(amostra);
			}
		}
	}

	public void updateTemperaturaMin(Prova prova) {
		Session session = HibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("UPDATE Prova set tempMin = :tempMin WHERE id = :id");
		query.setParameter("tempMin", prova.getTempMin());
		query.setParameter("id", prova.getId());
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public void updateTemperaturaMax(Prova prova) {
		Session session = HibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("UPDATE Prova set tempMax = :tempMax WHERE id = :id");
		query.setParameter("tempMax", prova.getTempMax());
		query.setParameter("id", prova.getId());
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public List<Prova> findByProjeto(Projeto projeto) {
		return null;
	}
}
