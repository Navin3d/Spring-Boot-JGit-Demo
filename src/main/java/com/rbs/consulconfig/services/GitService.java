package com.rbs.consulconfig.services;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

public interface GitService {
	public void cloneRepository(String repoLink, String branch) throws IOException, InvalidRemoteException, TransportException, GitAPIException;
}
