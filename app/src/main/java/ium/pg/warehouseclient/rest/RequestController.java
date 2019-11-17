package ium.pg.warehouseclient.rest;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import ium.pg.warehouseclient.domain.Tyre;
import ium.pg.warehouseclient.util.GsonRequest;

public class RequestController {

    private final static String SERVER_URL = "https://0f38e30c.ngrok.io/";
    private final static String RESOURCE_PATH = "tyres/";

    private final static int DEFAULT_TIMEOUT_MILIS = 10000;

    private RequestQueue requestQueue;

    public void getAll(Activity activity) {
        String url = SERVER_URL + RESOURCE_PATH;

        requestQueue = Volley.newRequestQueue(activity);
        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {
                    Toast.makeText(activity.getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    Log.i("Response", response.toString());
                },
                error -> {
                    Toast.makeText(activity.getApplicationContext(),
                            getErrorFromResponse(error), Toast.LENGTH_LONG).show();
                    Log.i("Error", getErrorFromResponse(error));
                });
        requestQueue.add(request);
    }

    public void getSingle(long id, Activity activity) {
        String url = SERVER_URL + RESOURCE_PATH + id;

        requestQueue = Volley.newRequestQueue(activity);
        GsonRequest<Tyre> request = new GsonRequest<>(Request.Method.GET, url, Tyre.class, null,
                response -> {
                    Toast.makeText(activity.getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    Log.i("Response", response.toString());
                },
                error -> {
                    Toast.makeText(activity.getApplicationContext(),
                            getErrorFromResponse(error), Toast.LENGTH_LONG).show();
                    Log.i("Error", getErrorFromResponse(error));
                });
        requestQueue.add(request);
    }

    public void add(Tyre tyre, Activity activity) {
        String url = SERVER_URL + RESOURCE_PATH;

        JSONObject json = new JSONObject();
        try {
            json.put("producer", tyre.getProducer());
            json.put("name", tyre.getName());
            json.put("rimSize", tyre.getRimSize());
            json.put("price", tyre.getPrice());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        requestQueue = Volley.newRequestQueue(activity);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                response -> {
                    Toast.makeText(activity.getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    Log.i("Response", response.toString());
                },
                error -> {
                    Toast.makeText(activity.getApplicationContext(),
                            getErrorFromResponse(error), Toast.LENGTH_LONG).show();
                    Log.i("Error", getErrorFromResponse(error));
                });
        disableRetryingAndIncreaseTimeout(request);
        requestQueue.add(request);
    }

    public void modify(Tyre tyre, Activity activity) {
        String url = SERVER_URL + RESOURCE_PATH + tyre.getId();

        JSONObject json = new JSONObject();
        try {
            json.put("producer", tyre.getProducer());
            json.put("name", tyre.getName());
            json.put("rimSize", tyre.getRimSize());
            json.put("price", tyre.getPrice());
            json.put("quantity", tyre.getQuantity());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(activity);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, json,
                response -> {
                    Toast.makeText(activity.getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    Log.i("Response", response.toString());
                },
                error -> {
                    Toast.makeText(activity.getApplicationContext(),
                            getErrorFromResponse(error), Toast.LENGTH_LONG).show();
                    Log.i("Error", getErrorFromResponse(error));
                });
        disableRetryingAndIncreaseTimeout(request);
        requestQueue.add(request);
    }

    public void changeQuantity(long id, int change, Activity activity) {
        String url = SERVER_URL + RESOURCE_PATH + id + "/quantity/" + change;

        requestQueue = Volley.newRequestQueue(activity);
        GsonRequest<Tyre> request = new GsonRequest<>(Request.Method.PATCH, url, Tyre.class, null,
                response -> {
                    Toast.makeText(activity.getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    Log.i("Response", response.toString());
                },
                error -> {
                    Toast.makeText(activity.getApplicationContext(),
                            getErrorFromResponse(error), Toast.LENGTH_LONG).show();
                    Log.i("Error", getErrorFromResponse(error));
                });
        disableRetryingAndIncreaseTimeout(request);
        requestQueue.add(request);
    }

    public void delete(long id, Activity activity) {
        String url = SERVER_URL + RESOURCE_PATH + id;

        requestQueue = Volley.newRequestQueue(activity);
        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    Toast.makeText(activity.getApplicationContext(),
                            "Product deleted successfully!", Toast.LENGTH_LONG).show();
                    Log.i("Response", response);
                },
                error -> {
                    Toast.makeText(activity.getApplicationContext(),
                            getErrorFromResponse(error), Toast.LENGTH_LONG).show();
                    Log.i("Error", getErrorFromResponse(error));
                });
        disableRetryingAndIncreaseTimeout(request);
        requestQueue.add(request);
    }

    private String getErrorFromResponse(VolleyError error) {
        byte[] responseData = error.networkResponse.data;
        return responseData != null ? new String(responseData, StandardCharsets.UTF_8) : "Unknown error has occured!";
    }

    private Request<?> disableRetryingAndIncreaseTimeout(Request<?> request) {
        return request.setRetryPolicy(new DefaultRetryPolicy(
                DEFAULT_TIMEOUT_MILIS,
                0, //No retries
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
