package info.kab.quote.ParseLoader;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.kab.quote.LocalDB.QuoteManager;
import info.kab.quote.Widget.ConfigActivity;

/**
 * Created by admin on 4/4/15.
 */
public class ParseManager {

    static final String TEXT_PARSE = "text";
    static final String IMAGE_PARSE = "image";
    static final String SOURCE_PARSE = "source";
    static final String LINK_PARSE = "link";
    static final String LOG_TAG = "path**";
    static final String FIRST_OBJECT = "first_object";

    private static String firstObject;
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    final String DIR_SD = "App_Widget";
    String URI_PATH = "android.resource://com.example.gleb.widget_01/drawable/baal_sulam_small";


    static ParseManager parseManager;
    static Context context;
    static ArrayList<HashMap<String, String>> arrayListQuots;
    HashMap<String, String> hashMap;
    int iterator = 0;
    String uri;


    ParseManager(Context context) {
        ParseManager.context = context;
    }

    public static ParseManager getInstance(Context context) {
        if (parseManager == null) {
            parseManager = new ParseManager(context);
            arrayListQuots = new ArrayList<HashMap<String, String>>();
            connectParse();
        }
        return parseManager;
    }

    public static void connectParse() {

        Parse.enableLocalDatastore(context);
        Parse.initialize(context, "j35nWopJYNDOpbvOUbdfOqevXQ0vYpyyLryGMlDO", "5d6aIFqz8wW9wLlfQVgO4aTVstw5x9AuSr3gFsrN");

        firstObject = "====000====";
        sp =  context.getSharedPreferences(ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putString(FIRST_OBJECT, firstObject);
        editor.commit();

    }


    public boolean checkUpdate() {

        boolean check = false;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Quotes");
        try {

            int lastNumber = query.find().size()-1;
            firstObject = query.find().get(lastNumber).getString(TEXT_PARSE);


            Log.i("id**", "ID dowloud = " + firstObject);
            Log.i("id**", "ID db = " + sp.getString(FIRST_OBJECT, ""));

            if(sp.getString(FIRST_OBJECT, "").equals(firstObject)){
                Log.i("id**", "check = false");
                check = false;
            }
            else {

                check = true;
                Log.i("id**", "check = true");
            }

            editor = sp.edit();
            editor.putString(FIRST_OBJECT, firstObject);
            editor.commit();


        } catch (ParseException e) {
            e.printStackTrace();
        }


        return check;
    }



    public void updateFromParse(){
        if(checkUpdate()){
            Log.i("id**", "updateFromParse = true");
            readParse();
        } else {
            Log.i("id**", "updateFromParse = fase");
        }
    }

    public void readParse() {




         ParseQuery<ParseObject> query = ParseQuery.getQuery("Quotes");


         query.findInBackground(
                 new FindCallback<ParseObject>() {
                     public void done(List<ParseObject> quoteList, ParseException e) {
                         if (e == null) {
                             Log.d("id**", "Retrieved " + quoteList.size() + " quote");
                             for (ParseObject parseObject : quoteList) {


                                 String textP = parseObject.getString(TEXT_PARSE);
                                 String sourceP = parseObject.getString(SOURCE_PARSE);
                                 String linkP = parseObject.getString(LINK_PARSE);

                                 ParseFile imageP = parseObject.getParseFile(IMAGE_PARSE);

//                                 try {
//                                     URI_PATH = convertToBitmap(imageP);
//                                 } catch (ParseException e1) {
//                                     e1.printStackTrace();
//                                 }


                                 Log.d(LOG_TAG, "URI_QUOTE / " + URI_PATH);

                                 hashMap = new HashMap<>();
                                 hashMap.put(QuoteManager.TEXT_QUOTE, textP);
                                 hashMap.put(QuoteManager.SOURCE_QUOTE, sourceP);
                                 hashMap.put(QuoteManager.URI_QUOTE, imageP.getUrl());
                                 hashMap.put(QuoteManager.LINK_QUOTE, linkP);

                                 Log.d(LOG_TAG, "EXT_QUOTE / " + textP);
                                 Log.d(LOG_TAG, "SOURCE_QUOTE / " + sourceP);

                                 arrayListQuots.add(hashMap);


                             }

                             //  ParseObject.pinAllInBackground(quoteList);

                             Log.d(LOG_TAG, "PARSE download complite / ");
                             addToLocalDB();

                         } else {
                             Log.d("quote", "Error: " + e.getMessage());
                         }
                     }
                 }
         );

    }


    public String convertToBitmap(ParseFile image) throws ParseException {


        if (image != null) {


            byte[] data = image.getData();

            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bmp != null) {

                OutputStream fOut = null;
                Time time = new Time();
                time.setToNow();

                try {

                    File file = new File(pathToSave(), Integer.toString(time.year) + Integer.toString(time.month) + Integer.toString(time.monthDay) + Integer.toString(time.hour) + Integer.toString(time.minute) + Integer.toString(time.second) + ".jpg"); // создать уникальное имя для файла основываясь на дате сохранения
                    fOut = new FileOutputStream(file);

                    uri = Uri.fromFile(file).toString();
                    Log.i(LOG_TAG, " uri ==" + uri);

                    bmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // сохранять картинку в jpeg-формате с 85% сжатия.
                    fOut.flush();
                    fOut.close();
                } catch (Exception exc) // здесь необходим блок отслеживания реальных ошибок и исключений, общий Exception приведен в качестве примера
                {
                    //
                }

            }


        } else {

            Log.e(LOG_TAG, " null");

        }

        Log.i(LOG_TAG, iterator + " //////// iterator");
        iterator++;
        return uri;
    }

    public File pathToSave() {

        // check access to SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            // return ;
        }
        // get path SD
        File sdPath = Environment.getExternalStorageDirectory();
        // add main folder to path
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        try {
            sdPath.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // create obj. File, with path to file
        //       File sdFile = new File(sdPath, FILENAME_SD);

        return sdPath;

    }


    public void addToLocalDB() {
        QuoteManager qrm = QuoteManager.greatInstance(context);
        qrm.deleteAll();

        for (int i = 0; i < arrayListQuots.size(); i++) {
            String textM = arrayListQuots.get(i).get(QuoteManager.TEXT_QUOTE);
            String sourceM = arrayListQuots.get(i).get(QuoteManager.SOURCE_QUOTE);
            String uriM = arrayListQuots.get(i).get(QuoteManager.URI_QUOTE);
            String linkM = arrayListQuots.get(i).get(QuoteManager.LINK_QUOTE);


            qrm.greatQuote(textM, sourceM, uriM, linkM);

        }

        qrm.readAllQuote();

    }


    public void updateParseDate() {
        ParseObject myObject = ParseObject.create("QuoteObject");
        myObject.fetchInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // Success!
                } else {
                    // Failure!
                }
            }
        });
    }

    public HashMap readQuote(int count) {
        //      hashMap = new HashMap<>();
        hashMap = arrayListQuots.get(count);
        return hashMap;
    }

    public int size() {
        int size = arrayListQuots.size();
        return size;
    }

}
