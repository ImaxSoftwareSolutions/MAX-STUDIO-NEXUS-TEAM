package pro.sketchware.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import org.json.JSONObject;

import mod.hey.studios.util.Helper;
import pro.sketchware.R;

/**
 * Links (Discord, Telegram, GitHub, ...) that are managed from the admin panel.
 * They come from the same JSON as the "About the team" page and are cached locally,
 * so they work from the drawer even if the About page was never opened.
 */
public final class RemoteLinks {
    public static final String DISCORD = "discordInviteLink";
    public static final String TELEGRAM = "telegramLink";
    public static final String GITHUB = "githubLink";

    private static final String PREFS = "AppData";
    private static final String KEY_ABOUT_DATA = "aboutData";

    private RemoteLinks() {
    }

    /** Fetches the latest admin panel data in the background and caches it. */
    public static void refresh(Context context) {
        Context app = context.getApplicationContext();
        new Network().get(Helper.getResString(R.string.link_about_team), response -> {
            if (response == null) return;
            try {
                new JSONObject(response); // only cache valid JSON
                app.getSharedPreferences(PREFS, Activity.MODE_PRIVATE)
                        .edit().putString(KEY_ABOUT_DATA, response).apply();
            } catch (Exception ignored) {
            }
        });
    }

    /** Returns the link saved from the admin panel, or null if none is set. */
    public static String get(Context context, String key) {
        try {
            String json = context.getSharedPreferences(PREFS, Activity.MODE_PRIVATE)
                    .getString(KEY_ABOUT_DATA, null);
            if (json == null) return null;
            String value = new JSONObject(json).optString(key, "").trim();
            return value.isEmpty() ? null : value;
        } catch (Exception e) {
            return null;
        }
    }

    /** Opens the admin panel link for the given key; refreshes and informs the user if it is not available. */
    public static void open(Context context, String key) {
        String url = get(context, key);
        if (url == null) {
            refresh(context);
            Toast.makeText(context, "Link is not available yet, please check your internet and try again", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (!(context instanceof Activity)) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Unable to open link", Toast.LENGTH_SHORT).show();
        }
    }
}
