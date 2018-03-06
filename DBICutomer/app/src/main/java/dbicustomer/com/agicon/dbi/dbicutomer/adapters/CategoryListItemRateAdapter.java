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

import java.util.List;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.models.CategoryItemsList;
import dbicustomer.com.agicon.dbi.dbicutomer.models.InvoiceItemsRates;

/**
 * Created by agicon06 on 17/8/17.
 */

public class CategoryListItemRateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CategoryItemsList> invoiceItemsRatesList;
    private onClickListener onClickListener;
    private onLongItemClickListener onLongItemClickListener;
    Context context;

    public interface onClickListener {
        void onClickButton(int position, int view, CategoryItemsList invoiceItemsRates);
    }

    public interface onLongItemClickListener {
        void onItemLongClick(int position, int view, CategoryItemsList categoryItemsList);
    }

    public CategoryListItemRateAdapter(Context context,
                                       List<CategoryItemsList> invoiceItemsRatesList,
                                       CategoryListItemRateAdapter.onClickListener onClickListener,
                                       onLongItemClickListener onLongItemClickListener) {
        this.context = context;
        this.invoiceItemsRatesList = invoiceItemsRatesList;
        this.onClickListener = onClickListener;
        this.onLongItemClickListener = onLongItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryListItemRateHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cat_list_item_rates, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CategoryListItemRateHolder listItemRateHolder = (CategoryListItemRateHolder) holder;

        listItemRateHolder.text_item_name.setText(invoiceItemsRatesList.get(position).getProduct_name());
        listItemRateHolder.text_item_price.setText(invoiceItemsRatesList.get(position).getProduct_price());

    }

    @Override
    public int getItemCount() {
        return invoiceItemsRatesList.size();
    }

    private class CategoryListItemRateHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView text_item_name, text_item_price;
        ImageView iv_invoice_add_list_item;
        LinearLayout ll_category_item;

        private CategoryListItemRateHolder(View itemView) {
            super(itemView);

            text_item_name = (TextView) itemView.findViewById(R.id.text_item_name);
            text_item_price = (TextView) itemView.findViewById(R.id.text_item_price);
            iv_invoice_add_list_item = (ImageView) itemView.findViewById(R.id.iv_invoice_add_list_item);
            ll_category_item = (LinearLayout) itemView.findViewById(R.id.ll_category_item);

            iv_invoice_add_list_item.setOnClickListener(this);
            ll_category_item.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onClickListener.onClickButton(getLayoutPosition(), view.getId(), invoiceItemsRatesList.get(getLayoutPosition()));

        }

        @Override
        public boolean onLongClick(View view) {

            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(70);

            onLongItemClickListener.onItemLongClick(getLayoutPosition(), view.getId(), invoiceItemsRatesList.get(getLayoutPosition()));

            return false;
        }
    }

}
