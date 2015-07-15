package com.serj.thevirusgame.app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;

// Клас, що відповідає за графічне відображення процесу гри. Завжди наслідується від View.
// Потрібно переробити більш оптимально домальовування ходу.
public class Draw extends View {

    //public TextView move_value;

    // Товщина лінії контурів поля
    final int LINE_WIDTH = 1;
    // Задання ширини та висоти поля
    // Слід зробити можливість зміни цих параметрів гравцем
    float pg_width = 15;
    float pg_height = 10;

    // Координати місця тапу
    float touchX = 0;
    float touchY = 0;

    // Створення сомого ігрового поля з заданими параметрами
    GameField field = new GameField((int)pg_height, (int)pg_width);
    static int move = 1;

    // Перевизначені конструктори за для витравлення помилки. Без них "печаль"
    public Draw(Context context) {
        super(context);
    }
    public Draw(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public Draw(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // Метод, що малює все на полі. Основний метод класу.
    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);

        float screen_width = c.getWidth();
        float screen_height = c.getHeight();

        // робимо розміщення поля більш раціональним
        if(pg_height > pg_width){
            float temp = pg_width;
            pg_width = pg_height;
            pg_height = temp;
        }

        float ratio_pg = pg_height/pg_width;
        float ratio_s = screen_height/screen_width;
        float cell;
        float corner_x, corner_y;

        // поля з боків
        // або згори та знизу
        if ((ratio_pg >= 1 && ratio_s <= 1)||
                (ratio_pg < 1 && ratio_s < 1 && ratio_pg > ratio_s)||
                (ratio_pg > 1 && ratio_s > 1 && ratio_pg < ratio_s)) {
            cell = (screen_height-1)/pg_height; // -1 для зсуву поля, який забезпечить коректне відображення границь
            corner_x = (c.getWidth() - cell*pg_width)/2;
            corner_y = 0;
        } else {
            cell = (screen_width-1)/pg_width;
            corner_x = 0;
            corner_y = (c.getHeight() - cell*pg_height)/2;
        }

        //Визначення координати клітинки в яку був дотик
        int cordX = (int)((touchX - corner_x)/cell);
        int cordY = (int)((touchY - corner_y)/cell);

        //move_value = (TextView) findViewById(R.id.move_value);

        //Запис в матрицю результату дотику
        if (cordX >= 0 && cordY >=0 && cordX < pg_width && cordY < pg_height){
            if (move >=1 && move <= 3){
                //move_value.setText("RED");
                //move_value.setTextColor(Color.RED);
                if (field.setMatrix(cordX,cordY,'r'))
                    ++move;
            } else if (move >=4 && move <= 6){
                //move_value.setText("BLUE");
                //move_value.setTextColor(Color.BLUE);
                if (field.setMatrix(cordX,cordY,'b'))
                    ++move;
                if (move > 6)
                    move = 1;
            }
        }

        //Малюємо поле
        drawField(corner_x, corner_y, cell, c);
        //Малюємо всі заповнені елементи на полі
        drawMove(corner_x, corner_y, cell, c);
        //Якщо дотик був у полі, малюємо поточний вказівник
        if (cordX >= 0 && cordY >=0 && cordX < pg_width && cordY < pg_height)
            drawMarker(corner_x, corner_y, cell, c);
    }

    // Метод, що малює ігрове поле
    public void drawField(float corner_x, float corner_y, float cell, Canvas c){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        float screen_width = c.getWidth();
        float screen_height = c.getHeight();
        // округляємо до цілих чисел для точності позиціювання
        for(int x = 0; x < pg_width + LINE_WIDTH; ++x){
            c.drawLine((int)(corner_x + x*cell), (int)(corner_y), (int)(corner_x + x*cell), (int)(screen_height - corner_y), paint);
        }
        for(int y = 0; y < pg_height + 1; ++y){
            c.drawLine((int)(corner_x), (int)(corner_y + y*cell), (int)(screen_width - corner_x), (int)(corner_y + y*cell), paint);
        }
    }

    // Метод малює стан гри.
    // Слід переписати таким чином, щоб не було потреби перемальовувати все знаново, а просто домальовувати новий хід
    public void drawMove(float corner_x, float corner_y, float cell, Canvas c){
        Rect myRect = new Rect();
        Paint redPaint = new Paint();
        Paint halfRedPaint = new Paint();
        Paint bluePaint = new Paint();
        Paint halfBluePaint = new Paint();
        Paint blackPaint = new Paint();

        redPaint.setColor(Color.argb(255,255,0,0));
        //redPaint.setColor(Color.parseColor("#A60000"));
        redPaint.setStyle(Paint.Style.FILL);
        //halfRedPaint.setColor(Color.rgb(127,0,0));
        halfRedPaint.setColor(Color.parseColor("#FF7373"));
        halfRedPaint.setStyle(Paint.Style.FILL);

        bluePaint.setColor(Color.argb(255,0,0,255));
        //bluePaint.setColor(Color.parseColor("#081272"));
        bluePaint.setStyle(Paint.Style.FILL);
        //halfBluePaint.setColor(Color.rgb(0,0,127));
        halfBluePaint.setColor(Color.parseColor("#717BD8"));
        halfBluePaint.setStyle(Paint.Style.FILL);
        blackPaint.setColor(Color.argb(255,0,0,0));
        blackPaint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < pg_height; ++i){
            for(int j = 0; j < pg_width; ++j){
                //
                System.out.print(field.getElement(j,i));
                int x1 = (int)(corner_x + j*cell)+2*LINE_WIDTH;
                int y1 = (int)(corner_y + i*cell)+2*LINE_WIDTH;
                int x2 = (int)(corner_x + j*cell + cell)-LINE_WIDTH;
                int y2 = (int)(corner_y + i*cell + cell)-LINE_WIDTH;
                myRect.set(x1, y1, x2, y2);
                if (field.getElement(j,i) == 1){
                    c.drawRect(myRect, halfRedPaint);
                } else if(field.getElement(j,i) == 2){
                    c.drawRect(myRect, redPaint);
                } else if (field.getElement(j,i) == -1){
                    c.drawRect(myRect, halfBluePaint);
                } else if(field.getElement(j,i) == -2){
                    c.drawRect(myRect, bluePaint);
                } else if(field.getElement(j,i) == 100){
                    c.drawRect(myRect, blackPaint);
                }
            }
        }
        //
        System.out.print("/n");


    }

    // Метод, що малює маркер клітинки в яку здійснено дотик
    public void drawMarker(float corner_x, float corner_y, float cell, Canvas c){
        float roundX = touchX - (touchX - corner_x)%cell;
        float roundY = touchY - (touchY - corner_y)%cell;

        float x = (roundX + cell/2);
        float y = (roundY + cell/2);
        float r = (cell/2)-LINE_WIDTH;

        Paint greenPaint = new Paint();
        greenPaint.setColor(Color.GREEN);
        greenPaint.setStyle(Paint.Style.STROKE);
        greenPaint.setAntiAlias(true);
        c.drawCircle(x, y, r, greenPaint);
        c.drawCircle(x, y, r-1, greenPaint);
        c.drawCircle(x, y, r-2, greenPaint);
    }

    // Зчитування дотику і виклик його опрацювання
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            touchX = event.getX();
            touchY = event.getY();
            //invalidate() викликає onDraw(), тобто запускає перемалювання
            invalidate();
        }
        return true;
    }
}
