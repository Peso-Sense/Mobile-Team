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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mykelneds on 7/23/15.
 */
public class LoginFacebook {


    String url = "http://search.onesupershop.com/api/auth/facebook";
    String token;
    String response;

    HttpEntity httpEntity;
    HttpResponse httpResponse;
    List<NameValuePair> httpParams;
    JSONObject jsonObject;

    public LoginFacebook(String token) {
        this.token = token;
    }

    public String login() {

        httpParams = new ArrayList<NameValuePair>();
        httpParams.add(new BasicNameValuePair("access_token", token));

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(httpParams));
            httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
            Log.d("response", "Login Response: " + response);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
