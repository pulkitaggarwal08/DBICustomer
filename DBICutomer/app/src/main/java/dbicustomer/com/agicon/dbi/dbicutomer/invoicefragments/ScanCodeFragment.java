package dbicustomer.com.agicon.dbi.dbicutomer.invoicefragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.activities.ScanActivity;

public class ScanCodeFragment extends Fragment {

    Button scan_qr_bar_code;
    View view;
    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_scan_code, container, false);
        scan_qr_bar_code = (Button) view.findViewById(R.id.scan_qr_bar_code);

        scan_qr_bar_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(),ScanActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }



}