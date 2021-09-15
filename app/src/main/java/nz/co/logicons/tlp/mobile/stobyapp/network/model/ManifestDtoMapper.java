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
        Manifest ret = new Manifest();
        ret.setId(model.getId());
        return ret;
    }

    @Override
    public ManifestDto mapFromDomainModel(Manifest model) {
        ManifestDto ret = new ManifestDto();
        ret.setId(model.getId());
        return ret;
    }

    public List<Manifest> toDomainList(List<ManifestDto> manifestDtos){
        List<Manifest> list = new ArrayList<>();
        for (ManifestDto rec : manifestDtos){
            list.add(mapToDomainModel(rec));
        }
        return list;
    }

    public List<ManifestDto> fromDomainList(List<Manifest> manifests){
        List<ManifestDto> list = new ArrayList<>();
        for (Manifest rec : manifests){
            list.add(mapFromDomainModel(rec));
        }
        return list;
    }

}
