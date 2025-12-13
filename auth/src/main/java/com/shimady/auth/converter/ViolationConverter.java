package com.shimady.auth.converter;

import com.shimady.auth.exception.ValidationError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.validation.FieldError;

public class ViolationConverter {
    public static ValidationError.Violation constraintViolation2Violation(ConstraintViolation<?> violation) {
        return ViolationMapper.INSTANCE.constraintViolation2Violation(violation);
    }

    public static ValidationError.Violation fieldError2Violation(FieldError error) {
        return ViolationMapper.INSTANCE.fieldError2Violation(error);
    }

    @Mapper
    public interface ViolationMapper {
        ViolationMapper INSTANCE = Mappers.getMapper(ViolationMapper.class);

        @Mapping(source = "propertyPath", target = "field")
        ValidationError.Violation constraintViolation2Violation(ConstraintViolation<?> violation);

        @Mapping(source = "defaultMessage", target = "message")
        ValidationError.Violation fieldError2Violation(FieldError error);

        default String path2String(Path path) {
            return path == null ? null : path.toString();
        }
    }
}
