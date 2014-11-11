package br.ufal.ic.ia;

import java.util.ArrayList;

public class MiniMax {
	private Context context;
	private Jogador jogador;
	private int v;
	
	private int[] origem;
	private int[] destino;
			
	public MiniMax() {
		origem = new int[2];
		destino = new int [2];
	}
	
	public void minimaxdecision(Context context, Jogador jogador) { //nao deve ser void
		this.context = context;
		this.jogador = jogador;
		v = maxvalue();
		//return the action in SUCCESSORS(state) with value v
	}
	
	private int maxvalue() {
		ArrayList<Item> pecasquepodemsemover;
		
		if (context.jogoAcabou()) {
			System.out.println("Jogo acabou, max!");
			return utility();
		}
		v = Integer.MIN_VALUE;
		
		pecasquepodemsemover = context.retornarPecasQuePodemSeMover(jogador);
		for (Item item:pecasquepodemsemover) {
			v = Math.max(v, minvalue());
		}
		return v;
	}
	
	private int minvalue() {
		ArrayList<Item> pecasquepodemsemover;

		if (context.jogoAcabou()) {
			System.out.println("Jogo acabou, min!");
			return utility();
		}
		
		v = Integer.MAX_VALUE;
		
		pecasquepodemsemover = context.retornarPecasQuePodemSeMover(jogador);
		for (Item item:pecasquepodemsemover) {
			v = Math.min(v, maxvalue());
		}
		return v;
		
		/*
		 * if TERMINAL_TEST(state) then return UTILITY(state)
		 * v<- infinito
		 * for a, s in SUCCESSORS(state) do 
		 	* v<- MIN(v, maxvalue(s)) 
		 	* return v
		 */
	}
	
	/**
	 * @author yvesbastos
	 * @return
	 */
	private int utility() {
		return context.quantidadePecasRestantesDesteJogador(jogador);
	}
	
	
}
