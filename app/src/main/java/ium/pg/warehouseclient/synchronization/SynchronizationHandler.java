package ium.pg.warehouseclient.synchronization;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ium.pg.warehouseclient.domain.Tyre;
import ium.pg.warehouseclient.persistence.DaoController;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SynchronizationHandler {

    private final Activity activity;

    public void handleSuccess(JSONArray response) {
        Log.i("Response", "Synchronization successful");

        List<Tyre> receivedTyres = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject responseElement = response.getJSONObject(i);
                Tyre tyre = Tyre.builder()
                        .id(responseElement.getLong("id"))
                        .producer(responseElement.getString("producer"))
                        .name(responseElement.getString("name"))
                        .rimSize(responseElement.getInt("rimSize"))
                        .quantity(responseElement.getInt("quantity"))
                        .price(responseElement.getDouble("price"))
                        .deleted(responseElement.getBoolean("deleted"))
                        .toBeAdded(false)
                        .build();
                receivedTyres.add(tyre);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(activity.getApplicationContext(),
                        "Error during reading remotely stored products!", Toast.LENGTH_LONG).show();
            }
        }

        DaoController daoController = new DaoController(activity);
        daoController.overrideWithSynchronized(receivedTyres);
        Toast.makeText(activity.getApplicationContext(),
                "Synchronization finished!", Toast.LENGTH_LONG).show();
    }

    public void handleFailure(VolleyError error) {
        Log.i("Error", getErrorFromResponse(error));
        Toast.makeText(activity.getApplicationContext(),
                getErrorFromResponse(error), Toast.LENGTH_LONG).show();
    }

    private String getErrorFromResponse(VolleyError error) {
        if (error != null && error.networkResponse != null) {
            byte[] responseData = error.networkResponse.data;
            return responseData != null ? new String(responseData, StandardCharsets.UTF_8) : "Unknown error has occured!";
        }

        return "Connection error!";
    }
}
