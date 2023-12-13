package com.example.cloudcomputingproject.datas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LikePostDataReponse {
    private List<PostItem> posts;

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    public List<PostItem> getPosts() {
        return posts;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
