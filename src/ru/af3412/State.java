package ru.af3412;

import java.util.LinkedList;

class State {

    static final int X = 1;     //Человек
    static final int O = -1;    //Компьютер
    private int EMPTY = 0;      //Пустое

    GamePlay lastMove; //последний ход
    int lastLetterPlayed;
    int winner;
    private int[][] gameBoard;
    String winningMethod;

    State() {
        lastMove = new GamePlay();
        lastLetterPlayed = O; //Игрок всегда ходит первым
        winner = 0;
        gameBoard = new int[6][7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                gameBoard[i][j] = EMPTY;
            }
        }
    }

    void printBoard() {
        System.out.println("| 1 | 2 | 3 | 4 | 5 | 6 | 7 |");
        System.out.println();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (gameBoard[i][j] == 1) {
                    System.out.print("| " + "X "); //Если пользователь
                } else if (gameBoard[i][j] == -1) {
                    System.out.print("| " + "O "); //Если компьютер
                } else {
                    System.out.print("| " + "-" + " ");
                }
            }
            System.out.println("|");
        }
    }

    //Ход. Колонка и игрок
    void makeMove(int col, int letter) {
        lastMove = lastMove.moveDone(getRowPosition(col), col);
        gameBoard[getRowPosition(col)][col] = letter;
        lastLetterPlayed = letter;
    }

    boolean checkFullColumn(int col) {
        if (gameBoard[0][col] == EMPTY)
            return false;
        else {
            System.out.println("Колонка " + (col + 1) + " Заполнена. Выбери другую колонку.");
            return true;
        }
    }

    //Расширяем доску с учетом хода
    State boardWithExpansion(State board) {
        State expansion = new State();
        expansion.lastMove = board.lastMove;
        expansion.lastLetterPlayed = board.lastLetterPlayed;
        expansion.winner = board.winner;
        expansion.gameBoard = new int[6][7];
        for (int i = 0; i < 6; i++) {
            System.arraycopy(board.gameBoard[i], 0, expansion.gameBoard[i], 0, 7);
        }
        return expansion;
    }

    //Генерируем потомков текущего состояния
    LinkedList<State> getChildren(int letter) {
        LinkedList<State> children = new LinkedList<>();
        for (int col = 0; col < 7; col++) {
            if (isValidMove(col)) {
                State child = boardWithExpansion(this);
                child.makeMove(col, letter);
                children.add(child);
            }
        }
        return children;
    }

    int utilityFunction() {
        //Максимум компьютера 'O'
        // +90 если 'O' выиграет, -90 если 'X' выиграет,
        // +10 если три 'O' в ряд, -5 если три 'X' в ряд,
        // +4 если два 'O' в ряд, -1 если два 'X' в ряд
        int Xlines = 0;
        int Olines = 0;
        if (checkWinState()) {
            if (winner == X) {
                Xlines = Xlines + 90;
            } else {
                Olines = Olines + 90;
            }
        }
        Xlines = Xlines + check3In(X) * 10 + check2In(X) * 4;
        Olines = Olines + check3In(O) * 5 + check2In(O);
        return Olines - Xlines;
    }

    boolean checkGameOver() {

        if (checkWinState()) {
            return true;
        }
        //проверка, что есть еще куда ходить
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                if (gameBoard[row][col] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setWinner(int winner) {
        this.winner = winner;
    }

    private void setWinnerMethod(String winningMethod) {
        this.winningMethod = winningMethod;
    }

    private boolean isValidMove(int col) {
        int row = getRowPosition(col);
        if ((row == -1) || (col == -1) || (row > 5) || (col > 6)) {
            return false;
        }
        return gameBoard[row][col] == EMPTY;
    }

    private boolean canMove(int row, int col) {
        return (row > -1) && (col > -1) && (row <= 5) && (col <= 6);
    }

    private int getRowPosition(int col) {
        int rowPosition = -1;
        for (int row = 0; row < 6; row++) {
            if (gameBoard[row][col] == EMPTY) {
                rowPosition = row;
            }
        }
        return rowPosition;
    }

    private boolean checkWinState() {

        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 4; j++) {
                if (gameBoard[i][j] == gameBoard[i][j + 1] && gameBoard[i][j] == gameBoard[i][j + 2] && gameBoard[i][j] == gameBoard[i][j + 3] && gameBoard[i][j] != EMPTY) {
                    setWinner(gameBoard[i][j]);
                    setWinnerMethod("Выиграл. Заполнил строку.");
                    return true;
                }
            }
        }

        for (int i = 5; i >= 3; i--) {
            for (int j = 0; j < 7; j++) {
                if (gameBoard[i][j] == gameBoard[i - 1][j] && gameBoard[i][j] == gameBoard[i - 2][j] && gameBoard[i][j] == gameBoard[i - 3][j] && gameBoard[i][j] != EMPTY) {
                    setWinner(gameBoard[i][j]);
                    setWinnerMethod("Выиграл. Заполнил колонку.");
                    return true;
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (gameBoard[i][j] == gameBoard[i + 1][j + 1] && gameBoard[i][j] == gameBoard[i + 2][j + 2] && gameBoard[i][j] == gameBoard[i + 3][j + 3] && gameBoard[i][j] != EMPTY) {
                    setWinner(gameBoard[i][j]);
                    setWinnerMethod("Выиграл. Заполнил диагональ.");
                    return true;
                }
            }
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i - 3, j + 3)) {
                    if (gameBoard[i][j] == gameBoard[i - 1][j + 1] && gameBoard[i][j] == gameBoard[i - 2][j + 2] && gameBoard[i][j] == gameBoard[i - 3][j + 3] && gameBoard[i][j] != EMPTY) {
                        setWinner(gameBoard[i][j]);
                        setWinnerMethod("Выиграл. Заполнил диагональ.");
                        return true;
                    }
                }
            }
        }
        //Если нет победителя
        setWinner(0);
        return false;
    }

    private int check3In(int player) {
        int times = 0;
        //В строке
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i, j + 2)) {
                    if (gameBoard[i][j] == gameBoard[i][j + 1] && gameBoard[i][j] == gameBoard[i][j + 2] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }

        //В колонке
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i - 2, j)) {
                    if (gameBoard[i][j] == gameBoard[i - 1][j] && gameBoard[i][j] == gameBoard[i - 2][j] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }

        //По диагонали
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i + 2, j + 2)) {
                    if (gameBoard[i][j] == gameBoard[i + 1][j + 1] && gameBoard[i][j] == gameBoard[i + 2][j + 2] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }

        //По диагонали
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i - 2, j + 2)) {
                    if (gameBoard[i][j] == gameBoard[i - 1][j + 1] && gameBoard[i][j] == gameBoard[i - 2][j + 2] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }
        return times;
    }

    private int check2In(int player) {
        int times = 0;
        //In a row
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i, j + 1)) {
                    if (gameBoard[i][j] == gameBoard[i][j + 1] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }

        //In a column
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i - 1, j)) {
                    if (gameBoard[i][j] == gameBoard[i - 1][j] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }

        //По диагонали
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i + 1, j + 1)) {
                    if (gameBoard[i][j] == gameBoard[i + 1][j + 1] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }

        //По диагонали
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i - 1, j + 1)) {
                    if (gameBoard[i][j] == gameBoard[i - 1][j + 1] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }
        return times;
    }

}