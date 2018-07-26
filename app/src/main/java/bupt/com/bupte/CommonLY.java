package bupt.com.bupte;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class CommonLY {
    public static boolean checkPermission(Context ctx, String permission){
        return ContextCompat.checkSelfPermission(ctx, permission)== PackageManager.PERMISSION_GRANTED;
    }

    public static void applyPermission(Context ctx, String permission, int requestCode){
        if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) ctx, permission)) {

        }
        ActivityCompat.requestPermissions((Activity)ctx, new String[]{permission}, requestCode);
    }

}
