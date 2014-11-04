package br.ufal.ic.ia;

/**
 *
 * @author Arjen Hoogesteger
 * @version 0.1
 */
public class JogadorHumano extends Jogador
{
	/**
	 * 
	 * @param name
	 */
	public JogadorHumano(String name)
	{
		super(name);
	}

	@Override
	public void minhaVez()
	{
		super.minhaVez();
		System.out.println(getName() + "'s turn!");
		getBoard().enableMouseListener();	// we're human and need the board to listen to us
	}
}
