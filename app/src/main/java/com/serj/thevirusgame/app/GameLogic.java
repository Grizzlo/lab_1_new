package com.serj.thevirusgame.app;

// Клас, що відповідає за алгоритми перевірки виконання правил гри
public class GameLogic {

    /* Метод, що відповідає за перевірку можливості ходу в клітинку, в яку було здійснено тап.
    Якщо хід можливий, повертається true. Який саме хід здійснюється, перевіряється поряд з викликом методу.
    Сама суть такого підходу трохи кривувата.
    */
    public boolean checkMove(int x, int y, GameField field, char value, char lastSide){

        int firstNeighbor;
        int secondNeighbor;

        if (value == 'r'){
            firstNeighbor = 1;
            secondNeighbor = 2;
        } else { //value == 'b'
            firstNeighbor = -1;
            secondNeighbor = -2;
        }

        int eTop;
        int eRight;
        int eBottom;
        int eLeft;

        if (x == 0)
            eLeft = 0;
        else
            eLeft = field.getElement(x - 1, y);
        if (x == field.getN()-1)
            eRight = 0;
        else
            eRight = field.getElement(x + 1, y);
        if (y == 0)
            eTop = 0;
        else
            eTop = field.getElement(x, y - 1);
        if (y == field.getM()-1)
            eBottom = 0;
        else
            eBottom = field.getElement(x, y + 1);

        if ((eTop == firstNeighbor) || (eRight == firstNeighbor) ||
                (eBottom == firstNeighbor) || (eLeft == firstNeighbor))
            return true;


        if (lastSide != 't'){
            if (eTop == secondNeighbor){

                field.setCellChecked(x, y, true);
                if (field.getCellChecked(x, y - 1))
                    return false;
                if (checkMove(x, y - 1, field, value, 'b'))
                    return true;
            }
        }

        if (lastSide != 'r'){
            if (eRight == secondNeighbor) {
                field.setCellChecked(x, y, true);
                if (field.getCellChecked(x + 1, y))
                    return false;
                if (checkMove(x + 1, y, field, value, 'l'))
                    return true;
            }
        }

        if (lastSide != 'b'){
            if (eBottom == secondNeighbor) {
                field.setCellChecked(x, y, true);
                if (field.getCellChecked(x, y + 1))
                    return false;
                if (checkMove(x, y + 1, field, value, 't'))
                    return true;
            }
        }

        if (lastSide != 'l'){
            if (eLeft == secondNeighbor){
                field.setCellChecked(x, y, true);
                if (field.getCellChecked(x - 1, y))
                    return false;
                if (checkMove(x - 1, y, field, value, 'r'))
                    return true;
            }
        }

        return false;
    }
}
