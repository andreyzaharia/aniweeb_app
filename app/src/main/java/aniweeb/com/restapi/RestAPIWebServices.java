package aniweeb.com.restapi;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import aniweeb.com.R;

public class RestAPIWebServices {

    private Context context;
    private HashMap<String, String> hashMap;
    private String path;
    private static boolean post_ok;
    private JSONObject json, objData;
    String id_user2, token;
    int method_main=0, id_user;

    private SharedPreferences preferences;

    public RestAPIWebServices(Context context, HashMap hashMap, String path) {
        this.context = context;
        this.hashMap = hashMap;
        this.path = path;
    }

    public RestAPIWebServices(Context context, JSONObject objData, String path) {
        this.context = context;
        this.objData = objData;
        this.path = path;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        id_user = preferences.getInt("id_user",id_user);
        token = preferences.getString("token","");
    }
    public RestAPIWebServices(Context context, String path){
        this.context = context;
        this.path = path;
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        id_user = preferences.getInt("id_user",id_user);
        token = preferences.getString("token","");
        Log.e("token rest",token);
    }

    public void logout() {
        SharedPreferences preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
        Intent intent = null;
        ((Activity) context).finish();
        /*intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);*/
    }

    public void postJsonResponse( final VolleyCallback callback){
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, path, objData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = response;
                            if ((jsonObject.getString("res").equalsIgnoreCase("KOT"))) {
                                Toast.makeText(context, context.getResources().getString(R.string.expira_sesion), Toast.LENGTH_SHORT).show();
                                logout();

                            }else{
                                callback.onSuccess(response.toString());
                            }
                        } catch (JSONException e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onErrorResponse(error);
                        String utf8String = "";
                    }
                }) {
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsObjRequest);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }



    public void postResponseApi(final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    callback.onSuccess(response);

                    //callback.onSuccess(response);

                } catch (JSONException e) {
                    Log.e("catch rest", e.toString());
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();

                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("volley rest", error.toString());
                        if (error == null || error.networkResponse == null) {

                            callback.onErrorResponse(error);
                            return;
                        }
                        if(String.valueOf(error.networkResponse.statusCode).equals("401")){
                            Log.e("ERROR 401 ", error.networkResponse.toString());
                            Toast.makeText(context, context.getResources().getString(R.string.expira_sesion), Toast.LENGTH_SHORT).show();
                            //logout();

                        }
                        if(String.valueOf(error.networkResponse.statusCode).equals("503")){
                            //Toast.makeText(context, context.getResources().getString(R.string.servidor_caido), Toast.LENGTH_SHORT).show();
                            logout();
                        }
                        method_main = Request.Method.POST;
                        callback.onErrorResponse(error);

                    }
                })



        {
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-MAL-CLIENT-ID", "e23ae05e01e71b0478325eb5281ac713");
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                    params.put(entry.getKey(), entry.getValue());
                }
                //returning parameter
                return params;
            }


            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String utf8String = "";

                try {
                    utf8String = new String(response.data, "UTF-8");
                    return Response.success(new JSONObject(utf8String).toString(), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.toString();
                }
                return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
            }


        };

        //Adding the string request to the queue

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    }

    public void putResponseApi(final VolleyCallback callback) {

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, path,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

/*
                            if ((jsonObject.getString("res").equalsIgnoreCase("KOT"))) {
                                Toast.makeText(context, context.getResources().getString(R.string.expira_sesion), Toast.LENGTH_SHORT).show();
                                logout();

                            }else{*/
                            callback.onSuccess(response);
                            // }




                        } catch (JSONException e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error == null || error.networkResponse == null) {

                            callback.onErrorResponse(error);
                            return;
                        }
                        if(String.valueOf(error.networkResponse.statusCode).equals("401")){
                            //Toast.makeText(context, context.getResources().getString(R.string.expira_sesion), Toast.LENGTH_SHORT).show();
                            logout();

                        }
                        if(String.valueOf(error.networkResponse.statusCode).equals("503")){
                            //Toast.makeText(context, context.getResources().getString(R.string.servidor_caido), Toast.LENGTH_SHORT).show();
                            logout();
                        }
                        method_main = Request.Method.POST;
                        callback.onErrorResponse(error);

                    }
                })



        {
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-MAL-CLIENT-ID", "e23ae05e01e71b0478325eb5281ac713");
                return params;
            }


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                    params.put(entry.getKey(), entry.getValue());
                }
                //returning parameter
                return params;
            }


            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String utf8String = "";

                try {
                    utf8String = new String(response.data, "UTF-8");
                    return Response.success(new JSONObject(utf8String).toString(), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.toString();
                }
                return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
            }


        };

        //Adding the string request to the queue

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    }

    public void postRecoverResponseApi(final VolleyCallback callback) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, path,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            if ((jsonObject.get("code").equals(1010))) {
                                Toast.makeText(context, context.getResources().getString(R.string.expira_sesion), Toast.LENGTH_SHORT).show();
                                logout();
                            }else{
                                callback.onSuccess(response);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error == null || error.networkResponse == null) {

                            callback.onErrorResponse(error);
                            return;
                        }
                        if(String.valueOf(error.networkResponse.statusCode).equals("401")){
                            //Toast.makeText(context, context.getResources().getString(R.string.error_send_password), Toast.LENGTH_SHORT).show();
                            callback.onErrorResponse(error);

                        }
                        if(String.valueOf(error.networkResponse.statusCode).equals("503")){
                            //Toast.makeText(context, context.getResources().getString(R.string.servidor_caido), Toast.LENGTH_SHORT).show();
                            logout();
                        }
                        method_main = Request.Method.POST;
                        //callback.onErrorResponse(error);

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                    params.put(entry.getKey(), entry.getValue());
                }
                //returning parameter
                return params;
            }


            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String utf8String = "";

                try {
                    utf8String = new String(response.data, "UTF-8");
                    return Response.success(new JSONObject(utf8String).toString(), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.toString();
                }
                return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
            }


        };

        //Adding the string request to the queue

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void patchResponseApi(final VolleyCallback callback) {

        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, path,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {




                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);


                            if ((jsonObject.getString("res").equalsIgnoreCase("KOT"))) {
                                Toast.makeText(context, context.getResources().getString(R.string.expira_sesion), Toast.LENGTH_SHORT).show();
                                logout();

                            }else{
                                callback.onSuccess(response);
                            }




                        } catch (JSONException e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();

                        }




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error == null || error.networkResponse == null) {

                            callback.onErrorResponse(error);
                            return;
                        }
                        if(String.valueOf(error.networkResponse.statusCode).equals("401")){
                            //Toast.makeText(context, context.getResources().getString(R.string.expira_sesion), Toast.LENGTH_SHORT).show();
                            logout();

                        }
                        if(String.valueOf(error.networkResponse.statusCode).equals("503")){
                            //Toast.makeText(context, context.getResources().getString(R.string.servidor_caido), Toast.LENGTH_SHORT).show();
                            logout();
                        }
                        callback.onErrorResponse(error);

                    }
                })



        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                    params.put(entry.getKey(), entry.getValue());
                }
                //returning parameter
                return params;
            }


            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String utf8String = "";
                try {
                    utf8String = new String(response.data, "UTF-8");
                    return Response.success(new JSONObject(utf8String).toString(), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.toString();
                }
                return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
            }


        };

        //Adding the string request to the queue

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    }
    public void deleteResponseApi(final VolleyCallback callback) {


        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, path,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {




                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);


                            if ((jsonObject.getString("res").equalsIgnoreCase("KOT"))) {
                                Toast.makeText(context, context.getResources().getString(R.string.expira_sesion), Toast.LENGTH_SHORT).show();
                                logout();

                            }else{
                                callback.onSuccess(response);
                            }




                        } catch (JSONException e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();

                        }




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error == null || error.networkResponse == null) {

                            callback.onErrorResponse(error);
                            return;
                        }
                        if(String.valueOf(error.networkResponse.statusCode).equals("401")){
                            //Toast.makeText(context, context.getResources().getString(R.string.expira_sesion), Toast.LENGTH_SHORT).show();
                            logout();

                        }
                        if(String.valueOf(error.networkResponse.statusCode).equals("503")){
                            //Toast.makeText(context, context.getResources().getString(R.string.servidor_caido), Toast.LENGTH_SHORT).show();
                            logout();
                        }
                        method_main = Request.Method.GET;

                        callback.onErrorResponse(error);

                    }
                })



        {



            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                String utf8String = "";
                try {
                    utf8String = new String(response.data, "UTF-8");
                    return Response.success(new JSONObject(utf8String).toString(), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.toString();
                }
                return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
            }


        };

        //Adding the string request to the queue

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    }


    public void getResponseApi(final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, path,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                         Log.e("response", response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                            callback.onSuccess(response);

                        } catch (JSONException e) {
                            Log.e("catch api", e.toString());
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error == null || error.networkResponse == null) {

                            callback.onErrorResponse(error);
                            return;
                        }
                        if(String.valueOf(error.networkResponse.statusCode).equals("401")){
                            //Toast.makeText(context, context.getResources().getString(R.string.expira_sesion), Toast.LENGTH_SHORT).show();
                            logout();

                        }
                        if(String.valueOf(error.networkResponse.statusCode).equals("503")){
                            //Toast.makeText(context, context.getResources().getString(R.string.servidor_caido), Toast.LENGTH_SHORT).show();
                            logout();
                        }
                        method_main = Request.Method.GET;

                        callback.onErrorResponse(error);

                    }
                })

        {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-MAL-CLIENT-ID", "e23ae05e01e71b0478325eb5281ac713");
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                String utf8String = "";
                try {
                    utf8String = new String(response.data, "UTF-8");
                    return Response.success(new JSONObject(utf8String).toString(), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.toString();
                }
                return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
            }

        };


        //Adding the string request to the queue

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void stringResponseApi(final int method , final VolleyCallback callback) {


        StringRequest stringRequest = new StringRequest(method, path,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {




                        callback.onSuccess(response);




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error == null || error.networkResponse == null) {

                            callback.onErrorResponse(error);
                            return;
                        }
                        if(String.valueOf(error.networkResponse.statusCode).equals("401")){
                            //Toast.makeText(context, context.getResources().getString(R.string.expira_sesion), Toast.LENGTH_SHORT).show();
                            logout();

                        }
                        if(String.valueOf(error.networkResponse.statusCode).equals("503")){
                            //Toast.makeText(context, context.getResources().getString(R.string.servidor_caido), Toast.LENGTH_SHORT).show();
                            logout();
                        }
                        method_main = Request.Method.GET;

                        callback.onErrorResponse(error);

                    }
                })



        {



            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                String utf8String = "";
                try {
                    utf8String = new String(response.data, "UTF-8");
                    return Response.success(new JSONObject(utf8String).toString(), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.toString();
                }
                return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
            }


        };

        //Adding the string request to the queue

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    }

    public void jsonResponse(final int method , final VolleyCallback callback){

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(method, path, objData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        method_main = method;

                        callback.onErrorResponse(error);
                        String utf8String = "";



                    }
                }) {


        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsObjRequest);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    public void getResponseWithDataApi(final VolleyCallback callback) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, path,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onErrorResponse(error);

                    }
                })



        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                    params.put(entry.getKey(), entry.getValue());
                }
                //returning parameter
                return params;
            }

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-MAL-CLIENT-ID", "e23ae05e01e71b0478325eb5281ac713");
                return params;
            }


            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String utf8String = "";
                try {
                    utf8String = new String(response.data, "UTF-8");
                    return Response.success(new JSONObject(utf8String).toString(), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.toString();
                }
                return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        //Adding the string request to the queue

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);



    }

    public void headerResponseApi(final VolleyCallback callback){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    callback.onSuccess(response);

                } catch (JSONException e) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    callback.onErrorResponse(error);
                    return;
                }
                if(String.valueOf(error.networkResponse.statusCode).equals("401")){
                    logout();

                }
                if(String.valueOf(error.networkResponse.statusCode).equals("503")){
                    logout();
                }
                method_main = Request.Method.POST;
                callback.onErrorResponse(error);

            }
        })
        {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-MAL-CLIENT-ID", "e23ae05e01e71b0478325eb5281ac713");
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                    params.put(entry.getKey(), entry.getValue());
                }
                //returning parameter
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String utf8String = "";
                try {
                    utf8String = new String(response.data, "UTF-8");
                    return Response.success(new JSONObject(utf8String).toString(), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.toString();
                }
                return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public static class ByteArrayRequest extends Request<byte[]> {
        private final Response.Listener<byte[]> mListener;


        public ByteArrayRequest(String url, Response.Listener<byte[]> listener,
                                Response.ErrorListener errorListener) {
            this(Method.GET, url, listener, errorListener);
        }

        public ByteArrayRequest(int method, String url, Response.Listener<byte[]> listener, Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            mListener = listener;

        }


        @Override
        protected Response parseNetworkResponse(NetworkResponse response) {
            return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        protected void deliverResponse(byte[] response) {
            if(null != mListener){
                mListener.onResponse(response);
            }
        }

        @Override
        public String getBodyContentType() {
            return "application/octet-stream";
        }

    }

    public interface VolleyCallback {
        void onSuccess(String result);
        void onErrorResponse(VolleyError error);
    }
}