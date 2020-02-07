package com.vicedev.vloadingviews;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vicedev.vloadingviewslib.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRvShow;

    private List<Integer> mViewLayoutList;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRvShow = findViewById(R.id.rv_show);

        initView();
        initData();
    }

    private void initView() {
        mAdapter = new MyAdapter();
        mRvShow.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        mRvShow.setAdapter(mAdapter);
    }

    private void initData() {
        mViewLayoutList = new ArrayList<>();
        mViewLayoutList.add(R.layout.v_loading_view_0);
        mViewLayoutList.add(R.layout.v_loading_view_1);
        mViewLayoutList.add(R.layout.v_loading_view_2);
        mViewLayoutList.add(R.layout.v_loading_view_3);
        mViewLayoutList.add(R.layout.v_loading_view_4);
        mViewLayoutList.add(R.layout.v_loading_view_5);
        mViewLayoutList.add(R.layout.v_loading_view_6);
        mViewLayoutList.add(R.layout.v_loading_view_7);
        mViewLayoutList.add(R.layout.v_loading_view_8);
        mViewLayoutList.add(R.layout.v_loading_view_9);

        mAdapter.notifyDataSetChanged();
    }


    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(mViewLayoutList.get(viewType), parent, false);
            view.getLayoutParams().width = (int) (DensityUtil.getScreenWidth() / 2.0);
            view.getLayoutParams().height = view.getLayoutParams().width;
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mViewLayoutList == null ? 0 : mViewLayoutList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
