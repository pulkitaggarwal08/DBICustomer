package dbicustomer.com.agicon.dbi.dbicutomer.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dbicustomer.com.agicon.dbi.dbicutomer.R;
import dbicustomer.com.agicon.dbi.dbicutomer.models.State;

/**
 * Created by agicon06 on 28/9/17.
 */

public class StateSpinnerAdapter implements SpinnerAdapter {

    ArrayList<State> arrayList;
    Context context;

    public StateSpinnerAdapter(Context context, ArrayList<State> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return android.R.layout.simple_spinner_dropdown_item;
    }

    /**
     * Returns the View that is shown when a element was selected.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_spinner, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        State state = (State) getItem(position);

        viewHolder.stateName.setText(state.getName());

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    /**
     * The Views which are shown in when the arrow is clicked (In this case,
     * I used the same as for the "getView"-method.
     */
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return this.getView(position, convertView, parent);
    }

    private class ViewHolder {
        TextView stateName;

        public ViewHolder(View view) {
            stateName = (TextView) view.findViewById(R.id.text);
        }
    }
}

