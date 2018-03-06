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
import dbicustomer.com.agicon.dbi.dbicutomer.models.Home;


public class HomeButtonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Home> list;
    private onClickButtonListener onClickButtonListener;

    public HomeButtonAdapter(List<Home> list, onClickButtonListener onClickButtonListener) {
        this.list = list;
        this.onClickButtonListener = onClickButtonListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeButtonHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_category_text_button, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HomeButtonHolder buttonHolder = (HomeButtonHolder) holder;

        buttonHolder.tvName.setText(list.get(position).getStore_type_name());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class HomeButtonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName;
        RelativeLayout ll_button;

        private HomeButtonHolder(View itemView) {
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
