package nz.co.logicons.tlp.mobile.stobyapp.network.model;

import nz.co.logicons.tlp.mobile.stobyapp.domain.model.ActionManifestItem;
import nz.co.logicons.tlp.mobile.stobyapp.domain.util.DomainMapper;

/*
 * @author by Allen
 */
public class ActionManifestItemDtoMapper implements DomainMapper<ActionManifestItemDto, ActionManifestItem> {

    @Override
    public ActionManifestItem mapToDomainModel(ActionManifestItemDto model) {
        return new ActionManifestItem(
                model.getAction(),
                model.getManifestId(),
                model.getBarcode(),
                model.getJobId(),
                model.getJobItemId()
        );
    }

    @Override
    public ActionManifestItemDto mapFromDomainModel(ActionManifestItem actionManifestItem) {
        return new ActionManifestItemDto(
                actionManifestItem.getAction(),
                actionManifestItem.getManifestId(),
                actionManifestItem.getBarcode(),
                actionManifestItem.getJobId(),
                actionManifestItem.getJobItemId()
        );
    }
}
