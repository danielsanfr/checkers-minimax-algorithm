package br.ufal.ic.ia;

import java.util.ArrayList;

/**
 *
 * @author Arjen Hoogesteger
 * @version 0.2
 */
public abstract class Jogador
{
	private String nome;
	private Tabuleiro tabuleiro;
	private boolean minhaVez = false;
	private ArrayList<PlayerListener> listeners = new ArrayList<PlayerListener>();

	/**
	 * Creates a new Player object.
	 * @param name the player's name
	 */
	public Jogador(String name)
	{
		this.nome = name;
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
	 *
	 * @param board
	 */
	public void setBoard(Tabuleiro board)
	{
		this.tabuleiro = board;
	}

	/**
	 * 
	 * @return
	 */
	public Tabuleiro getBoard()
	{
		return tabuleiro;
	}

	/**
	 * Returns the name of the player.
	 * @return the name
	 */
	public String getName()
	{
		return nome;
	}

	/**
	 * 
	 */
	public void minhaVez()
	{
		minhaVez = true;
	}

	/**
	 *
	 */
	public void stopTurn()
	{
		minhaVez = false;

		for(PlayerListener l : listeners)
			l.finishedTurn(this);
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
