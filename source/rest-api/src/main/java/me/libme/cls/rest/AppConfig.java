package me.libme.cls.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppConfig implements  EnvironmentAware {

	private Environment environment;
	
	@Override
	public void setEnvironment(Environment environment) {
		this.environment=environment;
	}
	
	@ConfigurationProperties(prefix="cpp")
	@Component
	public static class _Config{

		private String name;
		
		private User user;
		
		private Kaptcha kaptcha;

		private Cors cors;

		private StreamConfig streamConfig;

		public StreamConfig getStreamConfig() {
			return streamConfig;
		}

		public void setStreamConfig(StreamConfig streamConfig) {
			this.streamConfig = streamConfig;
		}

		public Cors getCors() {
			return cors;
		}

		public void setCors(Cors cors) {
			this.cors = cors;
		}

		public Kaptcha getKaptcha() {
			return kaptcha;
		}

		public void setKaptcha(Kaptcha kaptcha) {
			this.kaptcha = kaptcha;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public static class User{
			
			private String defaultPassword;

			public String getDefaultPassword() {
				return defaultPassword;
			}

			public void setDefaultPassword(String defaultPassword) {
				this.defaultPassword = defaultPassword;
			}
			
		}


		public static class Kaptcha{
			
			private boolean enable;
			
			private String headerKey;
			
			private String headerValue;
			
			private long expiredTime;

			public long getExpiredTime() {
				return expiredTime;
			}

			public void setExpiredTime(long expiredTime) {
				this.expiredTime = expiredTime;
			}

			public boolean isEnable() {
				return enable;
			}

			public void setEnable(boolean enable) {
				this.enable = enable;
			}

			public String getHeaderKey() {
				return headerKey;
			}

			public void setHeaderKey(String headerKey) {
				this.headerKey = headerKey;
			}

			public String getHeaderValue() {
				return headerValue;
			}

			public void setHeaderValue(String headerValue) {
				this.headerValue = headerValue;
			}
			
		}


		public static class Cors{

			private List<String> origins;

			public List<String> getOrigins() {
				return origins;
			}

			public void setOrigins(List<String> origins) {
				this.origins = origins;
			}

		}


		public static class StreamConfig{

			private String root;

			public String getRoot() {
				return root;
			}

			public void setRoot(String root) {
				this.root = root;
			}
		}

	}
	
}
