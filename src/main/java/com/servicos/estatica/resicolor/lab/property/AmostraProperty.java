package com.servicos.estatica.resicolor.lab.property;

import com.servicos.estatica.resicolor.lab.model.Prova;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class AmostraProperty {

	private static SimpleIntegerProperty temp = new SimpleIntegerProperty();
	private static SimpleIntegerProperty setPoint = new SimpleIntegerProperty();

	private static SimpleObjectProperty<Prova> prova = new SimpleObjectProperty();

	// ========================================
	public static SimpleIntegerProperty tempProperty() {
		return temp;
	}

	public static Integer getTemp() {
		return temp.get();
	}

	public final void setTemp(Integer temp) {
		tempProperty().set(temp);
	}

	// ========================================
	public static SimpleIntegerProperty setPointProperty() {
		return setPoint;
	}

	public static Integer getSetPoint() {
		return setPoint.get();
	}

	public final void setSetPoint(Integer setPoint) {
		setPointProperty().set(setPoint);
	}

	// ========================================
	public static SimpleObjectProperty<Prova> provaProperty() {
		return prova;
	}

	public static Prova getProva() {
		return prova.get();
	}

	public final void setProva(Prova prova) {
		provaProperty().set(prova);
	}
}
