package com.fridayapp.wallpaperapp.filter;

import android.widget.Filter;


import com.fridayapp.wallpaperapp.adapter.WallpaperAdapter;
import com.fridayapp.wallpaperapp.model.WallpaperModel;

import java.util.ArrayList;
import java.util.List;


public class CustomFilter extends Filter {

    WallpaperAdapter adapter;
    List<WallpaperModel> filterList;

    public CustomFilter(List<WallpaperModel> filterList, WallpaperAdapter adapter) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();

            ArrayList<WallpaperModel> filteredTask = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++) {
                if (filterList.get(i).getPage_no().toUpperCase().contains(constraint)) {
                    filteredTask.add(filterList.get(i));
                }
            }
            results.count = filteredTask.size();
            results.values = filteredTask;
        } else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.typeList = (List<WallpaperModel>) results.values;
        adapter.notifyDataSetChanged();
    }
}