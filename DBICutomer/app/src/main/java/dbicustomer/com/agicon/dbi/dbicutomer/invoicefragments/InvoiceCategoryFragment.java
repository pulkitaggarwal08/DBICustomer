package dbicustomer.com.agicon.dbi.dbicutomer.invoicefragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
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
import dbicustomer.com.agicon.dbi.dbicutomer.adapters.CategoryListItemRateAdapter;
import dbicustomer.com.agicon.dbi.dbicutomer.adapters.InvoiceSubCategoryButtonAdapter;
import dbicustomer.com.agicon.dbi.dbicutomer.constantfields.Constant;
import dbicustomer.com.agicon.dbi.dbicutomer.db.SQLController;
import dbicustomer.com.agicon.dbi.dbicutomer.interfaces.onClickButtonListener;
import dbicustomer.com.agicon.dbi.dbicutomer.models.AddCategoryItem;
import dbicustomer.com.agicon.dbi.dbicutomer.models.CategoryItemsList;
import dbicustomer.com.agicon.dbi.dbicutomer.models.SubCategoryList;
import dbicustomer.com.agicon.dbi.dbicutomer.sharedpreference.SharedPrefernceValue;
import dbicustomer.com.agicon.dbi.dbicutomer.utils.SnackbarUtil;

/**
 * Created by pulkit on 13/8/17.
 */

public class InvoiceCategoryFragment extends Fragment {

    private View view;
    private Intent intent;
    private Spinner sp_items_names;

    private List<CategoryItemsList> invoiceItemsRatesList = new ArrayList<>();
    private List<SubCategoryList> subCategoryLists = new ArrayList<>();

    private CategoryListItemRateAdapter categoryListItemRateAdapter;
    private InvoiceSubCategoryButtonAdapter invoiceSubCategoryButtonAdapter;

    private RecyclerView rv_item_rates, rv_sub_category_buttons;

    //    private ProgressDialog pDialog;
    private HashMap<String, String> map = new HashMap<>();
    private RequestQueue queue;
    private ProgressBar category_progress_bar;

    private SharedPreferences sharedPreferences;

    private String storeId, sp_selectedItem, user_id, categoryId, login_access_token, registered_access_token;

    private SQLController sqlController;

    private SharedPreferences.Editor editor;

    private TextView text_item_price;
    private Typeface fontAwesomeFont;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_invoice_category, container, false);
        queue = Volley.newRequestQueue(getActivity());
        sqlController = new SQLController(getActivity());

        categoryId = getArguments().getString("category_id");

        sharedPreferences = getActivity().getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        storeId = sharedPreferences.getString(SharedPrefernceValue.STORE_ID, "");
        user_id = sharedPreferences.getString(SharedPrefernceValue.USER_ID, "");

        login_access_token = sharedPreferences.getString(SharedPrefernceValue.LOGIN_ACCESS_TOKEN, "");
        registered_access_token = sharedPreferences.getString(SharedPrefernceValue.REGISTERED_ACCESS_TOKEN, "");

        rv_sub_category_buttons = (RecyclerView) view.findViewById(R.id.rv_sub_category_buttons);
        category_progress_bar = (ProgressBar) view.findViewById(R.id.category_progress_bar);
        text_item_price = (TextView) view.findViewById(R.id.text_item_price);

        rv_sub_category_buttons.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        /*InvoiceItemsRates Item Rate List Spinner Items*/
//        spinnerItems();

        init();
//        getListItemRates();
        setFontAwesome();

        return view;
    }

    private void init() {

        rv_item_rates = (RecyclerView) view.findViewById(R.id.rv_item_rates);
        rv_item_rates.setLayoutManager(new LinearLayoutManager(getContext()));

//        sp_items_names.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                sp_selectedItem = sp_items_names.getSelectedItem().toString();
//
//                if (sp_selectedItem.equalsIgnoreCase("All Items")) {
//
////                    pDialog = new ProgressDialog(getActivity());
////                    pDialog.setMessage("Loading...");
////                    pDialog.show();
                    category_progress_bar.setVisibility(View.VISIBLE);
////                    map.put("store_id", storeId);
////                    map.put("list_type", sp_selectedItem);
                    getSubCategoryButtons(login_access_token, Constant.BASE_URL + "/stores/" + storeId + "/categories/" + categoryId, map);
//
//                } else if (sp_selectedItem.equalsIgnoreCase("Wish List")) {
//
////                    pDialog = new ProgressDialog(getActivity());
////                    pDialog.setMessage("Loading...");
////                    pDialog.show();
//                    category_progress_bar.setVisibility(View.VISIBLE);
////                    map.put("user_id", user_id);
////                    getCategoryListResponse(Constant.BASE_URL + "/stores/" + storeId + "/categories/" + categoryId, map);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Loading...");
//        pDialog.show();
        category_progress_bar.setVisibility(View.VISIBLE);
//        map.put("store_id", homeStoreId);
//        map.put("category_id", categoryId);
//        getSubCategoryButtons(login_access_token, Constant.BASE_URL + "/stores/" + storeId + "/categories/" + categoryId, map);

    }

    private void setFontAwesome() {

        fontAwesomeFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        text_item_price.setTypeface(fontAwesomeFont);
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


    private void getSubCategoryButtons(final String login_access_token, String url, final HashMap<String, String> map) {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                category_progress_bar.setVisibility(View.INVISIBLE);
                try {
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    String subcategories = jsonObject.getString("subcategories");
                    JSONArray invoiceCategoriesJsonArray = new JSONArray(response);
                    for (int i = 0; i < invoiceCategoriesJsonArray.length(); i++) {
                        JSONObject jsonObject1 = invoiceCategoriesJsonArray.getJSONObject(i);
                        String subcategory_id = jsonObject1.getString("subcategory_id");
//                        String category_id = jsonObject1.getString("category_id");
                        String subcategory_name = jsonObject1.getString("name");

                        subCategoryLists.add(new SubCategoryList(subcategory_id, subcategory_name));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                invoiceSubCategoryButtonAdapter = new InvoiceSubCategoryButtonAdapter(subCategoryLists, new onClickButtonListener() {
                    @Override
                    public void onClickButton(int position) {

//                        Toast.makeText(getContext(), subCategoryLists.get(position).getSubcategory_name(), Toast.LENGTH_SHORT).show();

                        String sub_category_id = subCategoryLists.get(position).getSubcategory_id();
                        String sub_category_items = subCategoryLists.get(position).getSubcategory_name();

                        InvoiceSubCategoryFragment invoiceSubCategoryFragment = new InvoiceSubCategoryFragment();

                        Bundle args = new Bundle();
                        args.putString("sub_category_id", sub_category_id);
                        invoiceSubCategoryFragment.setArguments(args);

//                        editor.putString(SharedPrefernceValue.CATEGORY_ID, categoryId);
//                        editor.putString(SharedPrefernceValue.SUB_CATEGORY_ID, sub_category_id);
//                        editor.commit();

                        view.findViewById(R.id.category_invoice).setVisibility(View.GONE);
                        getFragmentManager().beginTransaction().replace(R.id.category_container, invoiceSubCategoryFragment).commit();

                    }
                });
                rv_sub_category_buttons.setAdapter(invoiceSubCategoryButtonAdapter);

                 /*Category Items List*/
                getCategoryList();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                category_progress_bar.setVisibility(View.INVISIBLE);
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

    private void getCategoryList() {

        getCategoryListResponse(Constant.BASE_URL + "/products/" + storeId + "/category/" + categoryId, map);

    }

    public void getCategoryListResponse(String url, final HashMap<String, String> str_map) {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                invoiceItemsRatesList.clear();

                String taxPercent;
                String cgst = null, sgst = null, total_price_included_tax = null;

                try {
                    JSONArray categoryListJsonArray = new JSONArray(response);

                    for (int i = 0; i < categoryListJsonArray.length(); i++) {
                        JSONObject jsonObject = categoryListJsonArray.getJSONObject(i);

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

                            invoiceItemsRatesList.add(new CategoryItemsList(product_id, product_name, product_price, "",
                                    product_id, product_brand, cgst, sgst, cess));

                        } else {

                            invoiceItemsRatesList.add(new CategoryItemsList(product_id, product_name, product_price, "",
                                    product_id, product_brand, "0", "0", "0"));

                        }
//                        invoiceItemsRatesList.add(new CategoryItemsList(product_id, product_name, product_price, product_discount,
//                                product_barcode, product_brand, cgst, sgst, cess));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                categoryListItemRateAdapter = new CategoryListItemRateAdapter(getActivity(), invoiceItemsRatesList, new CategoryListItemRateAdapter.onClickListener() {
                    @Override
                    public void onClickButton(int position, int view, final CategoryItemsList invoiceItemsRates) {

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

                                                AddCategoryItem addNewItem = new AddCategoryItem();

                                                addNewItem = sqlController.getCategoryItem(invoiceItemsRates.getProduct_barcode());
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
                }, new CategoryListItemRateAdapter.onLongItemClickListener() {
                    @Override
                    public void onItemLongClick(int position, int view, final CategoryItemsList categoryItemsList) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Are you sure you wnt to add in your wishlist?")
                                .setTitle("Alert!")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        map.clear();
                                        map.put("user_id", user_id);
                                        map.put("store_id", storeId);
                                        map.put("product_id", categoryItemsList.getProduct_id().toString());
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

                rv_item_rates.setAdapter(categoryListItemRateAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                category_progress_bar.setVisibility(View.INVISIBLE);
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

    private void spinnerItems() {

//        sp_items_names = (Spinner) view.findViewById(R.id.items_names);
        ArrayAdapter<String> adapter;
        List<String> list;

        list = new ArrayList<String>();
        list.add("All Items");
        list.add("Wish List");

        adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_text_view, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_items_names.setAdapter(adapter);

    }

}