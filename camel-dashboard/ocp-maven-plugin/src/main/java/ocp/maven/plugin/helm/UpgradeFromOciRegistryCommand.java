package ocp.maven.plugin.helm;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.maven.project.MavenProject;
import ocp.maven.plugin.SystemUtils;

/**
 * An implementation of Helm upgrade when referencing a chart from an OCI registry
 * 
 * @author Austin Dewey
 */
public class UpgradeFromOciRegistryCommand extends BaseUpgradeCommand {
	
	private final String chartName;
	private final String version;
	private final String url;
	
	private Logger log = LoggerFactory.getLogger(UpgradeFromOciRegistryCommand.class);
	
	private UpgradeFromOciRegistryCommand(Builder builder) {
		super(builder.getProject(), builder.getReleaseName(), builder.getValuesFiles(), builder.getInlineValues(), builder.getWait(), builder.getNamespace(), builder.getEnvironment());
		this.chartName = builder.chartName;
		this.version = builder.version;
		this.url = builder.url;
	}

	@Override
	String[] createCommand() {
		List<String> command = new ArrayList<>();
        
        command.add(Paths.get(project.getBuild().getDirectory(), "ocp", "helm", "bin", SystemUtils.systemSpecificSubDirectory(), "helm").toString());
        command.add("upgrade");
        command.add("--install");	
		command.add(releaseName);   
		command.add(String.format("%s/%s ", url, chartName));	
		     		
		if (version != null) {
            command.add("--version");
			command.add(version);
		}
		command.addAll(addUpgradeFlags());
		
		log.debug("Helm command: " + command);
        return command.toArray(new String[0]);
    }

	// String createCommand() {
	// 	String command = String.format("helm upgrade --install %s %s/%s ", releaseName, url, chartName);
	// 	if (version != null) {
	// 		command += String.format("--version %s ", version);
	// 	}
	// 	command += addUpgradeFlags();
		
	// 	log.debug("Helm command: " + command);
		
	// 	return command;
	// }
	
	public static class Builder extends BaseUpgradeBuilder<Builder> {
		
		private String chartName;
		private String version;
		private String url;
		
		public Builder(MavenProject project, String releaseName, String chartName, String url) {
			super(project, releaseName);
			this.chartName = chartName;
			this.url = url;
		}
		
		@Override
		Builder getBuilder() {
			return this;
		}
		
		public Builder version(String version) {
			this.version = version;
			return this;
		}
		
		public UpgradeFromOciRegistryCommand build() {
			return new UpgradeFromOciRegistryCommand(this);
		}
	}
}
