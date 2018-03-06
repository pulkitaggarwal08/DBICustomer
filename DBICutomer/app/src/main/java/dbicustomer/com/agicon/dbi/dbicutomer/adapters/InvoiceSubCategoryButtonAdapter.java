package dbicustomer.com.agicon.dbi.dbicutomer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.interfaces.onClickButtonListener;
import dbicustomer.com.agicon.dbi.dbicutomer.models.SubCategoryList;


public class InvoiceSubCategoryButtonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SubCategoryList> list;
    private onClickButtonListener onClickButtonListener;

    public InvoiceSubCategoryButtonAdapter(List<SubCategoryList> list, onClickButtonListener onClickButtonListener) {
        this.list = list;
        this.onClickButtonListener = onClickButtonListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubCategoryInvoiceButtonHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_subcat_text_button, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SubCategoryInvoiceButtonHolder buttonHolder = (SubCategoryInvoiceButtonHolder) holder;

        buttonHolder.tvName.setText(list.get(position).getSubcategory_name());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class SubCategoryInvoiceButtonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName;
        RelativeLayout ll_button;

        private SubCategoryInvoiceButtonHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            ll_button = (RelativeLayout) itemView.findViewById(R.id.ll_button);

            ll_button.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onClickButtonListener.onClickButton(getLayoutPosition());
        }
    }

}
