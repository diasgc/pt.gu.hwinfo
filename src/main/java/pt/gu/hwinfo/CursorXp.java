package pt.gu.hwinfo;

import android.content.Context;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.util.ArrayMap;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import pt.gu.utils.TypeUtils;

import static pt.gu.hwinfo.Info.DEFAULT_PROJECTION;

public class CursorXp {

    public static class Row {

        private final CursorXp cursorXp;
        private boolean isIdFullPath = false;
        String id;
        ArrayMap<String,String> query = new ArrayMap<>();
        String title = "";
        String summary = "";
        String mime = "";
        long size = 0;
        long date = 0;
        int icon = 0;
        int flags = 0;

        Row(CursorXp m){
            this.cursorXp = m;
        }

        public Row json(JSONObject root, String key){
            id = key;
            if (!root.has(key))
                return this;
            try {
                final Object o = root.get(key);
                DocId docId = cursorXp.mDocId.append(key);
                title = key;
                if (!(o instanceof JSONObject)){
                    summary = String.valueOf(o);
                    return this;
                }

                if (((JSONObject) o).has(".attr")) {
                    JSONObject attr = ((JSONObject) o).getJSONObject(".attr");
                    Object a;
                    if (attr.has("title"))
                        title = (a = attr.get("title")) instanceof Integer ? cursorXp.mContext.getString((int) a) : String.valueOf(a);
                    if (attr.has("summary"))
                        summary = (a = attr.get("summary")) instanceof Integer ? cursorXp.mContext.getString((int) a) : String.valueOf(a);
                    if (attr.has("mime"))
                        mime = (a = attr.get("mime")) instanceof Integer ? cursorXp.mContext.getString((int) a) : String.valueOf(a);
                    if (attr.has("size") && (a = attr.get("size")) instanceof Long)
                        size = (long) a;
                    if (attr.has("date") && (a = attr.get("date")) instanceof Long)
                        date = (long) a;
                    if (attr.has("flags") && (a = attr.get("flags")) instanceof Integer)
                        flags = (int) a;
                    if (attr.has("icon") && (a = attr.get("icon")) instanceof Integer)
                        flags = (int) a;
                    apply();
                }
            } catch (JSONException ignore){}
            return this;
        }

        public Row path(String path){
            this.id = path;
            isIdFullPath = true;
            return this;
        }

        public Row id(String id){
            this.id = id;
            isIdFullPath = false;
            return this;
        }

        public Row id(int id){
            this.id = String.valueOf(id);
            isIdFullPath = false;
            return this;
        }

        public Row query(String key, Object row){
            query.put(key, String.valueOf(row));
            return this;
        }

        public Row title(String title){
            this.title = title;
            return this;
        }

        public Row title(@StringRes int stringRes){
            this.title = cursorXp.mContext.getString(stringRes);
            return this;
        }

        public Row title(String format, Object... args) {
            this.title = String.format(format,args);
            return this;
        }

        public Row summary(String summary){
            this.summary = summary;
            return this;
        }

        public Row summary(@StringRes int stringRes){
            this.summary = cursorXp.mContext.getString(stringRes);
            return this;
        }

        public Row summary(String format, Object... args) {
            this.summary = String.format(format,args);
            return this;
        }

        public Row mime(String mime){
            this.mime = mime;
            return this;
        }

        public Row size(long size){
            this.size = size;
            return this;
        }

        public Row date(long date){
            this.date = date;
            return this;
        }

        public Row flag(int flag){
            this.flags |=flag;
            return this;
        }

        public Row flags(int flags){
            this.flags = flags;
            return this;
        }

        public Row icon(int icon){
            this.icon = icon;
            return this;
        }

        public void apply(){
            final String path = id == null ? cursorXp.mDocId.toString() :
                    isIdFullPath ? id : cursorXp.mDocId.append(id).toString();
            Uri.Builder u = cursorXp.mRoot.buildUpon().appendPath(path);
            if (query.size() > 0){
                for (Map.Entry<String,String> e : query.entrySet()){
                    u.appendQueryParameter(e.getKey(),e.getValue());
                }
            }
            cursorXp.mCursor.newRow().add(DocumentsContract.Document.COLUMN_DOCUMENT_ID, u.build().toString())
                    .add(DocumentsContract.Document.COLUMN_DISPLAY_NAME,title)
                    .add(DocumentsContract.Document.COLUMN_SUMMARY,summary)
                    .add(DocumentsContract.Document.COLUMN_MIME_TYPE, mime)
                    .add(DocumentsContract.Document.COLUMN_SIZE,size)
                    .add(DocumentsContract.Document.COLUMN_LAST_MODIFIED,date)
                    .add(DocumentsContract.Document.COLUMN_ICON,icon)
                    .add(DocumentsContract.Document.COLUMN_FLAGS,flags);
        }
    }

    public static CursorXp create(Context context, Uri uri){
        return new CursorXp(context, uri);
    }

    private final Context mContext;
    private final MatrixCursor mCursor;
    private final DocId mDocId;
    private final Uri mRoot;
    private final Uri mUri;

    private CursorXp(Context context, Uri uri){
        mContext = context;
        mCursor = new MatrixCursor(DEFAULT_PROJECTION);
        mUri = uri;
        mRoot = new Uri.Builder().scheme(uri.getScheme()).authority(uri.getAuthority()).build();
        mDocId = DocId.parseUri(uri);
    }

    public Context getContext(){
        return mContext;
    }

    public Uri getUri(){
        return mUri;
    }

    public Uri getRootUri(){
        return mRoot;
    }

    public DocId getDocId(){
        return mDocId;
    }

    public String doc(){return mDocId.doc;}
    public String root(){return mDocId.root;}

    public Row row(){
        return new Row(this);
    }

    public CursorXp row(JSONObject root, String key){
        new Row(this).json(root, key);
        return this;
    }

    @Nullable
    public String getQueryParameter(String key) {
        return mUri.getQueryParameter(key);
    }

    public int getQueryParameter(String key, int defVal){
        return TypeUtils.parseInt(getQueryParameter(key),defVal);
    }

    public MatrixCursor getCursor(){
        return mCursor;
    }


}
