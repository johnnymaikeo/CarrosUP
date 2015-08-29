/*package br.com.up.carrosup.activity.adapter;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.up.carrosup.R;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        if (position == 0) {
            args.putString("tipo", "classicos");
        } else if (position == 1) {
            args.putString("tipo", "esportivos");
        } else if (position == 2) {
            args.putString("tipo", "luxo");
        }
        Fragment f = new CarrosFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return getString(R.string.classicos);
            case 1:
                return getString(R.string.esportivos);
            case 2:
                return getString(R.string.luxo);
            default:
                return getString(R.string.luxo);
        }
    }
}*/