package affableBean;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import affableBean.domain.Member;
//import affableBean.domain.Privilege;
import affableBean.domain.Role;
import affableBean.repository.MemberRepository;
//import affableBean.repository.PrivilegeRepository;
import affableBean.repository.RoleRepository;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private MemberRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

//    @Autowired
//    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // == create initial privileges
//        final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
//        final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        // == create initial roles
//        final List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        final Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
        final Role userRole = createRoleIfNotFound("ROLE_USER");

//        final Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        final Member user = new Member();
        user.setName("Test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("test@test.com");
//        user.setRole(adminRole);
        user.setRoles(Arrays.asList(adminRole, userRole));
        user.setEnabled(true);
        userRepository.save(user);

        alreadySetup = true;
    }

//    @Transactional
//    private final Privilege createPrivilegeIfNotFound(final String name) {
//        Privilege privilege = privilegeRepository.findByName(name);
//        if (privilege == null) {
//            privilege = new Privilege(name);
//            privilegeRepository.save(privilege);
//        }
//        return privilege;
//    }

    @Transactional
    private final Role createRoleIfNotFound(final String name) { // , final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name, new HashSet<Member>());
//            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

}