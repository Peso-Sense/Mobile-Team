package one.com.pesosense.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentGovernment extends Fragment {

    Button btnSSS;
    Button btnPhilHealth;
    Button btnPagibig;

    public PaymentGovernment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.payment_government, container, false);

        initValues(v);
        return v;
    }

    private void initValues(View v) {

        btnSSS = (Button) v.findViewById(R.id.btnSSS);
        btnSSS.setTypeface(UtilsApp.opensansNormal());

        btnPhilHealth = (Button) v.findViewById(R.id.btnPhilHealth);
        btnPhilHealth.setTypeface(UtilsApp.opensansNormal());

        btnPagibig = (Button) v.findViewById(R.id.btnPagibig);
        btnPagibig.setTypeface(UtilsApp.opensansNormal());


    }


}
