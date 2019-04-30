package ru.af3412;

class GamePlay {
    int row;
    int col;
    private int value;      //Значиние функции веса

    GamePlay() {
        row = -1;
        col = -1;
        value = 0;
    }

    //Ход
    GamePlay moveDone(int row, int col) {
        GamePlay moveDone = new GamePlay();
        moveDone.row = row;
        moveDone.col = col;
        moveDone.value = -1;
        return moveDone;
    }

    //Ход для расширения
    GamePlay possibleMove(int row, int col, int value) {
        GamePlay posisibleMove = new GamePlay();
        posisibleMove.row = row;
        posisibleMove.col = col;
        posisibleMove.value = value;
        return posisibleMove;
    }

    //Перемещение, используется для минмакса
    GamePlay moveToCompare(int value) {
        GamePlay moveToCompare = new GamePlay();
        moveToCompare.row = -1;
        moveToCompare.col = -1;
        moveToCompare.value = value;
        return moveToCompare;
    }

    int getValue() {
        return value;
    }

    void setRow(int aRow) {
        row = aRow;
    }

    void setCol(int aCol) {
        col = aCol;
    }

    void setValue(int aValue) {
        value = aValue;
    }
}

