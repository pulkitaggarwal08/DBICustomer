package dbicustomer.com.agicon.dbi.dbicutomer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.adapters.CitySpinnerAdapter;
import dbicustomer.com.agicon.dbi.dbicutomer.adapters.CountrySpinnerAdapter;
import dbicustomer.com.agicon.dbi.dbicutomer.adapters.StateSpinnerAdapter;
import dbicustomer.com.agicon.dbi.dbicutomer.constantfields.Constant;
import dbicustomer.com.agicon.dbi.dbicutomer.models.City;
import dbicustomer.com.agicon.dbi.dbicutomer.models.Country;
import dbicustomer.com.agicon.dbi.dbicutomer.models.Register;
import dbicustomer.com.agicon.dbi.dbicutomer.models.State;
import dbicustomer.com.agicon.dbi.dbicutomer.sharedpreference.SharedPrefernceValue;
import dbicustomer.com.agicon.dbi.dbicutomer.utils.SnackbarUtil;


public class RegisterActivity extends AppCompatActivity {

    private Button create_account;
    private TextView username_font_icon, email_font_icon, phone_font_icon, password_font_icon, retype_password_font_icon, country_font_icon,
            city_font_icon, state_font_icon;
    private EditText et_username, et_email, et_phone, et_password, et_retype_password;
    private String str_username, str_email, str_phone, str_password, str_retype_password, str_country, str_city, str_states;

    private Typeface fontAwesomeFont;
    private Intent intent;

    private HashMap<String, String> map = new HashMap<>();
    //    private ProgressDialog pDialog;
    private RequestQueue queue;
    private ProgressBar progress_bar;

    private Spinner sp_countries, sp_cities, sp_states;
    //    private String country;

    private ArrayList<Country> registercountryArrayList = new ArrayList<>();
    private ArrayList<City> registercityArrayList = new ArrayList<>();
    private ArrayList<State> registerstateArrayList = new ArrayList<>();

    private String country_id;
    private String city_id;
    private String state_id;
    private String my_country_id;
    private String my_city_id;
    private String my_state_id;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String registered_access_token, login_access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPreferences = getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        init();
    }

//    private void getSpinnerCountries() {
//
//        Locale[] locale = Locale.getAvailableLocales();
//        ArrayList<String> countries = new ArrayList<>();
//        for (Locale loc : locale) {
//            country = loc.getDisplayCountry();
//            if (country.length() > 0 && !countries.contains(country)) {
//                countries.add(country);
//            }
//        }
//        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
//
//        sp_countries = (Spinner) findViewById(R.id.sp_countries);
//        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.spinner_text_view, countries);
//        sp_countries.setAdapter(adapter);
//        sp_countries.setSelection(101);
//
//    }

    private void init() {

        queue = Volley.newRequestQueue(getApplicationContext());

        registered_access_token = sharedPreferences.getString(SharedPrefernceValue.REGISTERED_ACCESS_TOKEN, "");
        login_access_token = sharedPreferences.getString(SharedPrefernceValue.LOGIN_ACCESS_TOKEN, "");

        username_font_icon = (TextView) findViewById(R.id.username_font_icon);
        email_font_icon = (TextView) findViewById(R.id.email_font_icon);
        phone_font_icon = (TextView) findViewById(R.id.phone_font_icon);
        password_font_icon = (TextView) findViewById(R.id.password_font_icon);
        retype_password_font_icon = (TextView) findViewById(R.id.retype_password_font_icon);
        country_font_icon = (TextView) findViewById(R.id.country_font_icon);
        city_font_icon = (TextView) findViewById(R.id.city_font_icon);
        state_font_icon = (TextView) findViewById(R.id.state_font_icon);

        sp_countries = (Spinner) findViewById(R.id.sp_countries);
        sp_cities = (Spinner) findViewById(R.id.sp_cities);
        sp_states = (Spinner) findViewById(R.id.sp_states);


        et_username = (EditText) findViewById(R.id.et_username);
        et_email = (EditText) findViewById(R.id.et_email);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_password = (EditText) findViewById(R.id.et_password);
        et_retype_password = (EditText) findViewById(R.id.et_retype_password);
        create_account = (Button) findViewById(R.id.create_account);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        username_font_icon.setTypeface(fontAwesomeFont);
        email_font_icon.setTypeface(fontAwesomeFont);
        phone_font_icon.setTypeface(fontAwesomeFont);
        password_font_icon.setTypeface(fontAwesomeFont);
        retype_password_font_icon.setTypeface(fontAwesomeFont);
        country_font_icon.setTypeface(fontAwesomeFont);
        city_font_icon.setTypeface(fontAwesomeFont);
        state_font_icon.setTypeface(fontAwesomeFont);

//        pDialog = new ProgressDialog(RegisterActivity.this);
//        pDialog.setMessage("Loading...");
//        pDialog.show();
        progress_bar.setVisibility(View.VISIBLE);

        getSpinnerCountry(Constant.BASE_URL + "/countries", map);
        getSpinnerCity(Constant.BASE_URL + "/cities", map);
        getSpinnerState(Constant.BASE_URL + "/states", map);

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_username = et_username.getText().toString();
                str_email = et_email.getText().toString();
                str_phone = et_phone.getText().toString();
                str_password = et_password.getText().toString();
                str_retype_password = et_retype_password.getText().toString();

                my_country_id = registercountryArrayList.get(sp_countries.getSelectedItemPosition()).getId();
                my_city_id = registercityArrayList.get(sp_cities.getSelectedItemPosition()).getId();
                my_state_id = registerstateArrayList.get(sp_states.getSelectedItemPosition()).getId();

                editor.putString(SharedPrefernceValue.CITY_ID, my_city_id);
                editor.commit();

                if (str_username.equals(null) || str_username.equals("")) {
                    SnackbarUtil.showLongSnackbar(RegisterActivity.this, "Username should not be empty");
                } else if (str_email.equals(null) || str_email.equals("")) {
                    SnackbarUtil.showLongSnackbar(RegisterActivity.this, "Email number should not be empty");
                } else if (str_phone.equals(null) || str_phone.equals("")) {
                    SnackbarUtil.showLongSnackbar(RegisterActivity.this, "Phone number should not be empty");
                } else if (str_password.equals(null) || str_password.equals("")) {
                    SnackbarUtil.showLongSnackbar(RegisterActivity.this, "Password should not be empty");
                } else if (str_retype_password.equals(null) || str_retype_password.equals("")) {
                    SnackbarUtil.showLongSnackbar(RegisterActivity.this, "Please Re-type your password");
                } else if (str_password.length() < 8) {
                    SnackbarUtil.showLongSnackbar(RegisterActivity.this, "Your password should be of minimum 8 characters");
                } else if (!str_email.contains("@")) {
                    Toast.makeText(RegisterActivity.this, "Your email is invalid, use special characters like @,!,#,*", Toast.LENGTH_LONG).show();
                } else {
                    getResgistrationParameter(str_username, str_email, str_phone, str_password, str_retype_password, my_country_id, my_city_id, my_state_id);
                }
            }
        });

    }

    private void getSpinnerCountry(String url, final HashMap<String, String> map) {
        System.out.println("url" + url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                progress_bar.setVisibility(View.INVISIBLE);

                try {

                    JSONArray countryJsonArray = new JSONArray(response);
                    for (int i = 0; i < countryJsonArray.length(); i++) {
                        JSONObject jsonObject1 = countryJsonArray.getJSONObject(i);

                        country_id = jsonObject1.getString("id");
                        String country_name = jsonObject1.getString("name");

                        registercountryArrayList.add(new Country(country_id, country_name));

                        CountrySpinnerAdapter countrySpinnerAdapter = new CountrySpinnerAdapter(getApplicationContext(), registercountryArrayList);
                        sp_countries.setAdapter(countrySpinnerAdapter);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                try {
                    Toast.makeText(RegisterActivity.this, "try again", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(RegisterActivity.this, "try again", Toast.LENGTH_LONG).show();
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

    private void getSpinnerCity(String url, final HashMap<String, String> map) {
        System.out.println("url" + url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                progress_bar.setVisibility(View.INVISIBLE);

                try {

                    JSONArray cityJsonArray = new JSONArray(response);
                    for (int i = 0; i < cityJsonArray.length(); i++) {
                        JSONObject jsonObject2 = cityJsonArray.getJSONObject(i);

                        city_id = jsonObject2.getString("id");
                        String city_name = jsonObject2.getString("name");

                        editor.putString(SharedPrefernceValue.CITY_ID, city_id);
                        editor.commit();

                        registercityArrayList.add(new City(city_id, city_name));

                        CitySpinnerAdapter citySpinnerAdapter = new CitySpinnerAdapter(getApplicationContext(), registercityArrayList);
                        sp_cities.setAdapter(citySpinnerAdapter);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                try {
                    Toast.makeText(RegisterActivity.this, "try again", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(RegisterActivity.this, "try again", Toast.LENGTH_LONG).show();
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

    private void getSpinnerState(String url, final HashMap<String, String> map) {
        System.out.println("url" + url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                progress_bar.setVisibility(View.INVISIBLE);

                try {


                    JSONArray stateJsonArray = new JSONArray(response);
                    for (int i = 0; i < stateJsonArray.length(); i++) {
                        JSONObject jsonObject3 = stateJsonArray.getJSONObject(i);

                        state_id = jsonObject3.getString("id");
                        String state_name = jsonObject3.getString("name");

                        registerstateArrayList.add(new State(state_id, state_name));

                        StateSpinnerAdapter stateSpinnerAdapter = new StateSpinnerAdapter(getApplicationContext(), registerstateArrayList);
                        sp_states.setAdapter(stateSpinnerAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                try {
                    Toast.makeText(RegisterActivity.this, "try again", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(RegisterActivity.this, "try again", Toast.LENGTH_LONG).show();
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

    private void getResgistrationParameter(String str_username, String str_email, String str_phone, String str_password
            , String str_retype_password, String str_country, String str_city, String str_state) {

//        pDialog = new ProgressDialog(RegisterActivity.this);
//        pDialog.setMessage("Loading...");
//        pDialog.show();
        progress_bar.setVisibility(View.VISIBLE);

        map.put("name", str_username);
        map.put("email", str_email);
        map.put("phone", str_phone);
        map.put("password", str_password);
        map.put("confirmpassword", str_password);
        map.put("user_type", "CONSUMER");
        map.put("city_id", my_city_id);
        map.put("state_id", my_state_id);
        map.put("country_id", my_country_id);

        getRegisterReponse(Constant.BASE_URL + "/register", map);
    }


    public void getRegisterReponse(String url, final HashMap<String, String> map) {
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
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    } else if (response.contains("success")) {
                        boolean success = Boolean.parseBoolean(jsonObject.getString("success"));

                        if (success) {
                            String registered_access_token = jsonObject.getString("access_token");
                            if (registered_access_token != null) {

                                editor.putString(SharedPrefernceValue.REGISTERED_ACCESS_TOKEN, registered_access_token);
                                editor.commit();

                                Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();
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
                    Toast.makeText(RegisterActivity.this, "try again", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(RegisterActivity.this, "try again", Toast.LENGTH_LONG).show();
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
