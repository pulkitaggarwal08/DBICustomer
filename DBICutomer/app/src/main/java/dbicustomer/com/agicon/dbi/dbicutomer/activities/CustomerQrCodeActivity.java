package dbicustomer.com.agicon.dbi.dbicutomer.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.constantfields.Constant;
import dbicustomer.com.agicon.dbi.dbicutomer.db.SQLController;
import dbicustomer.com.agicon.dbi.dbicutomer.sharedpreference.SharedPrefernceValue;

public class CustomerQrCodeActivity extends AppCompatActivity {

    private TextView fa_rupee, tv_please_request;
    private Typeface fontAwesomeFont;
    private Toolbar customer_code_toolbar;
    private ImageView iv_qr_code;

    private Button btn_ok;

    private HashMap<String, String> map = new HashMap<>();
    private RequestQueue queue;

    private Intent intent;
    private Bitmap bitmap;

    private ProgressBar pb_customer;

    public final static int QRcodeWidth = 500;

    public SQLController sqlController;

    private SharedPreferences sharedPreferences;

    private String user_id, store_id, order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_qr_code);

        init();

        setFontAwesomeFont();
    }

    private void init() {

        sqlController = new SQLController(this);

        intent = getIntent();
        user_id = getIntent().getStringExtra("user_id");
        store_id = getIntent().getStringExtra("store_id");
        order_id = getIntent().getStringExtra("order_id");

        queue = Volley.newRequestQueue(getApplicationContext());
        sharedPreferences = getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, Context.MODE_PRIVATE);

        customer_code_toolbar = (Toolbar) findViewById(R.id.customer_code_toolbar);
        iv_qr_code = (ImageView) findViewById(R.id.iv_qr_code);
        pb_customer = (ProgressBar) findViewById(R.id.pb_customer);
        tv_please_request = (TextView) findViewById(R.id.tv_please_request);
        btn_ok = (Button) findViewById(R.id.btn_ok);

        setSupportActionBar(customer_code_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (user_id == null || store_id == null || order_id == null) {
            return;
        } else {
            pb_customer.setVisibility(View.VISIBLE);
            tv_please_request.setText("Please wait while generating the QR Code...");
            tv_please_request.setVisibility(View.VISIBLE);

            new MyAsyncTask().execute();
        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerQrCodeActivity.this);
                builder.setMessage("Invoice has successfully been pushed to the shopkeeper's end?")
                        .setTitle("DBI Customer")
//                    .setIcon(R.drawable.dbi_logo)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sqlController.removeAll();

                                Intent intent = new Intent(CustomerQrCodeActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();
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

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            generateQRCode();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            iv_qr_code.setImageBitmap(bitmap);
            pb_customer.setVisibility(View.GONE);
            tv_please_request.setText("Please request shopkeeper to scan the above QR code");
//            qrCodeResponse();
        }
    }

    private void generateQRCode() {

        try {
//            bitmap = TextToImageEncode(user_id + "," + store_id + "," + order_id);
            bitmap = TextToImageEncode(order_id);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void setFontAwesomeFont() {

        fa_rupee = (TextView) findViewById(R.id.fa_rupee);
        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        fa_rupee.setTypeface(fontAwesomeFont);

    }

    public void qrCodeResponse() {

        map.put("user_id", user_id);
        map.put("qr_code_id", user_id);
        getQrCodeResponse(Constant.BASE_URL + "/qrList", map);

    }

    public void getQrCodeResponse(String url, final HashMap<String, String> map) {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");

                    Toast.makeText(CustomerQrCodeActivity.this, message, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

    private Bitmap TextToImageEncode(String Value) throws WriterException {

        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.DATA_MATRIX.QR_CODE, QRcodeWidth, QRcodeWidth, null);

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
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
//
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
