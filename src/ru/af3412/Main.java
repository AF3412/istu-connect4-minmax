package ru.af3412;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Собери 4!\n");
        //Компьютер 'O'
        int depth = 0;

        System.out.print("Выбери сложность от 1 до 7:");
        Scanner in = new Scanner(System.in);
        depth = in.nextInt();
        if (depth < 1 || depth > 7) {
            depth = 5;
        }
        MinMax computerPlayer = new MinMax(State.O, depth);
        State theBoard = new State();
        theBoard.printBoard();

        while (!theBoard.checkGameOver()) {
            System.out.println();
            int columnPosition;
            switch (theBoard.lastLetterPlayed) {
                //Если O был последним, то ходит Х
                case State.O:
                    System.out.print("Ход игрока X");
                    try {
                        do {
                            System.out.print("\nВыбери колонку, куда бросишь фишку (1-7): ");
                            in = new Scanner(System.in);
                            columnPosition = in.nextInt();
                        } while (theBoard.checkFullColumn(columnPosition - 1));
                    } catch (Exception e) {
                        System.out.println("\nРазрешены номеры 1,2,3,4,5,6 или 7. Попробуй снова\n");
                        break;
                    }
                    //Ход игрока
                    theBoard.makeMove(columnPosition - 1, State.X);
                    System.out.println();
                    break;
                //Если Х был последним, то ходит О
                case State.X:
                    GamePlay computerMove = computerPlayer.getNextMove(theBoard);
                    theBoard.makeMove(computerMove.col, State.O);
                    System.out.println("Компьютер O Кинул фишку в колонку " + (computerMove.col + 1) + ".");
                    System.out.println();
                    break;
                default:
                    break;
            }
            theBoard.printBoard();
        }

        System.out.println();
        if (theBoard.winner == State.X) {
            System.out.println("Игрок X выиграл!");
            System.out.println(theBoard.winningMethod);
        } else if (theBoard.winner == State.O) {
            System.out.println("Компьютер O выиграл!");
            System.out.println(theBoard.winningMethod);
        } else {
            System.out.println("Ничья!");
        }
        System.out.println("Конец игры.");
    }
}
