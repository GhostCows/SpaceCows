package com.space.cows;

import java.util.Scanner;

/**
 * Created by Lhaion on 07/06/2017.
 */
public class Main {

    private static String[] prevs = new String[2];

    public static void main(String[] str) {

        prevs[0] = "";
        prevs[1] = "";

        Scanner scanner = new Scanner(System.in);

        int[][] board = new int[7][7];

        boolean yourTurn = true;

        do {

            if (yourTurn) {
                println("É a sua vez!");
            } else {
                println("É a vez do adversário!");
            }

            printBoard(board);

            println("Onde você vai jogar? (lc)");

            String pos = scanner.nextLine();

            if (play(pos, board, yourTurn)) {

                yourTurn = !yourTurn;

            }

        } while (!gameEnd(board));

        if (!yourTurn) {
            println("Você Ganhou! Parabéns");
        } else {
            println("Que pena, você perdeu...");
        }

    }

    private static boolean play(String num, int[][] board, boolean yourTurn) {

        boolean valid = true;

        if (num.length() != 2) {
            println("O número deve conter dois dígitos!");
        } else {

            String sx = ((Character) num.charAt(1)).toString();
            int x = Integer.parseInt(sx) -
                    1;
            String sy = ((Character) num.charAt(0)).toString();
            int y = Integer.parseInt(sy) - 1;

            int index = yourTurn ? 0 : 1;

            if(board[x][y] != 0) {

                println("Você só pode jogar em slots vazios!");

                valid = false;

            } else if (prevs[index].equals(num)) {

                println("Você não pode jogar duas vezes no mesmo lugar");

                valid = false;

            } else {

                prevs[index] = num;

                for (int i = Math.max(x - 1, 0); i <= Math.min(x + 1, 6); i++) {
                    for (int j = Math.max(y - 1, 0); j <= Math.min(y + 1, 6); j++) {

                        if (board[i][j] == 0) {
                            if (yourTurn) {
                                board[i][j] = 1;
                            } else {
                                board[i][j] = 2;
                            }
                        } else {
                            board[i][j] = 0;
                        }
                    }
                }

            }
        }

        return valid;

    }

    /*private static String aiTurn(int[][] board) {



    }

    private static int[] analizePlay(int x, int y) {



    }*/

    private static void sleep(int ms) {

        long time = System.currentTimeMillis();

        while(time + ms > System.currentTimeMillis());

    }

    private static void println(String msg) {

        char[] chars = msg.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            System.out.print(chars[i]);
            sleep(50);
        }

        System.out.print('\n');

    }

    private static void printBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {

                System.out.print(board[j][i] + " ");
                sleep(10);

            }
            System.out.print("\n");
        }
    }

    private static boolean gameEnd(int[][] board) {

        int p1 = 0;
        int p2 = 0;

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                int value = board[i][j];
                if (value == 1) {
                    p1++;
                } else if (value == 2) {
                    p2++;
                }
            }
        }

        return p1 >= 25 || p2 >= 25;

    }
}
