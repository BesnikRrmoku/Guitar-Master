package com.example.android.guitar.learning;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.android.guitar.R;

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public CategoryAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new TunningFragment();
        } else if (position == 1) {
            return new ChordsFragment();
        } else {
            return new BtracksFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.category_tunning);
        } else if (position == 1) {
            return mContext.getString(R.string.category_chords);
        } else {
            return mContext.getString(R.string.category_btracks);
        }
    }
}
