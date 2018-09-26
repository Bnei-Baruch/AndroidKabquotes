package info.kab.quote.LocalDB;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import info.kab.android.widget.quote.R;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


/**
 * Created by admin on 4/3/15.
 */
public class QuoteManager {

    public static final String TEXT_QUOTE = "textQuote";
    public static final String SOURCE_QUOTE = "sourceQuote";
    public static final String URI_QUOTE = "uriQuote";
    public static final String LINK_QUOTE = "linkQuote";
    public static final String LOG = "my**";



    static QuoteManager instance;
    private static String[] quotsLink;
    private static String[] quotsSource;
    private static String[] quotsText;
    HashMap<String, String> hashMap;
    Realm realm;

    public static String link = "http://www.kab.co.il/";
    String quoteUri = Uri.parse("android.resource://info.kab.quote/drawable/baal_sulam_small").toString();
    private ArrayList<HashMap<String, String>> mQuots;


    public QuoteManager(Context context) {

        try {
            realm = Realm.getInstance(context);
            Log.i(LOG, "//////// INTT start");
            Resources resources = context.getResources();
            quotsText = resources.getStringArray(R.array.quotsText);
            quotsSource = resources.getStringArray(R.array.quotsSource);
            quotsLink = resources.getStringArray(R.array.quotsLink);
            Log.i(LOG, "//////// INTT finish");
        } catch (Exception e) {
            e.printStackTrace();
        }
        mQuots = new ArrayList<HashMap<String, String>>();

        createFirstData();

    }

    public void createFirstData(){

        try {
            for (int i = 0; i < quotsText.length; i++) {
                greatQuote(quotsText[i], quotsSource[i], quoteUri ,quotsLink[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        readAllQuote();
    }

    public static QuoteManager creatInstance(Context context) {
        if (instance == null) {
            instance = new QuoteManager(context);

        }

        return instance;
    }

    public void greatQuote(String quoteText, String quoteSource, String quoteUri, String link) {


            realm.beginTransaction();
            QuoteRecords quoteRecords = realm.createObject(QuoteRecords.class);
            quoteRecords.setTextQuote(quoteText);
            quoteRecords.setSourceQuote(quoteSource);
            quoteRecords.setUriQuote(quoteUri);
            quoteRecords.setLinkQuote(link);
            realm.commitTransaction();



    }

    public void deleteAll() {

        realm.beginTransaction();
        RealmQuery<QuoteRecords> query = realm.where(QuoteRecords.class);
        RealmResults<QuoteRecords> results = query.findAll();
        results.clear();
        realm.commitTransaction();
    }

    public void readAllQuote() {

        mQuots.clear();

            realm.beginTransaction();
            RealmQuery<QuoteRecords> query = realm.where(QuoteRecords.class);
            RealmResults<QuoteRecords> results = query.findAll();


            for (QuoteRecords quoteRecord : results) {

                hashMap = new HashMap<>();

                String textR = quoteRecord.getTextQuote();
                String sourceR = quoteRecord.getSourceQuote();
                String uriR = quoteRecord.getUriQuote();
                String linkR = quoteRecord.getLinkQuote();

                hashMap.put(TEXT_QUOTE, textR);
                hashMap.put(SOURCE_QUOTE, sourceR);
                hashMap.put(URI_QUOTE, uriR);
                hashMap.put(LINK_QUOTE, linkR);

                Log.d(LOG, "TEXT_REALM / " + textR);
                Log.d(LOG, "SOURCE_REALM / " + sourceR);
                Log.d(LOG, "LINK_REALM / " + linkR);

                mQuots.add(hashMap);

            }
            realm.commitTransaction();

    }

    public HashMap readQuote(int count) {
        hashMap = mQuots.get(count);
        return hashMap;
    }

    public int size() {
        int size = 0;

            realm.beginTransaction();
            RealmQuery<QuoteRecords> query = realm.where(QuoteRecords.class);
            RealmResults<QuoteRecords> results = query.findAll();
            size = results.size();
            realm.commitTransaction();

        return size;
    }

}
