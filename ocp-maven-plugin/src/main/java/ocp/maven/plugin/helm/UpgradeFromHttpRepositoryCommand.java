package ocp.maven.plugin.helm;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.maven.project.MavenProject;
import ocp.maven.plugin.SystemUtils;

/**
 * An implementation of Helm upgrade when referencing a chart from an HTTP(s) repository
 * 
 * @author Austin Dewey
 */
public class UpgradeFromHttpRepositoryCommand extends BaseUpgradeCommand {
	
	private final String chartName;
	private final String version;
	private final String url;
	private final String username;
	private final String password;
	
	private Logger log = LoggerFactory.getLogger(UpgradeFromHttpRepositoryCommand.class);
	
	private UpgradeFromHttpRepositoryCommand(Builder builder) {
		super(builder.getProject(), builder.getReleaseName(), builder.getValuesFiles(), builder.getInlineValues(), builder.getWait(), builder.getNamespace(), builder.getEnvironment());
		this.chartName = builder.chartName;
		this.version = builder.version;
		this.url = builder.url;
		this.username = builder.username;
		this.password = builder.password;
	}
	
	@Override
    String[] createCommand() {
		List<String> command = new ArrayList<>();
        
        command.add(Paths.get(project.getBuild().getDirectory(), "ocp", "helm", "bin", SystemUtils.systemSpecificSubDirectory(), "helm").toString());
        command.add("upgrade");
        command.add("--install");	
		command.add(url);	
		command.add(releaseName);        		
		command.add(chartName);
        
		if (version != null) {
            command.add("--version");
			command.add(version);
		}
		command.addAll(addUpgradeFlags());
		List<String> maskedCommand = List.copyOf(command);
		if (username != null && password != null) {
			command.add(String.format("--username %s --password %s --pass-credentials ", username, password));
			maskedCommand.add(String.format("--username %s --password <masked> --pass-credentials ", username));
		}
		log.debug("Helm command: " + maskedCommand);
        return command.toArray(new String[0]);
    }
	
	// String createCommand() {
	// 	String command = String.format("helm upgrade --install --repo %s %s %s ", url, releaseName, chartName);
	// 	if (version != null) {
	// 		command += String.format("--version %s ", version);
	// 	}
	// 	command += addUpgradeFlags();
		
	// 	String maskedCommand = command;
	// 	if (username != null && password != null) {
	// 		command += String.format("--username %s --password %s --pass-credentials ", username, password);
	// 		maskedCommand += String.format("--username %s --password <masked> --pass-credentials ", username);
	// 	}
		
	// 	log.debug("Helm command: " + maskedCommand);
		
	// 	return command;
	// }
	
	public static class Builder extends BaseUpgradeBuilder<Builder> {
		
		private String chartName;
		private String version;
		private String url;
		private String username;
		private String password;
		
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
		
		public Builder username(String username) {
			this.username = username;
			return this;
		}
		
		public Builder password(String password) {
			this.password = password;
			return this;
		}
		
		public UpgradeFromHttpRepositoryCommand build() {
			return new UpgradeFromHttpRepositoryCommand(this);
		}
	}
}
