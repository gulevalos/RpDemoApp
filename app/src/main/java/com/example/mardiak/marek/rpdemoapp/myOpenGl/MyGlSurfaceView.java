package com.example.mardiak.marek.rpdemoapp.myOpenGl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;


/**
 * Created by mm on 3/23/2016.
 */
public class MyGlSurfaceView extends GLSurfaceView {
    /*TextView tv;*/
    private final Context maContext; //mm context of Activity that instantiated this GLSurfaceview
    private final CubeModel model;


    public MyGlSurfaceView(Context context)
    {
        super(context);
        maContext= context;
        model = new CubeModel();

        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager) maContext.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2)
        {
            // Request an OpenGL ES 2.0 compatible context.
            setEGLContextClientVersion(2);
            setEGLConfigChooser(8, 8, 8, 8, 16, 0); //was not in tutorial

            // Set the renderer to our demo renderer, defined below.
            setRenderer(new MyGLRenderer(model));
        }
        else
        {
            // This is where you could create an OpenGL ES 1.x compatible
            // renderer if you wanted to support both ES 1 and ES 2.
            return;
        }

    }

    public void changeModelColor(float colors[]) {
        this.model.colorsCube = colors;
    }

   /* @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        final float x=e.getX();
        final float y=e.getY();
        Log.i("GL Surface View", "X=" + x + " Y=" + y);
        maContext.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                maContext.setTitle("X="+x+" Y="+y);
            }
        });
        return false;
    }*/
}
