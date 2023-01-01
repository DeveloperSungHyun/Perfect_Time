package com.example.perfect_time.Activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.perfect_time.MainActivity;
import com.example.perfect_time.R;

public class PopupView extends Activity {

    Intent intent;

    TextView TextView_Name, TextView_Memo;
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

        TextView_Name = findViewById(R.id.TextView_Name);
        TextView_Memo = findViewById(R.id.TextView_Memo);

        check_ok = findViewById(R.id.check_ok);
        delete = findViewById(R.id.delete);

        intent = getIntent();

        TextView_Name.setText(intent.getStringExtra("name"));
        TextView_Memo.setText(intent.getStringExtra("memo"));

        check_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();

                finish();
            }
        });

    }
}
