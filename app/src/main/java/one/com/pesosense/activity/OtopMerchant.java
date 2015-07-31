package one.com.pesosense.activity;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;



import java.util.ArrayList;

import one.com.pesosense.R;
import one.com.pesosense.adapter.RecyclerAdapterShop;
import one.com.pesosense.adapter.RecyclerAdapter_Otop1;
import one.com.pesosense.helper.DatabaseHelper;
import one.com.pesosense.model.Item;

/**
 * Created by mobile2 on 7/8/15.
 */
public class OtopMerchant extends ActionBarActivity {

    DatabaseHelper dbHelper;

    SQLiteDatabase db;
    Cursor cursor;
    RecyclerView rv;
    LinearLayoutManager llm,llm2;

    RecyclerAdapterShop adapter;
    RecyclerAdapter_Otop1 adapter2;
    ArrayList<Item> item;

    int id;
    String name;
    String province;
    String price;
    String product_image;
    String description;
    int ratings;
    String brand;
    String inventory;
    int view;
    ImageButton list,grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_otop);




        initValues();
    }


    public void initValues() {



        dbHelper = DatabaseHelper.getInstance(getApplicationContext());

        item = new ArrayList<Item>();
        adapter = new RecyclerAdapterShop(OtopMerchant.this, item);
        adapter2 = new RecyclerAdapter_Otop1(OtopMerchant.this, item);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setAdapter(adapter2

        );




        llm = new LinearLayoutManager(getApplicationContext());
//      llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//      llm = new GridLayoutManager(this, 2);

        llm = new LinearLayoutManager(getApplicationContext());
        llm2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        llm2 = new GridLayoutManager(this, 2);

        list = (ImageButton) findViewById(R.id.btnlist);

        grid = (ImageButton) findViewById(R.id.btngrid);
//

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                rv.setLayoutManager(llm);
                rv.setAdapter(adapter2);
                grid.setVisibility(View.VISIBLE);
                list.setVisibility(View.GONE);

            }
        });


        grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                rv.setLayoutManager(llm2);
                rv.setAdapter(adapter);
                grid.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);
            }
        });


                rv.setLayoutManager(llm);












        readDB();


    }

    public void readDB() {


        db = dbHelper.getReadableDatabase();
        cursor = db.query("tbl_otop", null, null, null, null, null, null);

        Toast.makeText(getApplicationContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_SHORT).show();
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
            }
        }
        adapter.notifyDataSetChanged();
    }




}
