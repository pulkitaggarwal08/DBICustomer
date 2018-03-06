package dbicustomer.com.agicon.dbi.dbicutomer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.interfaces.onClickButtonListener;
import dbicustomer.com.agicon.dbi.dbicutomer.models.Trending;
import de.hdodenhof.circleimageview.CircleImageView;

import static dbicustomer.com.agicon.dbi.dbicutomer.R.id.iv_image;

public class TrendingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Trending> list;
    private onClickButtonListener onClickButtonListener;
    private Context context;

    public TrendingAdapter(Context context, List<Trending> list, onClickButtonListener onClickButtonListener) {
        this.list = list;
        this.onClickButtonListener = onClickButtonListener;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrendingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_trending, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TrendingHolder trendingHolder = (TrendingHolder) holder;

        trendingHolder.tvName.setText(list.get(position).getStore_name());
        trendingHolder.trending_discount.setText(list.get(position).getStore_discount() + " %");

        String trendingImage = list.get(position).getStore_picture();

        Picasso.with(context)
                .load(trendingImage)
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
                .into(trendingHolder.trending_list_image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class TrendingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName, trending_discount;
        //        LinearLayout ll_button;
        ImageView trending_list_image;

        private TrendingHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.trending_store_name);
            trending_discount = (TextView) itemView.findViewById(R.id.trending_discount);
            trending_list_image = (ImageView) itemView.findViewById(R.id.trending_list_image);

//            ll_button = (LinearLayout) itemView.findViewById(R.id.ll_button);
//
//            ll_button.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickButtonListener.onClickButton(getLayoutPosition());
        }
    }

}
