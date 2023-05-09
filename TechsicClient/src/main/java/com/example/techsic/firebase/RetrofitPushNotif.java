package com.example.techsic.firebase;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitPushNotif {
    private static Retrofit retrofit_pushNotif;
    public static Retrofit getRetrofit(){
        if(retrofit_pushNotif ==null){
            retrofit_pushNotif = new Retrofit.Builder()
                    .baseUrl("https://fcm.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return retrofit_pushNotif;
    }
    public static String IDnguoinhan = "1";

    public static final String guiID = "guiID";
    public static final String nhanID = "nhanID";
    public static final String tinnhan = "tinnhan";
    public static final String thoigian = "thoigian";
    public static final String PATH_CHAT = "chat";

}
