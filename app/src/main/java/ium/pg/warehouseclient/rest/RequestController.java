package ium.pg.warehouseclient.rest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import ium.pg.warehouseclient.activity.warehousemanagement.WarehouseManagementActivity;
import ium.pg.warehouseclient.domain.Tyre;
import ium.pg.warehouseclient.util.CustomRequest;

public class RequestController {

    private final static String SERVER_URL = "https://1aab3ed9.ngrok.io/";
    private final static String RESOURCE_PATH = "tyres/";
    private final static String LOGIN_PATH = "auth/login";

    private final static int DEFAULT_TIMEOUT_MILLIS = 10000;

    private RequestQueue requestQueue;

    public void login(String login, String password, Activity activity) {
        String url = SERVER_URL + LOGIN_PATH;

        JSONObject json = new JSONObject();
        try {
            json.put("username", login);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        requestQueue = Volley.newRequestQueue(activity);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                response -> {
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
                },
                error -> {
                    Toast.makeText(activity.getApplicationContext(),
                            "Failed to log in!", Toast.LENGTH_LONG).show();
                    Log.i("Error", getErrorFromResponse(error));
                });
        requestQueue.add(request);
    }

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
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = getAuthorizationHeaders(activity);
                headers.putAll(headersSys);
                return headers;
            }
        };
        requestQueue.add(request);
    }

    public void getSingle(long id, Activity activity) {
        String url = SERVER_URL + RESOURCE_PATH + id;

        requestQueue = Volley.newRequestQueue(activity);
        CustomRequest<Tyre> request = new CustomRequest<>(Request.Method.GET, url, Tyre.class, null,
                response -> {
                    Toast.makeText(activity.getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    Log.i("Response", response.toString());
                },
                error -> {
                    Toast.makeText(activity.getApplicationContext(),
                            getErrorFromResponse(error), Toast.LENGTH_LONG).show();
                    Log.i("Error", getErrorFromResponse(error));
                });
        request.addHeaders(getAuthorizationHeaders(activity));
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
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = getAuthorizationHeaders(activity);
                headers.putAll(headersSys);
                return headers;
            }
        };
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
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = getAuthorizationHeaders(activity);
                headers.putAll(headersSys);
                return headers;
            }
        };
        disableRetryingAndIncreaseTimeout(request);
        requestQueue.add(request);
    }

    public void changeQuantity(long id, int change, Activity activity) {
        String url = SERVER_URL + RESOURCE_PATH + id + "/quantity/" + change;

        requestQueue = Volley.newRequestQueue(activity);
        CustomRequest<Tyre> request = new CustomRequest<>(Request.Method.PATCH, url, Tyre.class, null,
                response -> {
                    Toast.makeText(activity.getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    Log.i("Response", response.toString());
                },
                error -> {
                    Toast.makeText(activity.getApplicationContext(),
                            getErrorFromResponse(error), Toast.LENGTH_LONG).show();
                    Log.i("Error", getErrorFromResponse(error));
                });
        request.addHeaders(getAuthorizationHeaders(activity));
        disableRetryingAndIncreaseTimeout(request);
        requestQueue.add(request);
    }

    public void delete(long id, Activity activity) {
        String url = SERVER_URL + RESOURCE_PATH + id;

        requestQueue = Volley.newRequestQueue(activity);
        CustomRequest<String> request = new CustomRequest<>(Request.Method.DELETE, url, String.class, null,
                response -> {
                    Toast.makeText(activity.getApplicationContext(),
                            "Product deleted successfully!", Toast.LENGTH_LONG).show();
                    Log.i("Response", "Deleted");
                },
                error -> {
                    Toast.makeText(activity.getApplicationContext(),
                            getErrorFromResponse(error), Toast.LENGTH_LONG).show();
                    Log.i("Error", getErrorFromResponse(error));
                });
        request.addHeaders(getAuthorizationHeaders(activity));
        disableRetryingAndIncreaseTimeout(request);
        requestQueue.add(request);
    }

    private String getErrorFromResponse(VolleyError error) {
        byte[] responseData = error.networkResponse.data;
        return responseData != null ? new String(responseData, StandardCharsets.UTF_8) : "Unknown error has occured!";
    }

    private void addLogInResultToSharedPref(Activity activity, boolean success) {
        SharedPreferences pref = activity.getPreferences(Context.MODE_PRIVATE);
        pref.edit()
                .clear()
                .putBoolean("login_success", success)
                .commit();
    }

    private void addTokenToSharedPref(Activity activity, JSONObject response) throws JSONException {
        SharedPreferences pref = activity.getSharedPreferences("TOKENS", Context.MODE_PRIVATE);
        pref.edit()
                .putString("JWT_BEARER", "Bearer " + response.getString("token"))
                .commit();
    }

    private Map<String, String> getAuthorizationHeaders(Context context) {
        SharedPreferences pref = context.getSharedPreferences("TOKENS", Context.MODE_PRIVATE);
        String headerValue = pref.getString("JWT_BEARER", null);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", headerValue);
        return headers;
    }

    private Request<?> disableRetryingAndIncreaseTimeout(Request<?> request) {
        return request.setRetryPolicy(new DefaultRetryPolicy(
                DEFAULT_TIMEOUT_MILLIS,
                0, //No retries
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
