package io.github.llevague.jwt.services.security;

import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class UserService implements UserDetailsService {

    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();
    private final Map<String, User> userMap = new HashMap<String, User>() {{
        put("llevague", new User("llevague", "****", AuthorityUtils.createAuthorityList("ADMIN")));
    }};

    @Override
    public final User loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userMap.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        detailsChecker.check(user);
        return user;
    }
 }
