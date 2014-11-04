package br.ufal.ic.ia;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JFrame;

/**
 *
 * @author yvesbastos
 * @version 0.3
 */
public class Tabuleiro extends JPanel implements MouseListener, PlayerListener
{
	private static final int LARGURA_QUADRADO = 60;
	private static final int ALTURA_QUADRADO = 60;
	private Quadrado source = null;
	private Context contexto;
	private Quadrado[][] quadrados = new Quadrado[Context.WIDTH][Context.HEIGHT];
	private boolean mouseListener;
	
	
	private Jogador jogador1;
	private Jogador jogador2;

	/**
	 * 
	 */
	public Tabuleiro(Jogador jogador1, Jogador jogador2)
	{
		// configura jogadores para este tabuleiro
		this.jogador1 = jogador1;
		this.jogador1.setBoard(this);
		this.jogador1.addListener(this);
		this.jogador2 = jogador2;
		this.jogador2.setBoard(this);
		this.jogador2.addListener(this);
		
		// desativa mouse listener por padrão 
		desativarMouseListener();

		// please take care of the gap
		((FlowLayout)getLayout()).setHgap(0);
		((FlowLayout)getLayout()).setVgap(0);

		// set preferred size for the board
		setPreferredSize(new Dimension(Context.WIDTH * LARGURA_QUADRADO, Context.HEIGHT * ALTURA_QUADRADO));

		for(int i = 0; i < Context.WIDTH; i++)
		{
			for(int j = 0; j < Context.HEIGHT; j++)
			{
				// decide se cria quadrado preto ou branco
				quadrados[i][j] = i % 2 == j % 2 ? new Quadrado(new Color(250, 67, 0, 168), i, j) : new Quadrado(new Color(244, 241, 237), i, j);

				// definir tamanho do quadrado
				quadrados[i][j].setPreferredSize(new Dimension(LARGURA_QUADRADO, ALTURA_QUADRADO));

				// adiciona mouselistener
                quadrados[i][j].addMouseListener(this);
			}
		}

		// adiciona quadrados ao tabuleiro na ordem correta
		for(int i = Context.HEIGHT - 1; i >= 0; i--)
		{
			for(int j = 0; j < Context.WIDTH; j++)
				add(quadrados[j][i]);
		}
		
		// configura o contexto inicial do tabuleiro 
		setContext(new Context(jogador1, jogador2));		
	}

	/**
	 *
	 */
	public void ativarMouseListener()
	{
		mouseListener = true;
	}

	/**
	 * 
	 */
	public void desativarMouseListener()
	{
		mouseListener = false;
	}

	/**
	 * 
	 */
	private void setPieces()
	{
		Item[][] pieces = contexto.getPieces();

		for(int i = 0; i < Context.WIDTH; i++)
		{
			for(int j = 0; j < Context.HEIGHT; j++)
			{
				quadrados[i][j].setPiece(pieces[i][j]);
			}
		}
	}

	/**
	 *
	 * @param context the board's context
	 */
	public void setContext(Context context)
	{
		this.contexto = context;
		setPieces();
	}

	/**
	 *
	 * @return the board's context
	 */
	public Context getContext()
	{
		return contexto;
	}

	@Override
	public void repaint()
	{
		super.repaint();

		if(quadrados != null)
		{			
			for(int i = 0; i < quadrados.length; i++)
			{
				for(int j = 0; j < quadrados[i].length; j++)
					quadrados[i][j].repaint();
			}
		}
	}

	@Override
    public void mouseClicked(MouseEvent e)
    {
		boolean jogadorApertouNaSua;
		if (((Quadrado) e.getSource()).getPiece() == null) {
			jogadorApertouNaSua = true;
		}
		else {
			jogadorApertouNaSua = ( (((Quadrado) e.getSource()).getPiece().isLight() && contexto.isTurnLight()) || (((Quadrado) e.getSource()).getPiece().isDark() && contexto.isTurnDark()) );
		}
		
		if(mouseListener && jogadorApertouNaSua)
		{
			if(source == null)
			{
				// no source has been set yet
				source = (Quadrado) e.getSource();
				
				if(source.getCoordinateX() % 2 == source.getCoordinateY() % 2)
				{
					//so selecionar peca se for a vez do jogador. falta fazer o mesmo para quadrado azul

					//if (((source.getPiece().getCor() == Item.CLARO)&&contexto.isTurnLight()) || ((source.getPiece().getCor() == Item.ESCURO)&&(contexto.isTurnDark()))) {
						source.select();
					//}

					ArrayList<int[]> targets = contexto.pieceCouldMoveToFrom(source.getCoordinateX(), source.getCoordinateY());
					for(int[] target : targets)
						quadrados[target[0]][target[1]].target();
				}
				else
					source = null;
			}
			else if(source.equals((Quadrado) e.getSource()))
			{
				// selection equals previous set source, deselect
				ArrayList<int[]> targets = contexto.pieceCouldMoveToFrom(source.getCoordinateX(), source.getCoordinateY());
				for(int[] target : targets)
					quadrados[target[0]][target[1]].detarget();

				source.deselect();
				source = null;
			}
			else
			{
				// source has been set, this time destination has been selected
				Quadrado destination = (Quadrado) e.getSource();

				if(contexto.move(source.getCoordinateX(), source.getCoordinateY(), destination.getCoordinateX(), destination.getCoordinateY()))
				{
					detargetAllSquares();
					source.deselect();
					source = null;
					contexto.quantidadePecas();
					// context changed but make sure we visualise the changes
					setPieces();
					repaint();
				}
				else
					System.out.println("UNABLE TO MOVE [" + source.getCoordinateX() + ", " + source.getCoordinateY() + "] -> [" + destination.getCoordinateX() + ", " + destination.getCoordinateY() + "]");
			}

			if(contexto.isTurnDark() && jogador2.hasTurn())
			{
				jogador2.stopTurn();
			}
			else if(contexto.isTurnLight() && jogador1.hasTurn())
			{
				jogador1.stopTurn();
			}
		}
		//System.out.println("Peças claras: " + contexto.quantidadePecas()[0] + "\nPeças escuras: " + contexto.quantidadePecas()[1]);
    }

	/**
	 * Returns player one of the game.
	 * @return player one
	 */
	public Jogador getPlayer1()
	{
		return jogador1;
	}

	/**
	 * Returns player two of the game.
	 * @return player two
	 */
	public Jogador getPlayer2()
	{
		return jogador2;
	}

	/**
	 * 
	 */
	private void detargetAllSquares()
	{
		for(int i = 0; i < quadrados.length; i++)
		{
			for(int j = 0; j < quadrados[i].length; j++)
				quadrados[i][j].detarget();
		}
	}

	/**
	 *
	 */
	public void play()
	{
		jogador1.minhaVez();
	}

	public static void main(String[] args)
	{
		Tabuleiro b = new Tabuleiro(new Jogador("Raphael", true), new Jogador("João", true));

		JFrame frame = new JFrame("Damas");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel boardPane = new JPanel(new FlowLayout());
		((FlowLayout)boardPane.getLayout()).setAlignment(FlowLayout.CENTER);
		boardPane.add(b);
		
		frame.getContentPane().setLayout(new java.awt.BorderLayout());
		frame.getContentPane().add(boardPane, java.awt.BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);

		b.play();
	}

	// as for right now functions below are completely useless, but required

	@Override
    public void mousePressed(MouseEvent e)
	{
		// nothing, nada, zip ..
    }

	@Override
    public void mouseReleased(MouseEvent e)
	{
        // nothing, nada, zip ..
    }

	@Override
    public void mouseEntered(MouseEvent e)
	{
        // nothing, nada, zip ..
    }

	@Override
    public void mouseExited(MouseEvent e)
	{
        // nothing, nada, zip ..
    }

	@Override
	public void acabouRodada(Jogador p)
	{
		if((p.equals(jogador1) && !jogador2.hasTurn()) || (p.equals(jogador2) && !jogador1.hasTurn()))
		{
			desativarMouseListener(); // player will reactivate it if necessary

			if(p.equals(jogador1))
				jogador2.minhaVez();
			else
				jogador1.minhaVez();
		}
	}
}
