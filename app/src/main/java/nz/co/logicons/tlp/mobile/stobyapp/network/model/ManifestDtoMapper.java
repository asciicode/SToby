package nz.co.logicons.tlp.mobile.stobyapp.network.model;

import java.util.ArrayList;
import java.util.List;

import nz.co.logicons.tlp.mobile.stobyapp.domain.model.Manifest;
import nz.co.logicons.tlp.mobile.stobyapp.domain.util.DomainMapper;

/*
 * @author by Allen
 */
public class ManifestDtoMapper implements DomainMapper<ManifestDto, Manifest> {
    @Override
    public Manifest mapToDomainModel(ManifestDto model) {
        return new Manifest(
                model.getId(), model.isAllocated(), model.getService(),
                model.getWorkType(), model.getDriver(),
                model.getVehicle(), model.getTrailer1(), model.getTrailer2(),
                model.getTrailer3(), model.getFrom(), model.getTo(),
                model.isStoreLoad()
        );
    }

    @Override
    public ManifestDto mapFromDomainModel(Manifest domainModel) {
        return new ManifestDto(
                domainModel.getId(), domainModel.isAllocated(), domainModel.getService(),
                domainModel.getWorkType(), domainModel.getDriver(),
                domainModel.getVehicle(), domainModel.getTrailer1(), domainModel.getTrailer2(),
                domainModel.getTrailer3(), domainModel.getFrom(), domainModel.getTo(),
                domainModel.isStoreLoad()
        );
    }

    public List<Manifest> toDomainList(List<ManifestDto> manifestDtos) {
        List<Manifest> list = new ArrayList<>();
        for (ManifestDto rec : manifestDtos) {
            list.add(mapToDomainModel(rec));
        }
        return list;
    }

    public List<ManifestDto> fromDomainList(List<Manifest> manifests) {
        List<ManifestDto> list = new ArrayList<>();
        for (Manifest rec : manifests) {
            list.add(mapFromDomainModel(rec));
        }
        return list;
    }

}
