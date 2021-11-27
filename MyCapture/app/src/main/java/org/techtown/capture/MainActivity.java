package org.techtown.capture;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;

public class MainActivity extends AppCompatActivity{

    File file;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView=findViewById(R.id.imageView);

        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
    }

    public void takePicture(){
        if(file==null){
            file=createFile();
        }
        Uri fileUri= FileProvider.getUriForFile(this,"org.techtown.captureintent.fileprovider",file);
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent,101);//버튼을 눌렀을때 인텐트를 이용해서 사진을 띄워줌
        }

    }
    public File createFile(){
        String filename="capture.jpg";
        File storageDir= Environment.getExternalStorageDirectory();
        File outfile=new File(storageDir,filename);
        return outfile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101&&resultCode==RESULT_OK){
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize=6;
            Bitmap bitmap=BitmapFactory.decodeFile(file.getAbsolutePath(),options);

            imageView.setImageBitmap(bitmap);
        }
    }
}