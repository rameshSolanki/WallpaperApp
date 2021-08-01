package com.fridayapp.wallpaperapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.fridayapp.wallpaperapp.api.ApiInterface;
import com.fridayapp.wallpaperapp.api.RetrofitInstance;
import com.fridayapp.wallpaperapp.model.Category;
import com.fridayapp.wallpaperapp.filter.CustomFilterCategory;
import com.fridayapp.wallpaperapp.R;
import com.fridayapp.wallpaperapp.model.CategoryCount;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements Filterable {
    private Call<List<CategoryCount>> call;
    ApiInterface apiInterface;
    public List<Category> typeList;
    CustomFilterCategory filter;
    Context context;
    CategoryAdapter.RecyclerViewClickListener mListener;

    public CategoryAdapter(List<Category> typeList, Context context, CategoryAdapter.RecyclerViewClickListener listener1) {
        this.typeList = typeList;
        this.context = context;
        this.mListener = listener1;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category, parent, false);

        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CategoryAdapter.ViewHolder holder, final int position) {

        final Category dataModel = typeList.get(position);
        holder.aks_description.setText(dataModel.getCategory_name());
        //holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_enter));
        Picasso.get()
                .load(RetrofitInstance.BASE_URL + "/category/" + dataModel.getCategory_image())
                .error(R.drawable.ic_baseline_hourglass_empty_24)
                .placeholder(R.drawable.ic_baseline_hourglass_empty_24)
                .into(holder.imageViewIcon);
        apiInterface = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        call = apiInterface.searchCategoryCount(dataModel.getCategory_name());

        call.enqueue(new Callback<List<CategoryCount>>() {
            @Override
            public void onResponse(Call<List<CategoryCount>> call, Response<List<CategoryCount>>
                    response) {
                List<CategoryCount> categoryCounts = response.body();
                int count = 0;

                for (CategoryCount categoryCount : categoryCounts) {
                    count++;
                }
                holder.aks_count.setText("" + count + " wallpapers");
            }

            @Override
            public void onFailure(Call<List<CategoryCount>> call, Throwable throwable) {
                Toast.makeText(context, "Failed to retrieve data.", Toast.LENGTH_LONG).show();
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("type_of_category", dataModel.getCategory_name());
                //bundle.putString("type_of_category", count);
                Navigation.findNavController(view).navigate(R.id.wallpaper, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (typeList == null) ? 0 : typeList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilterCategory(typeList, this);
        }

        return filter;


    }

    public void setFilter(List<Category> allquestionList) {
        typeList = new ArrayList<>();
        typeList.addAll(allquestionList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView aks_description, aks_count;
        ImageView imageViewIcon;
        MaterialCardView cardView;

        ViewHolder(View view) {
            super(view);
            aks_count = view.findViewById(R.id.aks_count);
            aks_description = view.findViewById(R.id.aks_description);
            imageViewIcon = view.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.cardview_id);
            context = itemView.getContext();
        }
    }

    public interface RecyclerViewClickListener {
        void onRowClick(View view, int position);

    }
}