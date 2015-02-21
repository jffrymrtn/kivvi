package com.manji.cooper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import com.manji.cooper.R;
import com.manji.cooper.custom.CSVData;
import com.manji.cooper.custom.ItemInfo;
import com.manji.cooper.managers.DataManager;

public class ProductAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private HashMap<Integer, CSVData> data;
    private ArrayList<String> filteredData;

    public ProductAdapter(Context context) {
        this.context = context;
        this.data = DataManager.getInstance().getData();
        this.filteredData = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        if (filteredData != null) {
            return filteredData.size();
        }

        return 0;
    }

    @Override
    public String getItem(int i) {
        return filteredData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.product_item, viewGroup, false);
        TextView productTitle = (TextView) row.findViewById(R.id.product_title);

        productTitle.setText(getItem(i).toString());
        return row;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults searchResults = new FilterResults();

                if (constraint == null || constraint.length() == 0) {
                    searchResults.values = data;
                    searchResults.count = data.size();
                } else {
                    HashMap<String, ItemInfo> filteredResults = DataManager.getInstance().getFilteredData(constraint.toString());

                    for (String f: filteredResults.keySet()){
                        int csvKey = filteredResults.get(f).csvKey;
                        ArrayList<String> values = filteredResults.get(f).values;

                        CSVData dataSet = DataManager.getInstance().getData().get(csvKey);
                    }
//                    for (String item : itemNames) {
//                       if (item.toLowerCase().contains(constraint.toString().toLowerCase())) {
//                               String found = item + ": " + data..getEntry(k);
//                               searchResultsData.add(found);
//
//                       }
//
//                    }
//
//                    searchResults.values = searchResultsData;
//                    searchResults.count = searchResultsData.size();
                }
                return searchResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //filteredData = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}