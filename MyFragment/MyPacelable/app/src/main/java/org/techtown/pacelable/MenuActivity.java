package org.techtown.pacelable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent=getIntent();
        processIntent(intent);
    }
    public void processIntent(Intent intent){
        if(intent!=null){
            Bundle bundel=intent.getExtras();
            SimpleData data = bundel.getParcelable("data");
            if(data!=null){
                Toast.makeText(this,"전달받은 객체:"+data.code+","+data.message,Toast.LENGTH_LONG).show();
            }
        }
    }
}