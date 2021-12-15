package org.techtown.lifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.editText);
        Log.d("Main","OnCreate호출됨");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Main","onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Main","onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
        Log.d("Main","onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadState();
        Log.d("Main","onResume");
    }

    public void saveState(){
        SharedPreferences pref=getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString("name",editText.getText().toString());
        editor.commit();
    }
    public void loadState(){
        SharedPreferences pref=getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if(pref!=null){
            String name = pref.getString("name", "");
            editText.setText(name);
        }
    }
}