package br.com.devslab.gametrends.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import br.com.devslab.gametrends.R;

/**
 * Implementation of App Widget functionality.
 */
public class PulseAppWidget extends AppWidgetProvider {

    public static String EXTRAS_PULSE_ARTICLE_URL = "EXTRA_PULSE_ARTICLE_URL";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.pulse_app_widget);

        Intent intent = new Intent(context, PulseAppWidgetRemoteViewsService.class);
        rv.setRemoteAdapter(R.id.widget_list_view, intent);

        configureClickBroadcast(context, rv, appWidgetId);

        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private static void configureClickBroadcast(Context ctx, RemoteViews rv, int appWidgetId){
        Intent intent = new Intent(ctx, PulseAppWidget.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        Uri uri = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME));
        intent.setData(uri);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setPendingIntentTemplate(R.id.widget_list_view, pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.hasExtra(EXTRAS_PULSE_ARTICLE_URL)) {
            String articleUrl = intent.getStringExtra(EXTRAS_PULSE_ARTICLE_URL);
            Intent intentBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl));
            context.getApplicationContext().startActivity(intentBrowser);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

