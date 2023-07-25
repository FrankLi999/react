package ocp.maven.plugin.helm;

import java.util.List;
import java.util.Map;
import org.apache.maven.project.MavenProject;

/**
 * A builder object used to build different Helm upgrade commands
 * 
 * @author Austin Dewey
 * @param <T> The type of Helm upgrade builder
 */
public abstract class BaseUpgradeBuilder<T> extends BaseBuilder<T> {
	
	private T builder;

	private List<String> valuesFiles;
	private Map<String,String> inlineValues;
	
	public BaseUpgradeBuilder(MavenProject project, String releaseName) {
		super(project, releaseName);
		builder = getBuilder();
	}
	
	public T valuesFiles(List<String> valuesFiles) {
		this.valuesFiles = valuesFiles;
		return builder;
	}
	
	List<String> getValuesFiles() {
		return valuesFiles;
	}
	
	public T inlineValues(Map<String,String> inlineValues) {
		this.inlineValues = inlineValues;
		return builder;
	}
	
	Map<String,String> getInlineValues() {
		return inlineValues;
	}
}
