package com.space.cows;

import br.senai.sc.engine.Game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.awt.Color.*;

/**
 * Leonardo Antunes
 * Leonardo Vignatti
 * Lhaion Alexandre
 * Marcelo Vogt
 */
public class Interface extends Game {

	public static void main(String[] args) {
		Interface interf = new Interface();
		interf.startGame();
	}

	public Interface() {
		super("TSIFOC", 1040, 650);
		addMouseListener(new MouseInputHandler());
	}


	private int length = 7;
	private int slotSize = 84;
	private int marginTopBottom = 31;
	private int marginLeftRight = 226;

	/**
	 * Variable that defines in what screen you are in.
	 * -1 Splash Screen
	 * 0 Title Screen
	 * 1 Options Screen
	 * 2 Game Screen
	 * 3 Settings Screen
	 */
	private int screen = 0;

	private int difficulty = 0;

	private String gamemode = "PC";

	private Color selected = new Color(78, 102, 249);

	// screen 0
	private Image matrisse;

	//screen 1
	private Image computer;
	private Image person;


	@Override
	public void init() {

		matrisse = carregarImagem("images/Matrisse.png");

		computer = carregarImagem("images/computer.png");
		person = carregarImagem("images/person.png");

	}



	@Override
	public void gameLoop() {

		switch (screen) {
			case 0: // tela inicial

				desenharImagem(matrisse, 0, 0);

				desenharRetangulo(415, 490, 190, 40, WHITE);

				desenharString("COMEÇAR", 430, 520, BLACK, 30);

				break;
			case 1: // configurações

				desenharRetangulo(0, 0, 1040, 650, new Color(18, 18, 18));

				desenharString("Opções:", 463, 60, WHITE, 30);
				desenharRetangulo(450, 110, 70, 70, new Color(200, 200, 200));
				desenharRetangulo(520, 110, 70, 70, WHITE);

				if (gamemode.charAt(0) == 'P') {
					desenharImagem(person, 450, 110);
				} else {
					desenharImagem(computer, 450, 110);
				}
				if (gamemode.charAt(1) == 'P') {
					desenharImagem(person, 520, 110);
				} else {
					desenharImagem(computer, 520, 110);
				}

				desenharRetangulo(450, 200, 140, 40, WHITE);
				desenharString("FÁCIL", 480, 232, difficulty == 0 ? selected : Color.BLACK);
				desenharRetangulo(450, 240, 140, 40, new Color(200, 200, 200));
				desenharString("NORMAL", 455, 272, difficulty == 1 ? selected : Color.BLACK);
				desenharRetangulo(450, 280, 140, 40, WHITE);
				desenharString("DIFÍCIL", 470, 312, difficulty == 2 ? selected : Color.BLACK);

				break;
			case 2:

				desenharRetangulo(0,0,1040,650, BLACK);

				for (int i = 0; i < length; i++) {
					for (int j = 0; j < length; j++) {

						int num = (int) (((double) (i + j)) / 14 * 205 + 50);

						Color color = new Color(num, num, num);

						desenharRetangulo(marginLeftRight + slotSize * i, marginTopBottom + slotSize * j, slotSize, slotSize, color);

					}
				}

				break;
		}

	}

	@Override
	public void aposTermino() {

	}

	private class MouseInputHandler extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {

			int x = e.getX();
			int y = e.getY();

			System.out.println();
			System.out.println("X " + x);
			System.out.println("Y " + y);

			switch (screen) {
				case 0:

					if (between(415, x, 605) && between(490, y, 530)) {

						screen = 1;

					}

					break;
				case 1:
					if (between(450, x, 590)) {

						if (between(110, y, 180)) { // change gamemode

							int i = x < 520 ? 0 : 1;
							int j = 1 - i;
							char[] gm = new char[2];
							gm[i] = ((char) (147 - gamemode.charAt(i)));
							gm[j] = gamemode.charAt(j);
							gamemode = new String(gm);

						} else if (between(200, y, 320)) { // change difficulty

							difficulty = (int) Math.floor(y / 40 - 5);

						}
					}
					break;
			}

		}
	}

	private boolean between(int min, int num, int max) {
		return min < num && num < max;
	}
}
