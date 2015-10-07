package affableBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationSuccessHandler myAuthenticationSuccessHandler;


	// @Bean
	// public DaoAuthenticationProvider authProvider() {
	// final DaoAuthenticationProvider authProvider = new
	// DaoAuthenticationProvider();
	// authProvider.setUserDetailsService(userDetailsService);
	// authProvider.setPasswordEncoder(encoder());
	// return authProvider;
	// }

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
		.authorizeRequests()
        	.antMatchers("/checkout", "/purchase").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
    		.antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
        	.anyRequest().permitAll()
        .and()
        	.formLogin().loginPage("/login")
        	.failureUrl("/login?error").permitAll()
        	.defaultSuccessUrl("/")
        .and()
        	.logout()
               .deleteCookies("remove")
               .invalidateHttpSession(true)
               .logoutUrl("/admin/logout")
               .logoutSuccessUrl("/login?logout");
    }
	
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

//        JdbcUserDetailsManager userDetailsService = new JdbcUserDetailsManager();
//        userDetailsService.setDataSource(datasource);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
 
        auth
        	.jdbcAuthentication()
        	.dataSource(datasource)
        	.usersByUsernameQuery("select username, password, enabled from member where username=?")
        	.authoritiesByUsernameQuery("select m.username, 'ROLE_ADMIN' from member m, role r, role_members rm where m.username=? and rm.member_id = m.id and rm.role_id = r.id")
//        	.authoritiesByUsernameQuery("select member.username, role.name from member join role on member.role_id = role.id where username=?")
//        	.userDetailsService(userDetailsService)	// from baeldung... incompatible with jdbc authentication
        	.passwordEncoder(encoder());
        
        
        // adding two members for testing purposes
        Role userRole = roleRepo.findByName("ROLE_USER");
        Role adminRole = roleRepo.findByName("ROLE_ADMIN");
        
        Member adminMember = memberRepo.findOneByUsername("admin");
        Member userMember = memberRepo.findOneByUsername("user");
 
        if(userMember == null) {
            memberRepo.saveAndFlush(new Member("user", "user@user.com", "user@user.com", encoder.encode("user"), true, new ArrayList<>(Arrays.asList(userRole))));
        }
        
        if(adminMember == null) {
            memberRepo.saveAndFlush(new Member("admin", "admin@admin.com", "admin@admin.com", encoder.encode("admin"), true, new ArrayList<>(Arrays.asList(adminRole))));
        }
        
    }

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}
}