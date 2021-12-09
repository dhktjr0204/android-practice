package org.techtown.audiorecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    MediaRecorder recorder;
    MediaPlayer player;
    String filename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecording();
            }
        });

        Button button2=findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
            }
        });

        Button button3=findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAudio();
            }
        });

        Button button4=findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAudio();

            }
        });

        String sdcard= Environment.getExternalStorageDirectory().getAbsolutePath();
        filename=sdcard+ File.separator+"recorded.mp4";
    }
    public void startRecording(){
        if(recorder!=null) {
            recorder = new MediaRecorder();
        }
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(filename);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stopRecording(){
        if(recorder!=null){
            return;
        }

        recorder.stop();
        recorder.release();
        recorder=null;


    }

    public void playAudio(){
        killplayer();
        try {
            player = new MediaPlayer();
            player.setDataSource("file//" + filename);
            player.prepare();
            player.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stopAudio(){
            if(player!=null){
                player.stop();
            }
    }

    public void killplayer(){
        try{
            if(player!=null)
                player.release();
        }catch (Exception e){e.printStackTrace();}
    }
}