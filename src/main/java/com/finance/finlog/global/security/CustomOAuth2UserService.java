package com.finance.finlog.global.security;

import com.finance.finlog.domain.user.entity.Provider;
import com.finance.finlog.domain.user.entity.Role;
import com.finance.finlog.domain.user.entity.User;
import com.finance.finlog.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)throws OAuth2AuthenticationException {

        // 상속, super.loadUser로
        // 구글,네이버 사용자 정보를 가져올 수 있다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // "google" or "naver"

        String providerId = extractProviderId(registrationId,oAuth2User);
        String email = extractEmail(registrationId,oAuth2User);
        String name = extractName(registrationId,oAuth2User);
        String profileImage = extractProfileImage(registrationId, oAuth2User);
        Provider provider = Provider.valueOf(registrationId.toUpperCase());

        // DB조회
        User user = userRepository.findByProviderAndProviderId(provider, providerId)
                .map(existingUser -> {
                    existingUser.updateProfile(name, profileImage); // 있으면 업데이트
                    return existingUser;
                })
                .orElse(User.builder()
                        .email(email)
                        .name(name)
                        .profileImageUrl(profileImage)
                        .provider(provider)
                        .providerId(providerId)
                        .role(Role.USER)
                        .build()); // 없으면 새로 생성
        // if/Else 보다 map().orElse() 패턴이 더 깔끔하다.
        userRepository.save(user);

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }

    private String extractProviderId(String registrationId, OAuth2User oAuth2User) {
        if ("naver".equals(registrationId)) {
            Map<String, Object> response = (Map<String, Object>)
                    oAuth2User.getAttributes().get("response");
            return (String) response.get("id");
        }
        return oAuth2User.getAttribute("sub"); // Google
    }

    private String extractEmail(String registrationId, OAuth2User oAuth2User) {
        if ("naver".equals(registrationId)) {
            Map<String, Object> response = (Map<String, Object>)
                    oAuth2User.getAttributes().get("response");
            return (String) response.get("email");
        }
        return oAuth2User.getAttribute("email"); // Google
    }

    private String extractName(String registrationId, OAuth2User oAuth2User) {
        if ("naver".equals(registrationId)) {
            Map<String, Object> response = (Map<String, Object>)
                    oAuth2User.getAttributes().get("response");
            return (String) response.get("name");
        }
        return oAuth2User.getAttribute("name"); // Google
    }

    private String extractProfileImage(String registrationId, OAuth2User oAuth2User) {
        if ("naver".equals(registrationId)) {
            Map<String, Object> response = (Map<String, Object>)
                    oAuth2User.getAttributes().get("response");
            return (String) response.get("profile_image");
        }
        return oAuth2User.getAttribute("picture"); // Google
    }
}

