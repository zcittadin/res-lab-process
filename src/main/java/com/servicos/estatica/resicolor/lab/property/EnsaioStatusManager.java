package com.servicos.estatica.resicolor.lab.property;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class EnsaioStatusManager {

	private static Map<String, Boolean> provaStatus = new HashMap<>();

	public static void setProvaStatus(String provaName, Boolean status) {
		provaStatus.put(provaName, status);
	}

	public static Boolean getProvaStatus(String provaName) {
		return provaStatus.get(provaName);
	}

	public static Map<String, Boolean> getProvas() {
		return provaStatus;
	}

	public static Boolean verifyProvaRunning() {
		for (String procName : EnsaioStatusManager.getProvas().keySet()) {
			if (EnsaioStatusManager.getProvaStatus(procName)) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Atenção");
				alert.setHeaderText(
						"Existem provas que ainda não foram finalizadas. Finalize-as antes de encerrar o sistema.");
				alert.showAndWait();
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
}
