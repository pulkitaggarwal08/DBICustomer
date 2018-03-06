package dbicustomer.com.agicon.dbi.dbicutomer.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.interfaces.onClickButtonListener;
import dbicustomer.com.agicon.dbi.dbicutomer.models.AddNewItem;
import dbicustomer.com.agicon.dbi.dbicutomer.models.CategoryList;

public class InvoiceCategoryButtonsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CategoryList> list;
    private onClickListener onClickListener;
    Context context;

    public interface onClickListener {
        void onClickButton(int position, int view);
    }

    public InvoiceCategoryButtonsAdapter(Context context, List<CategoryList> list, onClickListener onClickListener) {
        this.list = list;
        this.onClickListener = onClickListener;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryInvoiceButtonHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_category_text_button, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CategoryInvoiceButtonHolder buttonHolder = (CategoryInvoiceButtonHolder) holder;

        buttonHolder.tvName.setText(list.get(position).getCategory_name());

//                ll_button.setBackgroundColor(Color.parseColor("#F48931"));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class CategoryInvoiceButtonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName;
        RelativeLayout ll_button;

        private CategoryInvoiceButtonHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            ll_button = (RelativeLayout) itemView.findViewById(R.id.ll_button);

            ll_button.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onClickListener.onClickButton(getLayoutPosition(), view.getId());

        }
    }

}
