package ocp.maven.plugin.helm;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.maven.project.MavenProject;
import ocp.maven.plugin.SystemUtils;

/**
 * An implementation of Helm upgrade when referencing a chart from the local file system
 * 
 * @author Austin Dewey
 */
public class UpgradeFromLocalChartCommand extends BaseUpgradeCommand {

	private final String localPath;
	
	private Logger log = LoggerFactory.getLogger(UpgradeFromLocalChartCommand.class);
	
	private UpgradeFromLocalChartCommand(Builder builder) {
		super(builder.getProject(), builder.getReleaseName(), builder.getValuesFiles(), builder.getInlineValues(), builder.getWait(), builder.getNamespace(), builder.getEnvironment());
		this.localPath = builder.localPath;
	}
	
	@Override
	String[] createCommand() {
		List<String> command = new ArrayList<>();
        
        command.add(Paths.get(project.getBuild().getDirectory(), "ocp", "helm", "bin", SystemUtils.systemSpecificSubDirectory(), "helm").toString());
        command.add("upgrade");
        command.add("--install");				
		command.add(releaseName);
        command.add(localPath);

		command.addAll(addUpgradeFlags());
		log.debug("Helm command: " + command.toString());
        return command.toArray(new String[0]);
    }
	// String createCommand() {
	// 	String command = String.format("helm upgrade --install %s %s ", releaseName, localPath);
	// 	command += addUpgradeFlags();
		
	// 	log.debug("Helm command: " + command);
		
	// 	return command;
	// }
	
	public static class Builder extends BaseUpgradeBuilder<Builder> {
		
		private String localPath;
		
		public Builder(MavenProject project, String releaseName, String localPath) {
			super(project, releaseName);
			this.localPath = localPath;
		}
		
		@Override
		Builder getBuilder() {
			return this;
		}
		
		public UpgradeFromLocalChartCommand build() {
			return new UpgradeFromLocalChartCommand(this);
		}
	}
}
