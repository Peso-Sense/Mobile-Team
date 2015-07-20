package one.com.pesosense.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;
import one.com.pesosense.activity.GovernmentPayment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentGovernment extends Fragment implements View.OnClickListener {

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
        btnSSS.setOnClickListener(this);

        btnPhilHealth = (Button) v.findViewById(R.id.btnPhilHealth);
        btnPhilHealth.setTypeface(UtilsApp.opensansNormal());
        btnPhilHealth.setOnClickListener(this);

        btnPagibig = (Button) v.findViewById(R.id.btnPagibig);
        btnPagibig.setTypeface(UtilsApp.opensansNormal());
        btnPagibig.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        Intent intent = new Intent(getActivity(), GovernmentPayment.class);

        if (view.getId() == R.id.btnSSS) {
            intent.putExtra("root", 0);
        }

        if (view.getId() == R.id.btnPhilHealth) {
            intent.putExtra("root", 1);
        }

        if (view.getId() == R.id.btnPagibig) {
            intent.putExtra("root", 2);
        }

        startActivity(intent);

    }
}
