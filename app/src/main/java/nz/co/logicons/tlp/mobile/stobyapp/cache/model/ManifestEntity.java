package nz.co.logicons.tlp.mobile.stobyapp.cache.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
 * @author by Allen
 */
@Entity
public class ManifestEntity {
    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @PrimaryKey
    @NonNull
    private String id;
}
