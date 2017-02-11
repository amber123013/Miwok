package com.example.android.miwok;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class FamilyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         *  activity_category XML 布局资源设置为内容视图。
         */
        setContentView(R.layout.activity_category);
//        创建一个新的 FamilyFragment，并使用 FragmentTransaction 将其插入 container 视图中
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new FamilyFragment())
                .commit();


    }


}
