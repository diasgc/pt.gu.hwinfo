package pt.gu.hwinfo;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.util.ArrayMap;

import org.json.JSONObject;
import org.w3c.dom.Element;

import java.io.File;

import pt.gu.utils.JsonUtils;
import pt.gu.utils.StringUtils;

@SuppressWarnings("unused")
public class Camera implements Info {

    private static final String TAG = Camera.class.getSimpleName();
    private static final boolean DBG = false;

    public static final String root = "cam";

    private final Context context;
    private final JSONObject japi;
    private final JSONObject jcam;

    private static final ArrayMap<String,CameraCharacteristics> camIds = new ArrayMap<>();

    public Camera(Context context){
        this.context =  context;
        this.japi = JsonUtils.openAsset(context,"cam2.json");
        this.jcam = JsonUtils.open(new File(context.getFilesDir(),"hwcam.json"));
        if (camIds.size() == 0)
            buildJcam(context);
    }

    private void buildJcam(Context context){
        CameraManager m = context.getSystemService(CameraManager.class);
        int i = 0;
        String id;
        while(true) {
            try {
                CameraCharacteristics c = m.getCameraCharacteristics(id = String.valueOf(i++));
                camIds.put(id, c);
            } catch (CameraAccessException ignore) {
                break;
            }
        }
    }

    @Override
    public void xml(Element root) {

    }

    @Override
    public void json(JSONObject root) {

    }

    @Override
    public void cursor(CursorXp cursor) {
        String id = cursor.getQueryParameter("id");
        if (id == null){
            for (String cid : camIds.keySet())
                cursor.row().path("cam:").query("id",cid)
                        .title(getCamTitle(cid))
                        .summary(getCamSummary(cid))
                        .mime(MIMETYPE_PROVIDER_CAMERA_DIR)
                        .apply();
            return;
        }
        CameraCharacteristics cc = camIds.get(id);
        if (cc != null){
            for (CameraCharacteristics.Key<?> key : cc.getKeys()){
                cursor.row().id(key.getName()).query("id",id)
                        .title(String.valueOf(cc.get(key)))
                        .summary(StringUtils.lastPathSegment(key.getName(),"."))
                        .apply();
            }
        }
    }



    public String getCamTitle(String key){
        final CameraCharacteristics cc = camIds.get(key);
        return cc == null ? "unavailable" : String.format("%s ", cc.get(CameraCharacteristics.LENS_FACING));
    }

    public String getCamSummary(String key){
        return "";
    }
}
