package com.servicos.estatica.resicolor.lab.dao;

import java.util.List;

import org.hibernate.Session;

import com.servicos.estatica.resicolor.lab.model.Amostra;
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
}
