package nz.co.logicons.tlp.mobile.stobyapp.domain.util;

/*
 * @author by Allen
 */
public interface DomainMapper<T, DomainModel> {
    DomainModel mapToDomainModel(T model);

    T mapFromDomainModel(DomainModel domainModel);
}
