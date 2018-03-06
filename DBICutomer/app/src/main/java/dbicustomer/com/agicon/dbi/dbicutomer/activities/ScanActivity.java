package dbicustomer.com.agicon.dbi.dbicutomer.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

import dbicustomer.com.agicon.dbi.dbicutomer.sharedpreference.SharedPrefernceValue;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScanActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;
    //camera permission is needed.

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Intent intent;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        sharedPreferences = getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        mScannerView = new ZBarScannerView(this);
        setContentView(mScannerView);

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(me.dm7.barcodescanner.zbar.Result result) {

        if (result.getContents() == null) {
            finish();

        } else {

            Log.v("kkkk", result.getContents());
            Log.v("uuuu", result.getBarcodeFormat().getName());

            int item_id = result.getBarcodeFormat().getId();
            String content_name = result.getBarcodeFormat().getName();
            String content_id_result = result.getContents();

//            editor.putString(SharedPrefernceValue.CONTENT_ID, String.valueOf(item_id));
//            editor.putString(SharedPrefernceValue.CONTENT_NAME, content_name);
//            editor.putString(SharedPrefernceValue.CONTENT_ID_RESULT, content_id_result);
//            editor.commit();

            intent = new Intent(this, ScanListActivity.class);
            intent.putExtra("item_id", item_id);
            intent.putExtra("content_name", content_name);
            intent.putExtra("content_id_result", content_id_result);
            startActivity(intent);

//            getContentResponse(String.valueOf(content_id_result));

        }
    }

}

