package dbicustomer.com.agicon.dbi.dbicutomer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.interfaces.onClickButtonListener;
import dbicustomer.com.agicon.dbi.dbicutomer.models.HomeStores;


public class HomeCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HomeStores> homeStoresList;
    private onClickButtonListener onClickBUttonListener;

    public HomeCategoryAdapter(List<HomeStores> homeStoresList, onClickButtonListener onClickBUttonListener) {
        this.homeStoresList = homeStoresList;
        this.onClickBUttonListener = onClickBUttonListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        HomeViewHolder homeViewHolder = (HomeViewHolder) holder;

        homeViewHolder.text_store_name.setText(homeStoresList.get(position).getStore_name());
        homeViewHolder.text_store_address.setText(homeStoresList.get(position).getStore_address());
        homeViewHolder.text_phone_number.setText(homeStoresList.get(position).getStore_mobile());
//        homeViewHolder.text_store_name.setText(invoiceList.get(position).getStore_name());

    }

    @Override
    public int getItemCount() {
        return homeStoresList.size();
    }

    private class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView text_store_name, text_store_address, text_phone_number;
        private ImageView iv_store_image;
        private LinearLayout ll_adapter_home_list;

        private HomeViewHolder(View itemView) {
            super(itemView);

            text_store_name = (TextView) itemView.findViewById(R.id.text_store_name);
            text_store_address = (TextView) itemView.findViewById(R.id.text_store_address);
            text_phone_number = (TextView) itemView.findViewById(R.id.text_phone_number);
            iv_store_image = (ImageView) itemView.findViewById(R.id.iv_store_image);

            ll_adapter_home_list = (LinearLayout) itemView.findViewById(R.id.ll_adapter_home_list);

            ll_adapter_home_list.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onClickBUttonListener.onClickButton(getLayoutPosition());
        }
    }

}
