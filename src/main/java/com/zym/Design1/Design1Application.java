package com.zym.Design1;

import com.zym.Design1.util.ValidateCodeServlet;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan({"com.zym.Design1.dao", "com.zym.Design1.mydao"})
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass=true, exposeProxy=true)
@ServletComponentScan
public class Design1Application {

	public static void main(String[] args) {
		SpringApplication.run(Design1Application.class, args);
	}

	/*@Bean
	public ServletRegistrationBean testServletRegistration() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new ValidateCodeServlet());
		registration.addUrlMappings("/validateCode");
		return registration;
	}*/

}
