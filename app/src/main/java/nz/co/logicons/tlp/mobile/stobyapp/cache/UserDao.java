package nz.co.logicons.tlp.mobile.stobyapp.cache;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import nz.co.logicons.tlp.mobile.stobyapp.cache.model.UserEntity;

/*
 * @author by Allen
 */
@Dao
public interface UserDao {
    @Query("SELECT * FROM UserEntity where username = :userId ")
    UserEntity getUserEntity(String userId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(UserEntity userEntity);

    @Delete
    void delete(UserEntity userEntity);

    @Update
    void update(UserEntity userEntity);

}
