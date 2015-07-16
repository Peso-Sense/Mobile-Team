package one.com.pesosense.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import one.com.pesosense.R;
import one.com.pesosense.adapter.PaymentPagerAdapter;
import one.com.pesosense.sliding.SlidingTabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {


    SlidingTabLayout tabs;
    PaymentPagerAdapter adapter;
    ViewPager pager;
    CharSequence Titles[] = {"Government", "Utilities", "Bank", "NGO"};


    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_payment, container, false);

        initValues(v);

        return v;
    }

    private void initValues(View v) {

        adapter = new PaymentPagerAdapter(getActivity().getSupportFragmentManager(), Titles, Titles.length);
        pager = (ViewPager) v.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) v.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(false);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.blue);
            }
        });

        tabs.setViewPager(pager);

    }


}
