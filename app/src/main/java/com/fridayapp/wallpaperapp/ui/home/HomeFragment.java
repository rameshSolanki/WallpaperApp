package com.fridayapp.wallpaperapp.ui.home;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fridayapp.wallpaperapp.model.Category;
import com.fridayapp.wallpaperapp.model.WallpaperModel;
import com.fridayapp.wallpaperapp.utils.InternetConnection;
import com.fridayapp.wallpaperapp.R;
import com.fridayapp.wallpaperapp.adapter.WallpaperAdapter;
import com.fridayapp.wallpaperapp.api.ApiInterface;
import com.fridayapp.wallpaperapp.api.RetrofitInstance;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private WallpaperAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ApiInterface apiInterface;
    public ArrayList<WallpaperModel> dataLists;
    WallpaperAdapter.RecyclerViewClickListener listener;
    SearchView searchView;
    MaterialButton retryButton;
    LinearLayout no_internet;
    String type_of_aks = "Minimal";
    private Call<List<WallpaperModel>> call;
    MenuItem searchMenuItem;
    ArrayList<Category> categoryArrayList;
    private HomeViewModel homeViewModel;
    GridLayoutManager gridLayoutManager;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        no_internet = root.findViewById(R.id.no_internet);
        retryButton = root.findViewById(R.id.retryButton);
        recyclerView = root.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        dataLists = new ArrayList<>();
        categoryArrayList  = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        //recyclerView.setLayoutManager(linearLayoutManager);
        apiInterface = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!InternetConnection.isInternetAvailable(getContext())) {
                    Snackbar.make(recyclerView, "Internet not available", Snackbar.LENGTH_SHORT).show();
                }else {
                    no_internet.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    searchMenuItem.setVisible(true);
                    //floatingActionButton.setVisibility(View.VISIBLE);
                    loadData();
                }
            }
        });

        loadData();
        setHasOptionsMenu(true);
        onBackPressed();
        // Load more images onScroll end
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                // Check if end of page has been reached
//                if (!isLoading && (gridLayoutManager).findLastVisibleItemPosition() ==
//                        mAdapter.getItemCount() - 1) {
//                    //Log.d("", "End has reached, loading more images!");
//                    isLoading = true;
//                    page++;
//                    Snackbar.make(recyclerView, "End has reached, no more wallpapers!", Snackbar.LENGTH_SHORT).show();
//                    //getImages();
//
//                }
//
//            }
//        });
        return root;
    }

    private void populateListView(List<WallpaperModel> dataList) {
        mAdapter = new WallpaperAdapter (dataList, getContext(), listener);
        recyclerView.setAdapter(mAdapter);
    }
    public void loadData() {
        mSwipeRefreshLayout.setRefreshing(true);
        Call<ArrayList<WallpaperModel>> call = apiInterface.getData();

        call.enqueue(new Callback<ArrayList<WallpaperModel>>() {
            @Override
            public void onResponse(Call<ArrayList<WallpaperModel>> call, Response<ArrayList<WallpaperModel>> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                dataLists = response.body();
                mAdapter = new WallpaperAdapter(dataLists, getContext(), listener);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<WallpaperModel>> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (!InternetConnection.isInternetAvailable(getContext())) {
                    Snackbar.make(recyclerView, "Internet not available", Snackbar.LENGTH_SHORT).show();
                    no_internet.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    no_internet.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    Snackbar.make(recyclerView, "Failed to retrieve data.", Snackbar.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem search = menu.findItem(R.id.search);
        searchView = (SearchView) search.getActionView();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchMenuItem = menu.findItem(R.id.search);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName())
        );
        if (!InternetConnection.isInternetAvailable(getContext())) {
            searchMenuItem.setVisible(false);
        }
        searchView.setQueryHint("Search here...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                if(mAdapter!=null){
                mAdapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(mAdapter!=null){
                mAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);

        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.optionSignOut:
//                startLoginActivity();
//                return true;
//            case R.id.setting:
//                Navigation.findNavController(getView())
//                        .navigate(R.id.setting, null,
//                                new NavOptions.Builder().setPopUpTo(R.id.navigation_home, false).build());
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                    searchView.onActionViewCollapsed();
                    loadData();
                } else {
                    getActivity().finish();
                }
            }
        };
    }


    @Override
    public void onRefresh() {
        if (!InternetConnection.isInternetAvailable(getContext())) {
            Snackbar.make(recyclerView, "Internet not available", Snackbar.LENGTH_SHORT).show();
        }else {
            no_internet.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            searchMenuItem.setVisible(true);
            loadData();
        }
        mSwipeRefreshLayout.setRefreshing(false);

    }
}