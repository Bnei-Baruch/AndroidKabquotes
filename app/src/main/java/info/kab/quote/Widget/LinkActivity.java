package info.kab.quote.Widget;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import info.kab.android.widget.quote.R;
import info.kab.quote.LocalDB.QuoteManager;


public class LinkActivity extends Activity {

    private TextView quote;
    private ImageView image;
    private TextView source;
    private QuoteManager quoteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_bb_black);

        quote = (TextView)findViewById(R.id.tvQuoteText);
        image = (ImageView)findViewById(R.id.iconBitmap);
        source = findViewById(R.id.tvQuoteSource);

        quoteManager = QuoteManager.creatInstance(this);

        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quote.setText(quoteManager.readQuote(counter(PreferenceManager.getDefaultSharedPreferences(LinkActivity.this),quoteManager)).get(QuoteManager.TEXT_QUOTE).toString());
                source.setText(quoteManager.readQuote(counter(PreferenceManager.getDefaultSharedPreferences(LinkActivity.this),quoteManager)).get(QuoteManager.SOURCE_QUOTE).toString());

            }
        });

        findViewById(R.id.main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quote.setText(quoteManager.readQuote(counter(PreferenceManager.getDefaultSharedPreferences(LinkActivity.this),quoteManager)).get(QuoteManager.TEXT_QUOTE).toString());
                source.setText(quoteManager.readQuote(counter(PreferenceManager.getDefaultSharedPreferences(LinkActivity.this),quoteManager)).get(QuoteManager.SOURCE_QUOTE).toString());

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_link, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        quote.setText(quoteManager.readQuote(counter(PreferenceManager.getDefaultSharedPreferences(this),quoteManager)).get(QuoteManager.TEXT_QUOTE).toString());
        source.setText(quoteManager.readQuote(counter(PreferenceManager.getDefaultSharedPreferences(this),quoteManager)).get(QuoteManager.SOURCE_QUOTE).toString());


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

}
