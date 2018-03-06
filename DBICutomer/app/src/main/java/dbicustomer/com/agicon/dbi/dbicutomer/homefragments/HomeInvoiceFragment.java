package dbicustomer.com.agicon.dbi.dbicutomer.homefragments;

import android.content.Context;
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
import dbicustomer.com.agicon.dbi.dbicutomer.adapters.InvoiceCategoryButtonsAdapter;
import dbicustomer.com.agicon.dbi.dbicutomer.constantfields.Constant;
import dbicustomer.com.agicon.dbi.dbicutomer.db.SQLController;
import dbicustomer.com.agicon.dbi.dbicutomer.invoicefragments.InvoiceCategoryFragment;
import dbicustomer.com.agicon.dbi.dbicutomer.invoicefragments.InvoiceItemsRateListFragment;
import dbicustomer.com.agicon.dbi.dbicutomer.invoicefragments.ScanCodeFragment;
import dbicustomer.com.agicon.dbi.dbicutomer.models.CategoryList;
import dbicustomer.com.agicon.dbi.dbicutomer.sharedpreference.SharedPrefernceValue;

import static android.R.attr.id;

public class HomeInvoiceFragment extends Fragment {

    private RecyclerView rv_category_buttons;
    private View view;
    private LinearLayout linear_list, linear_scan_code;
    private Intent intent;

    private InvoiceCategoryButtonsAdapter invoiceCategoryButtonsAdapter;
    private List<CategoryList> categoryLists = new ArrayList<>();

    private HashMap<String, String> map = new HashMap<>();
    //    private ProgressDialog pDialog;
    private RequestQueue queue;
    private ProgressBar invoice_progress_bar;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private TextView fa_rupee, fa_angle_right, item_total_sum;
    private Typeface fontAwesomeFont;

    public SQLController sqlController;

    private String home_store_id, registered_access_token, login_access_token, category_store_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home_invoice, container, false);
        queue = Volley.newRequestQueue(getActivity());

        sharedPreferences = getActivity().getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        sqlController = new SQLController(getContext());

//        item_total_sum = (TextView) view.findViewById(R.id.item_total_sum);

        init();

        return view;
    }

    public void init() {

        home_store_id = getActivity().getIntent().getStringExtra("home_store_id");

//        storeId = sharedPreferences.getString(SharedPrefernceValue.STORE_ID, "");
        login_access_token = sharedPreferences.getString(SharedPrefernceValue.LOGIN_ACCESS_TOKEN, "");
        registered_access_token = sharedPreferences.getString(SharedPrefernceValue.REGISTERED_ACCESS_TOKEN, "");

         /*Dashboard InvoiceItemsRates Category Buttons*/

        rv_category_buttons = (RecyclerView) view.findViewById(R.id.rv_category_buttons);
        invoice_progress_bar = (ProgressBar) view.findViewById(R.id.invoice_progress_bar);
        rv_category_buttons.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Loading...");
//        pDialog.show();
        invoice_progress_bar.setVisibility(View.VISIBLE);

        if ((!registered_access_token.equals(null)) && (!registered_access_token.equals(""))) {
            getCategoryButtons(registered_access_token);
        } else if ((!login_access_token.equals(null)) && (!login_access_token.equals(""))) {

            getCategoryButtons(login_access_token);
        }

        /*Dashboard InvoiceItemsRates Linear List and Scan Bar Code Buttons*/
        dashboardInvoiceButtons();

        /*Button Listeners*/
//        onClickNext();

//        setFontAwesomeFont();

    }

    /*Dashboard InvoiceItemsRates Category Buttons*/
    private void getCategoryButtons(String login_access_token) {

        getInvoiceCategories(login_access_token, Constant.BASE_URL + "/stores/" + home_store_id + "/categories", map);

        getFragmentManager().beginTransaction().add(R.id.invoice_container, new InvoiceItemsRateListFragment()).commit();
    }

    private void getInvoiceCategories(final String login_access_token, String url, final HashMap<String, String> map) {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                invoice_progress_bar.setVisibility(View.INVISIBLE);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

//                        category_store_id = jsonObject.getString("store_id");
                        String category_id = jsonObject.getString("id");
                        String category_name = jsonObject.getString("name");


                        categoryLists.add(new CategoryList(category_id, category_name));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                invoiceCategoryButtonsAdapter = new InvoiceCategoryButtonsAdapter(getActivity(), categoryLists, new InvoiceCategoryButtonsAdapter.onClickListener() {
                    @Override
                    public void onClickButton(int position, int v) {
//                        Toast.makeText(getContext(), categoryLists.get(position).getCategory_name(), Toast.LENGTH_SHORT).show();

                        String category_id = categoryLists.get(position).getCategory_id();
                        String category_items = categoryLists.get(position).getCategory_name();


                        InvoiceCategoryFragment newCategoryFragment = new InvoiceCategoryFragment();
                        Bundle args = new Bundle();
                        args.putString("category_id", category_id);
                        newCategoryFragment.setArguments(args);

                        view.findViewById(R.id.dashboard_invoice).setVisibility(View.GONE);
                        getFragmentManager().beginTransaction().replace(R.id.invoice_container, newCategoryFragment).commit();

                    }
                });
                rv_category_buttons.setAdapter(invoiceCategoryButtonsAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                invoice_progress_bar.setVisibility(View.INVISIBLE);
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

    /*Dashboard InvoiceItemsRates Linear List and Scan Bar Code Buttons*/
    private void dashboardInvoiceButtons() {

        linear_list = (LinearLayout) view.findViewById(R.id.linear_list);
        linear_scan_code = (LinearLayout) view.findViewById(R.id.linear_scan_code);

        linear_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv_category_buttons.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().replace(R.id.invoice_container, new InvoiceItemsRateListFragment()).commit();
            }
        });

        linear_scan_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rv_category_buttons.setVisibility(View.GONE);
                getFragmentManager().beginTransaction().replace(R.id.invoice_container, new ScanCodeFragment()).commit();
            }
        });
    }

    /*Button Listeners*/
//    private void onClickNext() {
//        rl_next = (RelativeLayout) view.findViewById(R.id.rl_next);
//
//        rl_next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                intent = new Intent(getContext(), GetAllShoppingListActivity.class);
//                startActivity(intent);
//            }
//        });
//    }

    public void setFontAwesomeFont() {

        fa_rupee = (TextView) view.findViewById(R.id.fa_rupee);
        fa_angle_right = (TextView) view.findViewById(R.id.fa_angle_right);
        fontAwesomeFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        fa_rupee.setTypeface(fontAwesomeFont);
        fa_angle_right.setTypeface(fontAwesomeFont);

    }

}
