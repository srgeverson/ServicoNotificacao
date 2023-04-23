package br.com.serviconotificacao.util;

public class ConsoleUtil {

	public static void mensagem(String origem, String texto) {
		String inicio = "<";
		String fim = ">";
		String tracos = "";
		
		for(int i = 0; i < (texto.length() - inicio.length() - fim.length() - origem.length() - "-inicio ".length()); i++)
			tracos += "-";
		System.out.println(String.format("%s-Início %s%s%s", inicio, origem, tracos, fim));
		
		System.out.println(String.format("%s", texto));
		
		tracos = "";
		for(int i = 0; i < (texto.length() - inicio.length() - fim.length() - origem.length() - "-fim ".length()); i++)
			tracos += "-";
		System.out.println(String.format("%s-Fim %s%s%s", inicio, origem, tracos, fim));
	}
}
