package dbicustomer.com.agicon.dbi.dbicutomer.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import dbicustomer.com.agicon.dbi.dbicutomer.adapters.InvoiceListInfoAdapter;
import dbicustomer.com.agicon.dbi.dbicutomer.constantfields.Constant;
import dbicustomer.com.agicon.dbi.dbicutomer.models.UserProduct;
import dbicustomer.com.agicon.dbi.dbicutomer.sharedpreference.SharedPrefernceValue;

public class InvoiceListInfoActivity extends AppCompatActivity {

    private Toolbar inbound_list_info_toolbar;
    private Intent intent;

    private RecyclerView rv_inbound_list_info;
    private InvoiceListInfoAdapter inboundListInfoAdapter;
    private List<UserProduct> userProductList = new ArrayList<>();

    private String user_id;

    private HashMap<String, String> map = new HashMap<>();
    //    private ProgressDialog pDialog;
    private RequestQueue queue;
    private ProgressBar progress_bar;

    private SharedPreferences sharedPreferences;
    private String registered_access_token, login_access_token, storeId, invoice_id, order_status;

    private TextView tv_invoice_status, tv_customer_name, tv_phone_number, tv_date, tv_sub_total;

    private LinearLayout ll_user_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list_info);

        init();
    }

    private void init() {

        inbound_list_info_toolbar = (Toolbar) findViewById(R.id.inbound_list_info_toolbar);

        queue = Volley.newRequestQueue(getApplicationContext());
        sharedPreferences = getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, Context.MODE_PRIVATE);

        user_id = sharedPreferences.getString(SharedPrefernceValue.USER_ID, "");

        registered_access_token = sharedPreferences.getString(SharedPrefernceValue.REGISTERED_ACCESS_TOKEN, "");
        login_access_token = sharedPreferences.getString(SharedPrefernceValue.LOGIN_ACCESS_TOKEN, "");

        order_status = getIntent().getStringExtra("order_status");
        invoice_id = getIntent().getStringExtra("invoice_id");
        storeId = getIntent().getStringExtra("store_id");

        setSupportActionBar(inbound_list_info_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv_inbound_list_info = (RecyclerView) findViewById(R.id.rv_inbound_list_info);
        tv_invoice_status = (TextView) findViewById(R.id.tv_invoice_status);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        tv_customer_name = (TextView) findViewById(R.id.tv_customer_name);
        tv_phone_number = (TextView) findViewById(R.id.tv_phone_number);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_sub_total = (TextView) findViewById(R.id.tv_sub_total);
        ll_user_info = (LinearLayout) findViewById(R.id.ll_user_info);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_inbound_list_info.setLayoutManager(mLayoutManager);
        rv_inbound_list_info.setNestedScrollingEnabled(false);

//        pDialog = new ProgressDialog(InvoiceListInfoActivity.this);
//        pDialog.setMessage("Loading...");
//        pDialog.show();
        progress_bar.setVisibility(View.VISIBLE);
        ll_user_info.setVisibility(View.VISIBLE);
//        map.put("invoice_id", invoice_id);
        getUserInfoResponse(Constant.BASE_URL + "/stores/" + storeId + "/orders", map);

    }

    private void getUserInfoResponse(String url, final HashMap<String, String> map) {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                progress_bar.setVisibility(View.GONE);
                ll_user_info.setVisibility(View.VISIBLE);
                userProductList.clear();
                try {

                    String user_name = null, user_phone = null, order_date = null, total_item_price = null;

                    JSONArray inboundListJsonArray = new JSONArray(response);

                    for (int i = 0; i < inboundListJsonArray.length(); i++) {
                        JSONObject jsonObject = inboundListJsonArray.getJSONObject(i);

                        String invoiceId = jsonObject.getString("id");
                        String userId = jsonObject.getString("user_id");
                        order_date = jsonObject.getString("created_at");
                        String orderStatus = jsonObject.getString("payments_count");

                        if (user_id.equalsIgnoreCase(userId) && invoice_id.equalsIgnoreCase(invoiceId) &&
                                order_status.equalsIgnoreCase(orderStatus)) {

                            String user = jsonObject.getString("user");
                            JSONObject jsonObject1 = new JSONObject(user);

                            String user_id = jsonObject1.getString("id");
                            user_name = jsonObject1.getString("name");
//                        String user_email = jsonObject1.getString("email");

                            String profile = jsonObject1.getString("profile");
                            JSONObject jsonProfile = new JSONObject(profile);

                            user_phone = jsonProfile.getString("phone");
                            String order_amount = jsonObject.getString("net_amount");

                            String products = jsonObject.getString("products");
                            JSONArray jsonArrayProducts = new JSONArray(products);
                            for (int j = 0; j < jsonArrayProducts.length(); j++) {
                                JSONObject jsonObjectProducts = jsonArrayProducts.getJSONObject(j);

                                String product_name = jsonObjectProducts.getString("name");

                                String pivot = jsonObjectProducts.getString("pivot");
                                JSONObject jsonObjectPivot = new JSONObject(pivot);

                                String unit_price = jsonObjectPivot.getString("unit_price");
                                String product_qty = jsonObjectPivot.getString("quantity");
                                total_item_price = jsonObjectPivot.getString("total_price");

                                userProductList.add(new UserProduct(product_name, total_item_price, product_qty, unit_price));
                            }
                        }
                    }

                    String[] splitTimeDate = order_date.split(" ");
                    String date = splitTimeDate[splitTimeDate.length - 2];
                    String time = splitTimeDate[splitTimeDate.length - 1];

                    tv_customer_name.setText(user_name);
                    tv_phone_number.setText(user_phone);
                    tv_date.setText(date);
                    tv_sub_total.setText(total_item_price);

                    if (order_status.equals("0")) {
                        tv_invoice_status.setText("Pending");
                    } else {
                        tv_invoice_status.setText("Received");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                inboundListInfoAdapter = new InvoiceListInfoAdapter(userProductList, new InvoiceListInfoAdapter.onClickListener() {
                    @Override
                    public void onClickButton(int position, int view, UserProduct userProduct) {

                    }
                });

                rv_inbound_list_info.setAdapter(inboundListInfoAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                progress_bar.setVisibility(View.GONE);
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


    MenuItem action_settings, action_search, action_invoice_list;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        action_settings = menu.findItem(R.id.action_settings);
//        action_search = menu.findItem(R.id.action_search);
//        action_invoice_list = menu.findItem(R.id.action_invoice_list);
//
//        action_settings.setVisible(false);
//        action_search.setVisible(false);
//        action_invoice_list.setVisible(false);
//
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:

                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
