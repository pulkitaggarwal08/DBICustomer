package dbicustomer.com.agicon.dbi.dbicutomer.activities;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

/**
 * Created by agicon06 on 16/8/17.
 */

public class ForgotPassword extends AppCompatActivity {

    private TextView gst_font_icon, et_email;
    private Typeface fontAwesomeFont;

    private Button btn_create_new_password;

    private String email, gst_number, message;

    private ProgressBar progress_bar;
    private RequestQueue queue;
    private HashMap<String, String> map = new HashMap<>();

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        init();
        setFontAwesome();

    }

    private void init() {

        et_email = (TextView) findViewById(R.id.et_email);
        btn_create_new_password = (Button) findViewById(R.id.btn_create_new_password);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        gst_font_icon = (TextView) findViewById(R.id.gst_font_icon);

        queue = Volley.newRequestQueue(getApplicationContext());
        sharedPreferences = getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, MODE_PRIVATE);
//        gst_number = sharedPreferences.getString(SharedPrefernceValue.GST_NUMBER, "");

        btn_create_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = et_email.getText().toString();

                getPassword(email);
            }
        });
    }

    private void getPassword(String str_email) {

        progress_bar.setVisibility(View.VISIBLE);

        map.put("email", email);
//        map.put("email", str_email);
        getPasswordResponse(Constant.BASE_URL + "/reset-password", map);
    }

    private void setFontAwesome() {

        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        gst_font_icon.setTypeface(fontAwesomeFont);
    }

    public void getPasswordResponse(String url, final HashMap<String, String> map) {
        System.out.println("url" + url);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                progress_bar.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (response.contains("errors")) {
                        String error = jsonObject.getString("errors");
                        Toast.makeText(ForgotPassword.this, "Please try some time", Toast.LENGTH_SHORT).show();
                    } else if (response.contains("success")) {
                        boolean success = Boolean.parseBoolean(jsonObject.getString("success"));

                        Toast.makeText(ForgotPassword.this, "otp has sent you on your email-Id", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    SnackbarUtil.showLongSnackbar(ForgotPassword.this, message);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                progress_bar.setVisibility(View.GONE);

                try {
                    Toast.makeText(ForgotPassword.this, "try again", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(ForgotPassword.this, "try again", Toast.LENGTH_LONG).show();
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

}
