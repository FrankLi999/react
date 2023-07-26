package ocp.maven.plugin;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ocp.maven.plugin.helm.UpgradeFromAddedRepositoryCommand;
import ocp.maven.plugin.helm.UpgradeFromHttpRepositoryCommand;
import ocp.maven.plugin.helm.UpgradeFromLocalChartCommand;
import ocp.maven.plugin.helm.UpgradeFromBuildInChartCommand;
import ocp.maven.plugin.helm.UpgradeFromOciRegistryCommand;
import ocp.maven.plugin.helm.model.Values;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import ocp.maven.plugin.helm.model.Chart;

/**
 * Install/Upgrade a Helm chart to Kubernetes.
 * This is equivalent to passing "helm upgrade --install" from the Helm CLI
 *
 * @author Austin Dewey
 */
@Mojo(name = "helm-upgrade", defaultPhase = LifecyclePhase.INSTALL)
public class HelmUpgradeMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	private MavenProject project;
	
	/**
	 * The Helm release name
	 */
	@Parameter(property = "releaseName", defaultValue = "${project.name}")
	private String releaseName;
	
	/**
	 * The target Helm chart and repository information
	 */
	@Parameter(property = "chart", required = true)
	private Chart chart;
	
	/**
	 * Helm values files and/or inline values
	 */
	@Parameter(property = "values")
	private Values values;
	
	/**
	 * Wait for the Helm installation/upgrade to complete
	 */
	@Parameter(property = "wait")
	private boolean wait;
	
	/**
	 * The target Kubernetes namespace
	 */
	@Parameter(property = "namespace")
	private String namespace;

	/**
	 * The target environment, sandbox, dev, qa, prod etc
	 */
	@Parameter(property = "environment")
	private String environment;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		chart.validate();
		
		List<String> valuesFiles = null;
		Map<String,String> inlineValues = null;
		
		if (values != null) {
			valuesFiles = values.getFiles();
			inlineValues = values.getSet();
		}
		String targetDir = this.project.getBuild().getDirectory();
		if (environment == null || environment.length() == 0) {
            environment = "sandbox";
		}

		if (namespace == null || namespace.length() == 0) {
            namespace = "my-sandbox";
		}

		if (valuesFiles == null) {
			valuesFiles = new ArrayList<>();
		}
		// String envSpecificValueFile = String.format("%s/ocp/helm/%s-values.yaml", targetDir, environment);
		String envSpecificValueFile = Paths.get(targetDir, "ocp", "helm", String.format("%s-values.yaml", environment)).toString();
		if (!valuesFiles.contains(envSpecificValueFile)) {
			valuesFiles.add(envSpecificValueFile);
		}
		String chartName = chart.getName();
		String repositoryUrl = chart.getRepository().getUrl();
		String repositoryName = chart.getRepository().getName();
		String username = chart.getRepository().getUsername();
		String password = chart.getRepository().getPassword();
		String chartVersion = chart.getVersion();
		
		getLog().info(String.format("Installing/Upgrading release \"%s\"", releaseName));
		
		switch (chart.getUpgradeType()) {
		case UPGRADE_FROM_HTTP_REPOSITORY:
			new UpgradeFromHttpRepositoryCommand.Builder(project, releaseName, chartName, repositoryUrl)
					.inlineValues(inlineValues)
					.valuesFiles(valuesFiles)
					.version(chartVersion)
					.username(username)
					.password(password)
					.wait(wait)
					.namespace(namespace)
					.environment(environment)
					.build()
					.prepareForExecution()
					.execute();
			break;
		case UPGRADE_FROM_OCI_REGISTRY:
			new UpgradeFromOciRegistryCommand.Builder(project, releaseName, chartName, repositoryUrl)
					.inlineValues(inlineValues)
					.valuesFiles(valuesFiles)
					.version(chartVersion)
					.wait(wait)
					.namespace(namespace)
					.environment(environment)
					.build()
					.prepareForExecution()
					.execute();
			break;
		case UPGRADE_FROM_ADDED_REPOSITORY:
			new UpgradeFromAddedRepositoryCommand.Builder(project, releaseName, chartName, repositoryName)
					.inlineValues(inlineValues)
					.valuesFiles(valuesFiles)
					.version(chartVersion)
					.wait(wait)
					.namespace(namespace)
					.environment(environment)
					.build()
					.prepareForExecution()
					.execute();
			break;
		case UPGRADE_FROM_LOCAL:
			new UpgradeFromLocalChartCommand.Builder(project, releaseName, repositoryUrl)
					.inlineValues(inlineValues)
					.valuesFiles(valuesFiles)
					.wait(wait)
					.namespace(namespace)
					.environment(environment)
					.build()
					.prepareForExecution()
					.execute();
			break;
        case UPGRADE_FROM_BUILD_IN_REPOSITORY:
			new UpgradeFromBuildInChartCommand.Builder(project, releaseName, repositoryUrl.substring("build-in://".length()))
					.inlineValues(inlineValues)
					.valuesFiles(valuesFiles)
					.wait(wait)
					.namespace(namespace)
					.environment(environment)
					.build()
					.prepareForExecution()
					.execute();
			break;			
		default:
			break;
		}
	}
}
