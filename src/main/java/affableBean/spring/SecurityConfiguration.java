package affableBean.spring;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import affableBean.domain.Member;
import affableBean.domain.Role;
import affableBean.repository.MemberRepository;
import affableBean.repository.RoleRepository;

 
@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
 
	@Autowired
	private DataSource datasource;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private MemberRepository memberRepo;
//    @Autowired
//    private UserDetailsService userDetailsService;

    
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.authorizeRequests().antMatchers("/admin/**").hasAuthority("ADMIN")
        	.anyRequest().permitAll()
        .and()
        	.formLogin().loginPage("/admin/login")
        	.failureUrl("/admin/login?error").permitAll().defaultSuccessUrl("/admin")
        .and()
        	.logout()
               .deleteCookies("remove")
               .invalidateHttpSession(true)
               .logoutUrl("/admin/logout")
               .logoutSuccessUrl("/admin/login?logout");

    }
	
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

//        JdbcUserDetailsManager userDetailsService = new JdbcUserDetailsManager();
//        userDetailsService.setDataSource(datasource);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
 
//        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
        auth
        	.jdbcAuthentication().dataSource(datasource)
        	.usersByUsernameQuery("select username, password, enabled from member where username=?")
        	.authoritiesByUsernameQuery("select member.username, role.name from member join role on member.role_id = role.id where username=?")
        	.passwordEncoder(encoder);
        
        
        // adding two members for testing purposes
        Role userRole = roleRepo.findByName("USER");
        Role adminRole = roleRepo.findByName("ADMIN");
        
        Member adminMember = memberRepo.findByEmail("admin@a.com");
        Member userMember = memberRepo.findByEmail("user@u.com");
 
        if(userMember == null) {
            Set<Role> userRoles = new HashSet<Role>();
            userRoles.add(userRole);
        	Member member = new Member("user", "user", "user@u.com", "user", encoder.encode("user"), true, false);
            member.setRoles(userRoles);
            memberRepo.saveAndFlush(member);
        }
        
        if(adminMember == null) {
            Set<Role> adminRoles = new HashSet<Role>();
            adminRoles.add(adminRole);
        	Member member = new Member("admin", "admin", "admin@a.com", "admin", encoder.encode("admin"), true, false);
            member.setRoles(adminRoles);
            memberRepo.saveAndFlush(member);

        }
        
    }

    // beans

//    @Bean
//    public DaoAuthenticationProvider authProvider() {
//        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(encoder());
//        return authProvider;
//    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }
}