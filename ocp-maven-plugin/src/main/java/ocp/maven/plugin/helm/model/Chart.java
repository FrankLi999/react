package ocp.maven.plugin.helm.model;

import ocp.maven.plugin.helm.CommandType;
import org.apache.maven.plugin.MojoExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a Helm chart
 * 
 * @author Austin Dewey
 *
 */
public class Chart {
	
	private String name;
	private String version = "0.0.1";
	private Repository repository;
	
	private Logger log = LoggerFactory.getLogger(Chart.class);
	
	public Chart() {
		this.name = "my-camel";
	}
	
	public Chart(String name, Repository repository) {
		this.name = name;
		this.repository = repository;
	}

	public Chart(String name, String version, Repository repository) {
		this.name = name;
		this.version = version;
		this.repository = repository;
	}

	/**
	 * Deduces the type of Helm upgrade needed based on user input
	 * 
	 * @return The type of Helm upgrade (CommandType)
	 */
	public CommandType getUpgradeType() {
		if (repository.getUrl() != null) {
			String r = repository.getUrl().toLowerCase();
			if (r.contains("build-in://")) {
				log.debug("Repository type: build in");
				return CommandType.UPGRADE_FROM_BUILD_IN_REPOSITORY;
			}
			if (r.contains("https://") || r.contains("http://")) {
				log.debug("Repository type: http(s)");
				return CommandType.UPGRADE_FROM_HTTP_REPOSITORY;
			}
			if (r.contains("oci://")) {
				log.debug("Repository type: oci");
				return CommandType.UPGRADE_FROM_OCI_REGISTRY;
			}			
		}
		
		if (repository.getName() != null) {
			log.debug("Repository type: repository added with \"helm repo add\"");
			return CommandType.UPGRADE_FROM_ADDED_REPOSITORY;
		}
		
		log.debug("Repository type: local chart on file system");
		return CommandType.UPGRADE_FROM_LOCAL;
	}
	
	/**
	 * Validates the chart object
	 * 
	 * @throws MojoExecutionException An exception is thrown if the chart object fails validation
	 */
	public void validate() throws MojoExecutionException {
		if (repository == null) {
			throw new MojoExecutionException("\"chart.repository\" must not be null");
		}
		
		CommandType type = getUpgradeType();
		if (type == CommandType.UPGRADE_FROM_HTTP_REPOSITORY ||
			type == CommandType.UPGRADE_FROM_OCI_REGISTRY || 
			type == CommandType.UPGRADE_FROM_ADDED_REPOSITORY) {
			
			if (name == null) {
				throw new MojoExecutionException("\"chart.name\" must not be null");
			}
		}
		
		if (type == CommandType.UPGRADE_FROM_LOCAL) {
			if (name != null) {
				throw new MojoExecutionException("\"chart.name\" is not supported for local chart paths. Remove \"chart.name\" from your configuration");
			}
		}
		
		if (type != CommandType.UPGRADE_FROM_HTTP_REPOSITORY) {
			if (repository.getUsername() != null || repository.getPassword() != null) {
				throw new MojoExecutionException("Repository username and password is only supported for HTTP(s) repositories");
			}
		}
		
		repository.validate();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public Repository getRepository() {
		return repository;
	}
	
	public void setRepository(Repository repository) {
		this.repository = repository;
	}
}
