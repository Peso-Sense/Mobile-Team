package one.com.pesosense.activity;

/**
 * Created by mobile2 on 7/9/15.
 */


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import one.com.pesosense.R;
import one.com.pesosense.adapter.RecyclerAdapterOnlineShop;
import one.com.pesosense.helper.DatabaseHelper;
import one.com.pesosense.model.Item_shop;


public class OnlineShop extends Fragment {


    Cursor cursor;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    ArrayList<Item_shop> itemshop;
    RecyclerAdapterOnlineShop adapter;
    RecyclerView rv;
    LinearLayoutManager llm;

    int product_id, subcat_id, cat_id, product_cost, product_price, product_quantity;
    String product_name, product_desc, product_image;


    private String url = "http://onesupershop.com/ShoppingCart/web_services/get_store.php?user=dexandrada5&template_folder=leroy";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.recycler_onlineshop, container, false);


        new LoadData().execute();


        initValues(v);
    return v;
    }

    public void initValues(View v) {

        dbHelper = DatabaseHelper.getInstance(getActivity());

        itemshop = new ArrayList<Item_shop>();
        adapter = new RecyclerAdapterOnlineShop(getActivity(), itemshop);

        rv = (RecyclerView) v.findViewById(R.id.rv);
        rv.setAdapter(adapter);


        llm = new LinearLayoutManager(getActivity());
//        llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        llm = new GridLayoutManager(getActivity(), 2);


        rv.setLayoutManager(llm);


        readDB();


    }


    public void readDB() {

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


                itemshop.add(new Item_shop(product_id, cat_id, subcat_id, product_name, product_desc, product_cost, product_price, product_quantity, product_image));
            }
        }
        adapter.notifyDataSetChanged();
    }


    public void insertCategory(int cat_id, String cat_name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cat_id", cat_id);
        values.put("cat_name", cat_name);
        db.insert("tbl_category", null, values);
        db.close();
    }

    public void insertSubCategory(int subcat_id, String subcat_name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("subcat_id", subcat_id);
        values.put("subcat_name", subcat_name);
        db.insert("tbl_subcategory", null, values);
        db.close();
    }

    public void insertProducts(int cat_id, int subcat_id, String product_name, String product_desc, int product_cost, int product_price, int product_quantity, String product_image) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cat_id", cat_id);
        values.put("subcat_id", subcat_id);
        values.put("product_name", product_name);
        values.put("product_desc", product_desc);
        values.put("product_cost", product_cost);
        values.put("product_price", product_price);
        values.put("product_quantity", product_quantity);
        values.put("product_image", product_image);
        db.insert("tbl_products", null, values);
        db.close();
    }

    public void insertGrabbed(int cat_id, int subcat_id, String grabbed_name, String grabbed_desc, int grabbed_cost, int grabbed_price, int grabbed_quantity, String grabbed_image) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cat_id", cat_id);
        values.put("subcat_id", subcat_id);
        values.put("grabbed_name", grabbed_name);
        values.put("grabbed_desc", grabbed_desc);
        values.put("grabbed_cost", grabbed_cost);
        values.put("grabbed_price", grabbed_price);
        values.put("grabbed_quantity", grabbed_quantity);
        values.put("grabbed_image", grabbed_image);
        db.insert("tbl_grabbed", null, values);
        db.close();
    }


    public class LoadData extends AsyncTask<Void, Void, Void> {

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet;
        HttpResponse response;
        HttpEntity httpEntity;
        ProgressDialog pDialog;
        String data;
        String product_name;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Data Please Wait...");
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(false);
        }

        @Override
        protected Void doInBackground(Void... params) {

            httpGet = new HttpGet(url);
            try {
                response = httpClient.execute(httpGet);
                httpEntity = response.getEntity();
                data = EntityUtils.toString(httpEntity);

                if (data != null) {
                    JSONObject allData = new JSONObject(data);
                    JSONArray categories = allData.getJSONArray("categories");

                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject category_object = categories.getJSONObject(i);
                        String category_name = category_object.getString("category_name");
                        String category_id = category_object.getString("c_id");
                        insertCategory(Integer.parseInt(category_id), category_name);

                        JSONArray sub_categ = category_object.getJSONArray("subcategories");
                        for (int j = 0; j < sub_categ.length(); j++) {

                            JSONObject subcateg_object = sub_categ.getJSONObject(j);
                            String subcategory_id = subcateg_object.getString("sub_id");
                            String subcategory_name = subcateg_object.getString("sub_name");
                            insertSubCategory(Integer.parseInt(subcategory_id), subcategory_name);

                        }


                    }

                    JSONArray product = allData.getJSONArray("products");
                    for (int i = 0; i < product.length(); i++) {
                        JSONObject product_object = product.getJSONObject(i);
                        String product_catId = product_object.getString("cat_id");
                        String product_subId = product_object.getString("sub_id");
                        if (product_object.getString("name").isEmpty()) {
                            product_name = "No Name";
                        } else
                            product_name = product_object.getString("name");
                        String product_description = Html.fromHtml(product_object.getString("description")).toString();
                        String product_price = product_object.getString("price");
                        String product_costprice = product_object.getString("cost_price");
                        String product_quantity = product_object.getString("p_qty");
                        String product_image = product_object.getString("image");
                        insertProducts(Integer.parseInt(product_catId), Integer.parseInt(product_subId), product_name, product_description, Integer.parseInt(product_costprice), Integer.parseInt(product_price), Integer.parseInt(product_quantity), product_image);

                    }


                    JSONArray products_grabbed = allData.getJSONArray("products_grabbed");
                    for (int i = 0; i < products_grabbed.length(); i++) {
                        JSONObject grabbed_object = products_grabbed.getJSONObject(i);
                        String product_catId = grabbed_object.getString("cat_id");
                        String product_subId = grabbed_object.getString("sub_id");
                        if (grabbed_object.getString("name").isEmpty()) {
                            product_name = "No Name";
                        } else
                            product_name = grabbed_object.getString("name");
                        String product_description = Html.fromHtml(grabbed_object.getString("description")).toString();
                        String product_price = grabbed_object.getString("price");
                        String product_costprice = grabbed_object.getString("cost_price");
                        String product_quantity = grabbed_object.getString("p_qty");
                        String product_image = grabbed_object.getString("image");
                        insertGrabbed(Integer.parseInt(product_catId), Integer.parseInt(product_subId), product_name, product_description, Integer.parseInt(product_costprice), Integer.parseInt(product_price), Integer.parseInt(product_quantity), product_image);

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

        }


    }


}