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
		if (contexto.quantidadePecas()[0] == 0) {
			//final; CPU venceu
		} else {
			
		}
		
		super.minhaVez();
		vantagem = contexto.quantidadePecas()[1]-contexto.quantidadePecas()[0];
		// implementar função minimax
	}
}


/*
ROTINA minimax(nó, profundidade)
    SE nó é um nó terminal OU profundidade = 0 ENTÃO
        RETORNE o valor da heurística do nó
    SENÃO SE o nó representa a jogada de algum adversário ENTÃO
        α ← +∞
        PARA CADA filho DE nó
            α ← min(α, minimax(filho, profundidade-1))
        FIM PARA
        RETORNE α
    SENÃO
        α ← -∞
        PARA CADA filho DE nó
            α ← max(α, minimax(filho, profundidade-1))
        FIM PARA
        RETORNE α
    FIM SE
FIM ROTINA 
 */
