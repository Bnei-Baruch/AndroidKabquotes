package info.kab.quote.Widget;


import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;



import info.kab.android.widget.quote.R;
import info.kab.quote.LocalDB.QuoteManager;

public class UpdateWidgetService extends Service {
	public static final String LOG = "my**";
    public static final String LAYOUT = "layout";

    @Override
	public void onStart(Intent intent, int startId) {
		Log.i(LOG, "Called");
		// Create some random data
        QuoteManager quoteManager = QuoteManager.greatInstance(this);
        SharedPreferences sp = this.getSharedPreferences(ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());

		int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

		ComponentName thisWidget = new ComponentName(getApplicationContext(), MyWidgetProvider.class);
		int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);

		Log.w(LOG, "From Intent" + String.valueOf(allWidgetIds.length));
		Log.w(LOG, "Direct" + String.valueOf(allWidgetIds2.length));

        int number = counter(sp, quoteManager);
        int layout = sp.getInt(LAYOUT, R.layout.widget_bb_black);


        for (int widgetId : allWidgetIds) {

            String quoteSource = quoteManager.readQuote(number).get(QuoteManager.SOURCE_QUOTE).toString();
            String quoteText = quoteManager.readQuote(number).get(QuoteManager.TEXT_QUOTE).toString();
            String quoteUri = quoteManager.readQuote(number).get(QuoteManager.URI_QUOTE).toString();
            String quoteLink = quoteManager.readQuote(number).get(QuoteManager.LINK_QUOTE).toString();



            RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), layout);
			Log.w("WidgetExample", String.valueOf(number));
			// Set the text
			remoteViews.setTextViewText(R.id.tvQuoteText, "Random 2 : " + String.valueOf(number));


            remoteViews.setTextViewText(R.id.tvQuoteText, quoteText);
            remoteViews.setTextViewText(R.id.tvQuoteSource, quoteSource);
            remoteViews.setImageViewUri(R.id.iconBitmap, Uri.parse(quoteUri));

			// Register an onClickListener
			Intent clickIntent = new Intent(this.getApplicationContext(), MyWidgetProvider.class);
			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.tvQuoteText, pendingIntent);



            // full screen
            Intent fullScreenIntent = new Intent(this.getApplicationContext(), ConfigActivity.class);
            fullScreenIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            fullScreenIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

            PendingIntent pIntent = PendingIntent.getActivity(this, startId, fullScreenIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.tvQuoteSource, pIntent);

            SharedPreferences.Editor editor = sp.edit();
            editor.putString(QuoteManager.TEXT_QUOTE, quoteText);
            editor.putString(QuoteManager.SOURCE_QUOTE, quoteSource);
            editor.commit();


            // share text of the Quote
            Intent shareIntent=new Intent(android.content.Intent.ACTION_SEND);
            String shareText = quoteText + " \n " + quoteSource;
            Log.i("share", shareText);
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
            shareIntent.setType("text/plain");
       //     startActivity(Intent.createChooser(shareIntent, "share via"));
            pIntent = PendingIntent.getActivity(this, number, shareIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.imageShare, pIntent);




            // go to url

            Intent linkIntent = new Intent(this.getApplicationContext(), LinkActivity.class);
            pIntent = PendingIntent.getActivity(this, startId, linkIntent, 0);
            linkIntent.putExtra(QuoteManager.LINK_QUOTE, quoteLink);
            Log.i(LOG, "LINK ///===== " + quoteLink);
            remoteViews.setOnClickPendingIntent(R.id.iconBitmap, pIntent);

            editor = sp.edit();
            editor.putString(QuoteManager.LINK_QUOTE, quoteLink);
            editor.commit();

            appWidgetManager.updateAppWidget(widgetId, remoteViews);

		}
		stopSelf();

		// super.onStart(intent, startId);
	}

    public int counter(SharedPreferences sp, QuoteManager qmr){

        int cnt = sp.getInt(ConfigActivity.WIDGET_COUNT, 0);

        if (cnt < qmr.size() - 1) {
            cnt++;
        } else {
            cnt = 0;
        }

        SharedPreferences.Editor editor = sp.edit();
        editor = sp.edit();
        editor.putInt(ConfigActivity.WIDGET_COUNT, cnt);
        editor.commit();

        return cnt;
    }

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
