package com.example.cloudcomputingproject.datas;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class PostDataPut { // 게시글 작성시, EditText에 작성한 내용을 서버로 전송할 것이다.
    @SerializedName("user_id")
    private String user_id;

    @SerializedName("title")
    private String title;

    @SerializedName("contents")
    private String contents;


    @SerializedName("img")
    private String img;

    @SerializedName("category_label")
    private String category_label;

    @SerializedName("price")
    private String price;

    @SerializedName("location")
    private String location;

    public PostDataPut(String user_id, String title, String contents, String img, String category_label,
                       String price, String location){
        this.user_id = user_id;
        this.title = title;
        this.contents = contents;
        this.img = img;
        this.category_label = category_label;
        this.price = price;
        this.location = location;
    }
}
