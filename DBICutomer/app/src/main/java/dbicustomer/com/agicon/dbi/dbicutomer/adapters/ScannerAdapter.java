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
import dbicustomer.com.agicon.dbi.dbicutomer.models.Product;

public class ScannerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> productList;
    private onClickListener onClickListener;

    public interface onClickListener {
        void onClickButton(int position, int view, Product product);
    }

    public ScannerAdapter(List<Product> productList, onClickListener onClickListener) {
        this.productList = productList;
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.after_scan_list_tems, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolder holder = (ViewHolder) viewHolder;

        holder.tv_name.setText(productList.get(position).getProduct_name());
        holder.tv_quantity.setText(productList.get(position).getProduct_quantity());
        holder.tv_unit_price.setText(productList.get(position).getProduct_price());

        DecimalFormat format_2Places = new DecimalFormat("0.00");
        double finalValue;

        double quantity = Double.parseDouble(productList.get(position).getProduct_quantity());
        double price = Double.parseDouble(productList.get(position).getNew_item_total_price_included_tax());

        finalValue = Double.valueOf(format_2Places.format(quantity * price));

        holder.tv_total_price.setText(String.valueOf(finalValue));

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_name, tv_quantity, tv_unit_price, tv_total_price;
        ImageView iv_scan_minus, iv_scan_add;

        public ViewHolder(View itemView) {
            super(itemView);


            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_quantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            tv_unit_price = (TextView) itemView.findViewById(R.id.tv_unit_price);
            tv_total_price = (TextView) itemView.findViewById(R.id.tv_total_price);

            iv_scan_minus = (ImageView) itemView.findViewById(R.id.iv_scan_minus);
            iv_scan_add = (ImageView) itemView.findViewById(R.id.iv_scan_add);

            iv_scan_minus.setOnClickListener(this);
            iv_scan_add.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onClickButton(getLayoutPosition(), view.getId(), productList.get(getLayoutPosition()));
        }
    }


}
