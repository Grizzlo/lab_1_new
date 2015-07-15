package com.serj.thevirusgame.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

// Клас, що відповідає activity з головним меню
public class MainActivity extends Activity {

    // Прив'язка до xml документа
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //не відображаємо заголовок та строку стану
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main_activity);
    }

    // Метод викликається при натисненні кнопки початку гри для переходу на ігрове поле
    public void goToPlayground(View v){
        Intent toPlayground = new Intent(this,Playground.class);
        startActivity(toPlayground);

    }
}
