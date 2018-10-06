package info.kab.quote.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import info.kab.quote.LocalDB.QuoteManager;
import info.kab.quote.ParseLoader.ParseManager;

public class MyWidgetProvider extends AppWidgetProvider {

	private static final String LOG = "my**";
    private static ParseManager db;


    @Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {



		Log.i(LOG, "onUpdate method called");
		// Get all ids
		ComponentName thisWidget = new ComponentName(context, MyWidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		// Build the intent to call the service
		Intent intent = new Intent(context.getApplicationContext(),	UpdateWidgetService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(intent);
//        } else {
//            context.startService(intent);
//        }
//		// Update the widgets via the service
//		context.startService(intent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }

      //  UpdateWidgetService.enqueueWork(context,intent);
	}

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Log.d(LOG, "Network connectivity change");



        if (intent.getExtras() != null) {
            NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                Log.i(LOG, "Network " + ni.getTypeName() + " connected");


               // parseDownload(context);

            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d(LOG, "There's no network connectivity");
            }
        }
    }


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        db = ParseManager.getInstance(context);
        db.updateFromParse();

        QuoteManager.creatInstance(context);
       // parseDownload(context);


        Log.i(LOG, "onEnabled ok");
    }

    public void parseDownload(Context context){
        db = ParseManager.getInstance(context);
        db.readParse();
    }
}