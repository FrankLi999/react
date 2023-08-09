package ocp.maven.plugin.kustomize;

import org.apache.maven.project.MavenProject;

/**
 * Base Kustomize builder object
 * Contains the common set of fields for all Kustomize command builders
 * @param <T> The type of builder
 */
public abstract class BaseBuilder<T> {
	
	private T builder;

	private MavenProject project;
	private String overlay;
	private String application;
	private boolean wait;
	
	public BaseBuilder(MavenProject project, String overlay, String application) {
		this.overlay = overlay;
		this.application = application;
		this.project = project;
		builder = getBuilder();
	}
	
	abstract T getBuilder();

	
	public T wait(boolean wait) {
		this.wait = wait;
		return builder;
	}
	
	String getOverlay() {
		return overlay;
	}

	String getApplication() {
		return application;
	}

	boolean getWait() {
		return wait;
	}

	MavenProject getProject() {
		return project;
	}
}
