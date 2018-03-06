package dbicustomer.com.agicon.dbi.dbicutomer.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.models.InvoiceList;

public class InvoiceListFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<InvoiceList> list;
    private Typeface fontAwesomeFont;
    private Context context;

    private onClickListener onClickListener;

    public interface onClickListener {
        void onClickButton(int position, int view, InvoiceList invoiceList);
    }

    public InvoiceListFragmentAdapter(Context context, List<InvoiceList> list, onClickListener onClickListener) {
        this.context = context;
        this.list = list;
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InvoiceHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_invoice_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        InvoiceHolder buttonHolder = (InvoiceHolder) holder;

        buttonHolder.invoice_number.setText("INV " + list.get(position).getInvoice_id());
        buttonHolder.tv_store_name.setText(list.get(position).getShop_name());
        buttonHolder.tv_date.setText(list.get(position).getOrder_date());
        buttonHolder.tv_amount.setText(list.get(position).getOrder_amount());

        String order_status = list.get(position).getOrder_status();

        if (order_status.equals("0")) {
            buttonHolder.tv_status.setText("Pending");
        }
        else if (order_status.equals("1")) {
            buttonHolder.tv_status.setText("Received");
        }

        fontAwesomeFont = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome-webfont.ttf");
        buttonHolder.fa_rupee.setTypeface(fontAwesomeFont);
        buttonHolder.invoice_fa_angle_right.setTypeface(fontAwesomeFont);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class InvoiceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView invoice_number, tv_store_name, tv_date, tv_amount, tv_status, fa_rupee, invoice_fa_angle_right;
        RelativeLayout main_relative_layout;

        private InvoiceHolder(View itemView) {
            super(itemView);

            invoice_number = (TextView) itemView.findViewById(R.id.invoice_number);
            tv_store_name = (TextView) itemView.findViewById(R.id.tv_store_name);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            fa_rupee = (TextView) itemView.findViewById(R.id.fa_rupee);
            invoice_fa_angle_right = (TextView) itemView.findViewById(R.id.invoice_fa_angle_right);

            main_relative_layout = (RelativeLayout) itemView.findViewById(R.id.main_relative_layout);

            main_relative_layout.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onClickListener.onClickButton(getLayoutPosition(), view.getId(), list.get(getLayoutPosition()));
        }
    }

}
