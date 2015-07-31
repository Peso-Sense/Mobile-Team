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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mykelneds on 7/23/15.
 */
public class DownloadHelper {
    private final String URL = "http://search.onesupershop.com/api";
    private final String URL_FB_AUTH = URL + "/auth/facebook";
    private final String URL_EMAIL = URL + "/auth";
    private final String URL_USER_DETAILS = URL + "/me";
    private final String URL_REGISTER_USER = URL + "/register";
    private final String URL_FORGET_PASSWORD = URL + "/password/forgot";
    private final String URL_CHANGED_PASSWORD = URL + "/password/reset";

    private final String AUTH_RESET = "password reset code sent to email";
    private final String AUTH_PASSWORD_CHANGED = "password changed";

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

    public boolean forgetPassword(Map<String, String> data) {

        boolean success = true;

        response = api.httpMakeRequest(URL_FORGET_PASSWORD, data, POST);
        Log.d(TAG, "Forget Password response: " + response);
        success = response(response);
        return success;
    }

    public boolean changedPassword(Map<String, String> data) {

        boolean success = true;

        response = api.httpMakeRequest(URL_CHANGED_PASSWORD, data, POST);
        Log.d(TAG, "Change Password Response: " + response);
        success = response(response);
        return success;
    }

    // use to parse the authentication message;
    public boolean response(String response) {

        JSONObject jsonObject;
        String message;

        try {
            jsonObject = new JSONObject(response);

            if (jsonObject.has("message")) {
                message = jsonObject.getString("message");

                //authentication for reset password;
                if (message.equalsIgnoreCase(AUTH_RESET) || message.equals(AUTH_PASSWORD_CHANGED)) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
