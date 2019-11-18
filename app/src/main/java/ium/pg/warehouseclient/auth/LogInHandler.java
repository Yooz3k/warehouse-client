package ium.pg.warehouseclient.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import ium.pg.warehouseclient.activity.warehousemanagement.WarehouseManagementActivity;
import ium.pg.warehouseclient.util.SharedPreferencesNames;

public class LogInHandler {

    public void handleSuccess(final Activity activity, JSONObject response) {
        Toast.makeText(activity.getApplicationContext(),
                "Logged in successfully!", Toast.LENGTH_LONG).show();
        Log.i("Auth", "Logged in");

        try {
            addTokenToSharedPref(activity, response);
            Intent intent = new Intent(activity, WarehouseManagementActivity.class);
            activity.startActivity(intent);
        } catch (JSONException ex) {
            Toast.makeText(activity.getApplicationContext(),
                    "Failed to log in!", Toast.LENGTH_LONG).show();
            Log.e("Auth", ex.getMessage());
        }
    }

    public void handleFailure(final Activity activity, VolleyError error) {
        Toast.makeText(activity.getApplicationContext(),
                "Failed to log in!", Toast.LENGTH_LONG).show();
        Log.i("Error", getErrorFromResponse(error));
    }

    private void addTokenToSharedPref(Activity activity, JSONObject response) throws JSONException {
        SharedPreferences pref = activity.getSharedPreferences(SharedPreferencesNames.TOKENS_PREF_NAME, Context.MODE_PRIVATE);
        pref.edit()
                .putString(SharedPreferencesNames.BEARER_KEY, "Bearer " + response.getString("token"))
                .commit();
    }

    private String getErrorFromResponse(VolleyError error) {
        if (error != null && error.networkResponse != null) {
            byte[] responseData = error.networkResponse.data;
            return responseData != null ? new String(responseData, StandardCharsets.UTF_8) : "Unknown error has occured!";
        }

        return "Connection error!";
    }
}
