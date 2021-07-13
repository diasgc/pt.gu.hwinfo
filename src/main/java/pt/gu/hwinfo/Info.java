package pt.gu.hwinfo;

import android.database.MatrixCursor;
import android.provider.DocumentsContract;

import org.json.JSONObject;
import org.w3c.dom.Element;

@SuppressWarnings("unused")
public interface Info {

    String TYPE_PROVIDER = "vnd.android.provider";
    String MIMETYPE_PROVIDERLAYOUT           = TYPE_PROVIDER+"/layout";
    String MIMETYPE_PROVIDER_CPU             = TYPE_PROVIDER+"/hw-cpu";
    String MIMETYPE_PROVIDER_CPU_LOAD        = TYPE_PROVIDER+"/hw-cpu-load";
    String MIMETYPE_PROVIDER_CPU_FREQ        = TYPE_PROVIDER+"/hw-cpu-freq";
    String MIMETYPE_PROVIDER_CPU_DIR         = TYPE_PROVIDER+"/hw-cpu-directory";
    String MIMETYPE_PROVIDER_THERMAL         = TYPE_PROVIDER+"/hw-thermal";
    String MIMETYPE_PROVIDER_THERMAL_DIR     = TYPE_PROVIDER+"/hw-thermal-directory";
    String MIMETYPE_PROVIDER_MEMORY          = TYPE_PROVIDER+"/hw-memory";
    String MIMETYPE_PROVIDER_SENSOR          = TYPE_PROVIDER+"/hw-sensor";
    String MIMETYPE_PROVIDER_BATTERY         = TYPE_PROVIDER+"/hw-battery";
    String MIMETYPE_PROVIDER_CAMERA          = TYPE_PROVIDER+"/hw-camera";
    String MIMETYPE_PROVIDER_CAMERA_DIR      = TYPE_PROVIDER+"/hw-camera-directory";
    String MIMETYPE_PROVIDER_WIFI            = TYPE_PROVIDER+"/hw-wireless";
    String MIMETYPE_PROVIDER_BLUETOOTH       = TYPE_PROVIDER+"/hw-bluetooth";
    String MIMETYPE_PROVIDER_MICROPHONE_FREQ = TYPE_PROVIDER+"/hw-microphone-freqresp";
    String MIMETYPE_PROVIDER_MICROPHONE_CHNL = TYPE_PROVIDER+"/hw-microphone-channels";
    String MIMETYPE_PROVIDER_MICROPHONE      = TYPE_PROVIDER+"/hw-microphone";

    String[] DEFAULT_PROJECTION = {
            DocumentsContract.Document.COLUMN_DOCUMENT_ID,
            DocumentsContract.Document.COLUMN_DISPLAY_NAME,
            DocumentsContract.Document.COLUMN_SUMMARY,
            DocumentsContract.Document.COLUMN_MIME_TYPE,
            DocumentsContract.Document.COLUMN_SIZE,
            DocumentsContract.Document.COLUMN_LAST_MODIFIED,
            DocumentsContract.Document.COLUMN_ICON,
            DocumentsContract.Document.COLUMN_FLAGS,
    };
    
    void xml(Element root);
    void json(JSONObject root);
    void cursor(CursorXp cursor);

}
