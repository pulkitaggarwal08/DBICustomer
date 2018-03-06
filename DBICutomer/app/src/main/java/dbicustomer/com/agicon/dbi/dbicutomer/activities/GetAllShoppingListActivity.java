package dbicustomer.com.agicon.dbi.dbicutomer.activities;

import android.app.ProgressDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.adapters.GetAllShoppingListAdapter;
import dbicustomer.com.agicon.dbi.dbicutomer.db.SQLController;
import dbicustomer.com.agicon.dbi.dbicutomer.models.AddNewItem;
import dbicustomer.com.agicon.dbi.dbicutomer.models.InvoiceItemsRates;
import dbicustomer.com.agicon.dbi.dbicutomer.sharedpreference.SharedPrefernceValue;

public class GetAllShoppingListActivity extends AppCompatActivity {

    private Toolbar shopping_list_toolbar;

    private TextView img_next;
    private Intent intent;

    private TextView fa_rupee, item_total_sum, tv_no_item_found, tv_fa_trash;
    private Typeface fontAwesomeFont;

    private RecyclerView rv_inbound_list;
    private ArrayList<AddNewItem> addNewItemList;
    private GetAllShoppingListAdapter getAllShoppingListAdapter;

    private HashMap<String, String> map = new HashMap<>();

    public SQLController sqlController;

    private String user_id;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_list);

        init();
        setAwesomeFont();

        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (addNewItemList.size() == 0) {

                    Toast.makeText(GetAllShoppingListActivity.this, "To proceed, please add some items to your cart!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {

                    intent = new Intent(getApplicationContext(), PushInvoiceActivity.class);
                    intent.putExtra("addNewItemList", addNewItemList);
                    startActivity(intent);
                }
            }
        });

        tv_fa_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(GetAllShoppingListActivity.this);
                builder.setTitle("Alert!")
                        .setCancelable(false)
                        .setMessage("Are you want to delete all items?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sqlController.removeAll();
                                init();
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.show();
            }
        });

    }

    private void init() {

        sharedPreferences = getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        user_id = sharedPreferences.getString(SharedPrefernceValue.USER_ID, "");

        shopping_list_toolbar = (Toolbar) findViewById(R.id.shopping_list_toolbar);
        tv_no_item_found = (TextView) findViewById(R.id.tv_no_item_found);

        setSupportActionBar(shopping_list_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv_inbound_list = (RecyclerView) findViewById(R.id.rv_inbound_list);
        img_next = (TextView) findViewById(R.id.img_next);
        fa_rupee = (TextView) findViewById(R.id.fa_rupee);
        item_total_sum = (TextView) findViewById(R.id.item_total_sum);
        tv_fa_trash = (TextView) findViewById(R.id.tv_fa_trash);

        rv_inbound_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_inbound_list.setNestedScrollingEnabled(false);

        sqlController = new SQLController(this);

        getProductResponse();

    }

    private void setAwesomeFont() {

        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        fa_rupee.setTypeface(fontAwesomeFont);
        tv_fa_trash.setTypeface(fontAwesomeFont);
        img_next.setTypeface(fontAwesomeFont);

    }

    public void getProductResponse() {

        try {
            sqlController.open();
            addNewItemList = sqlController.getAllItems();

            if (addNewItemList.size() == 0) {
                tv_no_item_found.setText("No Item Found");

                tv_no_item_found.setVisibility(View.VISIBLE);
                rv_inbound_list.setVisibility(View.GONE);

                item_total_sum.setText("0");

            } else {
                getAllShoppingListAdapter = new GetAllShoppingListAdapter(addNewItemList, new GetAllShoppingListAdapter.onClickListener() {
                    @Override
                    public void onClickButton(int position, int view, final AddNewItem addNewItem) {

                        int quantity = Integer.parseInt(addNewItem.getNew_item_quantity());
                        int newQuantity = 0;
                        String new_total_price;

                        AddNewItem addNewItem1 = new AddNewItem();
                        addNewItem1.setNew_item_name(addNewItem.getNew_item_name());
                        addNewItem1.setNew_item_price(addNewItem.getNew_item_price());
                        addNewItem1.setNew_item_key_id(addNewItem.getNew_item_key_id());
                        addNewItem1.setNew_item_total_price_included_tax(addNewItem.getNew_item_total_price_included_tax());

                        if (view == R.id.iv_minus) {
                            if (Integer.parseInt(addNewItem.getNew_item_quantity()) > 0) {
                                newQuantity = quantity - 1;

                                if (newQuantity > 0) {
                                    addNewItem1.setNew_item_quantity(String.valueOf(newQuantity));
                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(GetAllShoppingListActivity.this);
                                    builder.setMessage("Are you sure you want to remove all "
                                            + addNewItem.getNew_item_name().toString() + " item?")
                                            .setTitle("Alert!")
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    sqlController.deleteNewItem(addNewItem.getNew_item_key_id());
                                                    getProductResponse();
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

                            }
                        } else if (view == R.id.iv_add) {
                            newQuantity = quantity + 1;
                            addNewItem1.setNew_item_quantity(String.valueOf(newQuantity));

                        }

                        if (newQuantity != 0) {
                            new_total_price = String.valueOf(Double.parseDouble(addNewItem.getNew_item_total_price_included_tax()) * newQuantity);
                            addNewItem1.setNew_item_total_price(new_total_price);

                            addNewItemList.set(position, addNewItem1);

                            getAllShoppingListAdapter.notifyDataSetChanged();

                            calculateTotalPrice();

                            sqlController.updateNewItem(addNewItem.getNew_item_price(), String.valueOf(newQuantity), new_total_price,
                                    addNewItem.getNew_item_key_id());

                        } else {
                            getAllShoppingListAdapter.notifyDataSetChanged();

                            calculateTotalPrice();
                        }
                    }
                });

                rv_inbound_list.setAdapter(getAllShoppingListAdapter);

                calculateTotalPrice();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void calculateTotalPrice() {
        double temp = 0.0;
        double finalValue;
        DecimalFormat format_2Places = new DecimalFormat("0.00");

        for (int i = 0; i < addNewItemList.size(); i++) {

            double total_quantity = Double.parseDouble(addNewItemList.get(i).getNew_item_quantity());
            double total_price_included_tax = Double.parseDouble(addNewItemList.get(i).getNew_item_total_price_included_tax());

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

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        action_settings = menu.findItem(R.id.action_settings);
//        action_search = menu.findItem(R.id.action_search);
//        action_invoice_list = menu.findItem(R.id.action_invoice_list);
//
//        action_settings.setVisible(false);
//        action_search.setVisible(false);
//        action_invoice_list.setVisible(false);
//
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:

                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
