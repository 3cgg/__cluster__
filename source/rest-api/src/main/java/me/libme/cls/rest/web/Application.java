package me.libme.cls.rest.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import me.libme.cls.rest.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;

/**
 * Created by J on 2016/3/9.
 */

@SpringBootApplication
@EnableCaching
@EnableScheduling
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = {
                "me[.]libme[.]spring[.]kernel[.]jpa[.].+"
                ,"me[.]libme[.]webboot[.]fn[.]jpa[.].+"
        //        ,"me[.]libme[.]webboot[.]fn[.]mongo[.].+"
        }),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) },
        basePackages={"me.libme"}
)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class
        , DataSourceTransactionManagerAutoConfiguration.class
        , HibernateJpaAutoConfiguration.class
        , MongoAutoConfiguration.class
        , MongoDataAutoConfiguration.class})
public class Application extends SpringBootServletInitializer implements 
EmbeddedServletContainerCustomizer,WebApplicationInitializer {
	
	@Autowired
	private AppConfig._Config config;
	
    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        
        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName+"->"+ctx.getBean(beanName).getClass().getName());
        }
        
        Environment environment=ctx.getEnvironment();
        int port=environment.getProperty("server.port",int.class);
        
        System.err.println("startup completely....\n"
        		+ "access to {swagger2} http://localhost:"+port+"/api/swagger-ui.html"+"\n"
                +"access to {UI} http://localhost:"+port+"/api/index.html");



    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    public void customize(ConfigurableEmbeddedServletContainer container) {
    	
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MappingJackson2HttpMessageConverter converter = 
            new MappingJackson2HttpMessageConverter(mapper);
        return converter;
    }

    @Configuration
    public static class EhcacheConfig{
    	
//        @Bean
//        public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean bean){
//           return new EhCacheCacheManager(bean.getObject());
//        }
       
        /*
           * 据shared与否的设置,
           * Spring分别通过CacheManager.create()
           * 或new CacheManager()方式来创建一个ehcache基地.
           *
           * 也说是说通过这个来设置cache的基地是这里的Spring独用,还是跟别的(如hibernate的Ehcache共享)
           *
           */
//          @Bean
//          public EhCacheManagerFactoryBean ehCacheManagerFactoryBean(){
//            EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean ();
//            cacheManagerFactoryBean.setConfigLocation (new ClassPathResource("ehcache.xml"));
//            cacheManagerFactoryBean.setShared(true);
//            return cacheManagerFactoryBean;
//          }
          
          
    	
    }


    @Bean
    public WebMvcConfigurerAdapter corsConfigurer(@Autowired AppConfig._Config config) {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(
                        config.getCors().getOrigins().toArray(new String[]{}));
            }
        };
    }
    
    
    
    
    
    
    
    
    
}
