package ru.af3412;

import java.util.LinkedList;
import java.util.Random;


class MinMax {
    private int maxDepth;
    private int computerLetter;

    MinMax(int thePlayerLetter, int maxDepth) {
        this.maxDepth = maxDepth;
        this.computerLetter = thePlayerLetter;
    }

    GamePlay getNextMove(State board) {
        //Выбираем минимум рекурсивных обходов, чтобы получить максимальную функции.
        return max(board.boardWithExpansion(board), 0);
    }

    //Методы min и max вызываются поочередно, пока не будет достигнута глубина или игра не будет завершена
    private GamePlay min(State board, int depth) { //MIN plays 'X' (user)
        Random r = new Random();
        //Если MIN вызывается в состоянии, которое является конечным или после достижения максимальной глубины, то эвристика вычисляется для состояния и возвращается ход.
        if ((board.checkGameOver()) || (depth == maxDepth)) {
            GamePlay baseMove = new GamePlay();
            baseMove = baseMove.possibleMove(board.lastMove.row, board.lastMove.col, board.utilityFunction());
            return baseMove;
        } else {
            //Расширенный расчет ходов потомков
            LinkedList<State> children = new LinkedList<>(board.getChildren(State.X));
            GamePlay minMove = new GamePlay();
            minMove = minMove.moveToCompare(Integer.MAX_VALUE);
            for (State child : children) {
                //Для каждого потомка расчитывается Max следующей глубины
                GamePlay move = max(child, depth + 1);
                //Выбирается потомок с наименьшим значением
                if (move.getValue() <= minMove.getValue()) {
                    if ((move.getValue() == minMove.getValue())) {
                        //Если есть одинаковые значения, то выбираем случайный
                        if (r.nextInt(2) == 0) { //Если 0, перезаписываем min
                            minMove.setRow(child.lastMove.row);
                            minMove.setCol(child.lastMove.col);
                            minMove.setValue(move.getValue());
                        }
                    } else {
                        minMove.setRow(child.lastMove.row);
                        minMove.setCol(child.lastMove.col);
                        minMove.setValue(move.getValue());
                    }
                }
            }
            return minMove;
        }
    }

    //Методы min и max вызываются поочередно, пока не будет достигнута глубина или игра не будет завершена
    private GamePlay max(State board, int depth) { //MAX plays 'O'
        Random r = new Random();
        //Если MAX вызывается в состоянии, которое является конечным или после достижения максимальной глубины, то эвристика вычисляется для состояния и возвращается ход.
        if ((board.checkGameOver()) || (depth == maxDepth)) {
            GamePlay baseMove = new GamePlay();
            baseMove = baseMove.possibleMove(board.lastMove.row, board.lastMove.col, board.utilityFunction());
            return baseMove;
        } else {
            LinkedList<State> children = new LinkedList<>(board.getChildren(computerLetter));
            GamePlay maxMove = new GamePlay();
            maxMove = maxMove.moveToCompare(Integer.MIN_VALUE);
            for (State child : children) {
                GamePlay move = min(child, depth + 1);
                //Различие с min. Выбираем наибольшее значение
                if (move.getValue() >= maxMove.getValue()) {
                    if ((move.getValue() == maxMove.getValue())) {
                        if (r.nextInt(2) == 0) {
                            maxMove.setRow(child.lastMove.row);
                            maxMove.setCol(child.lastMove.col);
                            maxMove.setValue(move.getValue());
                        }
                    } else {
                        maxMove.setRow(child.lastMove.row);
                        maxMove.setCol(child.lastMove.col);
                        maxMove.setValue(move.getValue());
                    }
                }
            }
            return maxMove;
        }
    }
}

