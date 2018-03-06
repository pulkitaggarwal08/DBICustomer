package dbicustomer.com.agicon.dbi.dbicutomer.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.adapters.ScannerAdapter;
import dbicustomer.com.agicon.dbi.dbicutomer.constantfields.Constant;
import dbicustomer.com.agicon.dbi.dbicutomer.db.SQLController;
import dbicustomer.com.agicon.dbi.dbicutomer.models.AddNewItem;
import dbicustomer.com.agicon.dbi.dbicutomer.models.Product;
import dbicustomer.com.agicon.dbi.dbicutomer.sharedpreference.SharedPrefernceValue;

public class    ScanListActivity extends AppCompatActivity {

    private Toolbar scan_list_toolbar;
    private Intent intent;
    private TextView fa_angle_right, fa_rupee, item_total_sum;
    private Typeface fontAwesomeFont;
    ImageView iv_minus, iv_add;
    private TextView tv_no_item;
    private RelativeLayout rl_next;

    private Button btn_scan_add_to_cart;

    String store_id, user_id;

    private HashMap<String, String> map = new HashMap<>();
    //    private ProgressDialog pDialog;
    private RequestQueue queue;
    private ProgressBar scan_list_progress_bar;

    private ArrayList<Product> productArrayList = new ArrayList<>();
    private ScannerAdapter scannerAdapter;

    private SharedPreferences sharedPreferences;

    public SQLController sqlController;
    private SharedPreferences.Editor editor;

    RecyclerView lv_scan_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_item_list);

        queue = Volley.newRequestQueue(getApplicationContext());
        sqlController = new SQLController(this);

        sharedPreferences = getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        user_id = sharedPreferences.getString(SharedPrefernceValue.USER_ID, "");
        store_id = sharedPreferences.getString(SharedPrefernceValue.STORE_ID, "");

        init();
    }

    private void init() {

        int item_id = getIntent().getIntExtra("item_id", 0);
        String content_name = getIntent().getStringExtra("content_name");
        final String content_id_result = getIntent().getStringExtra("content_id_result");

        scan_list_toolbar = (Toolbar) findViewById(R.id.scan_list_toolbar);

        iv_minus = (ImageView) findViewById(R.id.iv_minus);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        item_total_sum = (TextView) findViewById(R.id.item_total_sum);

        rl_next = (RelativeLayout) findViewById(R.id.rl_next);
        btn_scan_add_to_cart = (Button) findViewById(R.id.btn_scan_add_to_cart);

        tv_no_item = (TextView) findViewById(R.id.tv_no_item);
        lv_scan_list = (RecyclerView) findViewById(R.id.lv_scan_list);

        scan_list_progress_bar = (ProgressBar) findViewById(R.id.scan_list_progress_bar);

        setSupportActionBar(scan_list_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        lv_scan_list.setLayoutManager(mLayoutManager);

        setFontAwesomeFont();

        getBarContentResponse(String.valueOf(content_id_result));

        rl_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ScanListActivity.this, GetAllShoppingListActivity.class);
                startActivity(intent);
            }
        });

        btn_scan_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    addToCart(content_id, item_price, product_quantity);

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ScanListActivity.this);
                builder.setMessage("Are you sure you want to add product?")
                        .setTitle("Add New Product")
//                        .setIcon(R.drawable.dbi_logo)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    sqlController.open();

                                    for (int i = 0; i < productArrayList.size(); i++) {

                                        AddNewItem addNewItem = new AddNewItem();

                                        addNewItem = sqlController.getItem(content_id_result);
                                        double get_price = 0, total_tax;
                                        double cgst, sgst, cess, total_price_included_tax;

                                        if (addNewItem != null) {

                                            int saved_quantity = Integer.parseInt(productArrayList.get(i).getProduct_quantity());
                                            int get_quantity = Integer.parseInt(addNewItem.getNew_item_quantity());

                                            double saved_total_price = Double.parseDouble(productArrayList.get(i).getProduct_total_price());
                                            get_price = Double.parseDouble(addNewItem.getNew_item_total_price());

                                            cgst = Double.parseDouble(productArrayList.get(i).getCgst()) * get_price / 100;
                                            sgst = Double.parseDouble(productArrayList.get(i).getSgst()) * get_price / 100;
                                            cess = Double.parseDouble(productArrayList.get(i).getCess()) * get_price / 100;

                                            total_price_included_tax = cgst + sgst + cess + get_price;

                                            String new_product_quantity = String.valueOf(saved_quantity + get_quantity);
                                            String new_product_total_price_with_tax = String.valueOf(saved_total_price + total_price_included_tax);

                                            sqlController.updateNewItem(addNewItem.getNew_item_price(),
                                                    new_product_quantity,
                                                    new_product_total_price_with_tax,
                                                    content_id_result);

                                        } else {

                                            get_price = Double.parseDouble(productArrayList.get(i).getProduct_price());

                                            cgst = Double.parseDouble(productArrayList.get(i).getCgst()) * get_price / 100;
                                            sgst = Double.parseDouble(productArrayList.get(i).getSgst()) * get_price / 100;
                                            cess = Double.parseDouble(productArrayList.get(i).getCess()) * get_price / 100;

                                            total_price_included_tax = cgst + sgst + cess + get_price;

                                            sqlController.addNewItem(productArrayList.get(i).getProduct_name(),
                                                    productArrayList.get(i).getProduct_price(),
                                                    productArrayList.get(i).getProduct_quantity(),
                                                    productArrayList.get(i).getProduct_price(),
                                                    content_id_result,
                                                    String.valueOf(total_price_included_tax));
                                        }
                                    }
                                    Toast.makeText(ScanListActivity.this, "Items Added Successfully", Toast.LENGTH_SHORT).show();

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });

    }

    private void getBarContentResponse(String content_id_result) {

//        pDialog = new ProgressDialog(ScanListActivity.this);
//        pDialog.setMessage("Loading...");
//        pDialog.show();
        scan_list_progress_bar.setVisibility(View.VISIBLE);
        map.put("content_id", content_id_result);
        map.put("store_id", store_id);

        getBarContentData(Constant.BASE_URL + "/products/" + store_id + "/search/" + content_id_result, map);
    }

    private void getBarContentData(String url, final HashMap<String, String> map) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                scan_list_progress_bar.setVisibility(View.GONE);

                String taxPercent;
                String cgst = null, sgst = null, total_price_included_tax = null;

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (response.contains("errors")) {

                        tv_no_item.setVisibility(View.VISIBLE);
                        lv_scan_list.setVisibility(View.GONE);

                        String errors = jsonObject.getString("errors");
                        JSONObject errorsJsonObject = new JSONObject(errors);
//                        String order = errorsJsonObject.getString("order");
                        Toast.makeText(ScanListActivity.this, "Unable to find the item", Toast.LENGTH_LONG).show();

                        Thread.sleep(1000);

                        finish();
                        return;
                    }
                    else {
                        tv_no_item.setVisibility(View.GONE);
                        lv_scan_list.setVisibility(View.VISIBLE);

                        String product_id = jsonObject.getString("id");
                        String product_name = jsonObject.getString("name");
                        String product_price = jsonObject.getString("price");
//                    String product_discount = jsonObject.getString("product_discount");
                        String product_brand = jsonObject.getString("brand_id");
//                    String product_quantity = jsonObject.getString("product_quantity");
//                        String product_total_price = jsonObject.getString("product_total_price");
//                        String product_barcode = jsonObject.getString("product_barcode");
//                        String cgst = jsonObject.getString("cgst");
//                        String sgst = jsonObject.getString("sgst");
                        String cess = jsonObject.getString("cess");
                        String tax = jsonObject.getString("tax");

                        if (!tax.equalsIgnoreCase("null")) {

                            JSONObject jsonObject1 = new JSONObject(tax);

                            taxPercent = jsonObject1.getString("percent");

                            double taxCalculate = Double.parseDouble(taxPercent) / 2;
                            cgst = String.valueOf(taxCalculate);
                            sgst = cgst;

                            double product_cgst_tax = (taxCalculate * Double.parseDouble(product_price)) / 100;
                            double product_sgst_tax = (taxCalculate * Double.parseDouble(product_price)) / 100;
                            double product_cess_tax = (Double.parseDouble(cess) * Double.parseDouble(product_price)) / 100;

                            total_price_included_tax = String.valueOf(product_price + product_cgst_tax + product_sgst_tax + product_cess_tax);

                            productArrayList.add(new Product(product_id, product_name, product_price,
                                    "", product_brand, "1", product_price, product_id, cgst, sgst, cess, total_price_included_tax));

                        } else {

                            total_price_included_tax = String.valueOf(product_price + 0 + 0 + 0);

                            productArrayList.add(new Product(product_id, product_name, product_price,
                                    "", product_brand, "1", product_price, product_id, "0", "0", cess, total_price_included_tax));

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                scannerAdapter = new ScannerAdapter(productArrayList, new ScannerAdapter.onClickListener() {
                    @Override
                    public void onClickButton(int position, int view, final Product product) {

                        int quantity = Integer.parseInt(product.getProduct_quantity());
                        int newQuantity = 0;
                        String new_total_price;

                        Product addNewItem1 = new Product();
                        addNewItem1.setProduct_name(product.getProduct_name());
                        addNewItem1.setProduct_price(product.getProduct_price());
                        addNewItem1.setNew_item_total_price_included_tax(product.getNew_item_total_price_included_tax());
                        addNewItem1.setCgst(product.getCgst());
                        addNewItem1.setSgst(product.getSgst());
                        addNewItem1.setCess(product.getCess());

                        addNewItem1.setProduct_total_price(product.getProduct_total_price());
                        addNewItem1.setProduct_id(product.getProduct_id());

                        if (view == R.id.iv_scan_minus) {
                            if (Integer.parseInt(product.getProduct_quantity()) > 0) {
                                newQuantity = quantity - 1;

                                if (newQuantity > 0) {
                                    addNewItem1.setProduct_quantity(String.valueOf(newQuantity));
                                } else {

                                    System.out.println("");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ScanListActivity.this);
                                    builder.setMessage("Are you sure you want to remove all "
                                            + product.getProduct_name().toString() + " item?")
                                            .setTitle("Alert!")
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    productArrayList.remove(product);
                                                    scannerAdapter.notifyDataSetChanged();
                                                    tv_no_item.setVisibility(View.VISIBLE);
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            });
                                    builder.show();
                                }

                            } else {
                                addNewItem1.setProduct_quantity(String.valueOf(0));
                            }
                        } else if (view == R.id.iv_scan_add) {
                            newQuantity = quantity + 1;
                            addNewItem1.setProduct_quantity(String.valueOf(newQuantity));

                            new_total_price = String.valueOf(Double.parseDouble(product.getNew_item_total_price_included_tax()) * newQuantity);
                            addNewItem1.setProduct_total_price(new_total_price);

                            productArrayList.set(position, addNewItem1);
                            scannerAdapter.notifyDataSetChanged();

                        }

                        if (newQuantity != 0) {

                            new_total_price = String.valueOf(Double.parseDouble(product.getNew_item_total_price_included_tax()) * newQuantity);
                            addNewItem1.setProduct_total_price(new_total_price);

                            productArrayList.set(position, addNewItem1);
                            scannerAdapter.notifyDataSetChanged();
                            calculateTotalPrice();

                        } else {
                            scannerAdapter.notifyDataSetChanged();
                            calculateTotalPrice();
                        }

                    }
                });

                lv_scan_list.setAdapter(scannerAdapter);
                calculateTotalPrice();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                scan_list_progress_bar.setVisibility(View.INVISIBLE);

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

    public void setFontAwesomeFont() {

        fa_rupee = (TextView) findViewById(R.id.fa_rupee);
        fa_angle_right = (TextView) findViewById(R.id.fa_angle_right);
        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        fa_rupee.setTypeface(fontAwesomeFont);
        fa_angle_right.setTypeface(fontAwesomeFont);

    }

    private void calculateTotalPrice() {
        double temp = 0.0;
        double finalValue;
        DecimalFormat format_2Places = new DecimalFormat("0.00");

        for (int i = 0; i < productArrayList.size(); i++) {

            double total_quantity = Double.parseDouble(productArrayList.get(i).getProduct_quantity());
            double total_price_included_tax = Double.parseDouble(productArrayList.get(i).getNew_item_total_price_included_tax());

            temp = (total_quantity * total_price_included_tax) + temp;
        }

        finalValue = Double.valueOf(format_2Places.format(temp));

        item_total_sum.setText(String.valueOf(String.valueOf(finalValue)));

        editor.putString(SharedPrefernceValue.TEMP, String.valueOf(finalValue));
        editor.commit();
    }

    MenuItem action_settings, action_search, action_invoice_list;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        action_settings = menu.findItem(R.id.action_settings);
        action_search = menu.findItem(R.id.action_search);
        action_invoice_list = menu.findItem(R.id.action_invoice_list);

//        action_settings.setVisible(false);
//        action_search.setVisible(false);
//        action_invoice_list.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:

                return true;

            case R.id.action_invoice_list:
                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
