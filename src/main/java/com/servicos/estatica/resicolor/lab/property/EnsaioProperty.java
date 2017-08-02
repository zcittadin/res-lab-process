package com.servicos.estatica.resicolor.lab.property;

import javafx.beans.property.SimpleBooleanProperty;

public class EnsaioProperty {

	private static SimpleBooleanProperty ensaio1 = new SimpleBooleanProperty();
	private static SimpleBooleanProperty ensaio2 = new SimpleBooleanProperty();
	private static SimpleBooleanProperty ensaio3 = new SimpleBooleanProperty();

	//========================================
	public static SimpleBooleanProperty ensaio1Property() {
		return ensaio1;
	}

	public static Boolean getEnsaio1() {
		return ensaio1.get();
	}

	public final void setEnsaio1(Boolean ensaio1) {
		ensaio1Property().set(ensaio1);
	}

	//========================================
	public static SimpleBooleanProperty ensaio2Property() {
		return ensaio2;
	}

	public static Boolean getEnsaio2() {
		return ensaio2.get();
	}

	public final void setEnsaio2(Boolean ensaio2) {
		ensaio2Property().set(ensaio2);
	}

	// ========================================
	public static SimpleBooleanProperty ensaio3Property() {
		return ensaio3;
	}

	public static Boolean getEnsaio3() {
		return ensaio3.get();
	}

	public final void setEnsaio3(Boolean ensaio3) {
		ensaio3Property().set(ensaio3);
	}

}
