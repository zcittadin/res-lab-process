package com.servicos.estatica.resicolor.lab.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import com.servicos.estatica.resicolor.lab.model.Projeto;
import com.servicos.estatica.resicolor.lab.util.HibernateUtil;

public class ProjetoDAO {

	public void saveProjeto(Projeto projeto) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.save(projeto);
		session.getTransaction().commit();
		session.clear();
		session.close();
	}

	public void removeProjeto(Projeto projeto) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.remove(projeto);
		session.getTransaction().commit();
		session.close();

	}

	@SuppressWarnings("unchecked")
	public List<Projeto> findLastProjetos() {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		Query query = session.createQuery("SELECT p FROM Projeto p ORDER BY id DESC");
		query.setMaxResults(15);
		List<Projeto> list = new ArrayList<>();
		list = query.getResultList();
		session.close();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Projeto> findProjetoByNome(String nome) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		Query query = session.createQuery("SELECT p FROM Projeto p WHERE nome LIKE '%" + nome + "%' ORDER BY id DESC");
		List<Projeto> list = new ArrayList<>();
		list = query.getResultList();
		session.close();
		return list;
	}
}
