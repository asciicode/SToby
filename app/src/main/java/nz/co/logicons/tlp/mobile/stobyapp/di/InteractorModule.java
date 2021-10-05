package nz.co.logicons.tlp.mobile.stobyapp.di;

import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.scopes.ViewModelScoped;
import nz.co.logicons.tlp.mobile.stobyapp.cache.ManifestDao;
import nz.co.logicons.tlp.mobile.stobyapp.cache.ManifestItemDao;
import nz.co.logicons.tlp.mobile.stobyapp.cache.UserDao;
import nz.co.logicons.tlp.mobile.stobyapp.cache.model.ManifestEntityMapper;
import nz.co.logicons.tlp.mobile.stobyapp.cache.model.ManifestItemEntityMapper;
import nz.co.logicons.tlp.mobile.stobyapp.cache.model.UserEntityMapper;
import nz.co.logicons.tlp.mobile.stobyapp.network.RetroApiMakeManifestItemClient;
import nz.co.logicons.tlp.mobile.stobyapp.network.RetroApiManifestClient;
import nz.co.logicons.tlp.mobile.stobyapp.network.RetroApiManifestItemClient;
import nz.co.logicons.tlp.mobile.stobyapp.network.RetroApiUserClient;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.ActionManifestItemDtoMapper;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.ManifestDtoMapper;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.ManifestItemDtoMapper;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.UserDtoMapper;

/*
 * @author by Allen
 */
@Module
@InstallIn(ViewModelComponent.class)
public class InteractorModule {
    @ViewModelScoped
    @Provides
    RetroApiUserClient provideRetroApiUserClient(SharedPreferences sharedPreferences,
            UserDtoMapper userDtoMapper, UserDao userDao, UserEntityMapper userEntityMapper) {
        return new RetroApiUserClient(sharedPreferences, userDtoMapper, userDao, userEntityMapper);
    }

    @ViewModelScoped
    @Provides
    RetroApiManifestClient provideRetroApiManifestClient(SharedPreferences sharedPreferences,
            ManifestDtoMapper manifestDtoMapper, ManifestDao manifestDao,
            ManifestEntityMapper manifestEntityMapper){
        return new RetroApiManifestClient(sharedPreferences, manifestDtoMapper, manifestDao,
                manifestEntityMapper);
    }

    @ViewModelScoped
    @Provides
    RetroApiManifestItemClient provideRetroApiManifestItemClient(SharedPreferences sharedPreferences,
            ManifestItemDtoMapper manifestItemDtoMapper, ManifestItemDao manifestItemDao,
            ManifestItemEntityMapper manifestItemEntityMapper){
        return new RetroApiManifestItemClient(sharedPreferences, manifestItemDtoMapper,
                manifestItemDao, manifestItemEntityMapper);
    }

    @ViewModelScoped
    @Provides
    RetroApiMakeManifestItemClient provideRetroApiMakeManifestItemClient(SharedPreferences sharedPreferences,
            ManifestItemDtoMapper manifestItemDtoMapper, ManifestItemDao manifestItemDao,
            ManifestItemEntityMapper manifestItemEntityMapper,
            ActionManifestItemDtoMapper actionManifestItemDtoMapper){
        return new RetroApiMakeManifestItemClient(sharedPreferences, manifestItemDtoMapper,
                manifestItemDao, manifestItemEntityMapper, actionManifestItemDtoMapper);
    }
}
