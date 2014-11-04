package br.ufal.ic.ia;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author 
 * @version 0.1
 */
public class ItemRei extends Item
{
	/**
	 * 
	 * @return
	 */
	public static ItemRei criarReiClaro(Jogador dono)
	{
		return new ItemRei(Item.CLARO, dono);
	}

	/**
	 * 
	 * @return
	 */
	public static ItemRei criarReiEscuro(Jogador dono)
	{
		return new ItemRei(Item.ESCURO, dono);
	}

	/**
	 * 
	 * @param color
	 */
	protected ItemRei(Color color, Jogador dono)
	{
		super(color, dono);
	}

	@Override
	public void desenhar(Graphics g)
	{
		super.desenhar(g);

		if(isLight())
			g.setColor(Item.ESCURO);
		else
			g.setColor(Item.CLARO);

		// both arrays must have same size
		int[] xcoords = {1, 8, 16, 23, 31, 28, 3};
		int[] ycoords = {0, 8, 0, 8, 0, 20, 20};

		for(int i = 0; i < xcoords.length; i++)
			xcoords[i] += 14; // add x-axis offset

		for(int i = 0; i < ycoords.length; i++)
			ycoords[i] += 21; // add y-axis offset
		
		g.drawPolygon(xcoords, ycoords, xcoords.length); // draw our precious crown
	}
}
