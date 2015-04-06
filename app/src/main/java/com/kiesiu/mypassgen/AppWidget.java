/*
 * Copyright 2015 Łukasz Kieś <kiesiu@kiesiu.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kiesiu.mypassgen;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.security.NoSuchAlgorithmException;


public class AppWidget extends AppWidgetProvider {
    final private static String REFRESH = "com.kiesiu.mypassgen.REFRESH";

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        try {
            view.setTextViewText(R.id.widgetPassword, MyPassGen.randomPassword().substring(0, 12));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Intent refresh = new Intent(context, AppWidget.class);
        refresh.setAction(REFRESH).putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        view.setOnClickPendingIntent(R.id.widgetBase, PendingIntent.getBroadcast(context, 0,
                refresh, PendingIntent.FLAG_UPDATE_CURRENT));
        appWidgetManager.updateAppWidget(appWidgetId, view);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(REFRESH)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            updateAppWidget(context, appWidgetManager,
                    intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                            AppWidgetManager.INVALID_APPWIDGET_ID));
        }
    }
}
