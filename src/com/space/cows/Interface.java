package com.space.cows;

import br.senai.sc.engine.Game;

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

    @Override
    public void init() {

    }

    @Override
    public void aposTermino() {

    }

    @Override
    public void gameLoop() {

    }

    private class MouseInputHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {

        }
    }
}
