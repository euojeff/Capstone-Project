package br.com.devslab.gametrends;

import android.content.Intent;
import android.widget.RemoteViewsService;


public class PulseAppWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new PulseAppWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
