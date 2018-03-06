package dbicustomer.com.agicon.dbi.dbicutomer.trendingfragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.activities.ProfileActivity;
import dbicustomer.com.agicon.dbi.dbicutomer.adapters.TrendingAdapter;
import dbicustomer.com.agicon.dbi.dbicutomer.constantfields.Constant;
import dbicustomer.com.agicon.dbi.dbicutomer.interfaces.onClickButtonListener;
import dbicustomer.com.agicon.dbi.dbicutomer.models.Trending;
import dbicustomer.com.agicon.dbi.dbicutomer.sharedpreference.SharedPrefernceValue;

public class TrendingFragment extends Fragment {


    private View view;
    private RecyclerView rv_trending_list;
    private TrendingAdapter trendingAdapter;
    private List<Trending> trendingList = new ArrayList<>();

    private HashMap<String, String> map = new HashMap<>();
    //    private ProgressDialog pDialog;
    private RequestQueue queue;
    private ProgressBar progress_bar;

    private SharedPreferences sharedPreferences;
    private String latitude, longitude, str_user_city_id, user_id, store_name, city_name, registered_access_token, login_access_token;
    private TextView text_your_dbi, tv_no_store_found;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trending_list, container, false);

        queue = Volley.newRequestQueue(getActivity());
        sharedPreferences = getActivity().getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, Context.MODE_PRIVATE);

        registered_access_token = sharedPreferences.getString(SharedPrefernceValue.REGISTERED_ACCESS_TOKEN, "");
        login_access_token = sharedPreferences.getString(SharedPrefernceValue.LOGIN_ACCESS_TOKEN, "");

        latitude = sharedPreferences.getString(SharedPrefernceValue.LATITUDE, "");
        longitude = sharedPreferences.getString(SharedPrefernceValue.LONGITUDE, "");
        city_name = sharedPreferences.getString(SharedPrefernceValue.CURRENT_CITY, "");
        user_id = sharedPreferences.getString(SharedPrefernceValue.USER_ID, "");
        str_user_city_id = sharedPreferences.getString(SharedPrefernceValue.USER_CITY_ID, "");

        rv_trending_list = (RecyclerView) view.findViewById(R.id.rv_trending_list);
        tv_no_store_found = (TextView) view.findViewById(R.id.tv_no_store_found);
        progress_bar = (ProgressBar) view.findViewById(R.id.progress_bar);
        text_your_dbi = (TextView) view.findViewById(R.id.text_your_dbi);
        rv_trending_list.setLayoutManager(new LinearLayoutManager(getContext()));

//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Loading...");
//        pDialog.show();
//        pDialog.setCancelable(false);
        progress_bar.setVisibility(View.VISIBLE);

        if (city_name.equals(null) || city_name.equals("") || city_name == null || city_name == "") {
            tv_no_store_found.setText("No Store Found");
            tv_no_store_found.setVisibility(View.VISIBLE);
            text_your_dbi.setVisibility(View.GONE);
            progress_bar.setVisibility(View.GONE);
        } else {
            tv_no_store_found.setVisibility(View.GONE);
            rv_trending_list.setVisibility(View.VISIBLE);
//            map.put("current_city", city_name);
//            map.put("user_id", user_id);
            getStoreResponse(Constant.BASE_URL + "/city/" + str_user_city_id + "/offers", map);
        }

        return view;
    }

    public void getStoreResponse(String url, final HashMap<String, String> map) {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                progress_bar.setVisibility(View.GONE);
                try {
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    String store_list = jsonObject.getString("store_list");
                    JSONArray storeListJsonArray = new JSONArray(response);
                    for (int i = 0; i < storeListJsonArray.length(); i++) {
                        JSONObject jsonObject = storeListJsonArray.getJSONObject(i);

                        String store_discount = jsonObject.getString("discount");
                        String stores = jsonObject.getString("store");

                        if (!stores.equals("null")) {
                            tv_no_store_found.setVisibility(View.GONE);
                            text_your_dbi.setVisibility(View.VISIBLE);
                            text_your_dbi.setText("Select your DBI Store");

                            JSONObject jsonObject1 = new JSONObject(stores);

                            store_name = jsonObject1.getString("name");
                            String store_picture = jsonObject1.getString("picture");

                            String store_image = Constant.OFFER_BASE_URL + store_picture;

                            trendingList.add(new Trending(store_name, store_discount, store_image));

                        } else {
                            tv_no_store_found.setText("No Store Found");
                            tv_no_store_found.setVisibility(View.VISIBLE);
                            text_your_dbi.setVisibility(View.GONE);
                            progress_bar.setVisibility(View.GONE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                if (store_name.equalsIgnoreCase(null) || store_name.equalsIgnoreCase("") || store_name.isEmpty()) {
//                    tv_no_store_found.setText("No Store Found");
//                    tv_no_store_found.setVisibility(View.VISIBLE);
//                    rv_trending_list.setVisibility(View.GONE);
//                } else {
//                    tv_no_store_found.setVisibility(View.GONE);
//                    rv_trending_list.setVisibility(View.VISIBLE);
//                }

                trendingAdapter = new TrendingAdapter(getActivity(), trendingList, new onClickButtonListener() {
                    @Override
                    public void onClickButton(int position) {
//                        Toast.makeText(getActivity(), trendingList.get(position).getStore_name(), Toast.LENGTH_SHORT).show();

                        String storeTypeName = trendingList.get(position).getStore_name();

//                        editor.putString(SharedPrefernceValue.STORE_ID,homeStoreId);
//                        InvoiceFragment invoiceFragment = new InvoiceFragment();
//                        Bundle argBundles = new Bundle();
//                        argBundles.putString("store_type_name", storeTypeName);
//                        argBundles.putString("store_type_id", homeStoreId);
//                        invoiceFragment.setArguments(argBundles);

//                        view.findViewById(R.id.dashboard_home).setVisibility(View.GONE);
//                        getFragmentManager().beginTransaction().replace(R.id.home_container, new InvoiceFragment()).commit();

//                        Intent intent = new Intent(getActivity(), InvoiceActivity.class);
//                        startActivity(intent);

                    }
                });
                rv_trending_list.setAdapter(trendingAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                progress_bar.setVisibility(View.INVISIBLE);
                try {
                    Toast.makeText(getActivity(), "username and password is invalid!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "try again", Toast.LENGTH_LONG).show();
                }
            }
        }) {
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

}
