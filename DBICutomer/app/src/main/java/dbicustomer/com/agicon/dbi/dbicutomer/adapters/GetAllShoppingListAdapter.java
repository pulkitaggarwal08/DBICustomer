package dbicustomer.com.agicon.dbi.dbicutomer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.models.AddNewItem;

public class GetAllShoppingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AddNewItem> productList;
    private onClickListener onClickListener;

    public interface onClickListener {
        void onClickButton(int position, int view, AddNewItem addNewItem);
    }

    public GetAllShoppingListAdapter(List<AddNewItem> productList, onClickListener onClickListener) {
        this.productList = productList;
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_product_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ProductViewHolder invoiceViewHolder = (ProductViewHolder) holder;

        invoiceViewHolder.tv_name.setText(productList.get(position).getNew_item_name());
        invoiceViewHolder.text_quantity.setText(productList.get(position).getNew_item_quantity());
        invoiceViewHolder.tv_unit_price.setText(productList.get(position).getNew_item_price());

        DecimalFormat format_2Places = new DecimalFormat("0.00");
        double finalValue;

        double quantity = Double.parseDouble(productList.get(position).getNew_item_quantity());
        double price = Double.parseDouble(productList.get(position).getNew_item_total_price_included_tax());

        finalValue = Double.valueOf(format_2Places.format(quantity * price));

        invoiceViewHolder.text_total_price.setText(String.valueOf(finalValue));

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_name, text_quantity, tv_unit_price, text_total_price;
        private ImageView iv_minus, iv_add;

        private ProductViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            text_quantity = (TextView) itemView.findViewById(R.id.text_quantity);
            tv_unit_price = (TextView) itemView.findViewById(R.id.tv_unit_price);
            text_total_price = (TextView) itemView.findViewById(R.id.text_total_price);
            iv_minus = (ImageView) itemView.findViewById(R.id.iv_minus);
            iv_add = (ImageView) itemView.findViewById(R.id.iv_add);


            iv_minus.setOnClickListener(this);
            iv_add.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onClickButton(getLayoutPosition(), view.getId(), productList.get(getLayoutPosition()));
        }
    }

}
