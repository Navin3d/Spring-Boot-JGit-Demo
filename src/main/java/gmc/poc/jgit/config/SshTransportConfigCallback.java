package gmc.poc.jgit.config;

import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.util.FS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SshTransportConfigCallback implements TransportConfigCallback {
	
	@Value("${git.key.private.path}")
    private String privateKeyPath;

	private final SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {

		@Override
		protected void configure(OpenSshConfig.Host hc, Session session) {
			session.setConfig("StrictHostKeyChecking", "no");
		}

		@Override
		protected JSch createDefaultJSch(FS fs) throws JSchException {
			JSch jSch = super.createDefaultJSch(fs);
			log.error("privateKeyPath: {}", privateKeyPath);
			jSch.addIdentity(privateKeyPath, "");
			return jSch;
		}
	};

	@Override
	public void configure(Transport transport) {
		SshTransport sshTransport = (SshTransport) transport;
		sshTransport.setSshSessionFactory(sshSessionFactory);
	}

}
