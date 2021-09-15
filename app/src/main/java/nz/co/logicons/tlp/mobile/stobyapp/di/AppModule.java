package nz.co.logicons.tlp.mobile.stobyapp.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import nz.co.logicons.tlp.mobile.stobyapp.util.PreferenceKeys;

/*
 * @author by Allen
 */
@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    @Singleton
    @Provides
    BaseApplication provideApplication(@ApplicationContext Context app) {
        return (BaseApplication) app;
    }

    @Singleton
    @Provides
    SharedPreferences.Editor provideSharedPrefsEditor(SharedPreferences sharedPreferences) {
        return sharedPreferences.edit();
    }

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences(Application application) {
        return application.getSharedPreferences(
                PreferenceKeys.STOBY_PREFERENCES,
                Context.MODE_PRIVATE
        );
    }

}
