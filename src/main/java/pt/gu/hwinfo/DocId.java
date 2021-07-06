package pt.gu.hwinfo;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.HashMap;

import pt.gu.utils.StringUtils;


public class DocId {

    public String root = null;
    public String doc = null;

    public static DocId parseUri(Uri uri){
        final String docId = uri != null ? uri.getLastPathSegment() : null;
        return docId != null ? new DocId(docId) : new DocId("","");
    }

    public static DocId parseFile(String rootName, String rootPath, File file){
        return new DocId(rootName,file.getAbsolutePath().substring(rootPath.length()));
    }

    public DocId append(String child){
        return appendChild(this,child);
    }

    public static DocId appendChild(DocId parent, String childId){
        return new DocId(
                parent.root,
                StringUtils.isEmpty(parent.doc) ?
                        childId :
                        parent.doc + "/" + childId
        );
    }

    public DocId(String docId){
        if (docId != null) {
            int i = docId.lastIndexOf(":");
            if (i > 0) {
                root = docId.substring(0, i);
                doc = docId.substring(i + 1);
            } else {
                root = docId;
                doc = "";
            }
        }
    }

    public DocId(String root, String doc){
        this.root = root;
        this.doc = doc;
    }


    public boolean isEmpty(){
        return (root == null || root.length() == 0) && (doc == null || doc.length() == 0);
    }

    public boolean isRoot(){
        return root != null && root.length() > 0 && (doc == null || doc.length() == 0);
    }
    public boolean isRoot(@NonNull String rootId){
        return rootId.equals(root) && (doc == null || doc.length() == 0);
    }

    @Nullable
    public String[] getPathSegments(){
        return doc == null ? null : doc.split("/");
    }

    @Override
    public String toString(){
        return root+":"+doc;
    }

    public Uri buildUri(Uri root){
        return new Uri.Builder().scheme(root.getScheme()).authority(root.getAuthority()).appendPath(toString()).build();
    }

    public Uri buildUri(Uri root, String newChild){
        return new Uri.Builder()
                .scheme(root.getScheme())
                .authority(root.getAuthority())
                .appendPath(DocId.appendChild(this,newChild).toString())
                .build();
    }

    public String getParentDoc() {
        return doc.lastIndexOf('/') > 0 ? doc.substring(0,doc.lastIndexOf('/')) : "";
    }

    @NonNull
    public String lastDocSegmentPath() {
        final int i = StringUtils.isEmpty(doc) ? -1 : doc.lastIndexOf("/");
        return i > 0 ? doc.substring(i + 1) : doc;
    }
}
