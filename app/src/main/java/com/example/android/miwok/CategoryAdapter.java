package com.example.android.miwok;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ASUS on 2017-02-11.
 */

public class CategoryAdapter extends FragmentPagerAdapter{
    private Context mContext;
    public CategoryAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    //设置Tablayout 标签页中的文本
    //需要Context来获取资源字符串，在构造函数中添加
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.category_numbers);
        } else if (position == 1) {
            return mContext.getString(R.string.category_family);
        } else if (position == 2) {
            return mContext.getString(R.string.category_colors);
        } else {
            return mContext.getString(R.string.category_phrases);
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NumbersFragment();
            case 1:
                return new FamilyFragment();
            case 2:
                return new ColorsFragment();
            case 3:
                return  new PhrasesFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
