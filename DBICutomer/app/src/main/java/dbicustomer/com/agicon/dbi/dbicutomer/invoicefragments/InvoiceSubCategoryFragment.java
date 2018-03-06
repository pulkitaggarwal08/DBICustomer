package dbicustomer.com.agicon.dbi.dbicutomer.invoicefragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.adapters.SubCategoryListItemRateAdapter;
import dbicustomer.com.agicon.dbi.dbicutomer.constantfields.Constant;
import dbicustomer.com.agicon.dbi.dbicutomer.db.SQLController;
import dbicustomer.com.agicon.dbi.dbicutomer.models.AddSubCategoryItem;
import dbicustomer.com.agicon.dbi.dbicutomer.models.SubCategoryItemList;
import dbicustomer.com.agicon.dbi.dbicutomer.sharedpreference.SharedPrefernceValue;
import dbicustomer.com.agicon.dbi.dbicutomer.utils.SnackbarUtil;


/**
 * Created by agicon06 on 20/7/17.
 */

public class InvoiceSubCategoryFragment extends Fragment {

    private RecyclerView rv_sub_category_items;

    private View view;

    private HashMap<String, String> map = new HashMap<>();
    //    private ProgressDialog pDialog;
    private RequestQueue queue;
    private ProgressBar subcategory_progress_bar;

    private String storeId, sub_category_id, login_access_token, registered_access_token;

    private SharedPreferences sharedPreferences;

    private ArrayList<SubCategoryItemList> subCategoryLists = new ArrayList<>();
    private SubCategoryListItemRateAdapter subCategoryListItemRateAdapter;

    private SQLController sqlController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_invoice_sub_category, container, false);

        queue = Volley.newRequestQueue(getActivity());
        sharedPreferences = getActivity().getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, Context.MODE_PRIVATE);
        sqlController = new SQLController(getActivity());

        storeId = sharedPreferences.getString(SharedPrefernceValue.STORE_ID, "");
        sub_category_id = getArguments().getString("sub_category_id");

        login_access_token = sharedPreferences.getString(SharedPrefernceValue.LOGIN_ACCESS_TOKEN, "");
        registered_access_token = sharedPreferences.getString(SharedPrefernceValue.REGISTERED_ACCESS_TOKEN, "");

//        category_id = sharedPreferences.getString(SharedPrefernceValue.CATEGORY_ID, "");
//        subcategory_id = sharedPreferences.getString(SharedPrefernceValue.SUB_CATEGORY_ID, "");

        init();

        return view;
    }

    private void init() {

//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Loading...");
//        pDialog.show();

        subcategory_progress_bar = (ProgressBar) view.findViewById(R.id.subcategory_progress_bar);

        rv_sub_category_items = (RecyclerView) view.findViewById(R.id.rv_sub_category_items);
        rv_sub_category_items.setLayoutManager(new LinearLayoutManager(getContext()));

        subcategory_progress_bar.setVisibility(View.VISIBLE);
//        map.put("store_id", storeId);
//        map.put("category_id", category_id);
//        map.put("subcategory_id", subcategory_id);
        getSubCategoryListResponse(Constant.BASE_URL + "/products/" + storeId + "/category/" + sub_category_id, map);

    }

    private void getSubCategoryListResponse(String url, final HashMap<String, String> map) {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                subcategory_progress_bar.setVisibility(View.INVISIBLE);

                String taxPercent;
                String cgst = null, sgst = null, total_price_included_tax = null;

                try {
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    String data = jsonObject.getString("data");
                    JSONArray subCategorydataJsonArray = new JSONArray(response);
                    for (int i = 0; i < subCategorydataJsonArray.length(); i++) {
                        JSONObject jsonObject = subCategorydataJsonArray.getJSONObject(i);

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

                            subCategoryLists.add(new SubCategoryItemList(product_id, product_name, product_price,
                                    "", product_id, product_brand, cgst, sgst, cess));

                        } else {

                            subCategoryLists.add(new SubCategoryItemList(product_id, product_name, product_price,
                                    "", product_id, product_brand, "0", "0", "0"));

                        }

//                        subCategoryLists.add(new SubCategoryItemList(product_id, product_name, product_price,
//                                "", product_id, product_brand, cgst, sgst, cess));

//                        subCategoryLists.add(new SubCategoryItemList(product_id, product_name, product_price,
//                                product_discount, product_barcode, product_brand, cgst, sgst, cess));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                subCategoryListItemRateAdapter = new SubCategoryListItemRateAdapter(subCategoryLists, new SubCategoryListItemRateAdapter.onClickListener() {
                    @Override
                    public void onClickButton(int position, int view, final SubCategoryItemList invoiceItemsRates) {

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

                                                AddSubCategoryItem addSubCategoryItem;

                                                addSubCategoryItem = sqlController.getSubCategoryItem(invoiceItemsRates.getProduct_barcode());
                                                double get_price;
                                                double cgst, sgst, cess, total_price_included_tax;

                                                if (addSubCategoryItem != null) {

                                                    int saved_quantity = Integer.parseInt(addSubCategoryItem.getNew_item_quantity());
                                                    int get_quantity = Integer.parseInt("1");

                                                    double saved_total_price = Double.parseDouble(addSubCategoryItem.getNew_item_total_price());
                                                    get_price = Double.parseDouble(invoiceItemsRates.getProduct_price());

                                                    cgst = Double.parseDouble(invoiceItemsRates.getCgst()) * get_price / 100;
                                                    sgst = Double.parseDouble(invoiceItemsRates.getSgst()) * get_price / 100;
                                                    cess = Double.parseDouble(invoiceItemsRates.getCess()) * get_price / 100;

                                                    total_price_included_tax = cgst + sgst + cess + get_price;

                                                    String new_product_quantity = String.valueOf(saved_quantity + get_quantity);
                                                    String new_product_total_price_with_tax = String.valueOf(saved_total_price + total_price_included_tax);

                                                    sqlController.updateNewItem(addSubCategoryItem.getNew_item_price(),
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
                });
                rv_sub_category_items.setAdapter(subCategoryListItemRateAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                subcategory_progress_bar.setVisibility(View.INVISIBLE);
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


}
