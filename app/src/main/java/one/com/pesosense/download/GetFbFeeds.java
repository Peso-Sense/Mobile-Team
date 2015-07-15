package one.com.pesosense.download;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

import one.com.pesosense.UtilsApp;
import one.com.pesosense.helper.DatabaseHelper;

/**
 * Created by mykelneds on 7/8/15.
 */
public class GetFbFeeds {

    private String URL = "https://graph.facebook.com/v2.3/229179187266839/posts?access_token=";
    private String QUERY = "1602942913280381|a662f932e1409f7b9fd483e4698c878b";
    public String profPic = "https://graph.facebook.com/229179187266839/picture?height=150&width=150&redirect=false";
    public String response = "";

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ContentValues values;

    Context context;

    String id = "";
    String picture = "";
    String link = ""; // IMAGE LINK;
    String message = ""; // MESSAGE BODY;
    int ilikes = 0;
    int icomments = 0;
    String timestamp = "";

    int limit = 5;

    public GetFbFeeds(Context context) {
        this.context = context;

        dbHelper = DatabaseHelper.getInstance(context);
    }

    public String getData() {

        deleteDB();

        String nextUrl = "";

        HttpGet request = new HttpGet(URL + URLEncoder.encode(QUERY) + "&limit=" + limit);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            HttpResponse httpResponse = httpclient.execute(request);
            HttpEntity httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

            JSONObject jsonObj;
            JSONArray jsonArray;


            if (response != null) {
                try {
                    jsonObj = new JSONObject(response);
                    jsonArray = jsonObj.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        id = c.getString("id");

                        if ((c.getString("type").equalsIgnoreCase("photo") || c.getString("type").equalsIgnoreCase("status")) && c.has("object_id") && c.has("likes")) { // (s) PesoSense Facebook image feed

                            HttpGet enlarge = new HttpGet("https://graph.facebook.com/v2.3/" + c.getString("object_id") + "?access_token=" + URLEncoder.encode(QUERY));
                            // &limit=25
                            //
                            HttpResponse httpResponse1 = httpclient.execute(enlarge);
                            HttpEntity httpEntity1 = httpResponse1.getEntity();
                            String imgLink = EntityUtils.toString(httpEntity1);
                            Log.d("imgLink", imgLink);
                            JSONObject jsonObj1 = new JSONObject(imgLink);
                            JSONArray jsonArray1 = jsonObj1.getJSONArray("images");
                            JSONObject largeImg = jsonArray1.getJSONObject(jsonArray1.length() - 1);

                            link = largeImg.getString("source");
                            if (c.has("message"))
                                message = c.getString("message");
                            else if (c.has("description"))
                                message = c.getString("description");


                            HttpGet likes = new HttpGet("https://graph.facebook.com/" + c.getString("id") + "/likes?summary=true&access_token=" + URLEncoder.encode(QUERY));
                            HttpResponse httpResponse2 = httpclient.execute(likes);
                            HttpEntity httpEntity2 = httpResponse2.getEntity();
                            String strLikes = EntityUtils.toString(httpEntity2);

                            JSONObject jsonObj2 = new JSONObject(strLikes);

                            ilikes = jsonObj2.getJSONObject("summary").getInt("total_count");

                            HttpGet comments = new HttpGet("https://graph.facebook.com/" + c.getString("object_id") + "/comments?summary=true&access_token=" + URLEncoder.encode(QUERY));
                            HttpResponse httpResponse3 = httpclient.execute(comments);
                            HttpEntity httpEntity3 = httpResponse3.getEntity();
                            String strComments = EntityUtils.toString(httpEntity3);

                            JSONObject jsonObj3 = new JSONObject(strComments);
                            icomments = jsonObj3.getJSONObject("summary").getInt("total_count");

                            HttpGet profilePic = new HttpGet(profPic);
                            HttpResponse httpResponse4 = httpclient.execute(profilePic);
                            HttpEntity httpEntity4 = httpResponse4.getEntity();
                            String pic = EntityUtils.toString(httpEntity4);

                            JSONObject jsonObj4 = new JSONObject(pic);
                            picture = jsonObj4.getJSONObject("data").getString("url");

                            if (c.has("updated_time"))
                                timestamp = c.getString("updated_time");
                            else
                                timestamp = c.getString("created_time");

                            if (!isDataExist("tbl_fb_image", id)) {
                                populateFbFeeds(id, 0, timestamp);
                                populateFbImage(id, picture, message, link, ilikes, icomments, timestamp);
                            }
                        } // (e) PesoSense Facebook image feed
                        else if (c.getString("type").equalsIgnoreCase("video") && c.has("likes")) {       // (s) PesoSense Facebook video feed
                            link = c.getString("source");
                            if (c.has("message"))
                                message = c.getString("message");
                            else if (c.has("description")) {
                                message = c.getString("description");
                            }

                            HttpGet likes = new HttpGet("https://graph.facebook.com/" + c.getString("id") + "/likes?summary=true&access_token=" + URLEncoder.encode(QUERY));
                            HttpResponse httpResponse2 = httpclient.execute(likes);
                            HttpEntity httpEntity2 = httpResponse2.getEntity();
                            String strLikes = EntityUtils.toString(httpEntity2);

                            JSONObject jsonObj2 = new JSONObject(strLikes);

                            ilikes = jsonObj2.getJSONObject("summary").getInt("total_count");

//                            HttpGet comments = new HttpGet("https://graph.facebook.com/" + c.getString("object_id") + "/comments?summary=true&access_token=" + URLEncoder.encode(QUERY));
//                            HttpResponse httpResponse3 = httpclient.execute(comments);
//                            HttpEntity httpEntity3 = httpResponse3.getEntity();
//                            String strComments = EntityUtils.toString(httpEntity3);
//
//                            JSONObject jsonObj3 = new JSONObject(strComments);
//                            icomments = jsonObj3.getJSONObject("summary").getInt("total_count");

                            icomments = 0;

                            HttpGet profilePic = new HttpGet(profPic);
                            HttpResponse httpResponse4 = httpclient.execute(profilePic);
                            HttpEntity httpEntity4 = httpResponse4.getEntity();
                            String pic = EntityUtils.toString(httpEntity4);

                            JSONObject jsonObj4 = new JSONObject(pic);
                            picture = jsonObj4.getJSONObject("data").getString("url");

                            if (c.has("updated_time"))
                                timestamp = c.getString("updated_time");
                            else
                                timestamp = c.getString("created_time");

                            if (!isDataExist("tbl_fb_video", id)) {
                                populateFbFeeds(id, 1, timestamp);
                                populateFbVideo(id, picture, message, link, ilikes, icomments, timestamp);
                            }


                        }   // (e) PesoSense Facebook video feed
                    }
                    nextUrl = jsonObj.getJSONObject("paging").getString("next");
                    Log.d("nextURL", nextUrl);

                    UtilsApp.putString("nextUrl", "NEXT URL: " + nextUrl);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return nextUrl;
    }


    public void deleteDB() {

        Log.d("DATABASE", "Database Deleted!!");
        db = dbHelper.getWritableDatabase();
        db.delete("tbl_fb_image", null, null);
        db.delete("tbl_fb_video", null, null);
        db.delete("tbl_fb_feeds", null, null);
        db.close();
    }

    public String getNext(String nextUrl) {


        HttpGet request = new HttpGet(nextUrl.replaceAll("access_token=[^&]+", "access_token=" + URLEncoder.encode(QUERY)));
        HttpClient httpclient = new DefaultHttpClient();

        try {
            HttpResponse httpResponse = httpclient.execute(request);
            HttpEntity httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

            JSONObject jsonObj;
            JSONArray jsonArray;


            if (response != null) {
                try {
                    jsonObj = new JSONObject(response);
                    jsonArray = jsonObj.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        id = c.getString("id");

                        if ((c.getString("type").equalsIgnoreCase("photo") || c.getString("type").equalsIgnoreCase("status")) && c.has("object_id") && c.has("likes")) { // (s) PesoSense Facebook image feed

                            HttpGet enlarge = new HttpGet("https://graph.facebook.com/v2.3/" + c.getString("object_id") + "?access_token=" + URLEncoder.encode(QUERY));
                            // &limit=25
                            //
                            HttpResponse httpResponse1 = httpclient.execute(enlarge);
                            HttpEntity httpEntity1 = httpResponse1.getEntity();
                            String imgLink = EntityUtils.toString(httpEntity1);
                            Log.d("imgLink", imgLink);
                            JSONObject jsonObj1 = new JSONObject(imgLink);
                            JSONArray jsonArray1 = jsonObj1.getJSONArray("images");
                            JSONObject largeImg = jsonArray1.getJSONObject(jsonArray1.length() - 1);

                            link = largeImg.getString("source");
                            if (c.has("message"))
                                message = c.getString("message");
                            else if (c.has("description"))
                                message = c.getString("description");


                            HttpGet likes = new HttpGet("https://graph.facebook.com/" + c.getString("id") + "/likes?summary=true&access_token=" + URLEncoder.encode(QUERY));
                            HttpResponse httpResponse2 = httpclient.execute(likes);
                            HttpEntity httpEntity2 = httpResponse2.getEntity();
                            String strLikes = EntityUtils.toString(httpEntity2);

                            JSONObject jsonObj2 = new JSONObject(strLikes);

                            ilikes = jsonObj2.getJSONObject("summary").getInt("total_count");

                            HttpGet comments = new HttpGet("https://graph.facebook.com/" + c.getString("object_id") + "/comments?summary=true&access_token=" + URLEncoder.encode(QUERY));
                            HttpResponse httpResponse3 = httpclient.execute(comments);
                            HttpEntity httpEntity3 = httpResponse3.getEntity();
                            String strComments = EntityUtils.toString(httpEntity3);

                            JSONObject jsonObj3 = new JSONObject(strComments);
                            icomments = jsonObj3.getJSONObject("summary").getInt("total_count");

                            HttpGet profilePic = new HttpGet(profPic);
                            HttpResponse httpResponse4 = httpclient.execute(profilePic);
                            HttpEntity httpEntity4 = httpResponse4.getEntity();
                            String pic = EntityUtils.toString(httpEntity4);

                            JSONObject jsonObj4 = new JSONObject(pic);
                            picture = jsonObj4.getJSONObject("data").getString("url");

                            if (c.has("updated_time"))
                                timestamp = c.getString("updated_time");
                            else
                                timestamp = c.getString("created_time");

                            if (!isDataExist("tbl_fb_image", id)) {
                                populateFbFeeds(id, 0, timestamp);
                                populateFbImage(id, picture, message, link, ilikes, icomments, timestamp);
                            }
                        } // (e) PesoSense Facebook image feed
                        else if (c.getString("type").equalsIgnoreCase("video") && c.has("likes")) {       // (s) PesoSense Facebook video feed
                            link = c.getString("source");
                            if (c.has("message"))
                                message = c.getString("message");
                            else if (c.has("description")) {
                                message = c.getString("description");
                            }

                            HttpGet likes = new HttpGet("https://graph.facebook.com/" + c.getString("id") + "/likes?summary=true&access_token=" + URLEncoder.encode(QUERY));
                            HttpResponse httpResponse2 = httpclient.execute(likes);
                            HttpEntity httpEntity2 = httpResponse2.getEntity();
                            String strLikes = EntityUtils.toString(httpEntity2);

                            JSONObject jsonObj2 = new JSONObject(strLikes);

                            ilikes = jsonObj2.getJSONObject("summary").getInt("total_count");

//                            HttpGet comments = new HttpGet("https://graph.facebook.com/" + c.getString("object_id") + "/comments?summary=true&access_token=" + URLEncoder.encode(QUERY));
//                            HttpResponse httpResponse3 = httpclient.execute(comments);
//                            HttpEntity httpEntity3 = httpResponse3.getEntity();
//                            String strComments = EntityUtils.toString(httpEntity3);
//
//                            JSONObject jsonObj3 = new JSONObject(strComments);
//                            icomments = jsonObj3.getJSONObject("summary").getInt("total_count");

                            icomments = 0;

                            HttpGet profilePic = new HttpGet(profPic);
                            HttpResponse httpResponse4 = httpclient.execute(profilePic);
                            HttpEntity httpEntity4 = httpResponse4.getEntity();
                            String pic = EntityUtils.toString(httpEntity4);

                            JSONObject jsonObj4 = new JSONObject(pic);
                            picture = jsonObj4.getJSONObject("data").getString("url");

                            if (c.has("updated_time"))
                                timestamp = c.getString("updated_time");
                            else
                                timestamp = c.getString("created_time");

                            if (!isDataExist("tbl_fb_video", id)) {
                                populateFbFeeds(id, 1, timestamp);
                                populateFbVideo(id, picture, message, link, ilikes, icomments, timestamp );
                            }


                        }   // (e) PesoSense Facebook video feed
                    }
                    nextUrl = jsonObj.getJSONObject("paging").getString("next");
                    Log.d("nextURL", nextUrl);

                    UtilsApp.putString("nextUrl", "NEXT URL: " + nextUrl);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return nextUrl;
    }

    public boolean isDataExist(String tableName, String id) {

        boolean exists = false;
        String tempID = "";

        db = dbHelper.getReadableDatabase();
        cursor = db.query(tableName, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                tempID = cursor.getString(0);

                if (id.equals(tempID)) {
                    Log.d("DATABASE", "EXISTS " + id);
                    exists = true;
                    break;
                }
            }
        }
        return exists;
    }

    public void populateFbImage(String id, String picture, String message, String link, int likes, int comment, String timestamp) {

        db = dbHelper.getWritableDatabase();
        values = new ContentValues();
        values.put("id", id);
        values.put("profile_pic", picture);
        values.put("message", message);
        values.put("link", link);
        values.put("likes", likes);
        values.put("comment", comment);
        values.put("timestamp", timestamp);

        db.insert("tbl_fb_image", null, values);
        db.close();

        Log.d("DATABASE", "INSERTED!! : IMAGE");

    }

    public void populateFbVideo(String id, String picture, String message, String link, int likes, int comment, String timestamp) {

        db = dbHelper.getWritableDatabase();
        values = new ContentValues();
        values.put("id", id);
        values.put("profile_pic", picture);
        values.put("message", message);
        values.put("link", link);
        values.put("likes", likes);
        values.put("comment", comment);
        values.put("timestamp", timestamp);

        db.insert("tbl_fb_video", null, values);
        db.close();

        Log.d("DATABASE", "INSERTED!! : VIDEO");

    }

    public void populateFbFeeds(String id, int type, String timestamp) {

        db = dbHelper.getWritableDatabase();
        values = new ContentValues();
        values.put("id", id);
        values.put("type", type);
        values.put("timestamp", timestamp);
        db.insert("tbl_fb_feeds", null, values);
        db.close();

    }
}
