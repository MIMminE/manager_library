package nuts.lib.manager.security_manager.authentication.token.jwt;

import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenService jwtTokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(JwtAuthenticationToken.class, authentication);

        String token = authentication.getCredentials().toString();
        String insertToken = token.split(" ")[1];
        List<? extends GrantedAuthority> grantedAuthority = jwtTokenService.getGrantedAuthority(insertToken);
        if (grantedAuthority.isEmpty()) throw new AuthenticationServiceException("This is not a valid token.");

        return JwtAuthenticationToken.authenticated(token, grantedAuthority);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
