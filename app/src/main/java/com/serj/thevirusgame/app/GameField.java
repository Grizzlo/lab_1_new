package com.serj.thevirusgame.app;

import java.util.Random;

// Клас, що відповідає за данні ігрового поля
public class GameField {

    // Матриця клітинок поляddnlqwkndlkwkqndlwq
    static private Cell matrix[][];

    // Розміри поля (висота та ширина)
    int fieldM;
    int fieldN;

    // Конструктор, що створює M*N клітинок, генерує перешкоди та перші клітинки гравців
    public GameField(int M, int N){

        matrix = new Cell[M][N];

        for (int y = 0; y < M; ++y){
            for (int x = 0; x < N; ++x){
                matrix[y][x] = new Cell();
            }
        }
        generateWalls(M, N);
        generateStarts(M, N);

        fieldM = M;
        fieldN = N;
    }

    /* Метод, що в залежності від того чий хід (value == 'r' - червоних, 'b' - синіх) здійснює запис значення
    клітинки, якщо туди можливо походити
       0 пуста клітинка
       1, 2 "жива" та "нежива" червона клітинка
       -1, -2 "жива" та "нежива" синя клітинка
       100 клітинка із перепоною
    */
    public boolean setMatrix(int x, int y, char value){

        int firstCaseToMove = 0;
        int secondCaseToMove;
        int toFirstLevel;
        int toSecondLevel;

        if (value == 'r'){
            secondCaseToMove = -1;
            toFirstLevel = 1;
            toSecondLevel = 2;
        } else { //value == 'b'
            secondCaseToMove = 1;
            toFirstLevel = -1;
            toSecondLevel = -2;
        }

        // Можливо, варто зробити оголошення глобальним, щоб надалі можна було використовувати у різних методах
        GameLogic logic = new GameLogic();

        // Перевірка, ставити живу чи мертву клітинку
        if (matrix[y][x].getCondition() == firstCaseToMove){
            clearCheck();
            if (logic.checkMove(x, y, this, value, 'n'))
                matrix[y][x].setCondition(toFirstLevel);
            else
                return false;
        } else if (matrix[y][x].getCondition() == secondCaseToMove){
            clearCheck();
            if (logic.checkMove(x, y, this, value, 'n'))
                matrix[y][x].setCondition(toSecondLevel);
            else
                return false;
        }
        else
            return false;

        return true;
    }

    // Отримання значення вмісту клітинки
    public Integer getElement(int x, int y){
        return matrix[y][x].getCondition();
    }

    // Генерація стін (займають від 0% до 10% ігрового поля)
    // Варто вдосконалити, за для більш логічної генерації, щоб не заважало першому ходу і т. п.
    private void generateWalls(int M, int N){
        Random rand = new Random();

        int randK = rand.nextInt((M*N/10));
        for (int i = 0; i < randK; ++i){
            int randM = rand.nextInt(M);
            int randN = rand.nextInt(N);
            //matrix[randM].set(randN, 100);
            matrix[randM][randN].setCondition(100);
        }

    }

    // Рендомний вибір кутка розміщення клітинки старту гравців
    // 1 - верхній лівий, далі за годинниковою
    private void generateStarts(int M, int N){
        Random rand = new Random();

        int randRedCorner = rand.nextInt(3)+1;
        int randBlueCorner = rand.nextInt(3)+1;
        while (randRedCorner == randBlueCorner){
            randBlueCorner = rand.nextInt(3)+1;
        }

        if (randRedCorner == 1)
            matrix[0][0].setCondition(1);
        else if (randRedCorner == 2)
            matrix[0][N-1].setCondition(1);
        else if (randRedCorner == 3)
            matrix[M-1][0].setCondition(1);
        else if (randRedCorner == 4)
            matrix[M-1][N-1].setCondition(1);

        if (randBlueCorner == 1)
            matrix[0][0].setCondition(-1);
        else if (randBlueCorner == 2)
            matrix[0][N-1].setCondition(-1);
        else if (randBlueCorner == 3)
            matrix[M-1][0].setCondition(-1);
        else if (randBlueCorner == 4)
            matrix[M-1][N-1].setCondition(-1);
    }

    // Отримання висоти поля
    public int getM(){
        return fieldM;
    }

    // Отримання ширини поля
    public int getN(){
        return fieldN;
    }

    // Зміна стану клітинки на "перевірено" чи "неперевірено"
    public void setCellChecked(int x, int y, boolean value){
        matrix[y][x].setWasChecked(value);
    }

    // Отримання стану клітинки (чи перевірена)
    public boolean getCellChecked(int x, int y){
        return matrix[y][x].getWasChecked();
    }

    // Задання всім клітинкам поля стану "неперевірено" після здійснення ходу
    private void clearCheck(){
        for (int y = 0; y < fieldM; ++y){
            for (int x = 0; x < fieldN; ++x){
                matrix[y][x].setWasChecked(false);
            }
        }
    }
}
