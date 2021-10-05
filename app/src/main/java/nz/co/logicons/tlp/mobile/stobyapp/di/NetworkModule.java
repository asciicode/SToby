package nz.co.logicons.tlp.mobile.stobyapp.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.ActionManifestItemDtoMapper;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.ManifestDtoMapper;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.ManifestItemDtoMapper;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.UserDtoMapper;

/*
 * @author by Allen
 */
@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {
//    @Singleton
//    @Provides
//    RetroApiService provideManifestService(SharedPreferences sharedPreferences) {
//        return new Retrofit.Builder()
//                .baseUrl(sharedPreferences.getString(PreferenceKeys.BASE_URL,""))
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//                .create(RetroApiService.class);
//    }

    @Singleton
    @Provides
    UserDtoMapper provideUserDtoMapper() {
        return new UserDtoMapper();
    }

    @Singleton
    @Provides
    ManifestDtoMapper provideManifestDtoMapper() {
        return new ManifestDtoMapper();
    }

    @Singleton
    @Provides
    ManifestItemDtoMapper provideManifestItemDtoMapper() {
        return new ManifestItemDtoMapper();
    }

    @Singleton
    @Provides
    ActionManifestItemDtoMapper provideActionManifestItemDtoMapper() {
        return new ActionManifestItemDtoMapper();
    }
}
