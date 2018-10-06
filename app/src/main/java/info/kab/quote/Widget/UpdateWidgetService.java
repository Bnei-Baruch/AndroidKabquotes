package info.kab.quote.Widget;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.InputStream;

import info.kab.android.widget.quote.R;
import info.kab.quote.LocalDB.QuoteManager;

public class UpdateWidgetService extends Service {
	public static final String LOG = "my**";
    public static final String LAYOUT = "layout";
    public static final int JOB_ID = 1;
    public static void enqueueWork(Context context, Intent work) {
       // enqueueWork(context, UpdateWidgetService.class, JOB_ID, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1,new Notification());
    }


    @Override
	public void onStart(Intent intent, int startId) {
		Log.i(LOG, "Called");
		// Create some random data
        QuoteManager quoteManager = QuoteManager.creatInstance(this);
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

            Bitmap mIcon_val = null;

            remoteViews.setImageViewBitmap(R.id.iconBitmap,null);
            DownloadImageTask down =  new DownloadImageTask(mIcon_val,remoteViews,R.id.iconBitmap,widgetId);
            down.execute(quoteUri);


            //remoteViews.setImageViewUri(R.id.iconBitmap,  Uri.parse(quoteUri));



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

//    @Override
//    protected void onHandleWork(@NonNull Intent intent) {
//        Log.i(LOG, "Called");
//        // Create some random data
//        QuoteManager quoteManager = QuoteManager.creatInstance(this);
//        SharedPreferences sp = this.getSharedPreferences(ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
//
//        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
//
//        ComponentName thisWidget = new ComponentName(getApplicationContext(), MyWidgetProvider.class);
//        int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);
//
//        Log.w(LOG, "From Intent" + String.valueOf(allWidgetIds.length));
//        Log.w(LOG, "Direct" + String.valueOf(allWidgetIds2.length));
//
//        int number = counter(sp, quoteManager);
//        int layout = sp.getInt(LAYOUT, R.layout.widget_bb_black);
//
//
//        for (int widgetId : allWidgetIds) {
//
//            String quoteSource = quoteManager.readQuote(number).get(QuoteManager.SOURCE_QUOTE).toString();
//            String quoteText = quoteManager.readQuote(number).get(QuoteManager.TEXT_QUOTE).toString();
//            String quoteUri = quoteManager.readQuote(number).get(QuoteManager.URI_QUOTE).toString();
//            String quoteLink = quoteManager.readQuote(number).get(QuoteManager.LINK_QUOTE).toString();
//
//
//
//            RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), layout);
//            Log.w("WidgetExample", String.valueOf(number));
//            // Set the text
//            remoteViews.setTextViewText(R.id.tvQuoteText, "Random 2 : " + String.valueOf(number));
//
//
//            remoteViews.setTextViewText(R.id.tvQuoteText, quoteText);
//            remoteViews.setTextViewText(R.id.tvQuoteSource, quoteSource);
//
//            Bitmap mIcon_val = null;
//
//            remoteViews.setImageViewBitmap(R.id.iconBitmap,null);
//            DownloadImageTask down =  new DownloadImageTask(mIcon_val,remoteViews,R.id.iconBitmap,widgetId);
//            down.execute(quoteUri);
//
//
//            //remoteViews.setImageViewUri(R.id.iconBitmap,  Uri.parse(quoteUri));
//
//
//
//            // Register an onClickListener
//            Intent clickIntent = new Intent(this.getApplicationContext(), MyWidgetProvider.class);
//            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.tvQuoteText, pendingIntent);
//
//
//
//            // full screen
//            Intent fullScreenIntent = new Intent(this.getApplicationContext(), ConfigActivity.class);
//            fullScreenIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
//            fullScreenIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
//
//            PendingIntent pIntent = PendingIntent.getActivity(this, 1, fullScreenIntent, 0);
//            remoteViews.setOnClickPendingIntent(R.id.tvQuoteSource, pIntent);
//
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putString(QuoteManager.TEXT_QUOTE, quoteText);
//            editor.putString(QuoteManager.SOURCE_QUOTE, quoteSource);
//            editor.commit();
//
//
//            // share text of the Quote
//            Intent shareIntent=new Intent(android.content.Intent.ACTION_SEND);
//            String shareText = quoteText + " \n " + quoteSource;
//            Log.i("share", shareText);
//            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
//            shareIntent.setType("text/plain");
//            //     startActivity(Intent.createChooser(shareIntent, "share via"));
//            pIntent = PendingIntent.getActivity(this, number, shareIntent, 0);
//            remoteViews.setOnClickPendingIntent(R.id.imageShare, pIntent);
//
//
//
//
//            // go to url
//
//            Intent linkIntent = new Intent(this.getApplicationContext(), LinkActivity.class);
//            pIntent = PendingIntent.getActivity(this, 1, linkIntent, 0);
//            linkIntent.putExtra(QuoteManager.LINK_QUOTE, quoteLink);
//            Log.i(LOG, "LINK ///===== " + quoteLink);
//            remoteViews.setOnClickPendingIntent(R.id.iconBitmap, pIntent);
//
//            editor = sp.edit();
//            editor.putString(QuoteManager.LINK_QUOTE, quoteLink);
//            editor.commit();
//
//            appWidgetManager.updateAppWidget(widgetId, remoteViews);
//
//        }
//        stopSelf();
//
//    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        Bitmap bmImage;
        RemoteViews rv;
        int id;
        int widgetid;

        public DownloadImageTask(Bitmap bmImage,RemoteViews review,int id,int wid) {
            this.bmImage = bmImage;
            this.rv = review;
            this.id = id;
            this.widgetid = wid;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage = result;
            rv.setImageViewBitmap(id,null);
            rv.setImageViewBitmap(id,result);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
            appWidgetManager.partiallyUpdateAppWidget(widgetid, rv);
        }
    }


}
