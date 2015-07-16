package one.com.pesosense.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import one.com.pesosense.fragment.PaymentBank;
import one.com.pesosense.fragment.PaymentGovernment;
import one.com.pesosense.fragment.PaymentNGO;
import one.com.pesosense.fragment.PaymentUtilities;

/**
 * Created by mykelneds on 7/4/15.
 */
public class PaymentPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence titles[];
    int count;


    public PaymentPagerAdapter(FragmentManager fm, CharSequence titles[], int count) {
        super(fm);
        this.titles = titles;
        this.count = count;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new PaymentGovernment();
            case 1:
                return new PaymentUtilities();
            case 2:
                return new PaymentBank();
            case 3:
                return new PaymentNGO();

        }

        return null;
    }

    @Override
    public int getCount() {
        return count;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        return titles[position];
    }
}
