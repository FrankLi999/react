package ocp.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import ocp.maven.plugin.helm.UninstallCommand;
import org.apache.maven.project.MavenProject;

/**
 * Uninstall a Helm release. This is the equivalent of passing "helm uninstall" from the Helm CLI
 * 
 * @author Austin Dewey
 */
@Mojo(name = "helm-uninstall")
public class HelmUninstallMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	private MavenProject project;

	/**
	 * The Helm release name
	 */
	@Parameter(property = "releaseName", defaultValue = "${project.name}")
	private String releaseName;
	
	/**
	 * The target Kubernetes namespace
	 */
	@Parameter(property = "namespace")
	private String namespace;
	
	/**
	 * Wait for the uninstall operation to complete
	 */
	@Parameter(property = "wait")
	private boolean wait;
	
	/**
	 * The target environment, sandbox, dev, qa, prod etc
	 */
	@Parameter(property = "environment")
	private String environment;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info(String.format("Uninstalling release \"%s\"", releaseName));
		
		new UninstallCommand.Builder(project, releaseName)
				.namespace(namespace)
				.wait(wait)
				.environment(environment)
				.build()
				.execute();
	}
}
