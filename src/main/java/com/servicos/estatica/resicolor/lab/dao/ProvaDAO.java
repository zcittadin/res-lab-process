package com.servicos.estatica.resicolor.lab.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

	public void updateDataInicial(Prova prova) {
		Session session = HibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("UPDATE Prova set dhInicial = :dtIni WHERE id = :id");
		query.setParameter("dtIni", Calendar.getInstance().getTime());
		query.setParameter("id", prova.getId());
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public void updateDataFinal(Prova prova) {
		Session session = HibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("UPDATE Prova set dhFinal = :dtFim WHERE id = :id");
		query.setParameter("dtFim", Calendar.getInstance().getTime());
		query.setParameter("id", prova.getId());
		query.executeUpdate();
		tx.commit();
		session.close();
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

	@SuppressWarnings("unchecked")
	public List<Prova> findEmAndamento() {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		String hql = "SELECT p FROM Prova p WHERE p.dhFinal IS NULL AND p.dhInicial IS NOT NULL";
		Query query = session.createQuery(hql);
		List<Prova> list = new ArrayList<>();
		list = query.getResultList();
		session.close();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Prova> findRecentes(int maxResults) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		String hql = "SELECT p FROM Prova p WHERE p.dhFinal IS NOT NULL ORDER BY p.id DESC";
		Query query = session.createQuery(hql);
		query.setMaxResults(maxResults);
		List<Prova> list = new ArrayList<>();
		list = query.getResultList();
		session.close();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Prova> findLastFinalizados(int maxResults) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		String hql = "SELECT p FROM Prova p ORDER BY p.id DESC";
		Query query = session.createQuery(hql);
		query.setMaxResults(maxResults);
		List<Prova> list = new ArrayList<>();
		list = query.getResultList();
		session.close();
		return list;
	}

	@SuppressWarnings({ "unchecked" })
	public List<Prova> findByNome(String provaName) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		String hql = "SELECT p FROM Prova p WHERE p.nomeProva LIKE '%" + provaName + "%' ORDER BY p.id DESC";
		Query query = session.createQuery(hql);
		List<Prova> provas = query.getResultList();
		session.close();
		if (provas != null && !provas.isEmpty()) {
			return provas;
		}
		return Collections.EMPTY_LIST;
	}

	@SuppressWarnings({ "unchecked" })
	public List<Prova> findByProjeto(String projetoName) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		String hqlProjeto = "SELECT p FROM Projeto p WHERE p.nome LIKE '%" + projetoName + "%' ORDER BY p.id DESC";
		Query queryProjeto = session.createQuery(hqlProjeto);
		List<Projeto> projetos = queryProjeto.getResultList();
		session.close();
		if (projetos != null && !projetos.isEmpty()) {
			return groupProvas(projetos);
		}
		return Collections.EMPTY_LIST;
	}

	private List<Prova> groupProvas(List<Projeto> projetos) {
		List<Prova> provas = new ArrayList<>();
		projetos.forEach(projeto -> {
			List<Prova> provasParciais = projeto.getProvas();
			provasParciais.forEach(provaParcial -> {
				provas.add(provaParcial);
			});
		});
		return provas;

	}

	@SuppressWarnings("unchecked")
	public List<Prova> findByPeriodo(String inicio, String fim) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		Query query = session.createQuery("SELECT p FROM Prova p WHERE DATE(dhInicial) BETWEEN '" + inicio + "' AND '"
				+ fim + "' ORDER BY id DESC");
		List<Prova> list = query.getResultList();
		session.close();
		return list;
	}

	public List<Prova> findByProjeto(Projeto projeto) {
		return null;
	}
}
