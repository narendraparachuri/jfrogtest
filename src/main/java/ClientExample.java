import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jfrog.artifactory.client.Artifactory;
import org.jfrog.artifactory.client.ArtifactoryClientBuilder;
import org.jfrog.artifactory.client.model.File;
import org.jfrog.artifactory.client.model.LightweightRepository;
import org.jfrog.artifactory.client.model.RepoPath;
import org.jfrog.artifactory.client.model.Repository;
import org.jfrog.artifactory.client.model.repository.settings.impl.GenericRepositorySettingsImpl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.jfrog.artifactory.client.model.impl.RepositoryTypeImpl.LOCAL;

public class ClientExample {

    private static String userName = "admin";
    private static String password = "dcrKak7j9D";
    private static String artifactoryUrl = "http://jfrog.local/artifactory";

    private static String repoName = "gradle-release-local";

    public static void main(String[] args) throws Exception {

        //create artifactory object
        Artifactory artifactory = createArtifactory(userName, password, artifactoryUrl);

        if (artifactory == null){
            throw new RuntimeException("artifactory creation failed");
        }

        //search for file
        List<RepoPath> searchResult = searchFile(artifactory, repoName);
        
        System.out.print("Example finished.");
    }


    /**
     * This method creates an artifactory object
     */
    private static Artifactory createArtifactory(String username, String password, String artifactoryUrl) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(artifactoryUrl)){
            throw new IllegalArgumentException("Arguments passed to createArtifactory are not valid");
        }

        return ArtifactoryClientBuilder.create()
                .setUrl(artifactoryUrl)
                .setUsername(username)
                .setPassword(password)
                .build();
    }

    /**
     * Search a specific repository, return the location of all the files
     */
    private static List<RepoPath> searchFile(Artifactory artifactory, String repoName) {
        if (artifactory == null || StringUtils.isEmpty(repoName)){
            throw new IllegalArgumentException("Arguments passed to serachFile are not valid");
        }

        return artifactory.searches()
                .repositories(repoName)
                .doSearch();
    }
}
