package org.techtown.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;

    static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.editTextTextPersonName);
        textView=findViewById(R.id.textView2);

        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlStr=editText.getText().toString();
                request(urlStr);
            }
        });
        requestQueue= Volley.newRequestQueue(getApplicationContext());
    }
    public void request(String urlStr){

        StringRequest request=new StringRequest(
                Request.Method.GET,
                urlStr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        println("응답->"+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("에러->"+error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();

                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
        println("요청 보냄");

    }
    public void println(String data){
        textView.append(data+"\n");
    }
}