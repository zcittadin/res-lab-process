package com.servicos.estatica.resicolor.lab.property;

import javafx.beans.property.SimpleIntegerProperty;

public class AmostraProperty {

	private static SimpleIntegerProperty temp = new SimpleIntegerProperty();
	private static SimpleIntegerProperty setPoint = new SimpleIntegerProperty();

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
}
