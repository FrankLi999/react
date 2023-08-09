package ocp.maven.plugin.helm;

import org.apache.maven.project.MavenProject;

/**
 * Base Helm builder object
 * Contains the common set of fields for all Helm command builders
 * 
 * @author Austin Dewey
 * @param <T> The type of builder
 */
public abstract class BaseBuilder<T> {
	
	private T builder;

	private String releaseName;
	private String environment;
	private String namespace;
	private boolean wait;
	private MavenProject project;
	
	public BaseBuilder(MavenProject project, String releaseName) {
		this.releaseName = releaseName;
		this.project = project;
		builder = getBuilder();
	}
	
	abstract T getBuilder();
	
	public T namespace(String namespace) {
		this.namespace = namespace;
		return builder;
	}
	
	public T environment(String environment) {
		this.environment = environment;
		return builder;
	}

	public T wait(boolean wait) {
		this.wait = wait;
		return builder;
	}
	
	String getReleaseName() {
		return releaseName;
	}
	
	String getNamespace() {
		return namespace;
	}
		
	String getEnvironment() {
		return environment;
	}
	
	boolean getWait() {
		return wait;
	}

	MavenProject getProject() {
		return project;
	}
}
