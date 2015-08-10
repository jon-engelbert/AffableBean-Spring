package affableBean;

import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
 
@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
 
	@Autowired
	private DataSource datasource;
    
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
        JdbcUserDetailsManager userDetailsService = new JdbcUserDetailsManager();
        userDetailsService.setDataSource(datasource);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
 
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
        auth
        	.jdbcAuthentication().dataSource(datasource).passwordEncoder(encoder);
 
        if(!userDetailsService.userExists("user")) {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            User userDetails = new User("user", encoder.encode("password"), authorities);
 
            userDetailsService.createUser(userDetails);
        }
        
        if(!userDetailsService.userExists("admin")) {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
            User userDetails = new User("admin", encoder.encode("admin"), authorities);
 
            userDetailsService.createUser(userDetails);
        }
        
    }

}