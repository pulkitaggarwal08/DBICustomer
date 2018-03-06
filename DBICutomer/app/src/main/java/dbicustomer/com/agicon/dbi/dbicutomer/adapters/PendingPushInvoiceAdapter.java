package dbicustomer.com.agicon.dbi.dbicutomer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.models.UserProduct;

public class PendingPushInvoiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserProduct> userProductList;
    private onClickListener onClickListener;

    public interface onClickListener {
        void onClickButton(int position, int view, UserProduct userProduct);
    }

    public PendingPushInvoiceAdapter(List<UserProduct> userProductList, onClickListener onClickListener) {
        this.userProductList = userProductList;
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PendingPushHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_product_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        PendingPushHolder invoiceViewHolder = (PendingPushHolder) holder;

        invoiceViewHolder.tv_name.setText(userProductList.get(position).getProduct_name());
        invoiceViewHolder.text_quantity.setText(userProductList.get(position).getProduct_qty());
        invoiceViewHolder.tv_unit_price.setText(userProductList.get(position).getUnit_price());
        invoiceViewHolder.text_total_price.setText(userProductList.get(position).getTotal_item_price());

    }

    @Override
    public int getItemCount() {
        return userProductList.size();
    }

    private class PendingPushHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_name, text_quantity, tv_unit_price, text_total_price;

        private PendingPushHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            text_quantity = (TextView) itemView.findViewById(R.id.text_quantity);
            tv_unit_price = (TextView) itemView.findViewById(R.id.tv_unit_price);
            text_total_price = (TextView) itemView.findViewById(R.id.text_total_price);

        }

        @Override
        public void onClick(View view) {
            onClickListener.onClickButton(getLayoutPosition(), view.getId(), userProductList.get(getLayoutPosition()));
        }
    }

}
