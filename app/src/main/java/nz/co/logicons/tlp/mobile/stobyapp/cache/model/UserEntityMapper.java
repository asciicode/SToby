package nz.co.logicons.tlp.mobile.stobyapp.cache.model;

import nz.co.logicons.tlp.mobile.stobyapp.domain.model.User;
import nz.co.logicons.tlp.mobile.stobyapp.domain.util.DomainMapper;

/*
 * @author by Allen
 */
public class UserEntityMapper implements DomainMapper<UserEntity, User> {
    @Override
    public User mapToDomainModel(UserEntity model) {
        return new User(model.getUsername(), model.getPassword());
    }

    @Override
    public UserEntity mapFromDomainModel(User user) {
        return new UserEntity(user.getUsername(), user.getPassword());
    }
}
