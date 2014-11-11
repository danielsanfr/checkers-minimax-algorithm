package br.ufal.ic.ia;

import java.util.ArrayList;

public class MiniMax {
	private Jogador jogador;
	private int v;
	
	private int[] origemescolhida;
	private int[] destinoescolhido;
		
	
	public MiniMax() {
		origemescolhida = new int[2];
		destinoescolhido = new int [2];
	}
	
	public int[] minimaxdecision(Context context, Jogador jogador) { //nao deve ser void
		int[] origemdestino = new int[4];
		
		this.jogador = jogador;
		v = maxvalue(context);
		//return the action in SUCCESSORS(state) with value v
		
		return origemdestino;
	}
	
	private int maxvalue(Context contexto) {
		ArrayList<Item> pecasquepodemsemover;
		Context proximoContexto;
		
		if (contexto.jogoAcabou()) {
			System.out.println("Jogo acabou, max!");
			return utility(contexto);
		}
		v = Integer.MIN_VALUE;
		
		pecasquepodemsemover = contexto.retornarPecasQuePodemSeMover(jogador);
		for (Item item:pecasquepodemsemover) {
			ArrayList<int[]>destinosPossiveis;
			proximoContexto = contexto.clonarContexto();
			destinosPossiveis = proximoContexto.pieceCouldMoveToFrom(item.getPosicaoAtual()[0], item.getPosicaoAtual()[1]);
			
			for (int[] proxDestino:destinosPossiveis) {
				proximoContexto.move(item.getPosicaoAtual()[0], item.getPosicaoAtual()[1], proxDestino[0], proxDestino[1]);
				v = Math.max(v, minvalue(proximoContexto));
			}
			
		}
		return v;
	}
	
	private int minvalue(Context contexto) {
		ArrayList<Item> pecasquepodemsemover;
		Context proximoContexto;

		if (contexto.jogoAcabou()) {
			System.out.println("Jogo acabou, min!");
			return utility(contexto);
		}
		
		v = Integer.MAX_VALUE;
		
		pecasquepodemsemover = contexto.retornarPecasQuePodemSeMover(jogador);
		for (Item item:pecasquepodemsemover) {
			ArrayList<int[]>destinosPossiveis;
			proximoContexto = contexto.clonarContexto();
			destinosPossiveis = proximoContexto.pieceCouldMoveToFrom(item.getPosicaoAtual()[0], item.getPosicaoAtual()[1]);
			
			for (int[] proxDestino:destinosPossiveis) {
				proximoContexto.move(item.getPosicaoAtual()[0], item.getPosicaoAtual()[1], proxDestino[0], proxDestino[1]);
				v = Math.min(v, maxvalue(proximoContexto));
			}
			
		}
		return v;
	}
	
	/**
	 * @author yvesbastos
	 * @return
	 */
	private int utility(Context contexto) {
		return contexto.quantidadePecasRestantesDesteJogador(jogador);
	}
	
	
}
