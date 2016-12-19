package io.realm;


import android.support.v11.util.JsonReader;
import android.support.v11.util.JsonToken;
import info.kab.quote.LocalDB.QuoteRecords;
import io.realm.RealmObject;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnType;
import io.realm.internal.ImplicitTransaction;
import io.realm.internal.LinkView;
import io.realm.internal.Table;
import io.realm.internal.TableOrView;
import io.realm.internal.android.JsonUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuoteRecordsRealmProxy extends QuoteRecords {

    private static long INDEX_SOURCEQUOTE;
    private static long INDEX_TEXTQUOTE;
    private static long INDEX_URIQUOTE;
    private static long INDEX_LINKQUOTE;
    private static Map<String, Long> columnIndices;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("sourceQuote");
        fieldNames.add("textQuote");
        fieldNames.add("uriQuote");
        fieldNames.add("linkQuote");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    @Override
    public String getSourceQuote() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_SOURCEQUOTE);
    }

    @Override
    public void setSourceQuote(String value) {
        realm.checkIfValid();
        row.setString(INDEX_SOURCEQUOTE, (String) value);
    }

    @Override
    public String getTextQuote() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_TEXTQUOTE);
    }

    @Override
    public void setTextQuote(String value) {
        realm.checkIfValid();
        row.setString(INDEX_TEXTQUOTE, (String) value);
    }

    @Override
    public String getUriQuote() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_URIQUOTE);
    }

    @Override
    public void setUriQuote(String value) {
        realm.checkIfValid();
        row.setString(INDEX_URIQUOTE, (String) value);
    }

    @Override
    public String getLinkQuote() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_LINKQUOTE);
    }

    @Override
    public void setLinkQuote(String value) {
        realm.checkIfValid();
        row.setString(INDEX_LINKQUOTE, (String) value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if(!transaction.hasTable("class_QuoteRecords")) {
            Table table = transaction.getTable("class_QuoteRecords");
            table.addColumn(ColumnType.STRING, "sourceQuote");
            table.addColumn(ColumnType.STRING, "textQuote");
            table.addColumn(ColumnType.STRING, "uriQuote");
            table.addColumn(ColumnType.STRING, "linkQuote");
            table.setPrimaryKey("");
            return table;
        }
        return transaction.getTable("class_QuoteRecords");
    }

    public static void validateTable(ImplicitTransaction transaction) {
        if(transaction.hasTable("class_QuoteRecords")) {
            Table table = transaction.getTable("class_QuoteRecords");
            if(table.getColumnCount() != 4) {
                throw new IllegalStateException("Column count does not match");
            }
            Map<String, ColumnType> columnTypes = new HashMap<String, ColumnType>();
            for(long i = 0; i < 4; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }
            if (!columnTypes.containsKey("sourceQuote")) {
                throw new IllegalStateException("Missing column 'sourceQuote'");
            }
            if (columnTypes.get("sourceQuote") != ColumnType.STRING) {
                throw new IllegalStateException("Invalid type 'String' for column 'sourceQuote'");
            }
            if (!columnTypes.containsKey("textQuote")) {
                throw new IllegalStateException("Missing column 'textQuote'");
            }
            if (columnTypes.get("textQuote") != ColumnType.STRING) {
                throw new IllegalStateException("Invalid type 'String' for column 'textQuote'");
            }
            if (!columnTypes.containsKey("uriQuote")) {
                throw new IllegalStateException("Missing column 'uriQuote'");
            }
            if (columnTypes.get("uriQuote") != ColumnType.STRING) {
                throw new IllegalStateException("Invalid type 'String' for column 'uriQuote'");
            }
            if (!columnTypes.containsKey("linkQuote")) {
                throw new IllegalStateException("Missing column 'linkQuote'");
            }
            if (columnTypes.get("linkQuote") != ColumnType.STRING) {
                throw new IllegalStateException("Invalid type 'String' for column 'linkQuote'");
            }

            columnIndices = new HashMap<String, Long>();
            for (String fieldName : getFieldNames()) {
                long index = table.getColumnIndex(fieldName);
                if (index == -1) {
                    throw new RealmMigrationNeededException("Field '" + fieldName + "' not found for type QuoteRecords");
                }
                columnIndices.put(fieldName, index);
            }
            INDEX_SOURCEQUOTE = table.getColumnIndex("sourceQuote");
            INDEX_TEXTQUOTE = table.getColumnIndex("textQuote");
            INDEX_URIQUOTE = table.getColumnIndex("uriQuote");
            INDEX_LINKQUOTE = table.getColumnIndex("linkQuote");
        } else {
            throw new RealmMigrationNeededException("The QuoteRecords class is missing from the schema for this Realm.");
        }
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    public static Map<String,Long> getColumnIndices() {
        return columnIndices;
    }

    public static QuoteRecords createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        QuoteRecords obj = realm.createObject(QuoteRecords.class);
        if (!json.isNull("sourceQuote")) {
            obj.setSourceQuote((String) json.getString("sourceQuote"));
        }
        if (!json.isNull("textQuote")) {
            obj.setTextQuote((String) json.getString("textQuote"));
        }
        if (!json.isNull("uriQuote")) {
            obj.setUriQuote((String) json.getString("uriQuote"));
        }
        if (!json.isNull("linkQuote")) {
            obj.setLinkQuote((String) json.getString("linkQuote"));
        }
        return obj;
    }

    public static QuoteRecords createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        QuoteRecords obj = realm.createObject(QuoteRecords.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("sourceQuote") && reader.peek() != JsonToken.NULL) {
                obj.setSourceQuote((String) reader.nextString());
            } else if (name.equals("textQuote")  && reader.peek() != JsonToken.NULL) {
                obj.setTextQuote((String) reader.nextString());
            } else if (name.equals("uriQuote")  && reader.peek() != JsonToken.NULL) {
                obj.setUriQuote((String) reader.nextString());
            } else if (name.equals("linkQuote")  && reader.peek() != JsonToken.NULL) {
                obj.setLinkQuote((String) reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static QuoteRecords copyOrUpdate(Realm realm, QuoteRecords object, boolean update, Map<RealmObject,RealmObject> cache) {
        if (object.realm != null && object.realm.getId() == realm.getId()) {
            return object;
        }
        return copy(realm, object, update, cache);
    }

    public static QuoteRecords copy(Realm realm, QuoteRecords newObject, boolean update, Map<RealmObject,RealmObject> cache) {
        QuoteRecords realmObject = realm.createObject(QuoteRecords.class);
        cache.put(newObject, realmObject);
        realmObject.setSourceQuote(newObject.getSourceQuote() != null ? newObject.getSourceQuote() : "");
        realmObject.setTextQuote(newObject.getTextQuote() != null ? newObject.getTextQuote() : "");
        realmObject.setUriQuote(newObject.getUriQuote() != null ? newObject.getUriQuote() : "");
        realmObject.setLinkQuote(newObject.getLinkQuote() != null ? newObject.getLinkQuote() : "");
        return realmObject;
    }

    static QuoteRecords update(Realm realm, QuoteRecords realmObject, QuoteRecords newObject, Map<RealmObject, RealmObject> cache) {
        realmObject.setSourceQuote(newObject.getSourceQuote() != null ? newObject.getSourceQuote() : "");
        realmObject.setTextQuote(newObject.getTextQuote() != null ? newObject.getTextQuote() : "");
        realmObject.setUriQuote(newObject.getUriQuote() != null ? newObject.getUriQuote() : "");
        realmObject.setLinkQuote(newObject.getLinkQuote() != null ? newObject.getLinkQuote() : "");
        return realmObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("QuoteRecords = [");
        stringBuilder.append("{sourceQuote:");
        stringBuilder.append(getSourceQuote());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{textQuote:");
        stringBuilder.append(getTextQuote());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{uriQuote:");
        stringBuilder.append(getUriQuote());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{linkQuote:");
        stringBuilder.append(getLinkQuote());
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        String realmName = realm.getPath();
        String tableName = row.getTable().getName();
        long rowIndex = row.getIndex();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (rowIndex ^ (rowIndex >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuoteRecordsRealmProxy aQuoteRecords = (QuoteRecordsRealmProxy)o;

        String path = realm.getPath();
        String otherPath = aQuoteRecords.realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = row.getTable().getName();
        String otherTableName = aQuoteRecords.row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (row.getIndex() != aQuoteRecords.row.getIndex()) return false;

        return true;
    }

}
