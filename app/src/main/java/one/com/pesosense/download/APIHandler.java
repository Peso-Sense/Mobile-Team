package one.com.pesosense.download;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by administration on 7/16/15.
 */
public class APIHandler {

    final String TAG_GET = "get";
    final String TAG_POST= "post";

    String method,url;
    String[] params;

    HttpClient httpClient;
    HttpResponse httpResponse;
    HttpGet httpGet;
    HttpPost httpPost;
    HttpEntity httpEntity;
    List<NameValuePair> httpParams;
    JSONObject jsonParams;
    StringEntity stringEntity;


    public APIHandler(){

    }

    public String httpMakeRequest(String url,Map<String,String> params,String method){
        String response = null;
        httpParams = new ArrayList<NameValuePair>();
        httpClient = new DefaultHttpClient();
        jsonParams = new JSONObject();


        if(method.equalsIgnoreCase(TAG_GET)){
            for(Map.Entry<String,String> entry: params.entrySet()){
            httpParams.add(new BasicNameValuePair(entry.getKey(),entry.getValue().toString()));
            }

            httpGet = new HttpGet(url+"?"+ URLEncodedUtils.format(httpParams,"utf-8"));
            Log.d("tag","url: " +httpGet.getURI());
            try{
                httpResponse = httpClient.execute(httpGet);
                httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);

            }catch(Exception e){
                e.printStackTrace();
            }



        }else if(method.equalsIgnoreCase(TAG_POST)){

            httpPost = new HttpPost(url);
            for(Map.Entry<String,String> entry: params.entrySet()){
                try {
                    jsonParams.put(entry.getKey(),entry.getValue().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            try {
                stringEntity = new StringEntity(jsonParams.toString(),"utf-8");
                stringEntity.setContentType("application/json");
                httpPost.setEntity(stringEntity);
                Log.d("tag", "entities: " + EntityUtils.toString(httpPost.getEntity()));
                httpResponse = httpClient.execute(httpPost);
                httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
            }catch(Exception e){
                e.printStackTrace();
            }

        }


        return response;
    }




}
