package com.example.chatbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chatbot.adapter.MessageAdapter;
import com.example.chatbot.models.MessageModel;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImageActivity extends AppCompatActivity {
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    MessageAdapter adapter;
    ImageView imageView;
    RecyclerView myRecyclerView;
    ArrayList<MessageModel> list;
    TextInputEditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);


        editText = findViewById(R.id.user_message);
        list = new ArrayList<>();

        myRecyclerView=findViewById(R.id.recyclerviewchat);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);

        layoutManager.setStackFromEnd(true);

        myRecyclerView.setLayoutManager(layoutManager);

        adapter=new MessageAdapter(list,ImageActivity.this);

        myRecyclerView.setAdapter(adapter);

        findViewById(R.id.sendbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInput = editText.getText().toString();
                list.add(new MessageModel(true, false, userInput));
                adapter.notifyItemInserted(list.size() - 1);
                myRecyclerView.getRecycledViewPool().clear();
                myRecyclerView.smoothScrollToPosition(list.size() - 1);
                adapter.notifyDataSetChanged();
                callApi(userInput);
            }
        });
    }
    private void callApi(String query) {
        String typing="Loading Image...";
        list.add(new MessageModel(false, false,typing.trim()));
        JSONObject jsonBody=new JSONObject();
        try {
            jsonBody.put("prompt",query);
            jsonBody.put("size","256x256");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestBody requestBody=RequestBody.create(jsonBody.toString(),JSON);
        Request request=new Request.Builder().url("https://api.openai.com/v1/images/generations")
                .header("Authorization","Bearer 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")
                .post(requestBody).build();
        OkHttpClient client=new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(ImageActivity.this,"Response Failed",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject=new JSONObject(response.body().string());
                        String imgURL=jsonObject.getJSONArray("data").getJSONObject(0).getString("url");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(list.size()-1);
                                editText.setText("");
                                list.add(new MessageModel(false, true, imgURL));
                                adapter.notifyItemInserted(list.size() - 1);
                                myRecyclerView.getRecycledViewPool().clear();
                                myRecyclerView.smoothScrollToPosition(list.size() - 1);
                                adapter.notifyDataSetChanged();
                            }
                        });

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }

}
