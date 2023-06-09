package ocp.maven.plugin.helm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of Helm upgrade when referencing a chart from the local file system
 * 
 * @author Austin Dewey
 */
public class UpgradeFromLocalChartCommand extends BaseUpgradeCommand {

	private final String localPath;
	
	private Logger log = LoggerFactory.getLogger(UpgradeFromLocalChartCommand.class);
	
	private UpgradeFromLocalChartCommand(Builder builder) {
		super(builder.getReleaseName(), builder.getValuesFiles(), builder.getInlineValues(), builder.getWait(), builder.getNamespace());
		this.localPath = builder.localPath;
	}
	
	@Override
	String createCommand() {
		String command = String.format("helm upgrade --install %s %s ", releaseName, localPath);
		command += addUpgradeFlags();
		
		log.debug("Helm command: " + command);
		
		return command;
	}
	
	public static class Builder extends BaseUpgradeBuilder<Builder> {
		
		private String localPath;
		
		public Builder(String releaseName, String localPath) {
			super(releaseName);
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
