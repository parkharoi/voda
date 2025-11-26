package org.delivery.voda.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {
  private final Key key;
  private final long validityInMilliseconds;
  private final UserDetailsService userDetailsService;

  public JwtTokenProvider(@Value("${jwt.secret}") String secretkey,
                          @Value("${jwt.expiration}") long validityInMilliseconds,
                          @Lazy UserDetailsService userDetailsService)  {
    byte[] keyBytes = Decoders.BASE64URL.decode(secretkey);
    this.key = Keys.hmacShaKeyFor(secretkey.getBytes());
    this.validityInMilliseconds = validityInMilliseconds;

    this.userDetailsService = userDetailsService;
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

  //토큰에서 인증 정보 조회
  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserEmail(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUserEmail(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build()
        .parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
      log.info("잘못된 JWT 서명입니다.");
    }catch (ExpiredJwtException e) {
      log.info("만료된 JWT 토큰입니다.");
    }catch (UnsupportedJwtException e) {
      log.info("지원하지 않는 JWT 토큰입니다.");
    }catch (IllegalArgumentException e) {
      log.info("JWT 토큰이 잘못되었습니다.");
    }
    return false;
  }

}
