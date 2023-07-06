package com.rbs.consulconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rbs.consulconfig.services.GitService;

@SpringBootApplication
public class ConsulconfigApplication implements CommandLineRunner {
	
	@Value("${git.repo.url}")
    	private String gitRepoUrl;
	
	@Value("${git.local.repo}")
	private String localRepoPath;
	
	@Autowired
	private GitService gitService;

	public static void main(String[] args) {
		SpringApplication.run(ConsulconfigApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		gitService.cloneRepository(gitRepoUrl, "main");		
		gitService.commitFiles("Test Commit", "main");
	}

}
