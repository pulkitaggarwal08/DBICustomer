package dbicustomer.com.agicon.dbi.dbicutomer.activities;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.squareup.picasso.Picasso;

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
import dbicustomer.com.agicon.dbi.dbicutomer.models.State;
import dbicustomer.com.agicon.dbi.dbicutomer.sharedpreference.SharedPrefernceValue;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar my_profile_toolbar;

    private String country_id, city_id, state_id;
    private Spinner sp_countries, sp_cities, sp_states;

    private HashMap<String, String> map = new HashMap<>();
    //    private ProgressDialog pDialog;
    private RequestQueue queue;
    private ProgressBar progress_bar;

    private SharedPreferences sharedPreferences;
    private String gst_number, store_id, str_username, str_email, str_phone, str_gst_number, str_country, str_country_id,
            str_city, str_city_id, str_state, str_state_id;

    private EditText et_username, et_email, et_phone, et_gst_number, et_country, et_city, et_state;
    private Button update_account;
    private CircleImageView iv_image;

    private TextView username_font_icon, email_font_icon, phone_font_icon, gst_font_icon, country_font_icon, city_font_icon, state_font_icon;
    private Typeface fontAwesomeFont;

    private String str_image;

    private ArrayList<Country> registercountryArrayList = new ArrayList<>();
    private ArrayList<City> registercityArrayList = new ArrayList<>();
    private ArrayList<State> registerstateArrayList = new ArrayList<>();

    private String registered_access_token, login_access_token;
    private String get_username, get_email, get_phone, get_city, get_state, get_country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        queue = Volley.newRequestQueue(getApplicationContext());
        sharedPreferences = getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, MODE_PRIVATE);
        store_id = sharedPreferences.getString(SharedPrefernceValue.STORE_ID, "");
//        gst_number = sharedPreferences.getString(SharedPrefernceValue.GST_NUMBER, "");

        login_access_token = sharedPreferences.getString(SharedPrefernceValue.LOGIN_ACCESS_TOKEN, "");
        registered_access_token = sharedPreferences.getString(SharedPrefernceValue.REGISTERED_ACCESS_TOKEN, "");

        init();
        setAwersomeFont();

    }

    private void setAwersomeFont() {

        username_font_icon = (TextView) findViewById(R.id.username_font_icon);
        email_font_icon = (TextView) findViewById(R.id.email_font_icon);
        phone_font_icon = (TextView) findViewById(R.id.phone_font_icon);
        gst_font_icon = (TextView) findViewById(R.id.gst_font_icon);
        country_font_icon = (TextView) findViewById(R.id.country_font_icon);
        city_font_icon = (TextView) findViewById(R.id.city_font_icon);
        state_font_icon = (TextView) findViewById(R.id.state_font_icon);

        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        username_font_icon.setTypeface(fontAwesomeFont);
        email_font_icon.setTypeface(fontAwesomeFont);
        phone_font_icon.setTypeface(fontAwesomeFont);
        gst_font_icon.setTypeface(fontAwesomeFont);
        country_font_icon.setTypeface(fontAwesomeFont);
        city_font_icon.setTypeface(fontAwesomeFont);
        state_font_icon.setTypeface(fontAwesomeFont);
    }

    private void init() {

        my_profile_toolbar = (Toolbar) findViewById(R.id.my_profile_toolbar);

        setSupportActionBar(my_profile_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_username = (EditText) findViewById(R.id.et_username);
        et_email = (EditText) findViewById(R.id.et_email);
        et_phone = (EditText) findViewById(R.id.et_phone);
        sp_countries = (Spinner) findViewById(R.id.sp_countries);
        sp_cities = (Spinner) findViewById(R.id.sp_cities);
        sp_states = (Spinner) findViewById(R.id.sp_states);
        iv_image = (CircleImageView) findViewById(R.id.iv_image);
        update_account = (Button) findViewById(R.id.update_account);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);


//        pDialog = new ProgressDialog(ProfileActivity.this);
//        pDialog.setMessage("Loading...");
//        pDialog.show();
//        progress_bar.setVisibility(View.VISIBLE);
//        map.put("gst_number", gst_number);
//        getProfileData(Constant.BASE_URL + "/getProfileData", map);

//        getSpinnerCity(Constant.BASE_URL + "/cities", map);

        update_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_username = et_username.getText().toString();
                str_email = et_email.getText().toString();
                str_phone = et_phone.getText().toString();

                str_country_id = registercountryArrayList.get((sp_countries.getSelectedItemPosition())).getId();
                str_city_id = registercityArrayList.get((sp_cities.getSelectedItemPosition())).getId();
                str_state_id = registerstateArrayList.get((sp_states.getSelectedItemPosition())).getId();

                str_country = registercountryArrayList.get((sp_countries.getSelectedItemPosition())).getName();
                str_city = registercityArrayList.get((sp_cities.getSelectedItemPosition())).getName();
                str_state = registerstateArrayList.get((sp_states.getSelectedItemPosition())).getName();

                if (!str_username.equals(get_username)) {
                    progress_bar.setVisibility(View.VISIBLE);
                    map.put("name", str_username);

                }
                if (!str_email.equals(get_email)) {
                    progress_bar.setVisibility(View.VISIBLE);
                    map.put("email", str_email);

                }
                if (!str_country.equals(str_country)) {
                    progress_bar.setVisibility(View.VISIBLE);
                    map.put("country_id", str_country_id);

                }
                if (!str_city.equals(get_city)) {
                    progress_bar.setVisibility(View.VISIBLE);
                    map.put("city_id", str_city_id);

                }
                if (!str_state.equals(get_state)) {
                    progress_bar.setVisibility(View.VISIBLE);
                    map.put("state_id", str_state_id);

                }
                if (!str_phone.equals(get_phone)) {
                    progress_bar.setVisibility(View.VISIBLE);

                    if (str_phone.length() > 10) {
                        Toast.makeText(ProfileActivity.this, "Please enter the valid mobile number", Toast.LENGTH_SHORT).show();
                    }
                    else if (str_phone.length() < 10) {
                        Toast.makeText(ProfileActivity.this, "Please enter the valid mobile number", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        map.put("phone", str_phone);
                        upateProfile(Constant.PROFILE_UPDATE_BASE_URL + "/user", map);
                    }
                }
//                else {
//
//                }

            }
        });

        getSpinnerCountry(Constant.BASE_URL + "/countries", map);
        getSelectedSpinnerItemList();

    }

    private void getSelectedSpinnerItemList() {

        sp_countries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String countryId = registercountryArrayList.get(sp_countries.getSelectedItemPosition()).getId();
                if ((!countryId.equals(null) && (!countryId.equals("")))) {

                    getSpinnerState(Constant.BASE_URL + "/countries/" + countryId, map);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String stateId = registerstateArrayList.get(sp_states.getSelectedItemPosition()).getId();
                if ((!stateId.equals(null) && (!stateId.equals("")))) {


                    getSpinnerCity(Constant.BASE_URL + "/states/" + stateId, map);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_cities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

//                String stateId = registerstateArrayList.get(sp_states.getSelectedItemPosition()).getId();
//                if ((!stateId.equals(null) && (!stateId.equals("")))) {
//
//
//                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void upateProfile(String url, final HashMap<String, String> map) {

        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                progress_bar.setVisibility(View.INVISIBLE);

                try {
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    String message = jsonObject.getString("message");

                    Toast.makeText(ProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                progress_bar.setVisibility(View.INVISIBLE);

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

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Picasso.with(getApplicationContext())
                    .load(Constant.IMAGE_BASE_URL + str_image)
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(iv_image);
        }

    }

    private void getSpinnerCountry(String url, final HashMap<String, String> map) {
        System.out.println("url" + url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                registercountryArrayList.clear();
                Log.e("response", response);
//                pDialog.dismiss();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        country_id = jsonObject.getString("id");
                        String country_name = jsonObject.getString("name");

                        registercountryArrayList.add(new Country(country_id, country_name));

                        CountrySpinnerAdapter countrySpinnerAdapter = new CountrySpinnerAdapter(getApplicationContext(), registercountryArrayList);
                        sp_countries.setAdapter(countrySpinnerAdapter);

                    }
//                    String countryId = registercountryArrayList.get(sp_countries.getSelectedItemPosition()).getId();
//                    if ((!countryId.equals(null) && (!countryId.equals("")))) {
//
//                        getSpinnerState(Constant.BASE_URL + "/countries" + countryId, map);
//
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                try {
                    Toast.makeText(ProfileActivity.this, "try again", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(ProfileActivity.this, "try again", Toast.LENGTH_LONG).show();
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
                registercityArrayList.clear();
                Log.e("response", response);
//                pDialog.dismiss();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

//                        city_id = jsonObject.getString("id");
//                        String city_name = jsonObject.getString("name");

                        String cities = jsonObject.getString("cities");
                        JSONArray citiesJsonArray = new JSONArray(cities);
                        for (int j = 0; j < citiesJsonArray.length(); j++) {
                            JSONObject jsonObject1 = citiesJsonArray.getJSONObject(j);

                            String city_id = jsonObject1.getString("id");
                            String city_name = jsonObject1.getString("name");

                            registercityArrayList.add(new City(city_id, city_name));
                        }

                        CitySpinnerAdapter citySpinnerAdapter = new CitySpinnerAdapter(getApplicationContext(), registercityArrayList);
                        sp_cities.setAdapter(citySpinnerAdapter);

                    }

                    getProfileData(Constant.BASE_URL + "/user", map);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                try {
                    Toast.makeText(ProfileActivity.this, "try again", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(ProfileActivity.this, "try again", Toast.LENGTH_LONG).show();
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
                registerstateArrayList.clear();
                Log.e("response", response);
//                pDialog.dismiss();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String states = jsonObject.getString("states");
                        JSONArray statesJsonArray = new JSONArray(states);
                        for (int j = 0; j < statesJsonArray.length(); j++) {
                            JSONObject jsonObjectProducts = statesJsonArray.getJSONObject(j);

                            state_id = jsonObjectProducts.getString("id");
                            String name = jsonObjectProducts.getString("name");

                            registerstateArrayList.add(new State(state_id, name));
                        }

                        StateSpinnerAdapter stateSpinnerAdapter = new StateSpinnerAdapter(getApplicationContext(), registerstateArrayList);
                        sp_states.setAdapter(stateSpinnerAdapter);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                progress_bar.setVisibility(View.VISIBLE);
//                map.put("gst_number", gst_number);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                try {
                    Toast.makeText(ProfileActivity.this, "try again", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(ProfileActivity.this, "try again", Toast.LENGTH_LONG).show();
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

    private void getProfileData(String url, final HashMap<String, String> map) {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                progress_bar.setVisibility(View.INVISIBLE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    get_username = jsonObject.getString("name");
                    get_email = jsonObject.getString("email");

                    String profile = jsonObject.getString("profile");
                    JSONObject jsonObject1 = new JSONObject(profile);

                    get_phone = jsonObject1.getString("phone");

                    String city = jsonObject1.getString("city");
                    JSONObject cityJsonObject = new JSONObject(city);
                    get_city = cityJsonObject.getString("name");

                    String state = cityJsonObject.getString("state");
                    JSONObject stateJsonObject = new JSONObject(state);
                    get_state = stateJsonObject.getString("name");

                    String country = stateJsonObject.getString("country");
                    JSONObject countryJsonObject = new JSONObject(country);
                    get_country = countryJsonObject.getString("name");

//                    str_image = jsonObject.getString("image");

                    et_username.setText(get_username);
                    et_email.setText(get_email);
                    et_phone.setText(get_phone);
//                    et_gst_number.setText(str_gst_number);
//                    et_country.setText(str_country);
//                    et_city.setText(str_city);
//                    et_state.setText(str_state);

                    for (int i = 0; i < registercountryArrayList.size(); i++) {
                        if (registercountryArrayList.get(i).getName().equalsIgnoreCase(get_country)) {
                            System.out.println(registercountryArrayList.get(i).getName() + "");
                            System.out.println(get_country + "");
                            sp_countries.setSelection(i, false);
//                            break;
                        }
                    }

                    for (int j = 0; j < registercityArrayList.size(); j++) {
                        System.out.println(registercityArrayList.get(j).getName() + "");
                        System.out.println(get_city + "");
                        if (registercityArrayList.get(j).getName().contains(get_city)) {
                            System.out.println(registercityArrayList.get(j).getName() + "");
                            System.out.println(get_city + "");
                            sp_cities.setSelection(j, false);
//                            break;
                        }
                    }
                    for (int k = 0; k < registerstateArrayList.size(); k++) {
                        System.out.println(registerstateArrayList.get(k).getName() + "");
                        System.out.println(get_state + "");
                        if (registerstateArrayList.get(k).getName().contains(get_state)) {
                            System.out.println(registerstateArrayList.get(k).getName() + "");
                            System.out.println(get_state + "");
                            sp_states.setSelection(k, false);
//                            break;
                        }
                    }

                    if (str_image != null) {
                        iv_image.setVisibility(View.VISIBLE);
                        iv_image.setImageResource(R.drawable.user);

                        new MyAsyncTask().execute();

                    } else {
                        iv_image.setImageResource(R.drawable.user);
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
                    Toast.makeText(getApplicationContext(), "username and password is invalid!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "try again", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            //            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                return map;
//            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                String auth = null;
                if ((!registered_access_token.equals(null)) && (!registered_access_token.equals(""))) {
                    auth = "Bearer " + registered_access_token;
                } else if ((!login_access_token.equals(null)) && (!login_access_token.equals(""))) {
                    auth = "Bearer " + login_access_token;
                }

                Map<String, String> headers = new HashMap<>();
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

    MenuItem action_settings;
    MenuItem action_search;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        action_settings = menu.findItem(R.id.action_settings);
//        action_search = menu.findItem(R.id.action_search);

//        action_settings.setVisible(false);
//        action_search.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
