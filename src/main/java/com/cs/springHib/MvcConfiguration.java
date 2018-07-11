package com.cs.springHib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


import org.apache.tomcat.jdbc.pool.*;
import javax.sql.DataSource;


@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter{

	@Autowired
    private Environment env;
	
	
	
	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		final String ip = System.getenv("IP"); 
		final String port = System.getenv("PORT");
		final String URL = "jdbc:mysql://"+ip+":"+port+"/apidb";

        org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl(URL);
        ds.setUsername(env.getProperty("jdbc.user.name"));
        ds.setPassword(env.getProperty("jdbc.user.passwd"));
        ds.setInitialSize(Integer.parseInt(env.getProperty("jdbc.pool.initialSize")));
        ds.setMaxActive(Integer.parseInt(env.getProperty("jdbc.pool.maxActive")));
        ds.setMaxIdle(Integer.parseInt(env.getProperty("jdbc.pool.maxIdle")));
        ds.setMinIdle(Integer.parseInt(env.getProperty("jdbc.pool.minIdle")));
        
        ds.setValidationQuery("select 1");
        ds.setTestOnBorrow(true);
        ds.setValidationInterval(5000);
        ds.setRemoveAbandoned(true);
        ds.setRemoveAbandonedTimeout(55);
        return ds;
    }
	
	
	 // To be used for small and single line queries where writing entire JDBC TEmplate and mapper is too cumbersome.
	@Bean
	public JdbcTemplate jdbcTemplateObj() {
		return new JdbcTemplate(dataSource());
	}
	
}
