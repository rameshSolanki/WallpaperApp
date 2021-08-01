package com.fridayapp.wallpaperapp.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.fridayapp.wallpaperapp.filter.CustomFilter;
import com.fridayapp.wallpaperapp.R;
import com.fridayapp.wallpaperapp.activities.WallpaperDetailActivity;
import com.fridayapp.wallpaperapp.model.WallpaperModel;
import com.fridayapp.wallpaperapp.api.RetrofitInstance;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.ViewHolder> implements Filterable {

    public List<WallpaperModel> typeList;
    CustomFilter filter;
    Context context;
    RecyclerViewClickListener mListener;
    public WallpaperAdapter(List<WallpaperModel> typeList, Context context, RecyclerViewClickListener listener) {
        this.typeList = typeList;
        this.context = context;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaper_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final WallpaperModel dataModel = typeList.get(position);
        holder.aks_description.setText(dataModel.getPage_no());
        //holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_enter));
        Picasso.get()
                .load(RetrofitInstance.BASE_URL + "/uploads/" + dataModel.getImage_location())
                .error(R.drawable.ic_baseline_hourglass_empty_24)
                .placeholder(R.drawable.ic_baseline_hourglass_empty_24)
                .into(holder.imageViewIcon);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WallpaperDetailActivity.class);
                intent.putExtra("page_no", dataModel.getPage_no());
                intent.putExtra("image_location", dataModel.getImage_location());
                intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) typeList);
                intent.putExtra("pos", position);
//                String transitionName = context.getString(R.string.transition);
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context,
//                        view,   // Starting view
//                        transitionName    // The String
//                );
//
//                ActivityCompat.startActivity(context, intent, options.toBundle());
                context.startActivity(intent);
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
            filter = new CustomFilter(typeList, this);
        }

        return filter;


    }

    public void setFilter(List<WallpaperModel> allquestionList) {
        typeList = new ArrayList<>();
        typeList.addAll(allquestionList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView aks_description, windows_linux, mac;
        ImageView imageViewIcon;
        MaterialCardView cardView;

        ViewHolder(View view) {
            super(view);
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
