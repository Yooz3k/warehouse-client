package ium.pg.warehouseclient.auth;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.nio.charset.StandardCharsets;

public class RegisterHandler {

    public void handleSuccess(final Activity activity) {
        Toast.makeText(activity.getApplicationContext(),
                "Registered successfully, now you can log in", Toast.LENGTH_LONG).show();
        Log.i("Auth", "Registered successfully");
    }

    public void handleFailure(final Activity activity, VolleyError error) {
        Toast.makeText(activity.getApplicationContext(),
                getErrorFromResponse(error), Toast.LENGTH_LONG).show();
        Log.i("Error", getErrorFromResponse(error));
    }

    private String getErrorFromResponse(VolleyError error) {
        if (error != null && error.networkResponse != null) {
            byte[] responseData = error.networkResponse.data;
            return responseData != null ? new String(responseData, StandardCharsets.UTF_8) : "Unknown error has occured!";
        }

        return "Connection error!";
    }
}
