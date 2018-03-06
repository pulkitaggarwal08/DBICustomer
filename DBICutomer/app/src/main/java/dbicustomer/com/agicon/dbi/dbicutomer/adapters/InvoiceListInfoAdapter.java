package dbicustomer.com.agicon.dbi.dbicutomer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.models.UserProduct;

public class InvoiceListInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserProduct> userProductList;
    private onClickListener onClickListener;

    public interface onClickListener {
        void onClickButton(int position, int view, UserProduct userProduct);
    }

    public InvoiceListInfoAdapter(List<UserProduct> userProductList, onClickListener onClickListener) {
        this.userProductList = userProductList;
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InboundListInfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_invoice_list_info, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        InboundListInfoViewHolder inboundListInfoViewHolder = (InboundListInfoViewHolder) holder;

        inboundListInfoViewHolder.tv_inbound_name.setText(userProductList.get(position).getProduct_name());
        inboundListInfoViewHolder.tv_quantity.setText(userProductList.get(position).getProduct_qty());
        inboundListInfoViewHolder.tv_unit_price.setText(userProductList.get(position).getUnit_price());
        inboundListInfoViewHolder.tv_total_price.setText(userProductList.get(position).getTotal_item_price());

    }

    @Override
    public int getItemCount() {
        return userProductList.size();
    }

    private class InboundListInfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_inbound_name, tv_quantity, tv_unit_price, tv_total_price;

        private InboundListInfoViewHolder(View itemView) {
            super(itemView);

            tv_inbound_name = (TextView) itemView.findViewById(R.id.tv_inbound_name);
            tv_quantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            tv_unit_price = (TextView) itemView.findViewById(R.id.tv_unit_price);
            tv_total_price = (TextView) itemView.findViewById(R.id.tv_total_price);

        }

        @Override
        public void onClick(View view) {
            onClickListener.onClickButton(getLayoutPosition(), view.getId(), userProductList.get(getLayoutPosition()));
        }
    }

}
