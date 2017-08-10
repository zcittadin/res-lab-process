package com.servicos.estatica.resicolor.lab.property;

import javafx.beans.property.SimpleBooleanProperty;

public class ProvaProperty {

	private static SimpleBooleanProperty ensaioTemp1 = new SimpleBooleanProperty();
	private static SimpleBooleanProperty ensaioTemp2 = new SimpleBooleanProperty();
	private static SimpleBooleanProperty ensaioTemp3 = new SimpleBooleanProperty();
	private static SimpleBooleanProperty ensaioClear1 = new SimpleBooleanProperty();
	private static SimpleBooleanProperty ensaioClear2 = new SimpleBooleanProperty();
	private static SimpleBooleanProperty ensaioClear3 = new SimpleBooleanProperty();

	// ========================================
	public static SimpleBooleanProperty ensaioTemp1Property() {
		return ensaioTemp1;
	}

	public static Boolean getEnsaioTemp1() {
		return ensaioTemp1.get();
	}

	public final void setEnsaioTemp1(Boolean ensaioTemp1) {
		ensaioTemp1Property().set(ensaioTemp1);
	}

	// ========================================
	public static SimpleBooleanProperty ensaioTemp2Property() {
		return ensaioTemp2;
	}

	public static Boolean getEnsaioTemp2() {
		return ensaioTemp2.get();
	}

	public final void setEnsaioTemp2(Boolean ensaioTemp2) {
		ensaioTemp2Property().set(ensaioTemp2);
	}

	// ========================================
	public static SimpleBooleanProperty ensaioTemp3Property() {
		return ensaioTemp3;
	}

	public static Boolean getEnsaioTemp3() {
		return ensaioTemp3.get();
	}

	public final void setEnsaioTemp3(Boolean ensaioTemp3) {
		ensaioTemp3Property().set(ensaioTemp3);
	}

	// ========================================
	public static SimpleBooleanProperty provaClear1Property() {
		return ensaioClear1;
	}

	public static Boolean getEnsaioClear1() {
		return ensaioClear1.get();
	}

	public final void setEnsaioClear1(Boolean ensaioClear1) {
		provaClear1Property().set(ensaioClear1);
	}

	// ========================================
	public static SimpleBooleanProperty provaClear2Property() {
		return ensaioClear2;
	}

	public static Boolean getEnsaioClear2() {
		return ensaioClear2.get();
	}

	public final void setEnsaioClear2(Boolean ensaioClear2) {
		provaClear2Property().set(ensaioClear2);
	}

	// ========================================
	public static SimpleBooleanProperty provaClear3Property() {
		return ensaioClear3;
	}

	public static Boolean getEnsaioClear3() {
		return ensaioClear3.get();
	}

	public final void setEnsaioClear3(Boolean ensaioClear3) {
		provaClear3Property().set(ensaioClear3);
	}

}
