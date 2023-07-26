package ocp.maven.plugin.helm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.File;
import org.apache.maven.project.MavenProject;
import ocp.maven.plugin.FileSystemUtils;
import ocp.maven.plugin.SystemUtils;
/**
 * The base Helm upgrade command class
 * 
 * @author Austin Dewey
 *
 */
public abstract class BaseUpgradeCommand extends BaseCommand {

	/**
	 * A list of values files
	 */
	final List<String> valuesFiles;
	
	/**
	 * A mapping of inline values (Helm's --set flag)
	 */
	final Map<String,String> inlineValues;
	
	public BaseUpgradeCommand(MavenProject project, String releaseName, List<String> valuesFiles, Map<String,String> inlineValues, boolean wait, String namespace, String environment) {
		super(project, releaseName, namespace, environment, wait);
		this.valuesFiles = valuesFiles;
		this.inlineValues = inlineValues;
	}
	
	@Override
    public BaseCommand prepareForExecution() {
        try {
            File baseDir = this.project.getBasedir();

            String targetDir = this.project.getBuild().getDirectory();

            File destFolder = new File(String.format("%s/ocp", targetDir));
            if(destFolder.exists()) {
                destFolder.delete();
            }
            destFolder.mkdirs();
            // copies executables, helm, from the jar file of this maven plugin 
            FileSystemUtils.copyDirectoryFromJar(String.format("/bin/%s", SystemUtils.systemSpecificSubDirectory()), String.format("%s/ocp/helm", targetDir), 0);
            // copy helm values files from the deployed application
            FileSystemUtils.copyDirectory(String.format("%s/src/ocp/helm/%s-values.yaml", baseDir, this.environment), String.format("%s/ocp/helm", targetDir));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    } 
	/**
	 * A common set of Helm upgrade flags
	 * 
	 * @return Common Helm upgrade flags
	 */
	List<String> addUpgradeFlags() {
		List<String> flags = new ArrayList<>();
		
		if (valuesFiles != null) {
			for (String file : valuesFiles) {
				flags.add(String.format("--values %s ", file));
			}
		}
		if (inlineValues != null) {
			for (Map.Entry<String,String> entry : inlineValues.entrySet()) {
				flags.add(String.format("--set %s=%s ", entry.getKey(), entry.getValue()));
			}
		}
		
		flags.addAll(addCommonFlags());
		
		return flags;
	}
}
