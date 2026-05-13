package ru.mephi.trainer.mapper;

import org.mapstruct.Mapper;
import ru.mephi.trainer.models.UserProfile;
import ru.mephi.trainer.rest.dto.response.profile.ProfileResponse;

@Mapper
public interface ProfileMapper {

    ProfileResponse toProfileResponse(UserProfile userProfile);
}
