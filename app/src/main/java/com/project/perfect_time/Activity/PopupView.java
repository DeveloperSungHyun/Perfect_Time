package com.project.perfect_time.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.perfect_time.R;
import com.project.perfect_time.Service.ForeGround_Service;

public class PopupView extends Activity {

    Intent intent;

    TextView TextView_Name_popup, TextView_Memo_popup;
    TextView check_ok, delete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;

        layoutParams.dimAmount = 0.7f;

        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.popup_view);

        TextView_Name_popup = findViewById(R.id.TextView_Name_popup);
        TextView_Memo_popup = findViewById(R.id.TextView_Memo_popup);

        check_ok = findViewById(R.id.check_ok);
        delete = findViewById(R.id.delete);

        intent = getIntent();

        TextView_Name_popup.setText(intent.getStringExtra("name"));
        TextView_Memo_popup.setText(intent.getStringExtra("memo"));

        check_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForeGroundService_Off();

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForeGroundService_Off();
                finish();
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);//화면터치 막기


        //WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

    }

    void ForeGroundService_Off(){
        Intent foreground_intent = new Intent(getApplicationContext(), ForeGround_Service.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
//            context.startForegroundService(foreground_intent);
            getApplicationContext().stopService(foreground_intent);
        }else{
//            context.startService(foreground_intent);
            getApplicationContext().stopService(foreground_intent);
        }

        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);//화면터치 막기 해제
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ForeGroundService_Off();

        Log.d("popup", "==================");
    }
}
