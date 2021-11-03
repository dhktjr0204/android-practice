package org.techtown.pacelable;
import android.os.Parcel;
import android.os.Parcelable;

public class SimpleData implements Parcelable{

    int code;
    String message;

    public SimpleData(int code, String message){
        this.code=code;
        this.message=message;
    }

    public SimpleData(Parcel src){
        code=src.readInt();
        message=src.readString();
    }
    public static final Parcelable.Creator CREATOR=new Parcelable.Creator(){
        public SimpleData createFormParcel(Parcel in){
            return new SimpleData(in);
        }
        public SimpleData[] newArray(int size){
            return new SimpleData[size];
        }
    };//Parcelabel이라는 형태로 만들기 위한 코드
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(code);
        dest.writeString(message);
    }
}
