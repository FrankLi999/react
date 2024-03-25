package com.bpwizard.configjdbc.core.security.dto;

import com.bpwizard.configjdbc.core.security.userstore.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    private String id;
    private String email;
    private String password;
    private String name;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String roles[];
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private String sessionId = "n/a";
    private long expireIn;

    public static UserProfile fromUserAndToken(User<Long> user) {
        return UserProfile.fromUserAndToken(user, null, 0, null);
    }
    public static UserProfile fromUserAndToken(User<Long> user, String accessToken, long expireIn, String sessionId) {
        UserProfile userProfile = new UserProfile();
        userProfile.setAccessToken(accessToken);
        userProfile.setId(user.getId().toString());
        userProfile.setEmail(user.getEmail());
        userProfile.setImageUrl(user.getImageUrl());
        userProfile.setName(user.getName());
        userProfile.setFirstName(user.getFirstName());
        userProfile.setLastName(user.getLastName());
        userProfile.setExpireIn(expireIn);
        userProfile.setSessionId(sessionId);
        userProfile.setRoles(user.getRoles().stream().toArray(String[]::new));
        return userProfile;
    }
}
