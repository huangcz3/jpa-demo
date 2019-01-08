package com.example.jpa.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author Huangcz
 * @date 2018-11-12 11:55
 * @desc druid + mybatis 配置
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.example.jpa.mapper", sqlSessionFactoryRef = "primarySqlSessionFactory")
public class DruidConfiguration {


	private static final Logger logger = LoggerFactory.getLogger(DruidConfiguration.class);

	@Primary
	@Bean(initMethod = "init",destroyMethod = "close",name = "primaryDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.primary")
	public DataSource dataSource() {
		logger.info("= = = = = DruidDataSource启动 = = = = =");
		return DruidDataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = "primarySqlSessionFactory")
	public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("primaryDataSource") DataSource dataSource)
			throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		/** 设置mybatis configuration 扫描路径 */
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("/mybatis/mybatis-config.xml"));
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/*.xml"));
		SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
		sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
		return sqlSessionFactory;
	}


	@Primary
	@Bean(name = "primarySqlSessionTemplate")
	public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Primary
	@Bean(name = "primaryTransactionManager")
	public DataSourceTransactionManager transactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}


	@Bean
	public ServletRegistrationBean druidStatViewServlet() {
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
		servletRegistrationBean.setServlet(new StatViewServlet());
		servletRegistrationBean.addUrlMappings("/druid/*");

//
//		ServletRegistrationBean servletRegistrationBean =
//				new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
		// 添加初始化参数：initParams
		// 白名单，如果不配置或value为空，则允许所有
		// servletRegistrationBean.addInitParameter(“allow","127.0.0.1,192.0.0.1″);
		// 黑名单，与白名单存在相同IP时，优先于白名单
		// servletRegistrationBean.addInitParameter(“deny","192.0.0.1″);

		// 用户名
		servletRegistrationBean.addInitParameter("loginUsername", "admin");
		// 密码
		servletRegistrationBean.addInitParameter("loginPassword", "123456");
		// 禁用页面上的“Reset All"功能
		servletRegistrationBean.addInitParameter("resetEnable", "false");
		return servletRegistrationBean;
	}


	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
		// 过滤规则
		filterRegistrationBean.addUrlPatterns("/*");
		// 忽略资源
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid2/*");
		return filterRegistrationBean;
	}


	/**
	 * 按照BeanId来拦截配置 用来bean的监控
	 *
	 * @return
	 */
	@Bean(value = "druid-stat-interceptor")
	public DruidStatInterceptor DruidStatInterceptor() {
		DruidStatInterceptor druidStatInterceptor = new DruidStatInterceptor();
		return druidStatInterceptor;
	}

	@Bean
	public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
		BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
		beanNameAutoProxyCreator.setProxyTargetClass(true);
		// 设置要监控的bean的id
		//beanNameAutoProxyCreator.setBeanNames("sysRoleMapper","loginController");
		beanNameAutoProxyCreator.setInterceptorNames("druid-stat-interceptor");
		return beanNameAutoProxyCreator;
	}


}
