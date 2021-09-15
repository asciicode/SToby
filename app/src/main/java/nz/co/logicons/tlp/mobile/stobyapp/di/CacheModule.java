package nz.co.logicons.tlp.mobile.stobyapp.di;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import nz.co.logicons.tlp.mobile.stobyapp.cache.ManifestDao;
import nz.co.logicons.tlp.mobile.stobyapp.cache.UserDao;
import nz.co.logicons.tlp.mobile.stobyapp.cache.database.AppDatabase;
import nz.co.logicons.tlp.mobile.stobyapp.cache.model.ManifestEntityMapper;
import nz.co.logicons.tlp.mobile.stobyapp.cache.model.UserEntityMapper;

@Module
@InstallIn(SingletonComponent.class)
public class CacheModule {
    @Singleton
    @Provides
    AppDatabase provideDb(BaseApplication app) {
        return Room.databaseBuilder(app, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    UserDao provideUserDao(AppDatabase db){
        return db.userDao();
    }

    @Singleton
    @Provides
    UserEntityMapper provideCacheUserMapper(){
        return new UserEntityMapper();
    }

    @Singleton
    @Provides
    ManifestDao provideManifestDao(AppDatabase db){
        return db.manifestDao();
    }

    @Singleton
    @Provides
    ManifestEntityMapper provideCacheManifestMapper(){
        return new ManifestEntityMapper();
    }
}