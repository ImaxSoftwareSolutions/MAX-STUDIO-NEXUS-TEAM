package com.besome.sketch;

import static com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowInsetsCompat;

import com.besome.sketch.help.ProgramInfoActivity;
import com.besome.sketch.tools.NewKeyStoreActivity;
import com.google.android.material.navigation.NavigationView;

import a.a.a.mB;
import dev.chrisbanes.insetter.Insetter;
import dev.chrisbanes.insetter.Side;
import mod.hilal.saif.activities.tools.AppSettings;
import pro.sketchware.R;
import pro.sketchware.activities.about.AboutActivity;
import pro.sketchware.activities.main.activities.MainActivity;
import pro.sketchware.utility.RemoteLinks;
import pro.sketchware.utility.UI;

public class MainDrawer extends NavigationView {
    private static final int DEF_STYLE_RES = R.style.Widget_SketchwarePro_NavigationView_Main;

    public MainDrawer(@NonNull Context context) {
        this(context, null);
    }

    public MainDrawer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.navigationViewStyle);
    }

    public MainDrawer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(wrap(context, attrs, defStyleAttr, DEF_STYLE_RES), attrs, defStyleAttr);
        context = getContext();

        var layoutDirection = context.getResources().getConfiguration().getLayoutDirection();
        Insetter.builder()
                .margin(WindowInsetsCompat.Type.displayCutout() | WindowInsetsCompat.Type.navigationBars(),
                        Side.create(layoutDirection == LAYOUT_DIRECTION_LTR,
                                false, layoutDirection == LAYOUT_DIRECTION_RTL, false))
                .applyToView(this);

        ViewGroup headerView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.main_drawer_header, null);
        headerView.findViewById(R.id.status_bar_overlapper).setMinimumHeight(UI.getStatusBarHeight(context));

        RemoteLinks.refresh(context);

        addHeaderView(headerView);
        inflateMenu(R.menu.main_drawer_menu);
        setNavigationItemSelectedListener(item -> {
            initializeSocialLinks(item.getItemId());
            initializeDrawerItems(item.getItemId());

            // Return false to prevent selection
            return false;
        });
    }

    private void initializeSocialLinks(@IdRes int id) {
        if (!mB.a()) {
            // Links are managed from the admin panel
            if (id == R.id.social_discord) {
                RemoteLinks.open(getContext(), RemoteLinks.DISCORD);
            } else if (id == R.id.social_telegram) {
                RemoteLinks.open(getContext(), RemoteLinks.TELEGRAM);
            } else if (id == R.id.social_github) {
                RemoteLinks.open(getContext(), RemoteLinks.GITHUB);
            }
        }
    }

    private void initializeDrawerItems(@IdRes int id) {
        Activity activity = unwrap(getContext());
        if (id == R.id.about_team) {
            Intent intent = new Intent(activity, AboutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
        } else if (id == R.id.program_info) {
            Intent intent = new Intent(activity, ProgramInfoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).programInfoLauncher.launch(intent);
            } else {
                activity.startActivity(intent);
            }
        } else if (id == R.id.app_settings) {
            Intent intent = new Intent(activity, AppSettings.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
        } else if (id == R.id.create_release_keystore) {
            Intent intent = new Intent(activity, NewKeyStoreActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
        }
    }

    private Activity unwrap(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        return (Activity) context;
    }
}
