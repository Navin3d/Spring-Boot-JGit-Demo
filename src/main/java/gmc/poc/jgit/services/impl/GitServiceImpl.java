package gmc.poc.jgit.services.impl;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import gmc.poc.jgit.config.SshTransportConfigCallback;
import gmc.poc.jgit.services.GitService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GitServiceImpl implements GitService {
	
	@Value("${git.repo.url}")
	private String gitRepoPath;
	
	@Value("${git.local.repo}")
	private String localRepoPath;
	
	@Autowired
	private SshTransportConfigCallback transportConfigCallback;

	public void cloneRepository(String repoLink, String branch) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		File workingDir = new File(localRepoPath);

//		List<String> toClone = new ArrayList<>();
//		toClone.add("origin/dev");
		
		Git git = Git.cloneRepository()
//				.setBranchesToClone(toClone)
		        .setDirectory(workingDir)
		        .setTransportConfigCallback(transportConfigCallback)
		        .setURI(repoLink)
		        .call();
		
		git.checkout()
//		        .setCreateBranch(true)
		        .setName(branch)
//		        .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
//		        .setStartPoint("origin/dev")
		        .call();
				
		for(String file : workingDir.list()) {
			log.info(file);
			log.info("" + workingDir.getAbsolutePath() + "/" + file);

		}
		
	}
	
	public void commitFiles(String commitMessage, String branch) throws IOException {		
		Runtime.getRuntime().exec("git config --global git@github.com:.insteadOf https://github.com/");

		try (Git git = Git.open(new File(localRepoPath))) {
            git.add().addFilepattern(".").call();
            
            git.commit()
                    .setMessage(commitMessage)
                    .call()
                    .getId();

            git.push().setTransportConfigCallback(transportConfigCallback).call();
            
            log.info("Successfully commited and pushed file to Github.");

        } catch (IOException | GitAPIException e) {
//            e.printStackTrace();
            log.error("Error pushing file to Github.");
        }

    }
	
}
