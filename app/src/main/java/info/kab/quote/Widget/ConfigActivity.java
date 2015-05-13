package info.kab.quote.Widget;

/**
 * Created by gleb on 3/23/2015.
 */

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;
import info.kab.android.widget.quote.R;
import info.kab.quote.LocalDB.QuoteManager;


public class ConfigActivity extends Activity  {

    public final static String WIDGET_PREF = "widget_pref";
    public final static String WIDGET_COUNT = "widget_count_";


    QuoteManager quoteManager;
    MyWidgetProvider myWidget;


    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;
    SharedPreferences sp;
    EditText etFormat;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        quoteManager = QuoteManager.greatInstance(this);
        myWidget = new MyWidgetProvider();
        // извлекаем ID конфигурируемого виджета
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // и проверяем его корректность
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        // формируем intent ответа
        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        // отрицательный ответ
        setResult(RESULT_CANCELED, resultValue);

        setContentView(R.layout.config);

        sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);

        String text = sp.getString(QuoteManager.TEXT_QUOTE, "");
        String source = sp.getString(QuoteManager.SOURCE_QUOTE, "");
        TextView tvText = (TextView) findViewById(R.id.tvQute);
        TextView tvSource = (TextView) findViewById(R.id.tvSourseOpen);
//        TextView tvDarkStyle = (TextView) findViewById(R.id.tvDarkStyle);
//        TextView tvLightStyle = (TextView) findViewById(R.id.tvLightSyle);



        tvText.setText(text);
        tvSource.setText(source);

        int cnt = sp.getInt(ConfigActivity.WIDGET_COUNT + widgetID, -1);
        if (cnt == -1) sp.edit().putInt(WIDGET_COUNT + widgetID, 0);





    }

    public void setDarkStyle(View view){

        int layout = R.layout.widget_bb_black;
        Log.i(UpdateWidgetService.LOG, "set dark");
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(UpdateWidgetService.LAYOUT, layout);
        editor.commit();

   myWidget.onUpdate(this, AppWidgetManager.getInstance(this), FOCUSED_STATE_SET );

        setResult(RESULT_OK, resultValue);
        finish();
    }

    public void setLightStyle(View view){
        int layout = R.layout.widget_bb;

        Log.i(UpdateWidgetService.LOG, "set light");
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(UpdateWidgetService.LAYOUT, layout);
        editor.commit();

        myWidget.onUpdate(this, AppWidgetManager.getInstance(this), FOCUSED_STATE_SET );

        setResult(RESULT_OK, resultValue);
        finish();
    }


    public void onClick(View v) {

        setResult(RESULT_OK, resultValue);
        finish();
    }
}