package dbicustomer.com.agicon.dbi.dbicutomer.homefragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import java.util.List;
import java.util.Map;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.activities.InvoiceActivity;
import dbicustomer.com.agicon.dbi.dbicutomer.adapters.HomeCategoryAdapter;
import dbicustomer.com.agicon.dbi.dbicutomer.constantfields.Constant;
import dbicustomer.com.agicon.dbi.dbicutomer.interfaces.onClickButtonListener;
import dbicustomer.com.agicon.dbi.dbicutomer.models.HomeStores;
import dbicustomer.com.agicon.dbi.dbicutomer.sharedpreference.SharedPrefernceValue;

public class HomeCategoryFragment extends Fragment {

    private View view;
    private List<HomeStores> homeStoresList = new ArrayList<>();
    private HomeCategoryAdapter homeCategoryAdapter;
    private RecyclerView rv_category_items;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    //    private ProgressDialog pDialog;
    private HashMap<String, String> map = new HashMap<>();
    private RequestQueue queue;
    private ProgressBar home_category_progress_bar;

    private String str_city_id, storeTypeId, registered_access_token, login_access_token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home_category, container, false);
        queue = Volley.newRequestQueue(getActivity());

        sharedPreferences = getActivity().getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        str_city_id = sharedPreferences.getString(SharedPrefernceValue.CITY_ID, "");

        init();

        return view;
    }

    private void init() {

        String storeTypeName = getArguments().getString("store_type_name");
        storeTypeId = getArguments().getString("storeTypeId");

        rv_category_items = (RecyclerView) view.findViewById(R.id.rv_category_items);
        home_category_progress_bar = (ProgressBar) view.findViewById(R.id.home_category_progress_bar);

        login_access_token = sharedPreferences.getString(SharedPrefernceValue.LOGIN_ACCESS_TOKEN, "");
        registered_access_token = sharedPreferences.getString(SharedPrefernceValue.REGISTERED_ACCESS_TOKEN, "");

        rv_category_items.setLayoutManager(new LinearLayoutManager(getContext()));

//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Loading...");
//        pDialog.show();
        home_category_progress_bar.setVisibility(View.VISIBLE);
//        map.put("store_type", storeTypeName);
//        map.put("city_name", str_city_id);

        getHomeStoreFragmentResponse(Constant.BASE_URL + "/stores/" + str_city_id + "/" + storeTypeId, map);

    }

    public void getHomeStoreFragmentResponse(String url, final HashMap<String, String> map) {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                home_category_progress_bar.setVisibility(View.INVISIBLE);
                try {
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    String home_store_list = jsonObject.getString("home_store_list");
//                    JSONArray homeStoresJsonArray = new JSONArray(home_store_list);
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        String store_id = jsonObject1.getString("id");
//                        String store_type_id = jsonObject1.getString("store_type_id");
                        String store_name = jsonObject1.getString("name");
                        String store_address = jsonObject1.getString("address_line_one");
                        String city_id = jsonObject1.getString("city_id");
//                        String store_city = jsonObject1.getString("store_city");
//                        String store_state = jsonObject1.getString("store_state");
//                        String store_country = jsonObject1.getString("store_country");
                        String store_zip = jsonObject1.getString("pincode");
                        String store_landline = jsonObject1.getString("landline");
                        String store_mobile = jsonObject1.getString("mobile");
                        String store_image = jsonObject1.getString("picture");

//                        editor.putString(SharedPrefernceValue.STORE_ID, store_id);
//                        editor.commit();

                        homeStoresList.add(new HomeStores(store_id, store_name, store_address, "", "",
                                "", store_zip, store_landline, store_mobile, store_image));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                homeCategoryAdapter = new HomeCategoryAdapter(homeStoresList, new onClickButtonListener() {
                    @Override
                    public void onClickButton(int position) {
//                        Toast.makeText(getActivity(), homeStoresList.get(position).getStore_name(), Toast.LENGTH_SHORT).show();

                        String storeTypeName = homeStoresList.get(position).getStore_name();
                        String homeStoreId = homeStoresList.get(position).getStore_id();

//                        editor.putString(SharedPrefernceValue.STORE_ID,homeStoreId);
//                        InvoiceFragment invoiceFragment = new InvoiceFragment();
//                        Bundle argBundles = new Bundle();
//                        argBundles.putString("store_type_name", storeTypeName);
//                        argBundles.putString("store_type_id", homeStoreId);
//                        invoiceFragment.setArguments(argBundles);

//                        view.findViewById(R.id.dashboard_home).setVisibility(View.GONE);
//                        getFragmentManager().beginTransaction().replace(R.id.home_container, new InvoiceFragment()).commit();

                        Intent intent = new Intent(getActivity(), InvoiceActivity.class);
                        intent.putExtra("home_store_id", homeStoreId);

                        editor.putString(SharedPrefernceValue.STORE_ID, homeStoreId);
                        editor.commit();
                        startActivity(intent);

                    }
                });
                rv_category_items.setAdapter(homeCategoryAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                home_category_progress_bar.setVisibility(View.INVISIBLE);
            }
        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                return map;
//            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                String auth = null;
                if ((!login_access_token.equals(null)) && (!login_access_token.equals(""))) {
                    auth = "Bearer " + login_access_token;
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
