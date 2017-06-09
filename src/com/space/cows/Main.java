package com.space.cows;

import java.util.Scanner;

/**
 * Created by Lhaion on 07/06/2017.
 */
public class Main {
    public static void main(String[] str) {

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

            println("Onde você vai jogar? (xy)");

            int pos = scanner.nextInt();


        } while (!gameEnd(board));

    }

    private static int[][] play(int pos, int[][] board, boolean yourTurn) {

        String num = String.valueOf(pos);

        if (num.length() != 2) {
            errorln("O número deve conter dois dígitos!");
        } else {

            int x = num.charAt(0);
            int y = num.charAt(1);

            for (int i = x - 1; i <= x + 1; i++) {
//                for (int j = y - );
            }

        }

        return board;

    }

    private static int constrain(int num, int min, int max) {

        return Math.max(min, Math.min(max, num));

    }

    private static void println(String msg) {
        System.out.println(msg);
    }

    private static void errorln(String msg) {
        System.err.println(msg);
    }

    private static void printBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {

                System.out.print(board[j][i] + " ");

            }
            System.out.print("\n");
        }
    }

    private static boolean gameEnd(int[][] board) {
        return false;
    }
}
