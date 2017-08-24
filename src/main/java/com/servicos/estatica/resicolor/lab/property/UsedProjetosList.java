package com.servicos.estatica.resicolor.lab.property;

import java.util.ArrayList;
import java.util.List;

import com.servicos.estatica.resicolor.lab.model.Projeto;

public class UsedProjetosList {

	private static List<Projeto> usedProjetos = new ArrayList<>();

	public static void useProjeto(Projeto projeto) {
		usedProjetos.add(projeto);
	}

	public static void discardProjeto(Projeto projeto) {
		usedProjetos.remove(projeto);
	}

	public static Boolean isProjetoUsed(Projeto projeto) {
		for (Projeto p : usedProjetos) {
			if (p.equals(projeto))
				return true;
		}
		return false;

	}

}
