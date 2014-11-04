package br.ufal.ic.ia;
/**
 *
 * @author yvesbastos
 * @version 0.1
 */
public class JogadorMinimax extends Jogador
{
	private Context contexto;
	
	private int vantagem=-1;
	
	/**
	 * 
	 * @param name
	 */
	public JogadorMinimax()
	{
		super("CPU", false);
	}
	
	public void setContexto(Context contexto) {
		this.contexto = contexto;
	}
	
	@Override
	public void minhaVez()
	{
		super.minhaVez();
		vantagem = contexto.quantidadePecas()[1]-contexto.quantidadePecas()[0];
		// implementar função minimax
	}
}
