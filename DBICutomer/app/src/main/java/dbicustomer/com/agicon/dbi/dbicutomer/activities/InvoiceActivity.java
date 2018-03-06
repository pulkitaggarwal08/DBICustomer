package dbicustomer.com.agicon.dbi.dbicutomer.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.homefragments.HomeFragment;
import dbicustomer.com.agicon.dbi.dbicutomer.homefragments.HomeInvoiceFragment;
import dbicustomer.com.agicon.dbi.dbicutomer.invoicefragments.InvoiceFragment;
import dbicustomer.com.agicon.dbi.dbicutomer.sharedpreference.SharedPrefernceValue;
import dbicustomer.com.agicon.dbi.dbicutomer.trendingfragments.TrendingFragment;

public class InvoiceActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_home, ll_invoice, ll_trending;
    private ImageView iv_home, iv_invoice, iv_trending;
    private Toolbar dashboard_toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private Intent intent;
    private TextView text_home, text_invoice, text_trending,
            projects_font_awsome_icon, manage_account_font_awsome_icon,
            change_password_font_awsome_icon, logout_font_awsome_icon, tv_dashboard_next, tv_dashboard_right;
    private Typeface fontAwesomeFont;

    private RelativeLayout rel_projects, rel_manage_account, rel_change_password, rel_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        init();
        setFontAwesomeFont();

    }

    private void init() {

        ll_home = (LinearLayout) findViewById(R.id.ll_home);
        ll_invoice = (LinearLayout) findViewById(R.id.ll_invoice);
        ll_trending = (LinearLayout) findViewById(R.id.ll_trending);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_invoice = (ImageView) findViewById(R.id.iv_invoice);
        iv_trending = (ImageView) findViewById(R.id.iv_trending);
        text_home = (TextView) findViewById(R.id.text_home);
        text_invoice = (TextView) findViewById(R.id.text_invoice);
        text_trending = (TextView) findViewById(R.id.text_trending);

        rel_projects = (RelativeLayout) findViewById(R.id.rel_profile);
        rel_manage_account = (RelativeLayout) findViewById(R.id.rel_home);
        rel_change_password = (RelativeLayout) findViewById(R.id.rel_change_password);
        rel_logout = (RelativeLayout) findViewById(R.id.rel_logout);

        dashboard_toolbar = (Toolbar) findViewById(R.id.dashboard_toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        tv_dashboard_right = (TextView) findViewById(R.id.tv_dashboard_right);
        tv_dashboard_next = (TextView) findViewById(R.id.tv_dashboard_next);

        setSupportActionBar(dashboard_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ll_home.setBackgroundColor(Color.parseColor("#F48931"));
        ll_invoice.setBackgroundColor(Color.parseColor("#ffffff"));
        ll_trending.setBackgroundColor(Color.parseColor("#ffffff"));

        iv_home.setImageResource(R.drawable.home_white);
        iv_invoice.setImageResource(R.drawable.invoice);
        iv_trending.setImageResource(R.drawable.trending);

        text_home.setTextColor(Color.parseColor("#ffffff"));
        text_invoice.setTextColor(Color.parseColor("#000000"));
        text_trending.setTextColor(Color.parseColor("#000000"));
        getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_container, new HomeInvoiceFragment()).commit();
        drawerLayout.closeDrawers();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, dashboard_toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, dashboard_toolbar, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };

        ll_home.setOnClickListener(this);
        ll_invoice.setOnClickListener(this);
        ll_trending.setOnClickListener(this);

        rel_projects.setOnClickListener(this);
        rel_manage_account.setOnClickListener(this);
        rel_change_password.setOnClickListener(this);
        rel_logout.setOnClickListener(this);

        tv_dashboard_next.setOnClickListener(this);
        tv_dashboard_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.ll_home || view.getId() == R.id.rel_home) {

            ll_home.setBackgroundColor(Color.parseColor("#F48931"));
            ll_invoice.setBackgroundColor(Color.parseColor("#ffffff"));
            ll_trending.setBackgroundColor(Color.parseColor("#ffffff"));

            iv_home.setImageResource(R.drawable.home_white);
            iv_invoice.setImageResource(R.drawable.invoice);
            iv_trending.setImageResource(R.drawable.trending);

            text_home.setTextColor(Color.parseColor("#ffffff"));
            text_invoice.setTextColor(Color.parseColor("#000000"));
            text_trending.setTextColor(Color.parseColor("#000000"));

            getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_container, new HomeFragment()).commit();
            drawerLayout.closeDrawers();

        } else if (view.getId() == R.id.ll_invoice) {

            ll_home.setBackgroundColor(Color.parseColor("#ffffff"));
            ll_invoice.setBackgroundColor(Color.parseColor("#F48931"));
            ll_trending.setBackgroundColor(Color.parseColor("#ffffff"));

            iv_home.setImageResource(R.drawable.home);
            iv_invoice.setImageResource(R.drawable.invoice_white);
            iv_trending.setImageResource(R.drawable.trending);

            text_home.setTextColor(Color.parseColor("#000000"));
            text_invoice.setTextColor(Color.parseColor("#ffffff"));
            text_trending.setTextColor(Color.parseColor("#000000"));
            getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_container, new InvoiceFragment()).commit();
//            drawerLayout.closeDrawers();

        } else if (view.getId() == R.id.ll_trending) {

            ll_home.setBackgroundColor(Color.parseColor("#ffffff"));
            ll_invoice.setBackgroundColor(Color.parseColor("#ffffff"));
            ll_trending.setBackgroundColor(Color.parseColor("#F48931"));

            iv_home.setImageResource(R.drawable.home);
            iv_invoice.setImageResource(R.drawable.invoice);
            iv_trending.setImageResource(R.drawable.trending_white);

            text_home.setTextColor(Color.parseColor("#000000"));
            text_invoice.setTextColor(Color.parseColor("#000000"));
            text_trending.setTextColor(Color.parseColor("#ffffff"));
            getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_container, new TrendingFragment()).commit();
//            drawerLayout.closeDrawers();

        } else if (view.getId() == R.id.rel_profile) {
            intent = new Intent(InvoiceActivity.this, ProfileActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawers();

        } else if (view.getId() == R.id.rel_change_password) {
            intent = new Intent(InvoiceActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawers();
        } else if (view.getId() == R.id.rel_logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to logout?")
                    .setTitle("DBI Customer")
//                    .setIcon(R.drawable.dbi_logo)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SharedPrefernceValue.MyPREFERENCES, Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.remove(SharedPrefernceValue.IS_LOGGED_IN);
//                            editor.commit();

                            Intent intent = new Intent(InvoiceActivity.this, SignInActivity.class);
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


        } else if (view.getId() == R.id.tv_dashboard_next) {
            intent = new Intent(InvoiceActivity.this, GetAllShoppingListActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.tv_dashboard_right) {
            intent = new Intent(InvoiceActivity.this, GetAllShoppingListActivity.class);
            startActivity(intent);
        }
    }

    public void setFontAwesomeFont() {

        projects_font_awsome_icon = (TextView) findViewById(R.id.profile_font_awsome_icon);
        manage_account_font_awsome_icon = (TextView) findViewById(R.id.home_font_awsome_icon);
        change_password_font_awsome_icon = (TextView) findViewById(R.id.change_password_font_awsome_icon);
        logout_font_awsome_icon = (TextView) findViewById(R.id.logout_font_awsome_icon);

        fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        projects_font_awsome_icon.setTypeface(fontAwesomeFont);
        manage_account_font_awsome_icon.setTypeface(fontAwesomeFont);
        change_password_font_awsome_icon.setTypeface(fontAwesomeFont);
        logout_font_awsome_icon.setTypeface(fontAwesomeFont);
        tv_dashboard_right.setTypeface(fontAwesomeFont);

    }

    MenuItem action_settings, action_search, action_invoice_list;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        action_settings = menu.findItem(R.id.action_settings);
        action_search = menu.findItem(R.id.action_search);
        action_invoice_list = menu.findItem(R.id.action_invoice_list);

        action_settings.setVisible(false);
        action_search.setVisible(false);
        action_invoice_list.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:

                return true;

//            case R.id.action_invoice_list:
//
//                intent = new Intent(getApplicationContext(), InvoiceListActivity.class);
//                startActivity(intent);
//                return true;
//
            case R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
