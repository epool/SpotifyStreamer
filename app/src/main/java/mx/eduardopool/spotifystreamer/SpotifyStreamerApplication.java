package mx.eduardopool.spotifystreamer;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;

import io.fabric.sdk.android.Fabric;
import mx.eduardopool.spotifystreamer.ws.SpotifyWS;

/**
 * Custom class application.
 * Created by EduardoPool on 6/13/15.
 */
public class SpotifyStreamerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        SpotifyWS.init();

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build()
        );
    }

}
