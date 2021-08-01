package com.fridayapp.wallpaperapp.ui.setting;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fridayapp.wallpaperapp.R;
import com.fridayapp.wallpaperapp.utils.DarkModePrefManager;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.text.DecimalFormat;

public class SettingFragment extends Fragment{
    private MaterialCardView notification, clear_app_cache;
    private TextView notification_text, cache_clear;
    private CheckBox darkModeSwitch;
    RelativeLayout relativeLayout;
    private SettingViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(SettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        clear_app_cache = root.findViewById(R.id.clear_app_cache);
        notification = root.findViewById(R.id.notification);
        relativeLayout = root.findViewById(R.id.container);
        //container.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.zoom_enter));
        notification_text = root.findViewById(R.id.notification_text);
        cache_clear = root.findViewById(R.id.cache_clear);
        darkModeSwitch = root.findViewById(R.id.nbtn);
        setDarkModeSwitch();

        if(new DarkModePrefManager(getContext()).isNightMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            notification_text.setText("Light Theme");
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            notification_text.setText("Black Theme");
        }

        initializeCache();

        clear_app_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCache(getContext());
                Toast.makeText(getContext(), "Cache cleared", Toast.LENGTH_SHORT).show();
                //requireActivity().recreate();
            }
        });
        return root;
    }
    private void setDarkModeSwitch(){
        darkModeSwitch.setChecked(new DarkModePrefManager(getContext()).isNightMode());
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DarkModePrefManager darkModePrefManager = new DarkModePrefManager(getContext());
                darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                requireActivity().recreate();
            }
        });
    }
    private void initializeCache() {
        long size = 0;
        size += getDirSize(getContext().getCacheDir());
        size += getDirSize(getContext().getExternalCacheDir());
        cache_clear.setText(readableFileSize(size));
    }

    public long getDirSize(File dir){
        long size = 0;
        for (File file : dir.listFiles()) {
            if (file != null && file.isDirectory()) {
                size += getDirSize(file);
            } else if (file != null && file.isFile()) {
                size += file.length();
            }
        }
        return size;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0 Bytes";
        final String[] units = new String[]{"Bytes", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

    }
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

}
