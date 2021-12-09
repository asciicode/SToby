package nz.co.logicons.tlp.mobile.stobyapp.cache.model;

import java.util.ArrayList;
import java.util.List;

import nz.co.logicons.tlp.mobile.stobyapp.domain.model.ManifestItem;
import nz.co.logicons.tlp.mobile.stobyapp.domain.util.DomainMapper;

/*
 * @author by Allen
 */
public class ManifestItemEntityMapper implements DomainMapper<ManifestItemEntity, ManifestItem> {
    @Override
    public ManifestItem mapToDomainModel(ManifestItemEntity model) {
        return new ManifestItem(
                model.getItemMovementId(),
                model.getManifestId(),
                model.getMovementId(),
                model.getJobId(),
                model.getItemId(),
                model.getBarCode(),
                model.getProductId(),
                model.getCustomerId(),
                model.isLoaded(),
                model.getItemIndex()
        );
    }

    @Override
    public ManifestItemEntity mapFromDomainModel(ManifestItem manifestItem) {
        return new ManifestItemEntity(
                manifestItem.getItemMovementId(),
                manifestItem.getManifestId(),
                manifestItem.getMovementId(),
                manifestItem.getJobId(),
                manifestItem.getItemId(),
                manifestItem.getBarCode(),
                manifestItem.getProductId(),
                manifestItem.getCustomerId(),
                manifestItem.isLoaded(),
                manifestItem.getItemIndex()
        );
    }

    public List<ManifestItem> toDomainList(List<ManifestItemEntity> manifestItemEntities){
        List<ManifestItem> list = new ArrayList<>();
        for (ManifestItemEntity rec : manifestItemEntities){
            list.add(mapToDomainModel(rec));
        }
        return list;
    }
}
