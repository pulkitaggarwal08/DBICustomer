package dbicustomer.com.agicon.dbi.dbicutomer.activities;

import android.content.Context;
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

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText et_old_password, et_new_password, et_confirm_password;
    private Button btn_confirm_password_account;

    private String str_old_password, str_new_password, str_confirm_password, gst_number;

    private TextView change_password_font_icon, new_password_font_icon, confirm_password_font_icon;
    private Typeface fontAwesomeFont;

    private SharedPreferences sharedPreferences;

    private RequestQueue queue;
    private HashMap<String, String> map = new HashMap<>();
    //    private ProgressDialog pDialog;
    private ProgressBar progress_bar;
    private String registered_access_token, login_access_token, user_name, user_email, user_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);

        queue = Volley.newRequestQueue(getApplicationContext());

        sharedPreferences = getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, Context.MODE_PRIVATE);

//        user_name = sharedPreferences.getString(SharedPrefernceValue.USER_NAME, "");
//        user_email = sharedPreferences.getString(SharedPrefernceValue.USER_EMAIL, "");
//        user_phone = sharedPreferences.getString(SharedPrefernceValue.USER_PHONE, "");

        registered_access_token = sharedPreferences.getString(SharedPrefernceValue.REGISTERED_ACCESS_TOKEN, "");
        login_access_token = sharedPreferences.getString(SharedPrefernceValue.LOGIN_ACCESS_TOKEN, "");

        et_old_password = (EditText) findViewById(R.id.et_old_password);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        change_password_font_icon = (TextView) findViewById(R.id.change_password_font_icon);
        new_password_font_icon = (TextView) findViewById(R.id.new_password_font_icon);
        confirm_password_font_icon = (TextView) findViewById(R.id.confirm_password_font_icon);

        btn_confirm_password_account = (Button) findViewById(R.id.btn_confirm_password_account);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        setFontAwesome();

        btn_confirm_password_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_old_password = et_old_password.getText().toString();
                str_new_password = et_new_password.getText().toString();
                str_confirm_password = et_confirm_password.getText().toString();

//                if (str_old_password.equals(null) || str_old_password.equals("")) {
//                    SnackbarUtil.showLongSnackbar(ChangePasswordActivity.this, "Old Password should not be empty");
//                }
                if (str_new_password.equals(null) || str_new_password.equals("")) {
                    SnackbarUtil.showLongSnackbar(ChangePasswordActivity.this, "Password should not be empty");
                } else if (str_confirm_password.equals(null) || str_confirm_password.equals("")) {
                    SnackbarUtil.showLongSnackbar(ChangePasswordActivity.this, "Confirm Password should not be empty");
                } else if (!str_confirm_password.equals(str_new_password)) {
                    SnackbarUtil.showLongSnackbar(ChangePasswordActivity.this, "New and confirm password should be same");
                } else if (!(str_new_password.length() >= 8)) {
                    SnackbarUtil.showLongSnackbar(ChangePasswordActivity.this, "Your password should be of minimum 8 characters");
                } else {
                    getChangePasswordResponse(str_new_password);
                }
            }
        });

    }

    private void setFontAwesome() {

        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        change_password_font_icon.setTypeface(fontAwesomeFont);
        new_password_font_icon.setTypeface(fontAwesomeFont);
        confirm_password_font_icon.setTypeface(fontAwesomeFont);
    }


    private void getChangePasswordResponse(String str_new_password) {

//        pDialog = new ProgressDialog(ChangePasswordActivity.this);
//        pDialog.setMessage("Loading...");
//        pDialog.show();
        progress_bar.setVisibility(View.VISIBLE);

//        map.put("gst_number", gst_number);

//        map.put("name", user_name);
//        map.put("email", user_email);
//        map.put("phone", user_phone);
        map.put("password", str_new_password);
        map.put("confirmpassword", str_new_password);
        getReponse(Constant.BASE_URL + "/user", map);
    }

    public void getReponse(String url, final HashMap<String, String> map) {
        System.out.println("url" + url);
        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                progress_bar.setVisibility(View.INVISIBLE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (response.contains("errors")) {
                        String error = jsonObject.getString("errors");
                        Toast.makeText(ChangePasswordActivity.this, "Please try some time", Toast.LENGTH_SHORT).show();
                    } else if (response.contains("success")) {
                        boolean success = Boolean.parseBoolean(jsonObject.getString("success"));

                        Toast.makeText(ChangePasswordActivity.this, "Your password has been successfully updated", Toast.LENGTH_LONG).show();

                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                progress_bar.setVisibility(View.INVISIBLE);

                try {
                    Toast.makeText(ChangePasswordActivity.this, "try again", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(ChangePasswordActivity.this, "try again", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                String auth = null;
                if ((!registered_access_token.equals(null)) && (!registered_access_token.equals(""))) {
                    auth = "Bearer " + registered_access_token;
                } else if ((!login_access_token.equals(null)) && (!login_access_token.equals(""))) {
                    auth = "Bearer " + login_access_token;
                }

                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
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
