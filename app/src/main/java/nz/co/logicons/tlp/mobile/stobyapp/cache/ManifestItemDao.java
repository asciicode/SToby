package nz.co.logicons.tlp.mobile.stobyapp.cache;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import nz.co.logicons.tlp.mobile.stobyapp.cache.model.ManifestItemEntity;

/*
 * @author by Allen
 */
@Dao
public interface ManifestItemDao {
    @Query("SELECT * FROM ManifestItemEntity where manifestId = :manifestId ")
    List<ManifestItemEntity> getByManifestId(String manifestId);

    @Query("SELECT * FROM ManifestItemEntity where manifestId = :manifestId and barcode = :barcode ")
    ManifestItemEntity getManifestItemEntityByManifestIdAndBarcode(String manifestId,
            String barcode);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ManifestItemEntity manifestItemEntity);

//    @Delete
    @Query("DELETE FROM ManifestItemEntity where manifestId = :manifestId ")
    void deleteByManifestId(String manifestId);

    @Update
    void update(ManifestItemEntity manifestItemEntity);
}
