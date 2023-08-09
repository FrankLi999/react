package ocp.maven.plugin.helm;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.project.MavenProject;
import ocp.maven.plugin.SystemUtils;

/**
 * The Helm uninstall implementation
 * 
 * @author Austin Dewey
 */
public class UninstallCommand extends BaseCommand {

	private UninstallCommand(Builder builder) {
		super(builder.getProject(), builder.getReleaseName(), builder.getNamespace(), builder.getEnvironment(), builder.getWait());
	}
	
	@Override
	String[] createCommand() {
		List<String> command = new ArrayList<>();
        
        command.add(Paths.get(project.getBuild().getDirectory(), "ocp", "helm", "bin", SystemUtils.systemSpecificSubDirectory(), "helm").toString());
        command.add("uninstall");        		
		command.add(releaseName);

		// String command = String.format("helm uninstall %s ", releaseName);
		// command += addCommonFlags();
		
		return command.toArray(new String[0]);
	}
	
	public static class Builder extends BaseBuilder<Builder> {
		
		public Builder(MavenProject project, String releaseName) {
			super(project, releaseName);
		}
		
		@Override
		Builder getBuilder() {
			return this;
		}
		
		public UninstallCommand build() {
			UninstallCommand command = new UninstallCommand(this);
			validate(command);
			return command;
		}
		
		private void validate(UninstallCommand command) {
			
		}
	}
}
