package dbicustomer.com.agicon.dbi.dbicutomer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.adapters.PushInvoiceAdapter;
import dbicustomer.com.agicon.dbi.dbicutomer.constantfields.Constant;
import dbicustomer.com.agicon.dbi.dbicutomer.db.SQLController;
import dbicustomer.com.agicon.dbi.dbicutomer.models.AddNewItem;
import dbicustomer.com.agicon.dbi.dbicutomer.models.Product;
import dbicustomer.com.agicon.dbi.dbicutomer.sharedpreference.SharedPrefernceValue;

public class PushInvoiceActivity extends AppCompatActivity {

    private TextView fa_rupee, item_total_sum, tv_no_item_found, tv_customer_name, tv_phone_number, tv_sub_total;
    private Typeface fontAwesomeFont;
    private Button push_invoice;
    private Intent intent;
    private Toolbar push_invoice_toolbar;

    private RecyclerView rv_products_list;
    private HashMap<String, String> map = new HashMap<>();
    //    private ProgressDialog pDialog;
    private RequestQueue queue;
    private ProgressBar push_invoice_progress_bar;

    private ArrayList<Product> productList = new ArrayList<>();
    private ArrayList<AddNewItem> addNewItemList;
    private ArrayList<AddNewItem> finalNewItemList;

    private PushInvoiceAdapter pushInvoiceAdapter;
    private String user_id, store_id;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String product_name, temp_total, customer_name, phone_number, login_access_token, registered_access_token;

    public SQLController sqlController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_invoice);

        queue = Volley.newRequestQueue(getApplicationContext());

        sharedPreferences = getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        login_access_token = sharedPreferences.getString(SharedPrefernceValue.LOGIN_ACCESS_TOKEN, "");
        registered_access_token = sharedPreferences.getString(SharedPrefernceValue.REGISTERED_ACCESS_TOKEN, "");

        store_id = sharedPreferences.getString(SharedPrefernceValue.STORE_ID, "");
        user_id = sharedPreferences.getString(SharedPrefernceValue.USER_ID, "");
        temp_total = sharedPreferences.getString(SharedPrefernceValue.TEMP, "");

        customer_name = sharedPreferences.getString(SharedPrefernceValue.CUSTOMER_NAME, "");
        phone_number = sharedPreferences.getString(SharedPrefernceValue.PHONE, "");

        sqlController = new SQLController(this);

        init();
    }

    public void init() {

        push_invoice = (Button) findViewById(R.id.push_invoice);
        push_invoice_toolbar = (Toolbar) findViewById(R.id.push_invoice_toolbar);
        rv_products_list = (RecyclerView) findViewById(R.id.rv_products_list);
        push_invoice_progress_bar = (ProgressBar) findViewById(R.id.push_invoice_progress_bar);
        item_total_sum = (TextView) findViewById(R.id.item_total_sum);
        tv_no_item_found = (TextView) findViewById(R.id.tv_no_item_found);
        tv_customer_name = (TextView) findViewById(R.id.tv_customer_name);
        tv_phone_number = (TextView) findViewById(R.id.tv_phone_number);
        tv_sub_total = (TextView) findViewById(R.id.tv_sub_total);

        rv_products_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_products_list.setNestedScrollingEnabled(false);

        setSupportActionBar(push_invoice_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setFontAwesomeFont();
        addNewItemList = (ArrayList<AddNewItem>) getIntent().getSerializableExtra("addNewItemList");

        item_total_sum.setText(temp_total);
        tv_customer_name.setText(customer_name);
        tv_phone_number.setText(phone_number);
        tv_sub_total.setText(temp_total);

        getProductResponse();

        push_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                pDialog = new ProgressDialog(PushInvoiceActivity.this);
//                pDialog.setMessage("Loading...");
//                pDialog.show();
                push_invoice_progress_bar.setVisibility(View.VISIBLE);

                try {
                    JSONObject finaljsonObject = new JSONObject();
//                    finaljsonObject.put("user_id", user_id);

                    JSONArray jsonArrayData = new JSONArray();
                    for (int i = 0; i < addNewItemList.size(); i++) {

                        String product_id = addNewItemList.get(i).getNew_item_key_id();
                        String product_name = addNewItemList.get(i).getNew_item_name();
                        String product_quantity = addNewItemList.get(i).getNew_item_quantity();
                        String product_price = addNewItemList.get(i).getNew_item_price();
                        String product_total_price = addNewItemList.get(i).getNew_item_total_price();

                        JSONObject jsonObjectData = new JSONObject();

                        jsonObjectData.put("id", product_id);
                        jsonObjectData.put("qty", product_quantity);
                        jsonArrayData.put(jsonObjectData);
                    }

                    finaljsonObject.put("products", jsonArrayData);

                    uploadList(Constant.BASE_URL + "/stores/" + store_id + "/order", map, finaljsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void setFontAwesomeFont() {

        fa_rupee = (TextView) findViewById(R.id.fa_rupee);
        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        fa_rupee.setTypeface(fontAwesomeFont);

    }

    private void getProductResponse() {

        try {
            sqlController.open();
            addNewItemList = sqlController.getAllItems();

            if (addNewItemList.size() == 0) {
                tv_no_item_found.setText("No Item Found");

                tv_no_item_found.setVisibility(View.VISIBLE);
                rv_products_list.setVisibility(View.GONE);

                item_total_sum.setText("0");
                tv_sub_total.setText("0");

            } else {

                tv_no_item_found.setVisibility(View.GONE);
                rv_products_list.setVisibility(View.VISIBLE);

                pushInvoiceAdapter = new PushInvoiceAdapter(addNewItemList, new PushInvoiceAdapter.onClickListener() {
                    @Override
                    public void onClickButton(int position, int view, final AddNewItem addNewItem) {

                    }
                });

                rv_products_list.setAdapter(pushInvoiceAdapter);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void uploadList(String url, final HashMap<String, String> hashMap, final JSONObject jsonObjectData) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                pDialog.dismiss();
                push_invoice_progress_bar.setVisibility(View.INVISIBLE);
                Log.e("response", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (response.contains("errors")) {
                        String error = jsonObject.getString("errors");
                        Toast.makeText(PushInvoiceActivity.this, "Network problem, please try after some time", Toast.LENGTH_SHORT).show();
                    } else if (response.contains("success")) {
                        boolean success = Boolean.parseBoolean(jsonObject.getString("success"));

                        if (success) {

                            String order_id = jsonObject.getString("order_id");

                            intent = new Intent(PushInvoiceActivity.this, CustomerQrCodeActivity.class);
                            intent.putExtra("order_id", order_id);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("store_id", store_id);

                            startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//
//                int  statusCode = error.networkResponse.statusCode;
//                NetworkResponse response = error.networkResponse;
//
//                Log.d("testerror",""+statusCode+" "+response.data);

                push_invoice_progress_bar.setVisibility(View.INVISIBLE);
                Toast.makeText(PushInvoiceActivity.this, "Some network issue,please try again!", Toast.LENGTH_SHORT).show();
                Log.i("error: ", error.toString());
            }
        }) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                String jsonData = jsonObjectData.toString();
                return jsonData.getBytes();
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
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);

                return headers;
            }

        };
        queue.add(request);
        queue.start();
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
