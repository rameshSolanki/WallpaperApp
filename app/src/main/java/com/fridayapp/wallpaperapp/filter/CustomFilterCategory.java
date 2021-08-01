package com.fridayapp.wallpaperapp.filter;

import android.widget.Filter;

import com.fridayapp.wallpaperapp.adapter.CategoryAdapter;
import com.fridayapp.wallpaperapp.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CustomFilterCategory extends Filter {

    CategoryAdapter adapter;
    List<Category> filterList;

    public CustomFilterCategory(List<Category> filterList, CategoryAdapter adapter) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();

            ArrayList<Category> filteredTask = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++) {
                if (filterList.get(i).getCategory_name().toUpperCase().contains(constraint)) {
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
        adapter.typeList = (List<Category>) results.values;
        adapter.notifyDataSetChanged();
    }
}
