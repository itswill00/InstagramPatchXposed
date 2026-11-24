package com.golda.patchinstagram;

import android.app.Activity;
import android.os.Bundle;

/** No-display entry used by LSPosed's settings button and the in-app shortcut. */
public final class OpenInInstagram extends Activity {
    @Override protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        finish();
    }
}
