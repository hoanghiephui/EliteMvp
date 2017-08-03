package com.jemshit.elitemvp.utils;

import android.app.Activity;
import android.content.Intent;

import com.jemshit.elitemvp.R;

/**
 * Created by hoanghiep on 8/3/17.
 */

public class LauncherUtilities {
    public static void openActivity(Activity activity, Class classOpen) {
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        activity.startActivity(new Intent(activity, classOpen));
        activity.finish();
    }
}
