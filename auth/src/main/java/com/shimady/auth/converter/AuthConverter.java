package com.shimady.auth.converter;

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
        @Mapping(target = "authorities", ignore = true)
        User signUpRequest2Domain(SignUpJwtRequest signUpJwtRequest);

        UserResponse domain2Response(User user);
    }
}
