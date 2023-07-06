package com.rbs.consulconfig.services.impl;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.util.FS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.rbs.consulconfig.services.GitService;

@Service
public class GitServiceImpl implements GitService {
	
	@Value("${git.repo.url}")
	private String gitRepoPath;
	
	@Value("${git.local.repo}")
	private String localRepoPath;
	
	@Value("${git.key.private.path}")
    private String privateKeyPath;

	public void cloneRepository(String repoLink, String branch) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		File workingDir = new File(localRepoPath);

		TransportConfigCallback transportConfigCallback = new SshTransportConfigCallback();

//		List<String> toClone = new ArrayList<>();
//		toClone.add("origin/dev");
		
		Git git = Git.cloneRepository()
//				.setBranchesToClone(toClone)
		        .setDirectory(workingDir)
		        .setTransportConfigCallback(transportConfigCallback)
		        .setURI(repoLink)
		        .call();
		
		Ref ref = git.checkout()
//		        .setCreateBranch(true)
		        .setName(branch)
//		        .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
//		        .setStartPoint("origin/dev")
		        .call();
				
		for(String file : workingDir.list()) {
			System.out.println("------------>"+file);
			System.out.println("" + workingDir.getAbsolutePath() + "/" + file);

		}
		
	}
	
	public void commitFiles(String commitMessage, String branch) throws IOException {
		Runtime.getRuntime().exec("git config --global url.git@github.com:Navin3d.insteadOf https://github.com/");

		try (Git git = Git.open(new File(localRepoPath))) {
            git.add().addFilepattern(".").call(); // Stage all changes
            git.commit().setMessage(commitMessage).call(); // Commit the changes
            git.push().setRemote("origin").setRefSpecs(new RefSpec(branch)).call(); // Push the changes
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }

    }
	
	private class SshTransportConfigCallback implements TransportConfigCallback {

	    private final SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
	    	
	        @Override
	        protected void configure(OpenSshConfig.Host hc, Session session) {
	            session.setConfig("StrictHostKeyChecking", "no");
	        }

	        @Override
	        protected JSch createDefaultJSch(FS fs) throws JSchException {
	            JSch jSch = super.createDefaultJSch(fs);
//	            jSch.addIdentity("C:/Users/shiju/.ssh/id_ecdsa",  "pass phrase used".getBytes());	           
	            jSch.addIdentity(privateKeyPath, "password".getBytes());

//	            jSch.addIdentity("C:/Users/shiju/.ssh", "".getBytes());
//	            jSch.addIdentity("C:\\workspace", "".getBytes());
	            
	            return jSch;
	        }
	    };

	    @Override
	    public void configure(Transport transport) {
	        SshTransport sshTransport = (SshTransport) transport;
	        sshTransport.setSshSessionFactory(sshSessionFactory);
	    }

	}
	
}
