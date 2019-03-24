package br.com.devslab.gametrends.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import br.com.devslab.gametrends.R;

public class WidgetService extends IntentService{
    public static final String ACTION_UPDATE_WIDGET = "br.com.devslab.gametrends.widget.update_widget";

    public WidgetService() {
        super("WidgetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                handleActionUpdateWidgets();
            }
        }
    }

    private void handleActionUpdateWidgets() {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, PulseAppWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);

        PulseAppWidget.updateAppWidgets(this, appWidgetManager, appWidgetIds);
    }

    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }

}
