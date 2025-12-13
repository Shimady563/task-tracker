package com.shimady.auth.converter;

import com.shimady.auth.model.Group;
import com.shimady.auth.model.User;
import com.shimady.auth.model.dto.SignUpJwtRequest;
import com.shimady.auth.model.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

public class AuthConverter {
    public static UserResponse domain2Response(User user) {
        return AuthMapper.INSTANCE.domain2Response(user);
    }

    public static User signUpRequest2Domain(SignUpJwtRequest signUpJwtRequest) {
        return AuthMapper.INSTANCE.signUpRequest2Domain(signUpJwtRequest);
    }

    @Mapper
    public interface AuthMapper {
        AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "role", ignore = true)
        @Mapping(target = "group", ignore = true)
        @Mapping(target = "authorities", ignore = true)
        User signUpRequest2Domain(SignUpJwtRequest signUpJwtRequest);

        @Mapping(source = "group", target = "groupName")
        @Mapping(source = "group", target = "groupId")
        UserResponse domain2Response(User user);

        default String group2String(Group group) {
            return group == null ? "Teacher" : group.getName();
        }

        default Long group2Long(Group group) {
            return group == null ? -1L : group.getId();
        }
    }
}
