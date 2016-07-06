package com.andview.example.swipeactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.andview.example.R;


public class SwipeMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.button1:
                startActivity(new Intent(this, SimpleRecycleListActivity.class));
                break;
            case R.id.button2:
                startActivity(new Intent(this, DifferentRecycleListActivity.class));
                break;
            case R.id.button3:
                startActivity(new Intent(this, RecycleGridActivity.class));
                break;
            case R.id.button4:
                startActivity(new Intent(this, StaggeredGridRvActivity.class));
                break;
            default:
                break;
        }
    }
}
