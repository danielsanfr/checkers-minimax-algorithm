package br.ufal.ic.ia;

import java.applet.Applet;
import java.awt.*;

import javax.swing.JPanel;

//
//

public class CheckerApplet extends Applet {
	public static int rows = 8;
	public static int colums = 8;
	public static Color col1=Color.black;
	public static Color col2=Color.white;
	
	/**
	 * @author yvesbastos
	 * Inicializa o tabuleiro
	 */
	public void init() {
		setLayout(new GridLayout(rows, colums));
		Color temp;
		
		for (int i=0; i<rows; i++) {
			if (i%2==0) {
				temp = col1;
			} else {
				temp = col2;
			}
			for (int j=0; j<colums;j++) {
				JPanel panel = new JPanel();
				panel.setBackground(temp);
				if (temp.equals(col1)) {
					temp = col2;
				} else {
					temp = col1;
				}
				add(panel);
			}
		}
	}
}
