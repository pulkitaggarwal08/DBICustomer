package dbicustomer.com.agicon.dbi.dbicutomer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.constantfields.Constant;
import dbicustomer.com.agicon.dbi.dbicutomer.sharedpreference.SharedPrefernceValue;
import dbicustomer.com.agicon.dbi.dbicutomer.utils.SnackbarUtil;


public class SignInActivity extends AppCompatActivity {

    private TextView signin_username_font_icon, signin_password_font_icon, tv_new_user, signin_forgot_password;
    private Typeface fontAwesomeFont;
    private Button btn_signin_create_account;
    private EditText et_signin_email, et_signin_password;
    private String str_signin_email, str_signin_password;

    private HashMap<String, String> map = new HashMap<>();
    //    private ProgressDialog pDialog;
    private RequestQueue queue;
    private ProgressBar progress_bar;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Intent intent;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();
        setAwesomeFont();

    }

    private void init() {
        queue = Volley.newRequestQueue(getApplicationContext());

        sharedPreferences = getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        signin_username_font_icon = (TextView) findViewById(R.id.signin_username_font_icon);
        signin_password_font_icon = (TextView) findViewById(R.id.signin_password_font_icon);
        btn_signin_create_account = (Button) findViewById(R.id.btn_signin_create_account);
        et_signin_email = (EditText) findViewById(R.id.et_signin_email);
        et_signin_password = (EditText) findViewById(R.id.et_signin_password);
        tv_new_user = (TextView) findViewById(R.id.tv_new_user);
        signin_forgot_password = (TextView) findViewById(R.id.signin_forgot_password);

        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        btn_signin_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_signin_email = et_signin_email.getText().toString();
                str_signin_password = et_signin_password.getText().toString();

                if (str_signin_email != null && str_signin_password != null) {
                    getSignParameter(str_signin_email, str_signin_password);
                } else {
                    SnackbarUtil.showLongSnackbar(SignInActivity.this, "Fields should not be empty");
                }
            }
        });

        tv_new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        signin_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(SignInActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

    }

    private void setAwesomeFont() {

        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        signin_username_font_icon.setTypeface(fontAwesomeFont);
        signin_password_font_icon.setTypeface(fontAwesomeFont);

    }

    private void getSignParameter(String str_signin_email, String str_signin_password) {

//        pDialog = new ProgressDialog(SignInActivity.this);
//        pDialog.setMessage("Loading...");
//        pDialog.show();
        progress_bar.setVisibility(View.VISIBLE);
        map.put("email", str_signin_email);
        map.put("password", str_signin_password);

        getSignResponse(Constant.BASE_URL + "/login", map);
    }

    public void getSignResponse(String url, final HashMap<String, String> map) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
                progress_bar.setVisibility(View.GONE);

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);

                    if (response.contains("errors")) {
                        String error = jsonObject.getString("errors");
                        Toast.makeText(SignInActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    } else if (response.contains("success")) {
                        boolean success = Boolean.parseBoolean(jsonObject.getString("success"));

                        if (success) {
                            String login_access_token = jsonObject.getString("access_token");
                            if (login_access_token != null) {

                                editor.putString(SharedPrefernceValue.LOGIN_ACCESS_TOKEN, login_access_token);
                                editor.commit();

                                getUserDetail(login_access_token);

                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                progress_bar.setVisibility(View.GONE);

                try {
                    Toast.makeText(getApplicationContext(), "try again", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "try again", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        queue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000
                , DefaultRetryPolicy.DEFAULT_MAX_RETRIES
                , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void getUserDetail(String login_access_token) {

        getUserDetail(login_access_token, Constant.BASE_URL + "/user", map);
    }

    private void getUserDetail(final String login_access_token, String url, final HashMap<String, String> map) {

        final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                receipt_progress_bar.setVisibility(View.INVISIBLE);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    String name = jsonObject.getString("name");
                    String user_id = jsonObject.getString("id");
                    String email = jsonObject.getString("email");

                    String profile = jsonObject.getString("profile");
                    JSONObject jsonObject1 = new JSONObject(profile);

                    String city_id = jsonObject1.getString("city_id");
                    String phone = jsonObject1.getString("phone");

                    editor.putString(SharedPrefernceValue.CITY_ID, city_id);
                    editor.putString(SharedPrefernceValue.USER_ID, user_id);
                    editor.putString(SharedPrefernceValue.CUSTOMER_NAME, name);
                    editor.putString(SharedPrefernceValue.PHONE, phone);
                    editor.commit();

                    intent = new Intent(SignInActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress_bar.setVisibility(View.GONE);

                try {
                    Toast.makeText(getApplicationContext(), "try again", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "try again", Toast.LENGTH_LONG).show();
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<>();
                String auth = "Bearer " + login_access_token;
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);

                return headers;
            }
        };
        queue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000
                , DefaultRetryPolicy.DEFAULT_MAX_RETRIES
                , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


}


