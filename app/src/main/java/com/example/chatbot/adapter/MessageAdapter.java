package com.example.chatbot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chatbot.R;
import com.example.chatbot.models.MessageModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.viewHolder> {
    ArrayList<MessageModel> list;
    Context context;

    public MessageAdapter(ArrayList<MessageModel> list, Context context){
        this.list=list;
        this.context=context;
    }
    public int getItemViewType(int position) {
        MessageModel message = list.get(position);
        if (message.isUser()==true){
            return 0;
        }
        else {
            return 1;
        }
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
       if (viewType==0){
           v=LayoutInflater.from(context).inflate(R.layout.right_chat,parent,false);
       }
       else{
           v=LayoutInflater.from(context).inflate(R.layout.left_chat,parent,false);
       }
       return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        MessageModel message = list.get(position);
        if (!message.isUser()){
            holder.imageContainer.setVisibility(View.GONE);
            holder.msgText.setText(message.getMessage());
        }
        if (message.isUser()==true){
            holder.msgText.setText(message.getMessage());
        }
        if (message.isImage()==true) {
            holder.imageContainer.setVisibility(View.VISIBLE);
            holder.msgText.setVisibility(View.GONE);
            Picasso.get().load(message.getMessage()).into(holder.image);

        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
TextView msgText;
ImageView image;
LinearLayout imageContainer;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            msgText=itemView.findViewById(R.id.show_message);
            image=itemView.findViewById(R.id.image);
            imageContainer=itemView.findViewById(R.id.imageCard);
        }
    }
}
