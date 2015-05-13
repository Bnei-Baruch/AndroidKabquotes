package info.kab.quote.LocalDB;

import io.realm.RealmObject;

/**
 * Created by gleb on 4/2/2015.
 */


public class QuoteRecords extends RealmObject {


    private String sourceQuote;
    private String textQuote;
    private String uriQuote;
    private String linkQuote;



    public String getLinkQuote() {
        return linkQuote;
    }

    public void setLinkQuote(String linkQuote) {
        this.linkQuote = linkQuote;
    }

    public String getUriQuote() {
        return uriQuote;
    }

    public void setUriQuote(String uriQuote) {
        this.uriQuote = uriQuote;
    }

    public String getSourceQuote() {
        return sourceQuote;
    }

    public void setSourceQuote(String sourceQuote) {
        this.sourceQuote = sourceQuote;
    }

    public String getTextQuote() {
        return textQuote;
    }

    public void setTextQuote(String textQuote) {
        this.textQuote = textQuote;
    }
}
