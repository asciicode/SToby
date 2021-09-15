package nz.co.logicons.tlp.mobile.stobyapp.network.model;

import nz.co.logicons.tlp.mobile.stobyapp.domain.model.User;
import nz.co.logicons.tlp.mobile.stobyapp.domain.util.DomainMapper;

/*
 * @author by Allen
 */
public class UserDtoMapper implements DomainMapper<UserDto, User> {
    @Override
    public User mapToDomainModel(UserDto model) {
        return new User(model.getUsername(), model.getPassword());
    }

    @Override
    public UserDto mapFromDomainModel(User user) {
        return new UserDto(user.getUsername(), user.getPassword());
    }
}
