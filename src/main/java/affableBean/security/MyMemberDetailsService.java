//package affableBean.security;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import affableBean.domain.Member;
////import affableBean.domain.Privilege;
//import affableBean.domain.Role;
//import affableBean.repository.MemberRepository;
//import affableBean.repository.RoleRepository;
//
//@Service("memberDetailsService")
//@Transactional
//public class MyMemberDetailsService implements UserDetailsService {
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private LoginAttemptService loginAttemptService;
//
//    @Autowired
//    private HttpServletRequest request;
//
//    public MyMemberDetailsService() {
//        super();
//    }
//
//    // API
//
//    @Override
//    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
//        final String ip = request.getRemoteAddr();
//        if (loginAttemptService.isBlocked(ip)) {
//            throw new RuntimeException("blocked");
//        }
//
//        try {
//            final Member member = memberRepository.findByEmail(email);
//            if (member == null) {
//                return new org.springframework.security.core.userdetails.User(" ", " ", true, true, true, true, getAuthorities(Arrays.asList(roleRepository.findByName("ROLE_USER"))));
//            }
//
//            return new org.springframework.security.core.userdetails.User(member.getEmail(), member.getPassword(), member.isEnabled(), true, true, true, getAuthorities(member.getRoles()));
//        } catch (final Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    // UTIL
//
//    public final Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
//        return getGrantedAuthorities(getPrivileges(roles));
//    }
//
//    private final List<String> getPrivileges(final Collection<Role> roles) {
//        final List<String> privileges = new ArrayList<String>();
//        final List<Privilege> collection = new ArrayList<Privilege>();
//        for (final Role role : roles) {
//            collection.addAll(role.getPrivileges());
//        }
//        for (final Privilege item : collection) {
//            privileges.add(item.getName());
//        }
//        return privileges;
//    }
//
//    private final List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
//        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//        for (final String privilege : privileges) {
//            authorities.add(new SimpleGrantedAuthority(privilege));
//        }
//        return authorities;
//    }
//
//}
