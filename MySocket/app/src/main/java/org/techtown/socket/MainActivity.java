package org.techtown.socket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    EditText input1;
    TextView output1;
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input1=findViewById(R.id.input1);
        output1=findViewById(R.id.output1);
        Button button=findViewById(R.id.sendbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String data=input1.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        send(data);
                    }
                }).start();
            }
        });
        Button startServerButton=findViewById(R.id.strartServerButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       startServer();
                   }
               }).start();
            }
        });

    }

    public void startServer(){
        int port=5001;
        try {
            ServerSocket server = new ServerSocket(port);

            while(true){
                Socket socket=server.accept();
                InetAddress clientHost=socket.getLocalAddress();
                int clientPort= socket.getPort();
                println("클라이언트 연결됨"+clientHost+", "+clientPort);

                ObjectInputStream instream=new ObjectInputStream(socket.getInputStream());
                String input=(String) instream.readObject();
                println("데이터받음:"+input);
                ObjectOutputStream outstream=new ObjectOutputStream(socket.getOutputStream());
                outstream.writeObject(input+"from server");
                outstream.flush();
                println("데이터보냄");
                socket.close();
            }
        }catch (Exception e){e.printStackTrace();}

    }
    public void println(final String data){
        handler.post(new Runnable() {
            @Override
            public void run() {
                output1.append(data+"\n");
            }
        });

    }
    public void send(String data){
        int port=5001;
        try {
            Socket sock=new Socket("localhost",port);

            ObjectOutputStream outstream=new ObjectOutputStream(sock.getOutputStream());
            outstream.writeObject(data);
            outstream.flush();//버퍼 비우기

            ObjectInputStream instream=new ObjectInputStream(sock.getInputStream());
            String  input=(String) instream.readObject();
            sock.close();

            //objectOutputStream은 글자를 바이트로 바꿔주는 역할을 한다.
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}