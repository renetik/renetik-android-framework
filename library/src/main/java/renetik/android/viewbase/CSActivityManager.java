package renetik.android.viewbase;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class CSActivityManager {

    protected CSActivity _activity;

    public CSActivityManager(CSActivity activity) {
        _activity = activity;
    }

    public CSViewController create() {
        return _activity.createController();
    }

    public void onCreate(Bundle state) {
        controller().setActivity(_activity.activity());
        controller().getView();
        controller().onBeforeCreate(state);
        _activity.activity().setContentView(controller().getView());
        controller().onCreate(state);
    }

    public void onDestroy() {
        onDestroyUnbindDrawables(controller().getView());
        controller().onDestroy();
        _activity = null;
    }

    private CSViewController controller() {
        return _activity.controller();
    }

    private void onDestroyUnbindDrawables(View view) {
        if (view.getBackground() != null) view.getBackground().setCallback(null);
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
                onDestroyUnbindDrawables(((ViewGroup) view).getChildAt(i));
            try {
                ((ViewGroup) view).removeAllViews();
            } catch (Exception e) {
            }
        }
    }

}