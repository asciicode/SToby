package nz.co.logicons.tlp.mobile.stobyapp.network.model;

import nz.co.logicons.tlp.mobile.stobyapp.domain.model.MakeManifestItem;
import nz.co.logicons.tlp.mobile.stobyapp.domain.util.DomainMapper;

/*
 * @author by Allen
 */
public class MakeManifestItemDtoMapper implements DomainMapper<MakeManifestItemDto, MakeManifestItem> {

    @Override
    public MakeManifestItem mapToDomainModel(MakeManifestItemDto model) {
        return new MakeManifestItem(
                model.getAction(),
                model.getManifestId(),
                model.getBarcode(),
                model.getJobId(),
                model.getJobItemId()
        );
    }

    @Override
    public MakeManifestItemDto mapFromDomainModel(MakeManifestItem makeManifestItem) {
        return new MakeManifestItemDto(
                makeManifestItem.getAction(),
                makeManifestItem.getManifestId(),
                makeManifestItem.getBarcode(),
                makeManifestItem.getJobId(),
                makeManifestItem.getJobItemId()
        );
    }
}
