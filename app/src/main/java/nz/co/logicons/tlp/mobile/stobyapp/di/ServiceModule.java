//package nz.co.logicons.tlp.mobile.stobyapp.di;
//
//import android.content.SharedPreferences;
//
//import javax.inject.Singleton;
//
//import dagger.Module;
//import dagger.Provides;
//import dagger.hilt.InstallIn;
//import dagger.hilt.components.SingletonComponent;
//import nz.co.logicons.tlp.mobile.stobyapp.cache.UserDao;
//import nz.co.logicons.tlp.mobile.stobyapp.cache.model.UserEntityMapper;
//import nz.co.logicons.tlp.mobile.stobyapp.network.model.UserDtoMapper;
//
///*
// * @author by Allen
// */
//@Module
//@InstallIn(SingletonComponent.class)
//public class ServiceModule {
//    @Singleton
//    @Provides
//    StobyFirebaseMessagingService provideFCM(){
//        return new StobyFirebaseMessagingService();
//    }
//    @Singleton
//    @Provides
//    RetroApiUserClient2 provideRetroApiUserClient2(SharedPreferences sharedPreferences,
//            UserDtoMapper userDtoMapper, UserDao userDao, UserEntityMapper userEntityMapper) {
//        return new RetroApiUserClient2(sharedPreferences, userDtoMapper, userDao, userEntityMapper);
//    }
//}
