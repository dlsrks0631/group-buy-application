package com.example.cloudcomputingproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.example.cloudcomputingproject.datas.MainPostDataGet;
import com.example.cloudcomputingproject.datas.MainPostDataGetResponse;
import com.example.cloudcomputingproject.datas.Post;
import com.example.cloudcomputingproject.postpage.PostAdapter;
import com.example.cloudcomputingproject.postpage.PostPreview;
import com.example.cloudcomputingproject.utility.APIInterface;
import com.example.cloudcomputingproject.utility.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<PostPreview> postPreviews;
    String u_id, email, category;
    private APIInterface service;
    ImageView category_iv, profile_iv;
    Intent intent;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post); // 여러분의 레이아웃 파일 이름으로 변경하세요.
        intent = getIntent();
        u_id = FirebaseAuth.getInstance().getUid(); // 로그인할때 전달해준 u_id를 변수에 저장.
        email = intent.getStringExtra("email"); // 로그인할때 전달해준 email를 변수에 저장.

        service = RetrofitClient.getClient().create(APIInterface.class); // 서버 연결


        // categorySelectbox스피너 아이템 선택 이벤트 리스너 설정

        category = intent.getStringExtra("categoryName");
        if(category == null){
            category = "전체 게시글";
        }

        startGetPosts(new MainPostDataGet(category));
        // RecyclerView 초기화
        recyclerView = findViewById(R.id.postView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(category);

        // 임시 데이터 생성
        postPreviews = new ArrayList<>();
        // 여기에 임의의 데이터 추가...

        category_iv = findViewById(R.id.category_iv);
        profile_iv = findViewById(R.id.profile_iv);
        // FloatingActionButton 초기화
        FloatingActionButton postingButton = findViewById(R.id.postingButton);
        postingButton.setOnClickListener(view -> {
            // 게시글 작성 페이지로 넘어가는 Intent 생성
            Intent intent = new Intent(PostActivity.this, AddPostActivity.class);
            intent.putExtra("u_id", u_id);
            intent.putExtra("category", category);
            startActivity(intent);
        });

        category_iv.setOnClickListener(v -> {
            Intent intent = new Intent(PostActivity.this, CategoryActivity.class);
            startActivity(intent);
        });
        profile_iv.setOnClickListener(v -> {
            Intent intent = new Intent(PostActivity.this, Mypage.class);
            startActivity(intent);
        });
    }

    // CustomItemDecoration 클래스, 리사이클러뷰의 세부조정사항을 설정합니다.
    class CustomItemDecoration extends RecyclerView.ItemDecoration {
        private final int verticalSpaceHeight;

        public CustomItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
            // 첫 번째 아이템에는 상단에도 공간을 추가합니다.
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = verticalSpaceHeight;
            }
        }
    }

    private void startGetPosts(MainPostDataGet data){ // 카테고리 라벨을 바탕으로 통신
        service.PostListGet(data).enqueue(new Callback<MainPostDataGetResponse>() {
            @Override
            public void onResponse(Call<MainPostDataGetResponse> call, Response<MainPostDataGetResponse> response) {
                MainPostDataGetResponse result = response.body();
                // 성공시, result에 정보를 불러올 것임. 여기서 result에 대한 정보는 MainPostDataGetResponse.java에 명시되어 있음.

                if (result.getCode() == 200) {
                    Log.d("post 불러오기 성공", String.valueOf("1"));

                    List<Post> posts = result.getPosts();

                    showPost(posts);
                }
            }
            @Override
            public void onFailure(Call<MainPostDataGetResponse> call, Throwable t) {
                Toast.makeText(PostActivity.this, "회원가입 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("유저 데이터 불러오기 실패", t.getMessage());
                t.printStackTrace();
            }
        });

    }
    public void showPost(List<Post> posts){

        postPreviews = new ArrayList<>();

        for (int i = posts.size() - 1; i >= 0; i--) {
            Post post = posts.get(i);
            postPreviews.add(new PostPreview("프로필 이미지 URL", post.nickname, post.img, post.title, post.post_id));
            Log.d("post_id는 ", post.post_id);
        }

        // 어댑터 생성 및 설정
        PostAdapter adapter = new PostAdapter(postPreviews, u_id, email);
        recyclerView.setAdapter(adapter);

        // CustomItemDecoration을 추가합니다.
        //int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_decoration_margin);
        //recyclerView.addItemDecoration(new PostActivity.CustomItemDecoration(spacingInPixels));
    }
}