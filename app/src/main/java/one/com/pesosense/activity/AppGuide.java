package one.com.pesosense.activity;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;


public class AppGuide extends FragmentActivity implements View.OnClickListener {

    Button btnLogin;
    Button btnSignup;

    RadioButton radio, radio1, radio2;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_guide);

        initValues();

    }

    public void initValues() {

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setTypeface(UtilsApp.opensansNormal());
        btnLogin.setOnClickListener(this);

        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnSignup.setTypeface(UtilsApp.opensansNormal());
        btnSignup.setOnClickListener(this);

        //
        radio = (RadioButton) findViewById(R.id.radioButton);
        radio1 = (RadioButton) findViewById(R.id.radioButton1);
        radio2 = (RadioButton) findViewById(R.id.radioButton2);

        radio.setClickable(false);
        radio1.setClickable(false);
        radio2.setClickable(false);
        radio.setChecked(true);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new AppGuidePagerAdapter(getSupportFragmentManager()));
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radio.setChecked(true);
                        break;
                    case 1:
                        radio1.setChecked(true);
                        break;
                    case 2:
                        radio2.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View view) {

        Intent intent = null;

        if (view.getId() == R.id.btnLogin)
            intent = new Intent(AppGuide.this, Login2.class);
        //intent = new Intent(AppGuide.this, Login.class);

        if (view.getId() == R.id.btnSignup)
            intent = new Intent(AppGuide.this, Signup.class);

        if (intent != null) {
            startActivity(intent);
            finish();
        }

    }


    public class AppGuidePagerAdapter extends FragmentPagerAdapter {


        public AppGuidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            AppGuideFragment fragment = AppGuideFragment.getInstance(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public static class AppGuideFragment extends Fragment {

        TextView text;

        final static String[] GUIDES = {"Peso Sense 1", "Peso Sense 2", "Peso Sense"};

        public static AppGuideFragment getInstance(int position) {

            AppGuideFragment fragment = new AppGuideFragment();
            Bundle args = new Bundle();
            args.putString("about", GUIDES[position]);
            fragment.setArguments(args);

            return fragment;

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            super.onCreateView(inflater, container, savedInstanceState);
            View v = inflater.inflate(R.layout.fragment_appguide, container, false);

            String message = getArguments().getString("about");

            text = (TextView) v.findViewById(R.id.text);
            text.setText(message);

            return v;
        }
    }

}
