package ocp.maven.plugin.helm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * The base command used to execute inheriting Helm commands
 * 
 * @author Austin Dewey
 */
public abstract class BaseCommand {

	final String releaseName;
	final String namespace;
	final String environment;
	final boolean wait;
	final MavenProject project;
	
	public BaseCommand(MavenProject project, String releaseName, String namespace, String environment, boolean wait) {
		this.releaseName = releaseName;
		this.namespace = namespace;
		this.environment = environment;
		this.wait = wait;
		this.project = project;
	}
	
	/**
	 * Generates the Helm command string for the implementing class
	 * 
	 * @return The Helm command string
	 */
	abstract String[] createCommand();
	
	/**
	 * Adds a common set of flags applicable to all Helm commands
	 * 
	 * @return The common set of Helm flags
	 */
	List<String> addCommonFlags() {
		List<String> flags = new ArrayList<>();
		
		
		if (wait) {
			// flags += "--wait ";
			flags.add("--wait");	
		}
		if (namespace != null) {
	 		// flags += String.format("--namespace %s ", namespace);
			flags.add("--namespace");	
			flags.add(namespace);	
	 	}
		return flags;
	}

	// String addCommonFlags() {
	// 	String flags = "";
		
	// 	if (wait) {
	// 		flags += "--wait ";
	// 	}
	// 	if (namespace != null) {
	// 		flags += String.format("--namespace %s ", namespace);
	// 	}
		
	// 	return flags;
	// }
	
	/**
	 * Execute the Helm command. Displays all output to the command line and returns a MojoExecutionException for Helm failures.
	 * 
	 * @throws MojoExecutionException An exception is thrown if the Helm command returns non-0
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

	public BaseCommand prepareForExecution() { return this; }
}
