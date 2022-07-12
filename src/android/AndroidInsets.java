


package com.gxgow.androidinsets;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Insets;

import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONException;


public class AndroidInsets extends CordovaPlugin {
    private static final String TAG = "AndroidInsets";

    
    /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param args              JSONArry of arguments for the plugin.
     * @param callbackContext   The callback id used when calling back into JavaScript.
     * @return                  True if the action was valid, false otherwise.
     */
    @Override
    public boolean execute(final String action, final CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
        final Activity activity = this.cordova.getActivity();

        if ("setLayout".equals("action")) {
            this.webView.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            return true;
        }

        final WindowInsets insets = getInsets();
        final float density = activity.getResources().getDisplayMetrics().density;
        float topInsets;
        float leftInsets = 0;
        float rightInsets = 0;
        float bottomInsets = 0;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            DisplayCutout cutout = insets.getDisplayCutout();

            if(cutout != null) {
                topInsets = cutout.getSafeInsetTop() / density;
                leftInsets = cutout.getSafeInsetLeft() / density;
                rightInsets = cutout.getSafeInsetRight() / density;
                bottomInsets = cutout.getSafeInsetBottom() / density;
            } else {
                topInsets = insets.getSystemWindowInsetTop() / density;
            }
        } else {
            topInsets = insets.getSystemWindowInsetTop() / density;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            Insets gestureInsets = getGestureInsets(insets);
            if(gestureInsets != null) {
                bottomInsets = gestureInsets.bottom / density;
            }
        }


        if ("hasCutout".equals(action)) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, topInsets > 0));
            return true;
        }

        if ("getInsetsTop".equals(action)) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, topInsets));
            return true;
        }
        
        if ("getInsetsRight".equals(action)) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, rightInsets));
            return true;
        }

        if ("getInsetsBottom".equals(action)) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, bottomInsets));
            return true;
        }

        if ("getInsetsLeft".equals(action)) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, leftInsets));
            return true;
        }

        return false;
    }

    @TargetApi(23)
    private WindowInsets getInsets() {
        return this.webView.getView().getRootWindowInsets();
    }

    @TargetApi(30)
    private Insets getGestureInsets(WindowInsets insets) {
        if(!this.gesturesEnabled()) {
            return null;
        }
        return insets.getInsets(WindowInsets.Type.systemGestures());
    }

    private boolean gesturesEnabled() {
        Resources resources = this.cordova.getContext().getResources();
        int resourceId = resources.getIdentifier("config_navBarInteractionMode", "integer", "android");
        if (resourceId > 0) {
            return resources.getInteger(resourceId) > 0;
        }
        return false;
    }
}
