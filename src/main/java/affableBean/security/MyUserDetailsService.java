package affableBean.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import affableBean.domain.Member;
//import affableBean.domain.Privilege;
import affableBean.domain.Role;
import affableBean.repository.MemberRepository;
import affableBean.repository.RoleRepository;

@Service("userDetailsService")
@Transactional
public class MyUserDetailsService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MemberRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private HttpServletRequest request;

    public MyUserDetailsService() {
        super();
    }

    // API

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        logger.info("In MyUserDetailsService#loadUserByUsername ");
        final String ip = request.getRemoteAddr();
        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("blocked");
        }

        try {
            logger.info("In MyUserDetailsService#loadUserByUsername, about to findOneByEmail ");
            final Member user = userRepository.findOneByEmail(email);
            logger.info("In MyUserDetailsService#loadUserByUsername, done with findOneByEmail: user " + user.toString());
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            if (user == null) {
            	return new User(" ", " ", authorities);
                // return new org.springframework.security.core.userdetails.User(" ", " ", true, true, true, true, getAuthorities(Arrays.asList(roleRepository.findByName("ROLE_USER"))));
            }
            logger.info("In MyUserDetailsService#loadUserByUsername, done with findOneByEmail: user.roles " + user.getRoles().toString());

            if (user.hasRole("ROLE_ADMIN"))
            	authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        	return new User(user.getEmail(), user.getPassword(), authorities);
//            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getEnabled(), true, true, true, (Collection<? extends GrantedAuthority>) Arrays.asList(user.getRole()));	//getAuthorities(user.getRoles()));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    // UTIL

    public final Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        logger.info("In MyUserDetailsService#getAuthorities ");
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private final List<String> getPrivileges(final Collection<Role> roles) {
        final List<String> privileges = new ArrayList<String>();
        final List<Role> collection = new ArrayList<Role>();
//        final List<Privilege> collection = new ArrayList<Privilege>();
        logger.info("In MyUserDetailsService#getPrivileges ");
        for (final Role role : roles) {
        	collection.add(role);
//            collection.addAll(role.getPrivileges());
        }
        for (final Role item : collection) {
            privileges.add(item.getName());
        }
//        for (final Privilege item : collection) {
//            privileges.add(item.getName());
//        }
        return privileges;
    }

    private final List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        logger.info("In MyUserDetailsService#getGrantedAuthorities ");
        for (final String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

}
