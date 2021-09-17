package nz.co.logicons.tlp.mobile.stobyapp.network.model;

import java.util.ArrayList;
import java.util.List;

import nz.co.logicons.tlp.mobile.stobyapp.domain.model.ManifestItem;
import nz.co.logicons.tlp.mobile.stobyapp.domain.util.DomainMapper;

/*
 * @author by Allen
 */
public class ManifestItemDtoMapper implements DomainMapper<ManifestItemDto, ManifestItem> {
    @Override
    public ManifestItem mapToDomainModel(ManifestItemDto model) {
        return new ManifestItem(
                model.getManifestId(),
                model.getMovementId(),
                model.getJobId(),
                model.getItemId(),
                model.getBarCode(),
                model.getProductId(),
                model.getCustomerId(),
                model.isLoaded()
        );
    }

    @Override
    public ManifestItemDto mapFromDomainModel(ManifestItem manifestItem) {
        return new ManifestItemDto(
                manifestItem.getManifestId(),
                manifestItem.getMovementId(),
                manifestItem.getJobId(),
                manifestItem.getItemId(),
                manifestItem.getBarCode(),
                manifestItem.getProductId(),
                manifestItem.getCustomerId(),
                manifestItem.isLoaded()
        );
    }

    public List<ManifestItem> toDomainList(List<ManifestItemDto> manifestItemDtos){
        List<ManifestItem> list = new ArrayList<>();
        for (ManifestItemDto rec : manifestItemDtos){
            list.add(mapToDomainModel(rec));
        }
        return list;
    }

    public List<ManifestItemDto> fromDomainList(List<ManifestItem> manifestItems){
        List<ManifestItemDto> list = new ArrayList<>();
        for (ManifestItem rec : manifestItems){
            list.add(mapFromDomainModel(rec));
        }
        return list;
    }
}
