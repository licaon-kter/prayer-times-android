package com.metinkale.prayerapp.custom;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.widget.Toast;
import com.metinkale.prayer.R;
import com.metinkale.prayerapp.settings.Prefs;
import com.metinkale.prayerapp.settings.Settings;

public class Changelog {
    private static final int CHANGELOG_VERSION = 12;
    private static Context mContext;

    public static void start(Context c) {
        mContext = c;
        if (Prefs.getChangelogVersion() < CHANGELOG_VERSION) {
            getDialog().show();
            Prefs.setChangelogVersion(CHANGELOG_VERSION);
        }
    }

    private static AlertDialog getDialog() {
        WebView wv = new WebView(mContext);
        wv.loadUrl("file:///android_asset/" + Prefs.getLanguage() + "/changelog.htm");

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.changelog)).setView(wv).setCancelable(false);

        builder.setNegativeButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNeutralButton(mContext.getResources().getString(R.string.sendemail), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Settings.sendMail(mContext);
            }
        });

        builder.setPositiveButton(mContext.getResources().getString(R.string.vote), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    mContext.startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(mContext, "Couldn't launch the market", Toast.LENGTH_LONG).show();
                }
            }
        });

        return builder.create();
    }
}