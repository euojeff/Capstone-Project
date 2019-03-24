package br.com.devslab.gametrends.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import br.com.devslab.gametrends.widget.PulseAppWidgetRemoteViewsFactory;


public class PulseAppWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new PulseAppWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
