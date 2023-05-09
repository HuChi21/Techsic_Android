package com.example.techsic.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techsic.R;
import com.example.techsic.adapters.ChatAdapter;
import com.example.techsic.firebase.ChatModel;
import com.example.techsic.firebase.RetrofitPushNotif;
import com.example.techsic.retrofit.RetrofitUtilities;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private TextView btnGui;
    private EditText edtMessage;
    private Toolbar toolbar;
    FirebaseFirestore db;
    private List<ChatModel> chatList = new ArrayList<>();
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        db =FirebaseFirestore.getInstance();
        getWidget();
        actionToolbar();
        listenMessage();
        themTaiKhoanChat();

    }
    private void getWidget() {
        toolbar = findViewById(R.id.chatToolbar);
        edtMessage = findViewById(R.id.edtMessage);
        btnGui = findViewById(R.id.btnGui);

        btnGui.setOnClickListener(this);

        recyclerView = findViewById(R.id.chatRecyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        chatAdapter = new ChatAdapter(getApplicationContext(),chatList,String.valueOf(RetrofitUtilities.taiKhoanGanDay.getIdtaikhoan()));
        recyclerView.setAdapter(chatAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGui:
                pushMessageInFirebase();
                break;
        }
    }

    private void listenMessage(){
        db.collection(RetrofitPushNotif.PATH_CHAT)
                .whereEqualTo(RetrofitPushNotif.guiID,String.valueOf(RetrofitUtilities.taiKhoanGanDay.getIdtaikhoan()))
                .whereEqualTo(RetrofitPushNotif.nhanID,RetrofitPushNotif.IDnguoinhan)
                .addSnapshotListener(eventListener);

        db.collection(RetrofitPushNotif.PATH_CHAT)
                .whereEqualTo(RetrofitPushNotif.guiID,RetrofitPushNotif.IDnguoinhan)
                .whereEqualTo(RetrofitPushNotif.nhanID,String.valueOf(RetrofitUtilities.taiKhoanGanDay.getIdtaikhoan()))
                .addSnapshotListener(eventListener);
    }


    private final EventListener<QuerySnapshot> eventListener = (value,error)->{
        if(error!=null){
            return;
        }
        if(value != null){
            int count = chatList.size();
            for(DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatModel chat = new ChatModel();
                    chat.guiID = documentChange.getDocument().getString(RetrofitPushNotif.guiID);
                    chat.nhanID = documentChange.getDocument().getString(RetrofitPushNotif.nhanID);
                    chat.tinnhan = documentChange.getDocument().getString(RetrofitPushNotif.tinnhan);
                    chat.thoigianObj = documentChange.getDocument().getDate(RetrofitPushNotif.thoigian);
                    chat.thoigian = FormatDatetime(documentChange.getDocument().getDate(RetrofitPushNotif.thoigian));
                    chatList.add(chat);
                }
            }
            Collections.sort(chatList,(obj1,obj2)->obj1.thoigianObj.compareTo(obj2.thoigianObj));
            if(count == 0){
                chatAdapter.notifyDataSetChanged();
            }else{
                chatAdapter.notifyItemRangeInserted(chatList.size(),chatList.size());
                recyclerView.smoothScrollToPosition(chatList.size()-1);
            }
        }
    };

    private String FormatDatetime(Date date){
        return new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault()).format(date);
    }
    private void pushMessageInFirebase() {
        String messageString = edtMessage.getText().toString().trim();
        if(!TextUtils.isEmpty(messageString)){
            HashMap<String,Object> message = new HashMap<>();
            message.put(RetrofitPushNotif.guiID,String.valueOf(RetrofitUtilities.taiKhoanGanDay.getIdtaikhoan()));
            message.put(RetrofitPushNotif.nhanID,String.valueOf(RetrofitPushNotif.IDnguoinhan));
            message.put(RetrofitPushNotif.tinnhan,messageString);
            message.put(RetrofitPushNotif.thoigian,new Date());
            db.collection(RetrofitPushNotif.PATH_CHAT).add(message);
            edtMessage.setText("");
        }
    }

    private void themTaiKhoanChat() {
        HashMap<String, Object> taikhoan = new HashMap<>();
        taikhoan.put("id",RetrofitUtilities.taiKhoanGanDay.getIdtaikhoan());
        taikhoan.put("email",RetrofitUtilities.taiKhoanGanDay.getEmail());
        taikhoan.put("hoten",RetrofitUtilities.taiKhoanGanDay.getHoten());
        taikhoan.put("hinhanh",RetrofitUtilities.taiKhoanGanDay.getHinhanh());
        db.collection("taikhoans").document(String.valueOf(RetrofitUtilities.taiKhoanGanDay.getIdtaikhoan())).set(taikhoan);

    }
    private void actionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle(null);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}