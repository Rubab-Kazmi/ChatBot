package com.example.chatbot;

import static kotlinx.coroutines.BuildersKt.withContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import android.widget.Toast;

import com.example.chatbot.adapter.MessageAdapter;
import com.example.chatbot.models.MessageModel;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
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


public class ChatActivity extends AppCompatActivity {
    MessageAdapter adapter;
    RecyclerView myRecyclerView;
    ArrayList<MessageModel> list;
    TextInputEditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ImageView back=findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        editText = findViewById(R.id.user_message);
        list = new ArrayList<>();

        myRecyclerView=findViewById(R.id.recyclerviewchat);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);

        layoutManager.setStackFromEnd(true);

        myRecyclerView.setLayoutManager(layoutManager);

        adapter=new MessageAdapter(list,ChatActivity.this);

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
    void callApi(String question) {
        String typing="Typing...";
        list.add(new MessageModel(false, false,typing.trim()));
        adapter.notifyItemInserted(list.size() - 1);
        myRecyclerView.getRecycledViewPool().clear();
        myRecyclerView.smoothScrollToPosition(list.size() - 1);
        adapter.notifyDataSetChanged();

        JSONObject jsonBody = new JSONObject();
        try {
            Log.d("JSON object", "created"); // Added missing semicolon
            jsonBody.put("model", "text-davinci-003");
            jsonBody.put("prompt", question);
            jsonBody.put("max_tokens", 500);
            jsonBody.put("temperature", 0.7);

            // Now you can use the jsonBody as needed
        } catch (JSONException e) {
            Log.d("JSON object", "catch at start");
            e.printStackTrace();
            // Handle JSON exception
        }

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(jsonBody.toString(), mediaType);
        Request request = new Request.Builder().url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer " + Utils.API_KEY).post(requestBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChatActivity.this, "Response is Failed", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String textResponse = jsonArray.getJSONObject(0).getString("text");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(list.size()-1);
                                editText.setText("");
                                list.add(new MessageModel(false, false, textResponse.trim()));
                                adapter.notifyItemInserted(list.size() - 1);
                                myRecyclerView.getRecycledViewPool().clear();
                                myRecyclerView.smoothScrollToPosition(list.size() - 1);
                                adapter.notifyDataSetChanged();

                            }
                        });
                        // Now you can use the textResponse as needed
                    } catch (JSONException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChatActivity.this, "Response is not successful", Toast.LENGTH_LONG).show();
                            }
                        });
                        e.printStackTrace();
                        // Handle JSON exception
                    }

                } else {
                    // Handle unsuccessful response
                }
            }
        });
    }

}