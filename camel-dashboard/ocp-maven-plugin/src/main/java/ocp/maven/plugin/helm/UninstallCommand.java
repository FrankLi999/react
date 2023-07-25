package ocp.maven.plugin.helm;

import org.apache.maven.project.MavenProject;

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
	String createCommand() {
		String command = String.format("helm uninstall %s ", releaseName);
		command += addCommonFlags();
		
		return command;
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
