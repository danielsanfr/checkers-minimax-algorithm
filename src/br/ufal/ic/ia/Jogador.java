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
	private Tabuleiro tabuleiro;
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
			System.out.println("Vez do(a) " + getName() + "!");
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
