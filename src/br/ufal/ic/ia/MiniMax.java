package br.ufal.ic.ia;

import java.util.ArrayList;

public class MiniMax {
	private Jogador jogador;
	private int v;
	
	private Context contextoFinal;
	
	
	public MiniMax() {
		
	}
	
	public int[] minimaxdecision(Context context, Jogador jogador) { //nao deve ser void
		ArrayList<int[]>historico;
		int[] melhormovimento = new int[4];
		
		this.jogador = jogador;
		v = maxvalue(context, 3, Integer.MIN_VALUE, Integer.MAX_VALUE);
		
		//recupera historico de movimentos da rota escolhida pelo minimax
		historico = contextoFinal.getMovementHistory();
		//recupera origem do primeiro movimento feito (=origem do movimento escolhido)
		
		//erro está aqui: era para ter dois itens: origem e destino
		if (historico.size()>1) {
			melhormovimento[0] = historico.get(0)[0];
			melhormovimento[1] = historico.get(0)[1];
			
			//recupera destino do primeiro movimento feito (=destino do movimento escolhido)
			melhormovimento[2] = historico.get(1)[0];
			melhormovimento[3] = historico.get(1)[1];
			
		} else {
			System.out.println("Erro!!");
		}
		
		
		//retorna a origem e destino do movimento escolhido para que ele seja usado na funcao move() de onde o minimax está sendo chamado
		return melhormovimento;
	}
	
	private int maxvalue(Context contexto, int profundidade, int alfa, int beta) {
		ArrayList<Item> pecasquepodemsemover;
		Context proximoContexto;
		//int a, b;

		if (contexto.jogoAcabou() || (profundidade==0)) {
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
			/*ArrayList<int[]>pulosPossiveis = proximoContexto.pieceCouldJumpToFrom(item.getPosicaoAtual()[0], item.getPosicaoAtual()[1]);

			if (pulosPossiveis.size()>0) {
				for (int[] proxDestinoPulo:pulosPossiveis) {
					proximoContexto.move(item.getPosicaoAtual()[0], item.getPosicaoAtual()[1], proxDestinoPulo[0], proxDestinoPulo[1]);
					
					v = Math.max(v, minvalue(proximoContexto, profundidade-1, alfa, beta));
					if (v>= beta) {
						return v;
					}
					alfa = Math.max(alfa, v);
				}
				
			} else {*/
				for (int[] proxDestino:destinosPossiveis) {
					proximoContexto.move(item.getPosicaoAtual()[0], item.getPosicaoAtual()[1], proxDestino[0], proxDestino[1]);
					
					v = Math.max(v, minvalue(proximoContexto, profundidade-1, alfa, beta));
					if (v>= beta) {
						return v;
					}
					alfa = Math.max(alfa, v);
				}
				
			//}
		}
		
		this.contextoFinal = contexto.clonarContexto();
		return v; //o slide do professor diz return v, mas não faz sentido... se fosse, o alfa nunca seria usado. retornar alfa?
	}
	
	private int minvalue(Context contexto, int profundidade, int alfa, int beta) {
		ArrayList<Item> pecasquepodemsemover;
		Context proximoContexto;
		
		
		if (contexto.jogoAcabou() || (profundidade==0)) {
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
			/*ArrayList<int[]>pulosPossiveis = proximoContexto.pieceCouldJumpToFrom(item.getPosicaoAtual()[0], item.getPosicaoAtual()[1]);

			if (pulosPossiveis.size()>0) {
				for (int[] proxDestinoPulo:pulosPossiveis) {
					proximoContexto.move(item.getPosicaoAtual()[0], item.getPosicaoAtual()[1], proxDestinoPulo[0], proxDestinoPulo[1]);
					
					v = Math.min(v, maxvalue(proximoContexto, profundidade-1, alfa, beta));
					if (v<= alfa) {
						return v;
					}
					beta = Math.min(beta, v);
				}
				
			} else {*/
				for (int[] proxDestino:destinosPossiveis) {
					proximoContexto.move(item.getPosicaoAtual()[0], item.getPosicaoAtual()[1], proxDestino[0], proxDestino[1]);
					
					v = Math.min(v, maxvalue(proximoContexto, profundidade-1, alfa, beta));
					if (v<= alfa) {
						return v;
					}
					beta = Math.min(beta, v);
				}
				
			//}
			
		}
		this.contextoFinal = contexto.clonarContexto();
		return v; //o slide do professor diz return v, mas não faz sentido... se fosse, o beta nunca seria usado. retornar beta?
	}
	
	/**
	 * @author yvesbastos
	 * @return
	 */
	private int utility(Context contexto) {
		return contexto.quantidadePecasRestantesDesteJogador(jogador);
	}
	
	
}
