package bupt.com.bupte;

import android.app.Application;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * Created by lenovo on 2018/8/9.
 */

public class SpecifiedFontApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Typeface mTypeface = Typeface.createFromAsset(getAssets(), "fonts/SourceHanSansCN-Medium.otf");

        try {
            Field field = Typeface.class.getDeclaredField("MONOSPACE");
            field.setAccessible(true);
            field.set(null, mTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
