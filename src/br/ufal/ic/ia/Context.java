package br.ufal.ic.ia;

import java.util.ArrayList;

/**
 *
 * @author 
 * @version 0.4
 */
public class Context
{
	private static final char VEZ_ESCURO = 0x000F;
	private static final char VEZ_CLARO = 0x00F0;
	public static final int ALTURA = 8;
	public static final int LARGURA = 8;

	private ArrayList<int[]> historicoMovimentos = new ArrayList<int[]>();
	private Item[][] pieces = new Item[LARGURA][ALTURA];
	private char turn = VEZ_ESCURO;	// dark is basically an alias for player 1
	private int puloRestanteX = -1, puloRestanteY = -1;

	private Jogador jogador1;
	private Jogador jogador2;
	
	public Context(Jogador jogador1, Jogador jogador2)
	{
		this.jogador1=jogador1;
		this.jogador2=jogador2;
		
		// criar contexto inicial
		for(int i = 0; i < LARGURA; i++)
		{
			for(int j = 0; j < ALTURA; j++)
			{
				// cria peças e organiza tabuleiro
				if(i % 2 == j % 2)
				{
					if(j < (ALTURA / 2) - 1) {
						pieces[i][j] = Item.criarItemClaro(jogador1);
						pieces[i][j].setPosicaoAtual(i, j);
					} else if(j > ALTURA / 2) {
						pieces[i][j] = Item.criarItemEscuro(jogador2);
						pieces[i][j].setPosicaoAtual(i, j);
					} else
						pieces[i][j] = null;
				}
			}
		}
		//quantidadePecas();
	}

	/**
	 * @author yvesbastos
	 * Função que analisa o tabuleiro e retorna um array de todas as peças do donoDaPeca que podem se mover
	 * @param donoDaPeca
	 * @return
	 */
	public ArrayList<Item> retornarPecasQuePodemSeMover(Jogador donoDaPeca) {
		ArrayList<Item> pecasQuePodemSeMover = new ArrayList<Item>();
		
		for (int i=0; i<LARGURA; i++) {
			for (int j=0; j<ALTURA; j++) {
				if ((i % 2 == j % 2) && (pieces[i][j] != null)) {
					ArrayList<int[]> movimentosPossiveis = pieceCouldMoveToFrom(i, j);
					if (pieces[i][j].getDono().equals(donoDaPeca) && movimentosPossiveis.size()>0) {
						pieces[i][j].definirDestinosPossiveis(movimentosPossiveis);
						pieces[i][j].setPosicaoAtual(i, j);
						pecasQuePodemSeMover.add(pieces[i][j]);
						
					}
				}
			}
		}
		return pecasQuePodemSeMover;
	}
	
	public boolean jogoAcabou() {
		
		int[] quantidadepecas = quantidadePecas();
		
		if ((quantidadepecas[0] != 0) || (quantidadepecas[1] != 0) ) {
			return false;
		}
		return true;
	}
	
	/**
	 * @author yvesbastos
	 * Retorna diferença de peças entre jogadores
	 * @return
	 */
	public int[] quantidadePecas() {
		//claro, escuro
		int[] totalPecas = {0,0};
		for (int i=0; i<LARGURA; i++) {
			for (int j=0; j<ALTURA; j++) {
				if ((i % 2 == j % 2) && (pieces[i][j] != null)) {
					if (pieces[i][j].isDark()) {
						totalPecas[1]+=1;
					}
					else if (pieces[i][j].isLight()) {
						totalPecas[0]+=1;
					}
				}
			}
 		}
		//System.out.println("Peças claras: " + totalPecas[0] + "\nPeças escuras: " + totalPecas[1]);

		return totalPecas;
	}
	
	/**
	 * 
	 * @author yvesbastos
	 * @param jogador
	 * @return quantidade de pecas do jogador
	 */
	public int quantidadePecasRestantesDesteJogador(Jogador jogador) {
		int totalPecas = 0;
		for (int i=0; i<LARGURA; i++) {
			for (int j=0; j<ALTURA; j++) {
				if ((i % 2 == j % 2) && (pieces[i][j] != null)) {
					if (pieces[i][j].getDono().equals(jogador)) {
						totalPecas+=1;
					}
				}
			}
 		}
		return totalPecas;
	}
	
	/**
	 * Retorna se é a vez do jogador claro 
	 * @return verdadeiro se for, falso se não
	 */
	public boolean vezDoJogadorClaro()
	{
		return turn == VEZ_CLARO;
	}

	/**
	 * Retorna se é a vez do jogador escuro. 
	 * @return verdadeiro se for, falso se não 
	 */
	public boolean vezDoJogadorEscuro()
	{
		return turn == VEZ_ESCURO;
	}


	/**
	 * 
	 * @return
	 */
	public Item[][] getPieces()
	{
		return pieces;
	}

	/**
	 *
	 * @return
	 */
	public boolean temPulosRestantes()
	{
		//System.out.println(puloRestanteX > -1 && puloRestanteY > -1);
		return puloRestanteX > -1 && puloRestanteY > -1;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<int[]> getMovementHistory()
	{
		return historicoMovimentos;
	}

	/**
	 * 
	 * @param srcX
	 * @param srcY
	 * @param dstX
	 * @param dstY
	 * @return
	 */
	public boolean move(int srcX, int srcY, int dstX, int dstY)
	{
		if(isValidMove(srcX, srcY, dstX, dstY))
		{
			if(srcX - dstX == 2 || srcX - dstX == -2)
			{
				// jump
				jump(srcX, srcY, dstX, dstY);
				pieces[dstX][dstY].setPosicaoAtual(dstX, dstY);

				if(pieceCouldJumpToFrom(dstX, dstY).size() == 0)
				{
					advanceTurn();
					//System.out.println("Tempulosrestantes: " + temPulosRestantes());
					if(temPulosRestantes())
						unsetRemainingJump();	// no remaining jumps from here
				}
				else {
					//System.out.println("setting");
					setRemainingJump(dstX, dstY);	// player's next move has to be a jump from here
				}
			}
			else
			{
				// move
				pieces[dstX][dstY] = pieces[srcX][srcY];
				pieces[dstX][dstY].setPosicaoAtual(dstX, dstY);
				pieces[srcX][srcY] = null;
				advanceTurn();	// other player to move next
			}

			if(!(pieces[dstX][dstY] instanceof ItemRei)) // has yet to be crowned perhaps
			{
				if((dstY == ALTURA - 1) && pieces[dstX][dstY].isLight())
					pieces[dstX][dstY] = ItemRei.criarReiClaro(jogador1);
				else if(dstY == 0 && pieces[dstX][dstY].isDark())
					pieces[dstX][dstY] = ItemRei.criarReiEscuro(jogador2);
			}

			historicoMovimentos.add(new int[]{srcX, srcY, dstX, dstY});

			return true;
		}

		return false;
	}

	/**
	 *
	 * @param srcX
	 * @param srcY
	 * @return
	 */
	public ArrayList<int[]> pieceCouldJumpToFrom(int srcX, int srcY)
	{
		ArrayList<int[]> destinations = new ArrayList<int[]>();

		if(pieces[srcX][srcY].isLight() || pieces[srcX][srcY] instanceof ItemRei)
		{
			if(srcY <= ALTURA - 3)		// otherwise simply impossible to jump ..
			{
				if(srcX + 2 < LARGURA)	// jump right
				{
					// check if the destination is empty
					if(pieces[srcX + 2][srcY + 2] == null)
					{
						// check if there is an enemy in front of us
						if(pieces[srcX + 1][srcY + 1] != null && !pieces[srcX + 1][srcY + 1].getCor().equals(pieces[srcX][srcY].getCor()))
							destinations.add(new int[]{srcX + 2, srcY + 2});
					}
				}

				if(srcX - 2 >= 0)		// jump left
				{
					// check if the destination is empty
					if(pieces[srcX - 2][srcY + 2] == null)
					{
						// check if there is an enemy in front of us
						if(pieces[srcX - 1][srcY + 1] != null && !pieces[srcX - 1][srcY + 1].getCor().equals(pieces[srcX][srcY].getCor()))
							destinations.add(new int[]{srcX - 2, srcY + 2});
					}
				}
			}
		}

		if(pieces[srcX][srcY].isDark() || pieces[srcX][srcY] instanceof ItemRei)	// we're allowed to jump backwards
		{
			if(srcY >= 2)	// otherwise simply impossible to jump ..
			{
				if(srcX + 2 < LARGURA)	// jump right
				{
					// check if the destination is empty
					if(pieces[srcX + 2][srcY - 2] == null)
					{
						// check if there is an enemy in front of us
						if(pieces[srcX + 1][srcY - 1] != null && !pieces[srcX + 1][srcY - 1].getCor().equals(pieces[srcX][srcY].getCor()))
							destinations.add(new int[]{srcX + 2, srcY - 2});
					}
				}

				if(srcX - 2 >= 0)		// jump left
				{
					// check if the destination is empty
					if(pieces[srcX - 2][srcY - 2] == null)
					{
						// check if there is an enemy in front of us
						if(pieces[srcX - 1][srcY - 1] != null && !pieces[srcX - 1][srcY - 1].getCor().equals(pieces[srcX][srcY].getCor()))
							destinations.add(new int[]{srcX - 2, srcY - 2});
					}
				}
			}
		}

		return destinations;
	}
	
	/**
	 *
	 * @param srcX
	 * @param srcY
	 * @return
	 */
	public ArrayList<int[]> pieceCouldMoveToFrom(int srcX, int srcY)
	{
		ArrayList<int[]> destinations = new ArrayList<int[]>();

		if(pieces[srcX][srcY] != null)
		{
			if(pieces[srcX][srcY].isLight() || pieces[srcX][srcY] instanceof ItemRei)
			{
				// upwards
				if(srcY + 1 < ALTURA)
				{
					if(srcX + 1 < LARGURA)
					{
						if(pieces[srcX + 1][srcY + 1] == null)
							destinations.add(new int[]{srcX + 1, srcY + 1});	// right
					}

					if(srcX - 1 >= 0)
					{
						if(pieces[srcX - 1][srcY + 1] == null)
							destinations.add(new int[]{srcX - 1, srcY + 1});	// left
					}
				}
			}

			if(pieces[srcX][srcY].isDark() || pieces[srcX][srcY] instanceof ItemRei)
			{
				// downwards
				if(srcY - 1 >= 0)
				{
					if(srcX + 1 < LARGURA)
					{
						if(pieces[srcX + 1][srcY - 1] == null)
							destinations.add(new int[]{srcX + 1, srcY - 1});	// right
					}

					if(srcX - 1 >= 0)
					{
						if(pieces[srcX - 1][srcY - 1] == null)
							destinations.add(new int[]{srcX - 1, srcY - 1});	// left
					}
				}
			}
		}

		return destinations;
	}
	
	/**
	 * @author yvesbastos
	 * Clonar contexto para funcoes minimax
	 * @return
	 */
	public Context clonarContexto() {
		Context novoContexto = new Context(this.jogador1, this.jogador2);
		novoContexto.historicoMovimentos = this.historicoMovimentos;
		novoContexto.pieces = this.pieces;
		novoContexto.turn = this.turn;
		novoContexto.puloRestanteX = this.puloRestanteX;
		novoContexto.puloRestanteY = this.puloRestanteY;
		
		return novoContexto;
	}
	
	
	/**
	 *
	 * @return
	 */
	public ArrayList<Context> getPossibleFollowingContexts()
	{
		ArrayList<Context> contexts = new ArrayList<Context>();

		return contexts;
	}

	/**
	 * 
	 * @param srcX
	 * @param srcY
	 * @param dstX
	 * @param dstY
	 */
	private void jump(int srcX, int srcY, int dstX, int dstY)
	{
		int targetX = srcX < dstX ? srcX + 1 : srcX - 1;
		int targetY = srcY < dstY ? srcY + 1 : srcY - 1;
		
		pieces[dstX][dstY] = pieces[srcX][srcY];		
		pieces[targetX][targetY] = null;
		pieces[srcX][srcY] = null;
	}

	/**
	 * 
	 * @return
	 */
	private boolean canJump()
	{
		for(int i = 0; i < LARGURA; i++)
		{
			for(int j = 0; j < ALTURA; j++)
			{
				if(pieces[i][j] != null)
				{
					if(vezDoJogadorClaro() && pieces[i][j].isLight() && pieceCouldJumpToFrom(i, j).size() > 0)
						return true;
					else if(vezDoJogadorEscuro() && pieces[i][j].isDark() && pieceCouldJumpToFrom(i, j).size() > 0)
						return true;
				}
			}
		}

		return false;
	}

	/**
	 * 
	 * @param srcX
	 * @param srcY
	 * @param dstX
	 * @param dstY
	 * @return
	 */
	private boolean isValidMove(int srcX, int srcY, int dstX, int dstY)
	{
		if(pieces[srcX][srcY] != null)
		{
			if((pieces[srcX][srcY].isLight() && vezDoJogadorClaro()) || (pieces[srcX][srcY].isDark() && vezDoJogadorEscuro()))
			{
				ArrayList<int[]> dsts = new ArrayList<int[]>();

				if(temPulosRestantes())
				{
					if(srcX == puloRestanteX && srcY == puloRestanteY)
						dsts = pieceCouldJumpToFrom(srcX, srcY);
				}
				else
					dsts = canJump() ? pieceCouldJumpToFrom(srcX, srcY) : pieceCouldMoveToFrom(srcX, srcY);

				for(int[] dst : dsts)
				{
					if(dst[0] == dstX && dst[1] == dstY)
						return true;
				}
			}
		}

		return false;
	}

	/**
	 * When this function is called it becomes the other player's turn. This function
	 * is used in the <code>move</code> function.
	 */
	private void advanceTurn()
	{
		turn = (char) (turn ^ 0x00FF);


	}

	/**
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean setRemainingJump(int x, int y)
	{
		if(x > -1 && y > -1)
		{
			puloRestanteX = x;
			puloRestanteY = y;
			System.out.println("true");
			return true;
		}
		System.out.println("false");
		return false;
	}

	/**
	 *
	 */
	private void unsetRemainingJump()
	{
		puloRestanteX = -1;
		puloRestanteY = -1;
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Context)
		{
			Item[][] p = ((Context) o).getPieces();

			for(int i = 0; i < LARGURA; i++)
			{
				for(int j = 0; j < ALTURA; j++)
				{
					if(p[i][j] == null ^ pieces[i][j] == null)
						return false;
					else if(p[i][j] != null && pieces[i][j] != null)
					{
						if(p[i][j] instanceof ItemRei ^ pieces[i][j] instanceof ItemRei)
							return false;
						if(!p[i][j].getCor().equals(pieces[i][j].getCor()))
							return false;
					}
				}
			}
		}
		else
			return false;

		return true;
	}
}
