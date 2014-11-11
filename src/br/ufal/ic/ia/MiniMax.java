package br.ufal.ic.ia;

import java.util.ArrayList;

public class MiniMax {
	private Jogador jogador;
	private int v;
	
	private Context contextoFinal;
	private int[] origemescolhida;
	private int[] destinoescolhido;
		
	
	public MiniMax() {
		origemescolhida = new int[2];
		destinoescolhido = new int [2];
	}
	
	public int[] minimaxdecision(Context context, Jogador jogador) { //nao deve ser void
		ArrayList<int[]>historico;
		int[] melhormovimento = new int[4];
		
		this.jogador = jogador;
		v = maxvalue(context, Integer.MIN_VALUE, Integer.MAX_VALUE);
		
		//recupera historico de movimentos da rota escolhida pelo minimax
		historico = contextoFinal.getMovementHistory();
		//recupera origem do primeiro movimento feito (=origem do movimento escolhido)
		melhormovimento[0] = historico.get(0)[0];
		melhormovimento[1] = historico.get(0)[1];
		
		//recupera destino do primeiro movimento feito (=destino do movimento escolhido)
		melhormovimento[2] = historico.get(1)[0];
		melhormovimento[3] = historico.get(1)[1];
		
		//retorna a origem e destino do movimento escolhido para que ele seja usado na funcao move() de onde o minimax está sendo chamado
		return melhormovimento;
	}
	
	private int maxvalue(Context contexto, int alfa, int beta) {
		ArrayList<Item> pecasquepodemsemover;
		Context proximoContexto;
		//int a, b;

		if (contexto.jogoAcabou()) {
			System.out.println("Jogo acabou, max!");
			this.contextoFinal = contexto.clonarContexto();
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
				v = Math.max(v, minvalue(proximoContexto, alfa, beta));
				if (v>= beta) {
					return v;
				}
				alfa = Math.max(alfa, v);
			}
			
		}
		
		this.contextoFinal = contexto.clonarContexto();
		return v;
	}
	
	private int minvalue(Context contexto, int alfa, int beta) {
		ArrayList<Item> pecasquepodemsemover;
		Context proximoContexto;
		int a, b;
		
		
		if (contexto.jogoAcabou()) {
			System.out.println("Jogo acabou, min!");
			this.contextoFinal = contexto.clonarContexto();
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
				v = Math.min(v, maxvalue(proximoContexto, alfa, beta));
				if (v <= alfa) {
					return v;
				}
				beta = Math.min(beta, v);
			}
			
		}
		this.contextoFinal = contexto.clonarContexto();
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
