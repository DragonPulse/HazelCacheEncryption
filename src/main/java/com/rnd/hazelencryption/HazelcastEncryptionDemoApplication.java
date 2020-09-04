package com.rnd.hazelencryption;

import com.rnd.hazelencryption.cache.EncryptedHazelcastCacheManager;
import com.rnd.hazelencryption.crypto.CipherService;
import com.rnd.hazelencryption.web.documentation.SwaggerConfiguration;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


import java.util.Collections;

@SpringBootApplication
@EnableCaching
@Import({SwaggerConfiguration.class})
public class HazelcastEncryptionDemoApplication {

	public static final String USERS_CACHE = "users";

	public static void main(String[] args) {
		SpringApplication.run(HazelcastEncryptionDemoApplication.class, args);
	}

	@Bean
	public CacheManager cacheManager(HazelcastInstance hazelcastInstance, CipherService cipherService) {
		return new EncryptedHazelcastCacheManager(hazelcastInstance, cipherService);
	}

	@Bean
	public HazelcastInstance hazelcastInstance(Config config) {
		return Hazelcast.newHazelcastInstance(config);
	}

	@Bean
	public Config config() {
		Config config = new Config();

		JoinConfig joinConfig = config.getNetworkConfig().getJoin();

		// disable multicast config for demo
		joinConfig.getMulticastConfig()
				.setEnabled(false);

		// enable tcp/ip config for demo
		joinConfig.getTcpIpConfig()
				.setMembers(Collections.singletonList("localhost"))
				.setEnabled(true);

		MapConfig usersMapConfig = new MapConfig()
				.setName(USERS_CACHE)
				.setTimeToLiveSeconds(600)
				.setEvictionPolicy(EvictionPolicy.LFU);

		config.addMapConfig(usersMapConfig);

		return config;
	}


}
