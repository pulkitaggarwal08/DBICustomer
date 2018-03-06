package dbicustomer.com.agicon.dbi.dbicutomer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.models.AddNewItem;


public class PushInvoiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AddNewItem> productList;
    private onClickListener onClickListener;

    public interface onClickListener {
        void onClickButton(int position, int view, AddNewItem addNewItem);
    }

    public PushInvoiceAdapter(List<AddNewItem> productList, onClickListener onClickListener) {
        this.productList = productList;
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PushReceiptHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_push_invoice, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        PushReceiptHolder pushReceiptHolder = (PushReceiptHolder) holder;

        pushReceiptHolder.tv_name.setText(productList.get(position).getNew_item_name());
        pushReceiptHolder.text_quantity.setText(productList.get(position).getNew_item_quantity());
        pushReceiptHolder.tv_unit_price.setText(productList.get(position).getNew_item_price());

        DecimalFormat format_2Places = new DecimalFormat("0.00");
        double finalValue;

        double quantity = Double.parseDouble(productList.get(position).getNew_item_quantity());
        double price = Double.parseDouble(productList.get(position).getNew_item_total_price_included_tax());

        finalValue = Double.valueOf(format_2Places.format(quantity * price));

        pushReceiptHolder.text_total_price.setText(String.valueOf(finalValue));

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private class PushReceiptHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_name, text_quantity, tv_unit_price, text_total_price;

        private PushReceiptHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            text_quantity = (TextView) itemView.findViewById(R.id.text_quantity);
            tv_unit_price = (TextView) itemView.findViewById(R.id.tv_unit_price);
            text_total_price = (TextView) itemView.findViewById(R.id.text_total_price);

        }

        @Override
        public void onClick(View view) {
            onClickListener.onClickButton(getLayoutPosition(), view.getId(), productList.get(getLayoutPosition()));
        }
    }

}
