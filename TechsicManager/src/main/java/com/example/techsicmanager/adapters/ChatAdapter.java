package com.example.techsicmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techsicmanager.R;
import com.example.techsicmanager.firebase.ChatModel;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ChatModel> chatModelList;
    private String guiid;

    public ChatAdapter(Context context, List<ChatModel> chatModelList, String guiid) {
        this.context = context;
        this.chatModelList = chatModelList;
        this.guiid = guiid;
    }

    private static final int TYPE_GUI = 1;
    private static final int TYPE_NHAN = 2;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == TYPE_GUI){
            view = LayoutInflater.from(context).inflate(R.layout.item_message_send,parent,false);
            return new SendMessageViewHolder(view);
        }
        else{
            view = LayoutInflater.from(context).inflate(R.layout.item_message_receive,parent,false);
            return new ReceiveMessageViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==TYPE_GUI){
            ((SendMessageViewHolder)holder).txtTinnhan.setText(chatModelList.get(position).tinnhan);
            ((SendMessageViewHolder)holder).txtThoigian.setText(chatModelList.get(position).thoigian);
        }else{
            ((ReceiveMessageViewHolder)holder).txtTinnhan.setText(chatModelList.get(position).tinnhan);
            ((ReceiveMessageViewHolder)holder).txtThoigian.setText(chatModelList.get(position).thoigian);
        }
    }

    @Override
    public int getItemCount() {
        return chatModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatModelList.get(position).guiID.equals(guiid)){
            return TYPE_GUI;
        }
        else{
            return TYPE_NHAN;
        }
    }

    class SendMessageViewHolder extends RecyclerView.ViewHolder{
        TextView txtTinnhan,txtThoigian;
        public SendMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTinnhan = itemView.findViewById(R.id.txtTinnhanSend);
            txtThoigian = itemView.findViewById(R.id.txtThoigianSend);
        }
    }
    class ReceiveMessageViewHolder extends RecyclerView.ViewHolder{
        TextView txtTinnhan,txtThoigian;
        public ReceiveMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTinnhan = itemView.findViewById(R.id.txtTinnhanReceive);
            txtThoigian = itemView.findViewById(R.id.txtThoigianReceive);
        }
    }
}
