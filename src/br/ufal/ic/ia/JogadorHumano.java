package br.ufal.ic.ia;

public class JogadorHumano extends Jogador {
	
	public JogadorHumano(String nome) {
		super(nome, true);
	}
	
	@Override
	public void minhaVez()
	{
		minhaVez = true;
		
		if (humano) {
		//	System.out.println("Vez do(a) " + getName() + "!");
			getBoard().ativarMouseListener();	// we're human and need the board to listen to us
		} 
		if (tabuleiro.getContext().quantidadePecas()[0] == 0 || tabuleiro.getContext().quantidadePecas()[1] == 0) {
			System.out.println("Fim!");
			//final; CPU venceu
		}
	}
}
