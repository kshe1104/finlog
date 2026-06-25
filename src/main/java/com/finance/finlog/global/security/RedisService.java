package com.finance.finlog.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String,String> redisTemplate;

    private static final String BLACKLIST_PREFIX = "blacklist:";

    // 블랙리스트 토큰 추가
    public void addToBlacklist(String token, long expirationMillis) {
        redisTemplate.opsForValue().set(
                BLACKLIST_PREFIX + token,
                "logout",
                expirationMillis,
                TimeUnit.MILLISECONDS
                );
    }

    // 블랙리스트에 토큰이 있는지 확인
    // redisTemplate.haskey(key)==true => NullpointException 이슈 발생 가능
    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(
                redisTemplate.hasKey(BLACKLIST_PREFIX + token)
        );
    }

    // 캐시 저장
    public void set(String key, String value, long expirationMillis) {
        redisTemplate.opsForValue().set(
                key, value, expirationMillis, TimeUnit.MILLISECONDS
        );
    }
    // 캐시 조회
    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    // 캐시 삭제
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    // 캐시 존재 여부
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

}
