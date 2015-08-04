package one.com.pesosense.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import one.com.pesosense.R;
import one.com.pesosense.UtilsApp;
import one.com.pesosense.activity.OnlineShop;
import one.com.pesosense.activity.OtopShop;
import one.com.pesosense.adapter.RecyclerAdapterOnlineShop;
import one.com.pesosense.download.APIHandler;
import one.com.pesosense.helper.DatabaseHelper;
import one.com.pesosense.model.Item_shop;

/**
 * Created by mobile2 on 7/8/15.
 */
public class Shop extends Fragment implements View.OnClickListener {

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    Button btnotop, btnbodl, btnharana;
    RecyclerView rv, rv1, rv2;
    LinearLayoutManager llm, llm1, llm2;

    RecyclerAdapterOnlineShop adapter2;
    ArrayList<Item_shop> itemshop;

    int prod_id, user_id;
    String prod_name, prod_desc, prod_image, prod_price, prod_brand, prod_quantity;

    APIHandler apiHandler;
    private String url = "http://search.onesupershop.com/api/items";
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIxLCJpc3MiOiJodHRwOlwvXC9zZWFyY2gub25lc3VwZXJzaG9wLmNvbVwvYXBpXC9hdXRoIiwiaWF0IjoiMTQzODU4NDEzNCIsImV4cCI6IjE0Mzg1ODc3MzQiLCJuYmYiOiIxNDM4NTg0MTM0IiwianRpIjoiMzBiOWRmMzI5OTFjZmQ4NDg3ZGFkMjFmZDcxMzEyOTUifQ.h_pMTB0ziSp27KUcsL_YaZuxsiWp5pB1v0j-x8yLCBM";
    SharedPreferences sp;
    Map<String, String> params;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycler_shop, container, false);

        sp = this.getActivity().getSharedPreferences("token", Activity.MODE_PRIVATE);
        apiHandler = new APIHandler();
        params = new HashMap<String, String>();

        UtilsApp.initSharedPreferences(getActivity());
        token = UtilsApp.getString("access_token");

        btnotop = (Button) v.findViewById(R.id.btnotop);
        btnotop.setOnClickListener(this);

        btnbodl = (Button) v.findViewById(R.id.btnbodl);
        btnbodl.setOnClickListener(this);


        btnharana = (Button) v.findViewById(R.id.btnharana);
        btnharana.setOnClickListener(this);



        initValues(v);
        return v;
    }

    @Override
    public void onClick(View v) {

        Intent intent = null;

        if (v.getId() == R.id.btnotop)

            intent = new Intent(getActivity(), OtopShop.class);

        if (v.getId() == R.id.btnbodl)

            startActivity(new Intent(getActivity(), OnlineShop.class));

        if (v.getId() == R.id.btnharana)

            startActivity(new Intent(getActivity(), OnlineShop.class));

        if (intent != null) {
            startActivity(intent);
            getActivity().finish();
        }

    }


    public void initValues(View v) {


        dbHelper = DatabaseHelper.getInstance(getActivity().getApplicationContext());

        itemshop = new ArrayList<Item_shop>();
        adapter2 = new RecyclerAdapterOnlineShop(getActivity(), itemshop);


        rv = (RecyclerView) v.findViewById(R.id.rv);
        rv.setAdapter(adapter2);

        rv1 = (RecyclerView) v.findViewById(R.id.rv1);
        rv1.setAdapter(adapter2);

        rv2 = (RecyclerView) v.findViewById(R.id.rv2);
        rv2.setAdapter(adapter2);

        llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);


        llm1 = new LinearLayoutManager(getActivity().getApplicationContext());
        llm1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);


        llm2 = new LinearLayoutManager(getActivity().getApplicationContext());
        llm2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);


        rv.setLayoutManager(llm);
        rv1.setLayoutManager(llm1);
        rv2.setLayoutManager(llm2);


        new LoadData().execute();
        readDBB();

    }


    public boolean isDataExist(String tableName, int id) {

        boolean exists = false;
        int tempID;

        db = dbHelper.getReadableDatabase();
        cursor = db.query(tableName, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                tempID = cursor.getInt(0);

                if (id == tempID) {
                    Log.d("DATABASE", "EXISTS " + id);
                    exists = true;
                    break;
                }
            }
        }
        return exists;


    }

    public void readDBB() {


        db = dbHelper.getReadableDatabase();
        cursor = db.query("tbl_prod", null, null, null, null, null, null);
        Toast.makeText(getActivity().getApplicationContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_SHORT).show();


        if (cursor != null) {
            while (cursor.moveToNext()) {
                prod_id = cursor.getInt(0);
                user_id = cursor.getInt(1);
                prod_name = cursor.getString(2);
                prod_desc = cursor.getString(3);
                prod_price = cursor.getString(4);
                prod_brand = cursor.getString(5);
                prod_quantity = cursor.getString(6);
                prod_image = cursor.getString(7);


                itemshop.add(new Item_shop(prod_id, user_id, prod_name, prod_desc, prod_price, prod_brand, prod_quantity, prod_image));


            }
        }
        adapter2.notifyDataSetChanged();
    }


    public void insertProduct(int prod_id, int user_id, String prod_name, String prod_desc, String prod_price, String prod_brand, String prod_quantity, String prod_image) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("prod_id", prod_id);
        values.put("user_id", user_id);
        values.put("prod_name", prod_name);
        values.put("prod_desc", prod_desc);
        values.put("prod_price", prod_price);
        values.put("prod_brand", prod_brand);
        values.put("prod_quantity", prod_quantity);
        values.put("prod_image", prod_image);

        if (!isDataExist("tbl_prod", prod_id)) {
            db.insert("tbl_prod", null, values);

            db.close();
        }
    }


    public class LoadData extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Data Please Wait...");
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voidsparams) {
            String response = apiHandler.httpMakeRequest(url, params, "get", token);
            Log.d("zzzzzzz", "response" + response);

            try {

                if (response != null) {
                    JSONObject allData = new JSONObject(response);
                    JSONArray datas = allData.getJSONArray("data");


                    for (int i = 0; i < datas.length(); i++) {
                        JSONObject data_object = datas.getJSONObject(i);

                        String prod_id = data_object.getString("id");
                        String user_id = data_object.getString("user_id");
                        String prod_name = data_object.getString("name");
                        String prod_desc = data_object.getString("description");
                        String prod_price = data_object.getString("price");
                        String prod_brand = data_object.getString("brand");
                        String prod_quantity = data_object.getString("quantity");

                        if (!data_object.isNull("photos")) {

                            JSONArray array_object = data_object.getJSONArray("photos");
                            String prod_image = array_object.getString(0);


                            insertProduct(Integer.parseInt(prod_id), Integer.parseInt(user_id), prod_name, prod_desc, prod_price, prod_brand, prod_quantity, prod_image);

                        }

                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(Void result) {
            if (pDialog.isShowing())
                pDialog.dismiss();

            readDBB();

        }

    }


}

