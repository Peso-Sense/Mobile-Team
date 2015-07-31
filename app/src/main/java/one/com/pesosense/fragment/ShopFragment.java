package one.com.pesosense.fragment;


import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;
import one.com.pesosense.adapter.ShopAdapter;
import one.com.pesosense.model.ShopItem;


/**
 * created by mykel neds 07/25/15
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment {

    public ShopFragment() {
        // Required empty public constructor
        context = getActivity();
    }

    /**
     * CONSTANT
     * Views
     * Variables
     */

    private final Context context;
    private final String TAG = "SHOP";


    LinearLayout containerOTOP, containerBODL, containerHSP, containerUProduct;
    RecyclerView rvOTOP, rvBODL, rvHSP, rvUProduct;
    TextView lblOTOP, lblBODL, lblHSP, lblUProduct;
    TextView lblSeeMore, lblSeeMore2, lblSeeMore3, lblSeeMore4;

    ArrayList<ShopItem> itemOTOP, itemBODL, itemHSP, itemUProduct;
    ShopAdapter adapterOTOP, adapterBODL, adapterHSP, adapterUProduct;
    LinearLayoutManager llm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Call Option Menu");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shop, container, false);

        initialize(v);

        return v;
    }

    private void initialize(View v) {

        initViews(v);
        initRV();
    }

    private void initViews(View v) {
        Log.d(TAG, "Views Initialized");

        containerOTOP = (LinearLayout) v.findViewById(R.id.containerOTOP);
        containerBODL = (LinearLayout) v.findViewById(R.id.containerBODL);
        containerHSP = (LinearLayout) v.findViewById(R.id.containerHSP);
        containerUProduct = (LinearLayout) v.findViewById(R.id.containerUProduct);

        rvOTOP = (RecyclerView) v.findViewById(R.id.rvOTOP);
        rvBODL = (RecyclerView) v.findViewById(R.id.rvBODL);
        rvHSP = (RecyclerView) v.findViewById(R.id.rvHSP);
        rvUProduct = (RecyclerView) v.findViewById(R.id.rvUProduct);

        lblOTOP = (TextView) v.findViewById(R.id.lblOTOP);
        lblOTOP.setTypeface(UtilsApp.opensansNormal());

        lblBODL = (TextView) v.findViewById(R.id.lblBODL);
        lblBODL.setTypeface(UtilsApp.opensansNormal());

        lblHSP = (TextView) v.findViewById(R.id.lblHSP);
        lblHSP.setTypeface(UtilsApp.opensansNormal());

        lblUProduct = (TextView) v.findViewById(R.id.lblUProduct);
        lblUProduct.setTypeface(UtilsApp.opensansNormal());

        lblSeeMore = (TextView) v.findViewById(R.id.lblSeeMore);
        lblSeeMore.setTypeface(UtilsApp.opensansNormal());

        lblSeeMore2 = (TextView) v.findViewById(R.id.lblSeeMore2);
        lblSeeMore2.setTypeface(UtilsApp.opensansNormal());

        lblSeeMore3 = (TextView) v.findViewById(R.id.lblSeeMore3);
        lblSeeMore3.setTypeface(UtilsApp.opensansNormal());

        lblSeeMore4 = (TextView) v.findViewById(R.id.lblSeeMore4);
        lblSeeMore4.setTypeface(UtilsApp.opensansNormal());


    }

    private void initRV() {
        Log.d(TAG, "Values initialized");


        //   llm = new
        itemOTOP = new ArrayList<>();
        adapterOTOP = new ShopAdapter(this.getActivity(), itemOTOP);
        llm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rvOTOP.setLayoutManager(llm);
        rvOTOP.setAdapter(adapterOTOP);

        itemBODL = new ArrayList<>();
        adapterBODL = new ShopAdapter(this.getActivity(), itemBODL);
        llm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rvBODL.setLayoutManager(llm);
        rvBODL.setAdapter(adapterBODL);

        itemHSP = new ArrayList<>();
        adapterHSP = new ShopAdapter(this.getActivity(), itemHSP);
        llm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rvHSP.setLayoutManager(llm);
        rvHSP.setAdapter(adapterHSP);

        itemUProduct = new ArrayList<>();
        adapterUProduct = new ShopAdapter(this.getActivity(), itemUProduct);
        llm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rvUProduct.setLayoutManager(llm);
        rvUProduct.setAdapter(adapterUProduct);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        setHasOptionsMenu(true);
        inflater.inflate(R.menu.menu_shop_fragment, menu);
        Log.d(TAG, "Options should be createD!!!");

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        //  getActivity().getMenuInflater().inflate(R.menu.menu_shop_fragment, menu);
    }
}
