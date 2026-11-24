package com.golda.patchinstagram;

import android.location.LocationManager;

/** Answers location APIs with no data while the switch is on. Instagram never receives a fix. */
final class LocationGuard {
    private LocationGuard() { }

    static void install() {
        Hooks.Hook off = new Hooks.Hook() {
            @Override protected void before(Hooks.Call param) {
                if (Prefs.on(Prefs.BLOCK_LOCATION)) param.setResult(null);
            }
        };
        Hooks.Hook denied = new Hooks.Hook() {
            @Override protected void before(Hooks.Call param) {
                if (Prefs.on(Prefs.BLOCK_LOCATION)) param.setResult(false);
            }
        };
        int hooks = 0;
        for (String name : new String[]{"requestLocationUpdates", "requestSingleUpdate", "getLastKnownLocation"}) {
            hooks += Hooks.hookAll(LocationManager.class, name, off);
        }
        for (String name : new String[]{"addGpsStatusListener", "registerGnssStatusCallback",
                "isLocationEnabled", "isProviderEnabled"}) {
            hooks += Hooks.hookAll(LocationManager.class, name, denied);
        }
        Hooks.log("location guard: hooks=" + hooks);
    }
}
