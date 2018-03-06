package dbicustomer.com.agicon.dbi.dbicutomer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.interfaces.onClickButtonListener;
import dbicustomer.com.agicon.dbi.dbicutomer.models.InvoiceItemsRates;
import dbicustomer.com.agicon.dbi.dbicutomer.models.SubCategoryItemList;

public class InvoiceSubCategoryItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<InvoiceItemsRates> list;
    private onClickButtonListener onClickButtonListener;

    public InvoiceSubCategoryItemAdapter(ArrayList<InvoiceItemsRates> list, onClickButtonListener onClickButtonListener) {
        this.list = list;
        this.onClickButtonListener = onClickButtonListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubCategoryItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_sub_category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SubCategoryItemHolder buttonHolder = (SubCategoryItemHolder) holder;

        buttonHolder.tv_name.setText(list.get(position).getProduct_name());
        buttonHolder.tv_price.setText("Rs " + list.get(position).getProduct_price());
        buttonHolder.tv_brand.setText(list.get(position).getProduct_brand());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class SubCategoryItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_name, tv_price, tv_brand;

        private SubCategoryItemHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_brand = (TextView) itemView.findViewById(R.id.tv_brand);

        }

        @Override
        public void onClick(View view) {
            onClickButtonListener.onClickButton(getLayoutPosition());

        }
    }

}
