# Spring-Boot-JGit-Demo

This is an Simple POC on Cloning and Pushing files to Github.

ssh-keygen -t ecdsa -b 521 -C "********@gmail.com" -m pem

```
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class GitLabService {
    @Value("${gitlab.host}")
    private String gitLabHost;

    @Value("${gitlab.username}")
    private String gitLabUsername;

    @Value("${gitlab.repository}")
    private String gitLabRepository;

    public String getCommitStatistics() throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(gitLabUsername, gitLabHost, 22);
        session.setConfig("StrictHostKeyChecking", "no");

        // Add your private SSH key path here
        jsch.addIdentity("/path/to/private/ssh/key");

        session.connect();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(session.exec("curl -s --header \"PRIVATE-TOKEN: your-access-token\" " +
                    "https://" + gitLabHost + "/api/v4/projects/" + gitLabRepository + "/repository/commits")));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.disconnect();
        }
    }
}
```
