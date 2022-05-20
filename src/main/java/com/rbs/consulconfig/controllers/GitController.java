package com.rbs.consulconfig.controllers;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rbs.consulconfig.services.GitService;

@RestController
@RequestMapping("/git")
public class GitController {
	
	@Autowired
	private GitService gitCheckoutServiceImpl;
	
	@GetMapping("/clone/repo")
	public void cloneRepo() {
//	@GetMapping("/clone/repo/{repoLink}/{branch}")
//	public void cloneRepo(@PathVariable String repoLink, @PathVariable String branch) {
		try {
			String repoLink = "", branch = "";
			
			if(repoLink.isEmpty())
				repoLink = "git@github.com:Navin3d/Bulk-Email-Service-Config.git";
			if(branch.isEmpty())
				branch = "origin/dev";
			
			gitCheckoutServiceImpl.cloneRepository(repoLink, branch);
		} catch (InvalidRemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
