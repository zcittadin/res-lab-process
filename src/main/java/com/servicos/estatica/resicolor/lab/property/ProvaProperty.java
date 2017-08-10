package com.servicos.estatica.resicolor.lab.property;

import com.servicos.estatica.resicolor.lab.model.Projeto;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ProvaProperty {

	private static SimpleBooleanProperty provaTemp1 = new SimpleBooleanProperty();
	private static SimpleBooleanProperty provaTemp2 = new SimpleBooleanProperty();
	private static SimpleBooleanProperty provaTemp3 = new SimpleBooleanProperty();
	private static SimpleBooleanProperty provaClear1 = new SimpleBooleanProperty();
	private static SimpleBooleanProperty provaClear2 = new SimpleBooleanProperty();
	private static SimpleBooleanProperty provaClear3 = new SimpleBooleanProperty();

	private static SimpleObjectProperty<Projeto> provaProjeto = new SimpleObjectProperty<>();

	// ========================================
	public static SimpleBooleanProperty provaTemp1Property() {
		return provaTemp1;
	}

	public static Boolean getEnsaioTemp1() {
		return provaTemp1.get();
	}

	public final void setEnsaioTemp1(Boolean ensaioTemp1) {
		provaTemp1Property().set(ensaioTemp1);
	}

	// ========================================
	public static SimpleBooleanProperty provaTemp2Property() {
		return provaTemp2;
	}

	public static Boolean getEnsaioTemp2() {
		return provaTemp2.get();
	}

	public final void setEnsaioTemp2(Boolean ensaioTemp2) {
		provaTemp2Property().set(ensaioTemp2);
	}

	// ========================================
	public static SimpleBooleanProperty provaTemp3Property() {
		return provaTemp3;
	}

	public static Boolean getEnsaioTemp3() {
		return provaTemp3.get();
	}

	public final void setEnsaioTemp3(Boolean ensaioTemp3) {
		provaTemp3Property().set(ensaioTemp3);
	}

	// ========================================
	public static SimpleBooleanProperty provaClear1Property() {
		return provaClear1;
	}

	public static Boolean getEnsaioClear1() {
		return provaClear1.get();
	}

	public final void setEnsaioClear1(Boolean ensaioClear1) {
		provaClear1Property().set(ensaioClear1);
	}

	// ========================================
	public static SimpleBooleanProperty provaClear2Property() {
		return provaClear2;
	}

	public static Boolean getEnsaioClear2() {
		return provaClear2.get();
	}

	public final void setEnsaioClear2(Boolean ensaioClear2) {
		provaClear2Property().set(ensaioClear2);
	}

	// ========================================
	public static SimpleBooleanProperty provaClear3Property() {
		return provaClear3;
	}

	public static Boolean getEnsaioClear3() {
		return provaClear3.get();
	}

	public final void setEnsaioClear3(Boolean ensaioClear3) {
		provaClear3Property().set(ensaioClear3);
	}

	// ========================================
	public static SimpleObjectProperty<Projeto> provaProjetoProperty() {
		return provaProjeto;
	}

	public static Projeto getProvaProjeto() {
		return provaProjeto.get();
	}

	public static void setProvaProjeto1(Projeto projeto) {
		provaProjetoProperty().set(projeto);
	}

}
