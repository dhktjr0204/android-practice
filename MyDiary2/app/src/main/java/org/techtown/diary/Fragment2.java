package org.techtown.diary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.github.channguyen.rsv.RangeSliderView;

import java.io.File;
import java.util.Date;

public class Fragment2 extends Fragment {
    private static final String TAG = "Fragment2";


    Context context;
    onTabItemSelectedListener listener;
    OnRequestListener requestListener;

    ImageView weatherIcon;
    TextView dateTextView;
    TextView locationTextView;

    EditText contentsInput;
    ImageView pictureImageView;

    boolean isPhotoCaptured;
    boolean isPhotoFileSaved;
    boolean isPhotoCanceled;

    int selectedPhotoMenu;

    File file;
    Bitmap resultPhotoBitmap;


    int mMode = AppConstants.MODE_INSERT;

    int _id = -1;

    int weatherIndex = 0;

    RangeSliderView moodSlider;
    int moodIndex = 2;

    Note item;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context=context;

        if(context instanceof onTabItemSelectedListener){
            listener=(onTabItemSelectedListener) context;
        }

        if(context instanceof OnRequestListener){
            requestListener=(OnRequestListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(context!=null){
            context=null;
            listener=null;
            requestListener=null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup) inflater.inflate(R.layout.fragment2,container,false);
        initUI(rootView);

        if(requestListener!=null){
            requestListener.onRequest("getCurrentLocation");
        }

        return rootView;
    }

    private void initUI(ViewGroup rootView){
        weatherIcon=rootView.findViewById(R.id.weatherIcon);
        dateTextView=rootView.findViewById(R.id.dateTextView);
        locationTextView=rootView.findViewById(R.id.locationTextView);

        contentsInput=rootView.findViewById(R.id.contentsInput);
        pictureImageView=rootView.findViewById(R.id.pictureImageView);
        pictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPhotoCaptured || isPhotoFileSaved) {
                    showDialog(AppConstants.CONTENT_PHOTO_EX);
                }else{
                    showDialog(AppConstants.CONTENT_PHOTO);
                }
            }
        });


        Button saveButton=rootView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(listener!=null){
                 listener.onTabSelected(0);
             }
            }
        });

        Button deleteButton=rootView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.onTabSelected(0);
                }
            }
        });

        Button closeButton=rootView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.onTabSelected(0);
                }
            }
        });
        RangeSliderView sliderView = rootView.findViewById(R.id.sliderView);
        final RangeSliderView.OnSlideListener listener = new RangeSliderView.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                AppConstants.println("moodIndex changed to " + index);
                moodIndex = index;
            }
        };

        sliderView.setOnSlideListener(listener);
        sliderView.setInitialIndex(2);
    }

    public void showDialog(int id){
        AlertDialog.Builder builder=null;

        switch (id){
            case AppConstants.CONTENT_PHOTO:
                builder=new AlertDialog.Builder(context);

                builder.setTitle("사진 메뉴 선택");
                builder.setSingleChoiceItems(R.array.array_photo, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int whichButton) {
                        selectedPhotoMenu=whichButton;
                    }
                });
                builder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int whichButton) {
                        if(selectedPhotoMenu==0){
                            showPhotoCaptureActivity();
                        }
                        else if(selectedPhotoMenu==1){
                            showPhotoSelectionActivity();
                        }
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                break;

                case AppConstants.CONTENT_PHOTO_EX:
                    builder=new AlertDialog.Builder(context);
                    builder.setTitle("사진 메뉴 선택");
                    builder.setSingleChoiceItems(R.array.array_photo_ex, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int whichButton) {
                            selectedPhotoMenu=whichButton;
                        }
                    });
                    builder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(selectedPhotoMenu==0){
                                showPhotoCaptureActivity();
                            }
                            else if(selectedPhotoMenu==1){
                                showPhotoSelectionActivity();
                            }else if(selectedPhotoMenu==2){
                                isPhotoCanceled=true;
                                isPhotoCaptured=false;

                                pictureImageView.setImageResource(R.drawable.picture1);
                            }
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    break;
            default:
                break;
        }
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    public void showPhotoCaptureActivity() {
        if (file == null) {
            file = createFile();
        }

        Uri fileUri = FileProvider.getUriForFile(context,"org.techtown.diary.fileprovider", file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            //설정에 맞는 액티비티를 찾고, 어떤 액티비티를 실행할 지 결정해줌
            startActivityForResult(intent, AppConstants.REQ_PHOTO_CAPTURE);
        }
    }

    public File createFile() {
        String filename="capture.jpg";
        File storageDir= Environment.getExternalStorageDirectory();
        File outFile=new File(storageDir,filename);

        return outFile;
    }

    public void showPhotoSelectionActivity(){
        Intent intent=new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//인텐트로 이미지 전달
        startActivityForResult(intent,AppConstants.REQ_PHOTO_SELECTION);
    }
//다른 액티비티로부터의 응답 처리
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);

        if(intent!=null){
            switch (requestCode){
                case AppConstants.REQ_PHOTO_CAPTURE://사진찍는 경우
                    Log.d(TAG,"onActivityResult() for REQ_PHOTO_CAPTURE");
                    Log.d(TAG,"resultCode:"+resultCode);

                    resultPhotoBitmap=decodeSampledBitmapFromResource(file,pictureImageView.getWidth(),pictureImageView.getHeight());
                    pictureImageView.setImageBitmap(resultPhotoBitmap);
                    break;
                case AppConstants.REQ_PHOTO_SELECTION://사진선택하는 경우
                    Log.d(TAG,"onActivityResult() for REQ_PHOTO_SELECTION");
                    Uri selectedImage=intent.getData();
                    String[] filePathColumn={MediaStore.Images.Media.DATA};

                    //커서는 데이터베이스에 저장되어있는 테이블의 행을 참조하여 데이터를 가져오는 역할을 함
                    //getContentResolver는 데이터 접근, query는 데이터를 쿼리함
                    //첫번째 입력칸 uri데이터를 가져오기 위한 정해진 주소,두번째 가져올 컬럼 이름 목록
                    Cursor cursor=context.getContentResolver().query(selectedImage,filePathColumn,null,null,null);

                    //제일 첫 행으로 이동동
                   cursor.moveToFirst();

                    int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
                    String filePath=cursor.getString(columnIndex);
                    cursor.close();

                    resultPhotoBitmap=decodeSampledBitmapFromResource(file,pictureImageView.getWidth(),pictureImageView.getHeight());
                    pictureImageView.setImageBitmap(resultPhotoBitmap);
                    isPhotoCaptured=true;
                    break;
            }
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(File res, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(res.getAbsolutePath(),options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(res.getAbsolutePath(),options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height;
            final int halfWidth = width;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    private String createFilename() {
        Date curDate = new Date();
        String curDateStr = String.valueOf(curDate.getTime());

        return curDateStr;
    }

    public void setWeather(String data) {
        if (data != null) {
            if (data.equals("맑음")) {
                weatherIcon.setImageResource(R.drawable.weather_1);
                weatherIndex = 0;
            } else if (data.equals("구름 조금")) {
                weatherIcon.setImageResource(R.drawable.weather_2);
                weatherIndex = 1;
            } else if (data.equals("구름 많음")) {
                weatherIcon.setImageResource(R.drawable.weather_3);
                weatherIndex = 2;
            } else if (data.equals("흐림")) {
                weatherIcon.setImageResource(R.drawable.weather_4);
                weatherIndex = 3;
            } else if (data.equals("비")) {
                weatherIcon.setImageResource(R.drawable.weather_5);
                weatherIndex = 4;
            } else if (data.equals("눈/비")) {
                weatherIcon.setImageResource(R.drawable.weather_6);
                weatherIndex = 5;
            } else if (data.equals("눈")) {
                weatherIcon.setImageResource(R.drawable.weather_7);
                weatherIndex = 6;
            } else {
                Log.d("Fragment2", "Unknown weather string : " + data);
            }
        }
    }

    public void setAddress(String data){
        locationTextView.setText(data);
    }

    public void setDataString(String dateString){
        dateTextView.setText(dateString);
    }
}
