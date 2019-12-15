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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ium.pg.warehouseclient.activity.show.ShowActivity;
import ium.pg.warehouseclient.auth.LogInHandler;
import ium.pg.warehouseclient.auth.RegisterHandler;
import ium.pg.warehouseclient.domain.Tyre;
import ium.pg.warehouseclient.synchronization.SynchronizationHandler;
import ium.pg.warehouseclient.util.CustomRequest;
import ium.pg.warehouseclient.util.SharedPreferencesNames;
import ium.pg.warehouseclient.util.TyreConverter;

public class RequestController {

    private final static String SERVER_URL = "https://922b4ab4.ngrok.io/";
    private final static String RESOURCE_PATH = "tyres/";
    private final static String SYNCHRONIZE_PATH = "tyres/synchronize";
    private final static String REGISTER_PATH = "auth/register";
    private final static String LOGIN_PATH = "auth/login";
    private final static String GOOGLE_LOGIN_PATH = "auth/google/login";

    private final static int DEFAULT_TIMEOUT_MILLIS = 10000;

    private TyreConverter tyreConverter = new TyreConverter();
    private LogInHandler logInHandler = new LogInHandler();
    private RequestQueue requestQueue;

    public void register(String login, String password, Activity activity) {
        String url = SERVER_URL + REGISTER_PATH;

        JSONObject json = new JSONObject();
        try {
            json.put("username", login);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        requestQueue = Volley.newRequestQueue(activity);

        RegisterHandler handler = new RegisterHandler();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, json,
                response -> handler.handleSuccess(activity),
                error -> handler.handleFailure(activity, error));
        disableRetryingAndIncreaseTimeout(request);
        requestQueue.add(request);
    }

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
                response -> logInHandler.handleSuccess(activity, response),
                error -> logInHandler.handleFailure(activity, error));
        disableRetryingAndIncreaseTimeout(request);
        requestQueue.add(request);
    }

    public void verifyGoogleToken(String idToken, Activity activity) {
        String url = SERVER_URL + GOOGLE_LOGIN_PATH;

        JSONObject json = new JSONObject();
        try {
            json.put("idToken", idToken);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        requestQueue = Volley.newRequestQueue(activity);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                response -> logInHandler.handleSuccess(activity, response),
                error -> logInHandler.handleFailure(activity, error));
        disableRetryingAndIncreaseTimeout(request);
        requestQueue.add(request);
    }

    public void synchronize(List<Tyre> tyres, Activity activity) {
        String url = SERVER_URL + SYNCHRONIZE_PATH;

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();
        JSONArray jsonTyres = new JSONArray();
        tyres.forEach(tyre -> jsonTyres.put(gson.toJson(tyre)));

        SynchronizationHandler handler = new SynchronizationHandler(activity);

        requestQueue = Volley.newRequestQueue(activity);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, jsonTyres,
                handler::handleSuccess, handler::handleFailure) {
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

    public void getAll(Activity activity) {
        String url = SERVER_URL + RESOURCE_PATH;

        requestQueue = Volley.newRequestQueue(activity);
        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {
                    Log.i("Response", response.toString());
                    Toast.makeText(activity.getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                },
                error -> {
                    Log.i("Error", getErrorFromResponse(error));
                    Toast.makeText(activity.getApplicationContext(),
                            getErrorFromResponse(error), Toast.LENGTH_LONG).show();
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
                    Log.i("Response", response.toString());
//                    Toast.makeText(activity.getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    showResult(response, activity);
                },
                error -> {
                    Log.i("Error", getErrorFromResponse(error));
                    Toast.makeText(activity.getApplicationContext(),
                            getErrorFromResponse(error), Toast.LENGTH_LONG).show();
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
                    Log.i("Response", response.toString());
//                    Toast.makeText(activity.getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    showResult(tyreConverter.convert(response), activity);
                },
                error -> {
                    Log.i("Error", getErrorFromResponse(error));
                    Toast.makeText(activity.getApplicationContext(),
                            getErrorFromResponse(error), Toast.LENGTH_LONG).show();
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
                    Log.i("Response", response.toString());
//                    Toast.makeText(activity.getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    showResult(tyreConverter.convert(response), activity);
                },
                error -> {
                    Log.i("Error", getErrorFromResponse(error));
                    Toast.makeText(activity.getApplicationContext(),
                            getErrorFromResponse(error), Toast.LENGTH_LONG).show();
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
                    Log.i("Response", response.toString());
//                    Toast.makeText(activity.getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    showResult(response, activity);
                },
                error -> {
                    Log.i("Error", getErrorFromResponse(error));
                    Toast.makeText(activity.getApplicationContext(),
                            getErrorFromResponse(error), Toast.LENGTH_LONG).show();
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
                    Log.i("Response", "Deleted");
                    Toast.makeText(activity.getApplicationContext(),
                            "Product deleted successfully!", Toast.LENGTH_LONG).show();
                },
                error -> {
                    Log.i("Error", getErrorFromResponse(error));
                    Toast.makeText(activity.getApplicationContext(),
                            getErrorFromResponse(error), Toast.LENGTH_LONG).show();
                });
        request.addHeaders(getAuthorizationHeaders(activity));
        disableRetryingAndIncreaseTimeout(request);
        requestQueue.add(request);
    }

    private void showResult(Tyre tyre, Activity activity) {
        Intent intent = new Intent(activity, ShowActivity.class);
        intent.putExtra("result", tyre);
        activity.startActivity(intent);
    }

    private String getErrorFromResponse(VolleyError error) {
        if (error != null && error.networkResponse != null) {
            byte[] responseData = error.networkResponse.data;
            return responseData != null ? new String(responseData, StandardCharsets.UTF_8) : "Unknown error has occured!";
        }

        return "Connection error!";
    }

    private Map<String, String> getAuthorizationHeaders(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SharedPreferencesNames.TOKENS_PREF_NAME, Context.MODE_PRIVATE);
        String headerValue = pref.getString(SharedPreferencesNames.BEARER_KEY, null);

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
