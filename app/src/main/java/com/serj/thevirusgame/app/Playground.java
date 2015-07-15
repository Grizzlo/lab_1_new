package com.serj.thevirusgame.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

// Клас, що відповідає activity з ігровим полем
public class Playground extends Activity {

    // Прив'язка до xml документа
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //не відображаємо заголовок та строку стану
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.playground);
        //setContentView(new Draw(this));
    }

    // Метод викликається при натисненні кнопки переходу в головне меню
    public void goToMenu(View v){
        Intent toMenu = new Intent(this,MainActivity.class);
        startActivity(toMenu);
    }

}
