package one.com.pesosense.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import one.com.pesosense.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentUtilities extends Fragment {


    public PaymentUtilities() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.payment_utilities, container, false);
    }


}
