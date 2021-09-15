package nz.co.logicons.tlp.mobile.stobyapp.cache.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import nz.co.logicons.tlp.mobile.stobyapp.cache.ManifestDao;
import nz.co.logicons.tlp.mobile.stobyapp.cache.UserDao;
import nz.co.logicons.tlp.mobile.stobyapp.cache.model.ManifestEntity;
import nz.co.logicons.tlp.mobile.stobyapp.cache.model.UserEntity;

/*
 * @author by Allen
 */
@Database(entities = {UserEntity.class, ManifestEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "stoby_db";

    public abstract UserDao userDao();

    public abstract ManifestDao manifestDao();
}
