package one.com.pesosense.asynctask;

import android.os.AsyncTask;

/**
 * Created by mykelneds on 8/5/15.
 */
public class LoginFacebookTask extends AsyncTask<Void, Void, Void> {

    String token;
    boolean response = false;

    public LoginFacebookTask(String token) {
        this.token = token;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }


}
