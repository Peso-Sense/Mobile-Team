package one.com.pesosense.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import one.com.pesosense.R;
import one.com.pesosense.sliding.SlidingTabLayout;

/**
 * Created by Maox on 6/30/2015.
 */
public class TabActivity extends FragmentActivity {

    private ViewPager mPager;
    private SlidingTabLayout mTabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appguidemain);


        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setViewPager(mPager);

    }


    class MyPagerAdapter extends FragmentPagerAdapter {

        String[] tabs;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {

//            switch (position) {
//                case 0:
//                    // Top Rated fragment activity
//                    new Intent(new Intent("android.intent.action.PICK_ACTIVITY"));
//                case 1:
//                    // Games fragment activity
//                    new Intent(new Intent("android.intent.action.PICK_ACTIVITY"));
//
//
//                case 2:
//                    // Movies fragment activity
//                    new Intent(new Intent("android.intent.action.PICK_ACTIVITY"));
//
//            }
//
//            return null;


       MyFragment myfragment = MyFragment.getInstance(position);
       return myfragment;
    }




        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }




        @Override
        public int getCount() {
            return 3;
        }


    }










    public static class MyFragment extends Fragment {
        private TextView textView;
    public static MyFragment getInstance(int position){

        MyFragment myFragment = new MyFragment();
        Bundle args=new Bundle();
        args.putInt("position", position);
        myFragment.setArguments(args);
        return myFragment;
    }

        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View layout = inflater.inflate(R.layout.fragment_my, container, false);
            textView = (TextView)layout.findViewById(R.id.position);
            Bundle bundle=getArguments();
            if(bundle!=null){
                textView.setText("The page selected is" + bundle.getInt("position"));
            }
            return layout;
        }



    }













}

