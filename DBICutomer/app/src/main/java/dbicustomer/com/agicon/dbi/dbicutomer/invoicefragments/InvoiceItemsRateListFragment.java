package dbicustomer.com.agicon.dbi.dbicutomer.invoicefragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.activities.RegisterActivity;
import dbicustomer.com.agicon.dbi.dbicutomer.adapters.ListItemRateAdapter;
import dbicustomer.com.agicon.dbi.dbicutomer.constantfields.Constant;
import dbicustomer.com.agicon.dbi.dbicutomer.db.SQLController;
import dbicustomer.com.agicon.dbi.dbicutomer.homefragments.HomeInvoiceFragment;
import dbicustomer.com.agicon.dbi.dbicutomer.models.AddNewItem;
import dbicustomer.com.agicon.dbi.dbicutomer.models.InvoiceItemsRates;
import dbicustomer.com.agicon.dbi.dbicutomer.sharedpreference.SharedPrefernceValue;
import dbicustomer.com.agicon.dbi.dbicutomer.utils.SnackbarUtil;

public class InvoiceItemsRateListFragment extends Fragment {

    private View view;
    private Intent intent;
    private Spinner sp_items_names;
    private List<InvoiceItemsRates> invoiceItemsRatesList = new ArrayList<>();
    private ListItemRateAdapter listItemRateAdapter;
    private RecyclerView rv_item_rates;

    //    private ProgressDialog pDialog;
    private HashMap<String, String> map = new HashMap<>();
    private RequestQueue queue;
    private ProgressBar rate_list_progress_bar;

    private SharedPreferences sharedPreferences;

    private String storeId, sp_selectedItem, user_id, categoryId, login_access_token, registered_access_token;

    private SQLController sqlController;

    private TextView text_item_price;
    private Typeface fontAwesomeFont;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.invoice_items_rate_list, container, false);
        queue = Volley.newRequestQueue(getActivity());
        sqlController = new SQLController(getActivity());

//        categoryId = getArguments().getString("category_items");

        sharedPreferences = getActivity().getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, Context.MODE_PRIVATE);
        storeId = sharedPreferences.getString(SharedPrefernceValue.STORE_ID, "");

        login_access_token = sharedPreferences.getString(SharedPrefernceValue.LOGIN_ACCESS_TOKEN, "");
        registered_access_token = sharedPreferences.getString(SharedPrefernceValue.REGISTERED_ACCESS_TOKEN, "");

//        user_id = sharedPreferences.getString(SharedPrefernceValue.USER_ID, "");

        /*InvoiceItemsRates Item Rate List Spinner Items*/
//        spinnerItems();

        init();

        setFontAwesome();

        return view;
    }

    private void init() {

        rv_item_rates = (RecyclerView) view.findViewById(R.id.rv_item_rates);
        rate_list_progress_bar = (ProgressBar) view.findViewById(R.id.rate_list_progress_bar);
        text_item_price = (TextView) view.findViewById(R.id.text_item_price);

        rv_item_rates.setLayoutManager(new LinearLayoutManager(getContext()));

//        sp_items_names.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                sp_selectedItem = sp_items_names.getSelectedItem().toString();
//
//                if (sp_selectedItem.equalsIgnoreCase("All Items") || sp_selectedItem.equalsIgnoreCase("Discounted")) {
//
////                    pDialog = new ProgressDialog(getActivity());
////                    pDialog.setMessage("Loading...");
////                    pDialog.show();
                    rate_list_progress_bar.setVisibility(View.VISIBLE);
////                    map.put("store_id", storeId);
////                    map.put("list_type", sp_selectedItem);
                    getInvoiceResponse(Constant.BASE_URL + "/products/" + storeId, map);
//
//                } else if (sp_selectedItem.equalsIgnoreCase("Wish List")) {
//
////                    pDialog = new ProgressDialog(getActivity());
////                    pDialog.setMessage("Loading...");
////                    pDialog.show();
////                    map.put("user_id", user_id);
//                    rate_list_progress_bar.setVisibility(View.VISIBLE);
////                    getInvoiceResponse(Constant.BASE_URL + "/product/wishlist", map);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

    }

    private void setFontAwesome() {

        fontAwesomeFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        text_item_price.setTypeface(fontAwesomeFont);

    }

    public void getInvoiceResponse(String url, final HashMap<String, String> str_map) {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                rate_list_progress_bar.setVisibility(View.INVISIBLE);
                invoiceItemsRatesList.clear();

                String taxPercent;
                String cgst = null, sgst = null, total_price_included_tax = null;

                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String all_product_list = jsonObject.getString("all_product_list");
//                    JSONArray allProductListJsonArray = new JSONArray(all_product_list);

                    JSONArray allProductListJsonArray = new JSONArray(response);

                    for (int i = 0; i < allProductListJsonArray.length(); i++) {
                        JSONObject jsonObject = allProductListJsonArray.getJSONObject(i);

                        String cess = jsonObject.getString("cess");

                        String product = jsonObject.getString("product");
                        JSONObject productjsonObject = new JSONObject(product);

                        String product_id = productjsonObject.getString("id");
                        String product_name = productjsonObject.getString("name");
                        String product_price = productjsonObject.getString("price");
//                        String product_discount = jsonObject.getString("product_discount");
//                        String product_barcode = jsonObject.getString("product_barcode");
                        String product_brand = productjsonObject.getString("brand_id");
//                        String cgst = jsonObject.getString("cgst");
//                        String sgst = jsonObject.getString("sgst");

                        String tax = productjsonObject.getString("tax");

                        if (!tax.equalsIgnoreCase("null")) {

                            JSONObject jsonObject1 = new JSONObject(tax);

                            taxPercent = jsonObject1.getString("percent");

                            double taxCalculate = Double.parseDouble(taxPercent) / 2;
                            cgst = String.valueOf(taxCalculate);
                            sgst = cgst;

                            invoiceItemsRatesList.add(new InvoiceItemsRates(product_id, product_name, product_price,
                                    "", product_id, product_brand, cgst, sgst, cess));
                        } else {

                            invoiceItemsRatesList.add(new InvoiceItemsRates(product_id, product_name, product_price,
                                    "", product_id, product_brand, "0", "0", "0"));

                        }
//                        invoiceItemsRatesList.add(new ReceiptItemsRates(product_id, product_name, product_price, product_discount,
//                                product_barcode, product_brand, cgst, sgst, cess));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listItemRateAdapter = new ListItemRateAdapter(getContext(), invoiceItemsRatesList, new ListItemRateAdapter.onClickListener() {
                    @Override
                    public void onClickButton(int position, int view, final InvoiceItemsRates invoiceItemsRates) {

                        if (view == R.id.iv_invoice_add_list_item) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Are you sure you want to add product?")
                                    .setTitle("Add New Product")
//                                  .setIcon(R.drawable.dbi_logo)
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            try {
                                                sqlController.open();

                                                AddNewItem addNewItem = new AddNewItem();

                                                addNewItem = sqlController.getItem(invoiceItemsRates.getProduct_barcode());
                                                double get_price = 0, total_tax;
                                                double cgst, sgst, cess, total_price_included_tax;

                                                if (addNewItem != null) {

                                                    int saved_quantity = Integer.parseInt(addNewItem.getNew_item_quantity());
                                                    int get_quantity = Integer.parseInt("1");

                                                    double saved_total_price = Double.parseDouble(addNewItem.getNew_item_total_price());
                                                    get_price = Double.parseDouble(invoiceItemsRates.getProduct_price());

                                                    cgst = Double.parseDouble(invoiceItemsRates.getCgst()) * get_price / 100;
                                                    sgst = Double.parseDouble(invoiceItemsRates.getSgst()) * get_price / 100;
                                                    cess = Double.parseDouble(invoiceItemsRates.getCess()) * get_price / 100;

                                                    total_price_included_tax = cgst + sgst + cess + get_price;

                                                    String new_product_quantity = String.valueOf(saved_quantity + get_quantity);
                                                    String new_product_total_price_with_tax = String.valueOf(saved_total_price + total_price_included_tax);

                                                    sqlController.updateNewItem(addNewItem.getNew_item_price(),
                                                            new_product_quantity,
                                                            new_product_total_price_with_tax,
                                                            invoiceItemsRates.getProduct_barcode());

                                                } else {

                                                    get_price = Double.parseDouble(invoiceItemsRates.getProduct_price());

                                                    cgst = Double.parseDouble(invoiceItemsRates.getCgst()) * get_price / 100;
                                                    sgst = Double.parseDouble(invoiceItemsRates.getSgst()) * get_price / 100;
                                                    cess = Double.parseDouble(invoiceItemsRates.getCess()) * get_price / 100;

                                                    total_price_included_tax = cgst + sgst + cess + get_price;

                                                    sqlController.addNewItem(invoiceItemsRates.getProduct_name(),
                                                            invoiceItemsRates.getProduct_price(), "1",
                                                            invoiceItemsRates.getProduct_price(),
                                                            invoiceItemsRates.getProduct_barcode(),
                                                            String.valueOf(total_price_included_tax));

                                                }

                                                Toast.makeText(getContext(), "Add Item successfully", Toast.LENGTH_SHORT).show();

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
                    }
                }, new ListItemRateAdapter.onLongItemClickListener() {
                    @Override
                    public void onItemLongClick(int position, int view, final InvoiceItemsRates invoiceItemsRates) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Are you sure you wnt to add in your wishlist?")
                                .setTitle("Alert!")
                                .setCancelable(false)
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        map.clear();
                                        map.put("user_id", user_id);
                                        map.put("store_id", storeId);
                                        map.put("product_id", invoiceItemsRates.getProduct_id().toString());
                                        addToWishList(Constant.BASE_URL + "/product/wishlist/add", map);
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
                });

                rv_item_rates.setAdapter(listItemRateAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                rate_list_progress_bar.setVisibility(View.INVISIBLE);
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

    private void addToWishList(String url, final HashMap<String, String> map) {
        System.out.println("url" + url);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");

                    SnackbarUtil.showLongSnackbar(getActivity(), message);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    Toast.makeText(getContext(), "try again", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "try again", Toast.LENGTH_LONG).show();
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


    private void spinnerItems() {

//        sp_items_names = (Spinner) view.findViewById(R.id.items_names);
        ArrayAdapter<String> adapter;
        List<String> list;

        list = new ArrayList<String>();
        list.add("All Items");
        list.add("Wish List");
        list.add("Discounted");

        adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_text_view, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_items_names.setAdapter(adapter);

    }

}