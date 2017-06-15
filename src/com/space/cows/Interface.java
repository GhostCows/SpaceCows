package com.space.cows;

import br.senai.sc.engine.Game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Leonardo Antunes
 * Leonardo Vignatti
 * Lhaion Alexandre
 * Marcelo Vogt
 */
public class Interface extends Game {

    public static  void main(String[] args) {
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

    private int screen = 1;

    private String gamemode = "PC";

    private Image computer;
    private Image person;


    @Override
    public void init() {

		computer = carregarImagem("images/computer.png");
		person = carregarImagem("images/person.png");

    }

    @Override
    public void gameLoop() {

    	desenharRetangulo(0,0,1040, 650, new Color(30,30,30));

    	switch (screen) {
		    case 1:

				desenharString("Opções:", 463, 60, Color.WHITE, 30);
				desenharRetangulo(450,110,70,70,new Color(200,200,200));
				desenharRetangulo(520,110,70,70,Color.WHITE);

				if(gamemode.charAt(0) == 'P') {
					desenharImagem(person, 450, 110);
				} else {
					desenharImagem(computer, 450, 110);
				}
				if(gamemode.charAt(1) == 'P') {
					desenharImagem(person, 520, 110);
				} else {
					desenharImagem(computer, 520, 110);
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

//	        System.out.println("X " + x);
//	        System.out.println("Y " + y);

	        switch (screen) {
		        case 1:

		        	if(between(450, x, 590) && between(110, y, 180)) {

		        		if(x < 520) {

		        			gamemode = ((char) (147 - gamemode.charAt(0))) + String.valueOf(gamemode.charAt(1));

				        } else {

					        gamemode = String.valueOf(gamemode.charAt(0)) + ((char) (147 - gamemode.charAt(1)));


				        }

				        System.out.println("gamemode2 " + gamemode);

			        }

		        	break;
	        }

        }
    }

    private boolean between(int min, int num, int max) {
    	return min < num && num < max;
    }
}
