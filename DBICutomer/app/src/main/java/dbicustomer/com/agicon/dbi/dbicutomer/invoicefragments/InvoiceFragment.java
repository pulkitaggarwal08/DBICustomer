package dbicustomer.com.agicon.dbi.dbicutomer.invoicefragments;

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
import dbicustomer.com.agicon.dbi.dbicutomer.activities.InvoiceListInfoActivity;
import dbicustomer.com.agicon.dbi.dbicutomer.activities.PendingPushInvoiceActivity;
import dbicustomer.com.agicon.dbi.dbicutomer.adapters.InvoiceListFragmentAdapter;
import dbicustomer.com.agicon.dbi.dbicutomer.constantfields.Constant;
import dbicustomer.com.agicon.dbi.dbicutomer.models.InvoiceList;
import dbicustomer.com.agicon.dbi.dbicutomer.sharedpreference.SharedPrefernceValue;

public class InvoiceFragment extends Fragment {

    private View view;
    private String store_id, registered_access_token, login_access_token;
    ;

    private List<InvoiceList> invoiceList = new ArrayList<>();

    private HashMap<String, String> map = new HashMap<>();
    //    private ProgressDialog pDialog;
    private RequestQueue queue;
    private ProgressBar invocie_progress_bar;

    private InvoiceListFragmentAdapter invoiceListFragmentAdapter;
    RecyclerView rv_invoice_list;

    private SharedPreferences sharedPreferences;
    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_invoice, container, false);
        queue = Volley.newRequestQueue(getActivity());

        sharedPreferences = getActivity().getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, Context.MODE_PRIVATE);
        store_id = sharedPreferences.getString(SharedPrefernceValue.STORE_ID, "");

        registered_access_token = sharedPreferences.getString(SharedPrefernceValue.REGISTERED_ACCESS_TOKEN, "");
        login_access_token = sharedPreferences.getString(SharedPrefernceValue.LOGIN_ACCESS_TOKEN, "");

        rv_invoice_list = (RecyclerView) view.findViewById(R.id.rv_invoice_list);
        invocie_progress_bar = (ProgressBar) view.findViewById(R.id.invocie_progress_bar);

        rv_invoice_list.setLayoutManager(new LinearLayoutManager(getContext()));

//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Loading...");
//        pDialog.show();
        invocie_progress_bar.setVisibility(View.VISIBLE);
//        map.put("store_id", storeId);
        getInvoiceList(Constant.BASE_URL + "/user/my-orders", map);

        return view;
    }

    private void getInvoiceList(String url, final HashMap<String, String> map) {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                invocie_progress_bar.setVisibility(View.INVISIBLE);
                try {
                    JSONArray inboundListJsonArray = new JSONArray(response);
                    for (int i = 0; i < inboundListJsonArray.length(); i++) {
                        JSONObject jsonObject = inboundListJsonArray.getJSONObject(i);

                        String invoice_id = jsonObject.getString("id");
                        String created_at = jsonObject.getString("created_at");
                        String order_status = jsonObject.getString("payments_count");
                        String order_amount = jsonObject.getString("net_amount");

                        String store = jsonObject.getString("store");
                        JSONObject jsonObject1 = new JSONObject(store);
                        String store_name = jsonObject1.getString("name");
                        String store_id = jsonObject1.getString("id");

                        String splitDate[] = created_at.split(" ");
                        String order_date = splitDate[splitDate.length - 2];

                        invoiceList.add(new InvoiceList(invoice_id, store_name, order_date, order_status, order_amount,store_id));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                invoiceListFragmentAdapter = new InvoiceListFragmentAdapter(getActivity(), invoiceList, new InvoiceListFragmentAdapter.onClickListener() {
                    @Override
                    public void onClickButton(int position, int view, InvoiceList invoiceList) {

                        if (view == R.id.main_relative_layout) {

                            String order_status = invoiceList.getOrder_status();
                            String invoice_id = invoiceList.getInvoice_id();
                            String store_id = invoiceList.getStore_id();

                            if (order_status.equalsIgnoreCase("0")) {
                                intent = new Intent(getActivity(), PendingPushInvoiceActivity.class);
                                intent.putExtra("order_status", order_status);
                                intent.putExtra("invoice_id", invoice_id);
                                intent.putExtra("store_id", store_id);
                                startActivity(intent);
                            } else if (order_status.equalsIgnoreCase("1")) {
                                intent = new Intent(getActivity(), InvoiceListInfoActivity.class);
                                intent.putExtra("order_status", order_status);
                                intent.putExtra("invoice_id", invoice_id);
                                intent.putExtra("store_id", store_id);
                                startActivity(intent);
                            }
                        }

                    }
                });

                rv_invoice_list.setAdapter(invoiceListFragmentAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                invocie_progress_bar.setVisibility(View.INVISIBLE);

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

}
