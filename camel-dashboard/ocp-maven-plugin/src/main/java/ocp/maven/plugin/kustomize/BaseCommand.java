package ocp.maven.plugin.kustomize;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * The base command used to execute Kustomize commands
 */
public abstract class BaseCommand {

	final String overlay;
	final String application;
	final boolean wait;
	final MavenProject project;
	public BaseCommand(MavenProject project, String overlay, String application, boolean wait) {
		this.overlay = overlay;
		this.wait = wait;
		this.application = application;
		this.project = project;
	}
	
	/**
	 * Generates the Kustomize command string for the implementing class
	 * 
	 * @return The Kustomize command string
	 */
	abstract String[] createCommand();

	/**
	 * Adds a common set of flags applicable to all Kustomize commands
	 * 
	 * @return The common set of Kustomize flags
	 */
	String[] addCommonFlags() {
		String flags = "-k ";
		
		if (wait) {
			// flags += "--wait ";
			flags += "wait --for=condition=Ready";

		}
		return new String[] {flags};
	}
	
	/**
	 * Execute the Kustomize command. Displays all output to the command line and returns a MojoExecutionException for Kustomize failures.
	 * 
	 * @throws MojoExecutionException An exception is thrown if the Kustomize command returns non-0
	 */
	public void execute() throws MojoExecutionException {
		try {
			// Process proc = Runtime.getRuntime().exec(createCommand());
			Process proc = new ProcessBuilder(createCommand()).start();
			BufferedReader stdin = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			String s;
			while ((s = stdin.readLine()) != null) {
				System.out.println(s);
			}
			proc.waitFor();
			if (proc.exitValue() != 0) {
				String errMsg = "";
				while ((s = stderr.readLine()) != null) {
					errMsg += s;
				}
				throw new MojoExecutionException(errMsg);
			}
		} catch (Exception e) {
			throw new MojoExecutionException(e);
		}
	}

	public BaseCommand prepareForExecution() {
		return this;
	}

	
}
