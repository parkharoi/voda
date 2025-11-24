package org.delivery.voda.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
  private final Key key;
  private final long validityInMilliseconds;

  public JwtTokenProvider(@Value("${jwt.secret}") String secretkey,
                          @Value("${jwt.expiration}") long validityInMilliseconds)  {
    this.key = Keys.hmacShaKeyFor(secretkey.getBytes());
    this.validityInMilliseconds = validityInMilliseconds;
  }

  public String createToken(String email, String role) {
    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()
        .setSubject(email) //토큰의 주인(이메일)
        .claim("role", role) //사용자 권한 정보 담기
        .setIssuedAt(now) //발행시간
        .setExpiration(validity) //만료시간
        .signWith(key, SignatureAlgorithm.HS256) //암호화 알고리즘
        .compact();
  }

  //Todo : 검증로직

}
