package ocp.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import ocp.maven.plugin.kustomize.KustomizeCommand;

@Mojo(name = "kustomize", defaultPhase = LifecyclePhase.DEPLOY)
public class KustomizeMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    /**
     * The Kustomize overlay path
     */
    @Parameter(property = "overlay", defaultValue = "sandbox")
    private String overlay;

    @Parameter(property = "application", defaultValue = "my-camel")
    private String application;

    /**
     * Wait for the apply operation to complete
     */
    @Parameter(property = "wait")
    private boolean wait;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info(String.format("Build kustomize \"%s\"", overlay));

        new KustomizeCommand.Builder(project, overlay, application)
                .wait(wait)
                .build()
                .prepareForExecution()
                .execute();
    }
}
