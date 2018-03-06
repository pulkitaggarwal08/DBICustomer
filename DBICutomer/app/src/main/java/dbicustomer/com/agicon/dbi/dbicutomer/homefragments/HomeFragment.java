package dbicustomer.com.agicon.dbi.dbicutomer.homefragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
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
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.adapters.HomeButtonAdapter;
import dbicustomer.com.agicon.dbi.dbicutomer.constantfields.Constant;
import dbicustomer.com.agicon.dbi.dbicutomer.interfaces.onClickButtonListener;
import dbicustomer.com.agicon.dbi.dbicutomer.models.Home;
import dbicustomer.com.agicon.dbi.dbicutomer.sharedpreference.SharedPrefernceValue;


public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private View view;
    private HomeButtonAdapter homeButtonAdapter;
    private RecyclerView rv_category_buttons;
    private List<Home> categoryList = new ArrayList<>();
    private List<Home> image_list = new ArrayList<>();

    private TextView text_bill_uploaded, text_spending, text_unbilled, text_orders, offers_date;

    //    private ProgressDialog pDialog;
    private RequestQueue queue;
    private HashMap<String, String> map = new HashMap<>();
    private ProgressBar home_progress_bar;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String str_user_id;
    private String str_user_city_id;
    private String str_city_name;
    private String str_bills_uploaded;
    private String str_total_spending;
    private String str_total_orders;
    private String str_offers_date, registered_access_token, login_access_token;

    private SliderLayout sliderLayout;
    private HashMap<String, String> hash_file_maps;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        queue = Volley.newRequestQueue(getActivity());
        sharedPreferences = getActivity().getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        registered_access_token = sharedPreferences.getString(SharedPrefernceValue.REGISTERED_ACCESS_TOKEN, "");
        login_access_token = sharedPreferences.getString(SharedPrefernceValue.LOGIN_ACCESS_TOKEN, "");

        hash_file_maps = new HashMap<String, String>();

        init();

        return view;
    }

    private void init() {

        context = getActivity();

        str_user_id = sharedPreferences.getString(SharedPrefernceValue.USER_ID, "");
        str_user_city_id = sharedPreferences.getString(SharedPrefernceValue.USER_CITY_ID, "");

        sliderLayout = (SliderLayout) view.findViewById(R.id.slider);

        rv_category_buttons = (RecyclerView) view.findViewById(R.id.rv_category_buttons);
        text_bill_uploaded = (TextView) view.findViewById(R.id.text_bill_uploaded);
        text_spending = (TextView) view.findViewById(R.id.text_spending);
        text_unbilled = (TextView) view.findViewById(R.id.text_unbilled);
        text_orders = (TextView) view.findViewById(R.id.text_orders);
        offers_date = (TextView) view.findViewById(R.id.offers_date);

        home_progress_bar = (ProgressBar) view.findViewById(R.id.home_progress_bar);

        rv_category_buttons.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Loading...");
//        pDialog.show();
//        pDialog.setCancelable(false);
        home_progress_bar.setVisibility(View.VISIBLE);
//        map.put("user_id", str_user_id);
//        map.put("user_city_id", str_user_city_id);
        getHomeFragmentResponse(Constant.BASE_URL + "/city/" + str_user_city_id + "/offers", map);
    }

    private void getHomeFragmentResponse(String url, final HashMap<String, String> map) {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                home_progress_bar.setVisibility(View.GONE);
                image_list.clear();
                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    str_city_name = jsonObject.getString("city_name");
//                    str_bills_uploaded = jsonObject.getString("bills_uploaded");
//                    str_total_spending = jsonObject.getString("total_spending");
//                    str_total_orders = jsonObject.getString("total_orders");
//                    str_offers_date = jsonObject.getString("date");
//
//                    editor.putString(SharedPrefernceValue.CITY_NAME, str_city_name);
//                    editor.commit();
//
//                    String offer_images = jsonObject.getString("offer_images");
                    JSONArray offerImageJsonArray = new JSONArray(response);
                    for (int i = 0; i < offerImageJsonArray.length(); i++) {
                        JSONObject jsonObject1 = offerImageJsonArray.getJSONObject(i);
                        String picture = jsonObject1.getString("picture");

                        String image = Constant.OFFER_BASE_URL + picture;

                        image_list.add(new Home(image));
                        System.out.println("");
                    }

                    getUserStats(Constant.BASE_URL + "/user/stats", map);
                    getStoreTypes(Constant.BASE_URL + "/store/types", map);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getImagesslider();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                home_progress_bar.setVisibility(View.GONE);
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

    private void getUserStats(String url, final HashMap<String, String> map) {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                home_progress_bar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String bills_uploaded = jsonObject.getString("bills_uploaded");
                    String orders_completed = jsonObject.getString("orders_completed");
                    String total_spent = jsonObject.getString("total_spent");

                    offers_date.setText(str_offers_date);
                    text_bill_uploaded.setText(bills_uploaded);
                    text_spending.setText(total_spent);

                    int total_bills_uploaded = Integer.parseInt(bills_uploaded);
                    int total_orders_completed = Integer.parseInt(orders_completed);

                    int total_unbilled = total_bills_uploaded - total_orders_completed;

                    text_unbilled.setText(String.valueOf(total_unbilled));
                    text_orders.setText(total_spent);

                    Date date = new Date();
                    CharSequence sequence = DateFormat.format("MMMM", date.getTime());
                    String dateFormat = sequence.toString();

                    offers_date.setText(dateFormat);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getImagesslider();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                home_progress_bar.setVisibility(View.GONE);
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

    private void getStoreTypes(String url, final HashMap<String, String> map) {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
//                pDialog.dismiss();
                home_progress_bar.setVisibility(View.GONE);
                try {
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    String store_type = jsonObject.getString("store_type");
                    JSONArray storeTypeJsonArray = new JSONArray(response);

                    for (int i = 0; i < storeTypeJsonArray.length(); i++) {
                        JSONObject jsonObject1 = storeTypeJsonArray.getJSONObject(i);
                        String store_type_id = jsonObject1.getString("id");
                        String store_type_name = jsonObject1.getString("name");

                        categoryList.add(new Home(store_type_id, store_type_name, str_city_name, str_bills_uploaded,
                                str_total_spending, str_total_orders, str_offers_date));
                    }

                    homeButtonAdapter = new HomeButtonAdapter(categoryList, new onClickButtonListener() {
                        @Override
                        public void onClickButton(int position) {

                            String storeTypeName = categoryList.get(position).getStore_type_name();
                            String storeTypeId = categoryList.get(position).getStore_type_id();

                            HomeCategoryFragment homeCategoryFragment = new HomeCategoryFragment();
                            Bundle argBundles = new Bundle();
                            argBundles.putString("store_type_name", storeTypeName);
                            argBundles.putString("storeTypeId", storeTypeId);
                            homeCategoryFragment.setArguments(argBundles);

                            view.findViewById(R.id.dashboard_home).setVisibility(View.GONE);
                            getFragmentManager().beginTransaction().replace(R.id.home_container, homeCategoryFragment).commit();

                        }
                    });
                    rv_category_buttons.setAdapter(homeButtonAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getImagesslider();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                home_progress_bar.setVisibility(View.GONE);
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

    private void getImagesslider() {

        for (int i = 0; i < image_list.size(); i++) {

            hash_file_maps.put("Android" + i, image_list.get(i).getImage());
        }

        for (String name : hash_file_maps.keySet()) {

            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView
//                    .description(name)
                    .image(hash_file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
        sliderLayout.addOnPageChangeListener(this);

    }

    @Override
    public void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
