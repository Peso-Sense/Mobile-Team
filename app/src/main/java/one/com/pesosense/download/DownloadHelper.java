package one.com.pesosense.download;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mykelneds on 7/23/15.
 */
public class DownloadHelper {

    private final String URL_FB_AUTH = "http://search.onesupershop.com/api/auth/facebook";
    private final String URL_EMAIL = "http://search.onesupershop.com/api/auth";
    private final String URL_USER_DETAILS = "http://search.onesupershop.com/api/me";
    private final String URL_REGISTER_USER = "http://search.onesupershop.com/api/register";

    private final String GET = "get";
    private final String POST = "post";
    private final String TAG = "Download Helper";

    APIHandler api;
    String response;

    HttpEntity httpEntity;
    HttpResponse httpResponse;
    List<NameValuePair> httpParams;

    public DownloadHelper() {
        api = new APIHandler();
    }

    public String fbLogin(String token) {

        httpParams = new ArrayList<NameValuePair>();
        httpParams.add(new BasicNameValuePair("access_token", token));

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL_FB_AUTH);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(httpParams));
            httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
            Log.d(TAG, "Facebook Login Response: " + response);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String emailLogin(Map<String, String> data) {

        response = api.httpMakeRequest(URL_EMAIL, data, POST);
        Log.d(TAG, "Email Login Response: " + response);

        return response;
    }

    public String userDetails(Map<String, String> data) {

        response = api.httpMakeRequest(URL_USER_DETAILS, data, GET);
        Log.d(TAG, "User details: " + response);
        return response;
    }

    public String registerUser(Map<String, String> data) {
        response = api.httpMakeRequest(URL_REGISTER_USER, data, POST);
        Log.d(TAG, "Register response: " + response);
        return response;
    }
}
