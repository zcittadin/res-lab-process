package com.servicos.estatica.resicolor.lab.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ensaio")
public class Ensaio implements Serializable {

	private static final long serialVersionUID = -2212557629858281679L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@OneToMany(mappedBy = "ensaio", targetEntity = Leitura.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Leitura> leituras;
	@Column(name = "lote")
	private String lote;
	@Column(name = "balao")
	private String balao;
	@Column(name = "t_max")
	private double tempMax;
	@Column(name = "t_min")
	private double tempMin;
	@Column(name = "dh_inicial")
	private Date dhInicial;
	@Column(name = "dh_final")
	private Date dhFinal;

	public Ensaio() {

	}

	public Ensaio(Long id, List<Leitura> leituras, String lote, String balao, double tempMax, double tempMin,
			Date dhInicial, Date dhFinal) {
		this.id = id;
		this.leituras = leituras;
		this.lote = lote;
		this.balao = balao;
		this.tempMax = tempMax;
		this.tempMin = tempMin;
		this.dhInicial = dhInicial;
		this.dhFinal = dhFinal;
	}

	public Date getDhInicial() {
		return dhInicial;
	}

	public void setDhInicial(Date dhInicial) {
		this.dhInicial = dhInicial;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Leitura> getLeituras() {
		return leituras;
	}

	public void setLeituras(List<Leitura> leituras) {
		this.leituras = leituras;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getBalao() {
		return balao;
	}

	public void setBalao(String balao) {
		this.balao = balao;
	}

	public double getTempMax() {
		return tempMax;
	}

	public void setTempMax(double tempMax) {
		this.tempMax = tempMax;
	}

	public double getTempMin() {
		return tempMin;
	}

	public void setTempMin(double tempMin) {
		this.tempMin = tempMin;
	}

	public Date getDhFinal() {
		return dhFinal;
	}

	public void setDhFinal(Date dhFinal) {
		this.dhFinal = dhFinal;
	}

	@Override
	public String toString() {
		return "Ensaio [id=" + id + ", leituras=" + leituras + ", lote=" + lote + ", balao=" + balao + ", tempMax="
				+ tempMax + ", tempMin=" + tempMin + ", dhInicial=" + dhInicial + ", dhFinal=" + dhFinal + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ensaio other = (Ensaio) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
