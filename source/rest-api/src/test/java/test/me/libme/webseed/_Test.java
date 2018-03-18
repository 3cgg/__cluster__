package test.me.libme.webseed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@Import(_TestConfig.class)
@SpringBootTest(properties={"spring.profiles.active=test,dev-error"},
	webEnvironment=WebEnvironment.RANDOM_PORT
		)
public class _Test {
	
	@Autowired
	protected TestRestTemplate restTemplate;
	
	@Autowired
	private Environment environment;
	
	private String contextPath(){
		return environment.getProperty("server.contextPath");
	}
	
	@Test
	public void one(){
		System.out.println("server.contextPath : "+contextPath());
		String errorCode=errorCode();
		System.out.println(errorCode);
	}
	
	@Test
	public void login(){
		
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("userName", "1@1.com");
		paramMap.add("password", "1");

		String jwt=restTemplate.postForObject("/aclmanager/login", paramMap, String.class);
		
		System.out.println(jwt);
	}
	
	
	
	private String url(String url){
		return url;
	}
	
	private String errorCode(){
		return restTemplate.getForObject(url("/cfg/errorCode"), String.class);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
