package one.com.pesosense.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;

import one.com.pesosense.R;
import one.com.pesosense.activity.OnlineShop;
import one.com.pesosense.activity.OtopMerchant;

import one.com.pesosense.adapter.RecyclerAdapterOnlineShop;
import one.com.pesosense.adapter.RecyclerAdapterShop;
import one.com.pesosense.helper.DatabaseHelper;
import one.com.pesosense.model.Item;
import one.com.pesosense.model.Item_shop;

/**
 * Created by mobile2 on 7/20/15.
 */
public class FragmentShop extends Fragment {

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    Button btnotop, btnbodl, btnharana;
    RecyclerView rv,rv1,rv2;
    LinearLayoutManager llm,llm1,llm2;

    RecyclerAdapterShop adapter;
    RecyclerAdapterOnlineShop adapter2;
    List<Item> item;
    List<Item_shop> itemshop;

    int id;
    String name,brand,inventory;
    String province;
    String price;
    String description;
    int ratings;
    int product_id,subcat_id,cat_id,product_cost,product_price,product_quantity;
    String product_name,product_desc,product_image;


    @Override


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycler_shop, container, false);




        btnotop = (Button) v.findViewById(R.id.btnotop);
        btnotop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent i = new Intent(getActivity(), OtopMerchant.class);
                startActivity(i);
            }
        });


        btnbodl = (Button) v.findViewById(R.id.btnbodl);
        btnbodl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), OnlineShop.class);
                startActivity(i);
            }
        });



        btnharana = (Button) v.findViewById(R.id.btnharana);
        btnharana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), OnlineShop.class);
                startActivity(i);
            }
        });




        initValues(v);
        return v;
    }


    public void initValues(View v) {


        dbHelper = DatabaseHelper.getInstance(getActivity());

        itemshop = new ArrayList<Item_shop>();
        adapter2 = new RecyclerAdapterOnlineShop(getActivity(), itemshop);

        item = new ArrayList<Item>();
        adapter = new RecyclerAdapterShop(getActivity(), item);


        rv = (RecyclerView) v.findViewById(R.id.rv);
        rv.setAdapter(adapter);

        rv1 = (RecyclerView) v.findViewById(R.id.rv1);
        rv1.setAdapter(adapter2);

        rv2 = (RecyclerView) v.findViewById(R.id.rv2);
        rv2.setAdapter(adapter2);

        llm = new LinearLayoutManager(getActivity());
        llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//      llm = new GridLayoutManager(getActivity(), 2);

        llm1 = new LinearLayoutManager(getActivity());
        llm1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//      llm1 = new GridLayoutManager(this, 2);

        llm2 = new LinearLayoutManager(getActivity());
        llm2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//      llm2 = new GridLayoutManager(this, 2);

        rv.setLayoutManager(llm);
        rv1.setLayoutManager(llm1);
        rv2.setLayoutManager(llm2);



        readDB();
        readDBb();

    }



    public void readDB() {
        int counter = 0;

        db = dbHelper.getReadableDatabase();
        cursor = db.query("tbl_otop", null, null, null, null, null, null);

        Toast.makeText(getActivity(), String.valueOf(cursor.getCount()), Toast.LENGTH_SHORT).show();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                id = cursor.getInt(0);
                province = cursor.getString(1);
                name = cursor.getString(2);
                price = cursor.getString(3);
                product_image = cursor.getString(4);
                description = cursor.getString(5);
                ratings = cursor.getInt(6);
                brand = cursor.getString(7);
                inventory = cursor.getString(8);

                item.add( new Item(id, province,name,price,product_image,description,ratings,brand,inventory));
                counter++;
                if(counter == 5){
                    break;
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void readDBb() {
        int counter = 0;
        db = dbHelper.getReadableDatabase();
        cursor = db.query("tbl_products", null, null, null, null, null, null);

        Toast.makeText(getActivity(), String.valueOf(cursor.getCount()), Toast.LENGTH_SHORT).show();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                product_id = cursor.getInt(0);
                cat_id = cursor.getInt(1);
                subcat_id = cursor.getInt(2);
                product_name = cursor.getString(3);
                product_desc = cursor.getString(4);
                product_cost = cursor.getInt(5);
                product_price = cursor.getInt(6);
                product_quantity = cursor.getInt(7);
                product_image = cursor.getString(8);


                itemshop.add( new Item_shop(product_id,cat_id,subcat_id,product_name,product_desc,product_cost,product_price,product_quantity, product_image));
                counter++;
                if(counter == 5){
                    break;
                }
            }
        }
        adapter.notifyDataSetChanged();
    }



}