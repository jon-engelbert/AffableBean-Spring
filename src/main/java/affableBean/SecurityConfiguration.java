package affableBean;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
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
    
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.authorizeRequests().antMatchers("/admin/**").hasAuthority("ADMIN")
        	.anyRequest().permitAll()
        .and()
        	.formLogin().loginPage("/admin/login")
        	.failureUrl("/admin/login?error").permitAll().defaultSuccessUrl("/admin")
//        	.formLogin().loginPage("/front_store/memberlogin")
//        	.failureUrl("/front_store/memberlogin?error").permitAll().defaultSuccessUrl("/admin")
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
        
        Member adminMember = memberRepo.findOneByUsername("admin");
        Member userMember = memberRepo.findOneByUsername("user");
 
        if(userMember == null) {
            memberRepo.saveAndFlush(new Member("user", "user@user.com", "user@user.com", encoder.encode("user"), true, userRole));
        }
        
        if(adminMember == null) {
            memberRepo.saveAndFlush(new Member("admin", "admin@admin.com", "admin@admin.com", encoder.encode("admin"), true, adminRole));
        }
        
    }

}