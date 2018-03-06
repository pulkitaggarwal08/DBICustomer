package dbicustomer.com.agicon.dbi.dbicutomer.adapters;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.models.InvoiceItemsRates;
import dbicustomer.com.agicon.dbi.dbicutomer.models.SubCategoryItemList;

/**
 * Created by agicon06 on 17/8/17.
 */

public class SubCategoryListItemRateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SubCategoryItemList> subCategoryItemLists;
    private onClickListener onClickListener;

    public interface onClickListener {
        void onClickButton(int position, int view, SubCategoryItemList invoiceItemsRates);
    }

    public SubCategoryListItemRateAdapter(List<SubCategoryItemList> subCategoryItemLists, onClickListener onClickListener) {
        this.subCategoryItemLists = subCategoryItemLists;
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubCategoryListItemRateHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_item_rates, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SubCategoryListItemRateHolder subCategoryListItemRateHolder = (SubCategoryListItemRateHolder) holder;

        subCategoryListItemRateHolder.text_item_name.setText(subCategoryItemLists.get(position).getProduct_name());
        subCategoryListItemRateHolder.text_item_price.setText(subCategoryItemLists.get(position).getProduct_price());

    }

    @Override
    public int getItemCount() {
        return subCategoryItemLists.size();
    }

    private class SubCategoryListItemRateHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView text_item_name, text_item_price;
        ImageView iv_invoice_add_list_item;

        private SubCategoryListItemRateHolder(View itemView) {
            super(itemView);

            text_item_name = (TextView) itemView.findViewById(R.id.text_item_name);
            text_item_price = (TextView) itemView.findViewById(R.id.text_item_price);
            iv_invoice_add_list_item = (ImageView) itemView.findViewById(R.id.iv_invoice_add_list_item);

            iv_invoice_add_list_item.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onClickListener.onClickButton(getLayoutPosition(), view.getId(), subCategoryItemLists.get(getLayoutPosition()));

        }
    }

}
