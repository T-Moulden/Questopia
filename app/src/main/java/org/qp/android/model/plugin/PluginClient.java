package org.qp.android.model.plugin;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;

import com.qsp.player.QuestPlugin;

import java.util.List;

public class PluginClient {
    private QuestPlugin servicePlugin;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name , IBinder service) {
            servicePlugin = QuestPlugin.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            servicePlugin = null;
        }
    };

    public void connectPlugin (Context context, String pluginName) {
        Intent intent = new Intent("org.qp.plugin"+pluginName);
        Intent updatedIntent = createExplicitIntent(context, intent);
        if (updatedIntent != null) {
            context.bindService(updatedIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private Intent createExplicitIntent(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(intent, 0);
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        Intent explicitIntent = new Intent(intent);
        explicitIntent.setComponent(component);
        return explicitIntent;
    }
}
