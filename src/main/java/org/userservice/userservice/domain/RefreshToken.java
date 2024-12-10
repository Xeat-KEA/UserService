package org.userservice.userservice.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 1000 * 60 * 60 * 24L) //24시간
@AllArgsConstructor
@Getter
@ToString
public class RefreshToken {
    @Id
    private String id;
    private String refreshToken;
}
