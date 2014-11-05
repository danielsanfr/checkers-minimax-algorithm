package br.ufal.ic.ia;

import java.util.ArrayList;

/**
 *
 * @author yvesbastos
 * @version 0.2 
 */
public abstract class Jogador
{
	private String nome;
	protected Tabuleiro tabuleiro;
	protected boolean minhaVez = false;
	private ArrayList<PlayerListener> listeners = new ArrayList<PlayerListener>();
	protected final boolean humano;
	
	/**
	 * Construtor: cria um jogador
	 * @param nome do jogador
	 * @param humano boolean indicando se o jogador é humano ou CPU
	 */
	public Jogador(String nome, boolean humano)
	{
		this.nome = nome;
		
		this.humano = humano;
	}
	
	/**
	 * Construtor: cria um jogador CPU
	 */
	public Jogador() {
		this.nome="Computador";
		this.humano=false;
	}
	
	/**
	 * Função minimax
	 * Incompleta 
	 * OBS: deve realizar um movimento ou uma sequencia de movimentos
	 *	sinal ainda não está sendo usado 
	 */
	protected void minimax(int sinal) {
		//pecas deste jogador que podem se movimentar
		ArrayList<Item>pecasASeremAnalisadas = tabuleiro.getContext().retornarPecasQuePodemSeMover(this);
		int maiorPontuacao=-1;
		
		for (Item item:pecasASeremAnalisadas) {
			int pulos=0;
			
			//se esta peça pode comer outra, entrar
			if (tabuleiro.getContext().pieceCouldJumpToFrom(item.getPosicaoAtual()[0], item.getPosicaoAtual()[1])!=null) {
				int[] destinoPrimeiroPulo = tabuleiro.getContext().pieceCouldJumpToFrom(item.getPosicaoAtual()[0], item.getPosicaoAtual()[1]).get(0);
				//criar contexto temporario, já que várias jogadas serão analisadas
				Context tempContext = tabuleiro.getContext();
				//sempre haverá somente uma opção de primeiro pulo por rodada; fazer tal movimento:
				tempContext.move(item.posicaoAtual[0], item.posicaoAtual[1], destinoPrimeiroPulo[0], destinoPrimeiroPulo[1]);
				pulos++;
				
				//proximo destino temporario
				int[] tempDest = {destinoPrimeiroPulo[0],destinoPrimeiroPulo[1]};
				
				int pulosAdicionais=0;
				int[] destEscolhido;
				
				//para cada uma das proximas opções de pulos, é preciso escolher a melhor opção
				for (int[]puloSeguinte:tempContext.pieceCouldJumpToFrom(tempDest[0], tempDest[1])) {
					//calcular e guardar a quantidade de pulos que seria possivel caso este pulo fosse escolhido dentre todas as outras opções
					//se maior que o guardado ate agora, guardar a origem e destino para efetuar o movimento na saída do for
				}
				//fazer o movimento temp.context.move()
				//salvar melhor opcao para comparar com outros do for externo
			}
		}
	}
	
	/**
	 * 
	 * @param l
	 */
	public void addListener(PlayerListener l)
	{
		listeners.add(l);
	}
	
	/**
	 * Retorna o nome do jogador
	 * @return the name
	 */
	public String getName()
	{
		return nome;
	}
	
	/**
	 * Retorna o tabuleiro
	 * @return
	 */
	public Tabuleiro getBoard()
	{
		return tabuleiro;
	}

	/**
	 * Configura o tabuleiro
	 * @param board
	 */
	public void setBoard(Tabuleiro board)
	{
		this.tabuleiro = board;
	}

	/**
	 * 
	 */
	public void minhaVez()
	{
		minhaVez = true;
		
		if (humano) {
			//System.out.println("Vez do(a) " + getName() + "!");
			getBoard().ativarMouseListener();	// we're human and need the board to listen to us
		} 
	}

	/**
	 *
	 */
	public void stopTurn()
	{
		minhaVez = false;
		
		for(PlayerListener l : listeners)
			l.acabouRodada(this);
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasTurn()
	{
		return minhaVez;
	}
}
