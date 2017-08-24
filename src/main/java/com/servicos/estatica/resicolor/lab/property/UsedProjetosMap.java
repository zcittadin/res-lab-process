package com.servicos.estatica.resicolor.lab.property;

import java.util.HashMap;
import java.util.Map;

import com.servicos.estatica.resicolor.lab.model.Projeto;
import com.servicos.estatica.resicolor.lab.util.Baloes;

public class UsedProjetosMap {

	private static Map<Baloes, Projeto> usedProjetos = new HashMap<>();

	public static void useProjeto(Baloes balao, Projeto projeto) {
		usedProjetos.put(balao, projeto);
	}

	public static void discardProjeto(Baloes balao, Projeto projeto) {
		usedProjetos.remove(balao, projeto);
	}

	public static Boolean isProjetoUsed(Projeto projeto) {
		for (Map.Entry<Baloes, Projeto> entry : usedProjetos.entrySet()) {
			if (projeto.equals(entry.getValue()))
				return true;
		}

		return false;

	}

}
