package com.fridayapp.wallpaperapp.ui.category;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fridayapp.wallpaperapp.model.Category;
import com.fridayapp.wallpaperapp.adapter.CategoryAdapter;
import com.fridayapp.wallpaperapp.utils.InternetConnection;
import com.fridayapp.wallpaperapp.R;
import com.fridayapp.wallpaperapp.adapter.WallpaperAdapter;
import com.fridayapp.wallpaperapp.api.ApiInterface;
import com.fridayapp.wallpaperapp.api.RetrofitInstance;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private CategoryAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ApiInterface apiInterface;
    public ArrayList<Category> dataLists;
    WallpaperAdapter.RecyclerViewClickListener listener;
    LinearLayoutManager linearLayoutManager;
    SearchView searchView;
    MaterialButton retryButton;
    LinearLayout no_internet;
    MenuItem searchMenuItem;
    private CategoryViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(CategoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_category, container, false);

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        apiInterface = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!InternetConnection.isInternetAvailable(getContext())) {
                    Snackbar.make(recyclerView, "Internet not available", Snackbar.LENGTH_SHORT).show();
                } else {
                    no_internet.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    searchMenuItem.setVisible(true);
                    loadData();
                }
            }
        });
        loadData();
        setHasOptionsMenu(true);
        onBackPressed();
        return root;
    }

    public void loadData() {
        mSwipeRefreshLayout.setRefreshing(true);
        Call<ArrayList<Category>> call = apiInterface.getCategory();

        call.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                dataLists = response.body();
                mAdapter = new CategoryAdapter(dataLists, getContext(), (CategoryAdapter.RecyclerViewClickListener) listener);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
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
                if (mAdapter != null) {
                    mAdapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mAdapter != null) {
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
        } else {
            no_internet.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            searchMenuItem.setVisible(true);
            loadData();
        }
        mSwipeRefreshLayout.setRefreshing(false);

    }
}