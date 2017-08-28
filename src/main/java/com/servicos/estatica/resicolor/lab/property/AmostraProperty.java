package com.servicos.estatica.resicolor.lab.property;

import com.servicos.estatica.resicolor.lab.model.Prova;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class AmostraProperty {

	private static SimpleDoubleProperty temp = new SimpleDoubleProperty();
	private static SimpleDoubleProperty setPoint = new SimpleDoubleProperty();

	private static SimpleObjectProperty<Prova> prova = new SimpleObjectProperty<Prova>();

	// ========================================
	public static SimpleDoubleProperty tempProperty() {
		return temp;
	}

	public static Double getTemp() {
		return temp.get();
	}

	public final void setTemp(Double temp) {
		tempProperty().set(temp);
	}

	// ========================================
	public static SimpleDoubleProperty setPointProperty() {
		return setPoint;
	}

	public static Double getSetPoint() {
		return setPoint.get();
	}

	public final void setSetPoint(Double setPoint) {
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
