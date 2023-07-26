package ocp.maven.plugin.helm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

            File destFolder = new File(Paths.get(targetDir, "ocp", "helm").toString());
            if(destFolder.exists()) {
                destFolder.delete();
            }
            destFolder.mkdirs();
			// copy helm values files from the deployed application
            // FileSystemUtils.copyDirectory(String.format("%s/src/ocp/helm/%s-values.yaml", baseDir, this.environment), String.format("%s/ocp/helm", targetDir));
			Files.copy(
				Paths.get(baseDir.getPath(), "src", "ocp", "helm", String.format("%s-values.yaml", this.environment)), 
				Paths.get(targetDir, "ocp", "helm", String.format("%s-values.yaml", this.environment)), 
				StandardCopyOption.REPLACE_EXISTING);

            // copies executables, helm, from the jar file of this maven plugin 
            FileSystemUtils.copyDirectoryFromJar(
				String.format("/bin/%s", SystemUtils.systemSpecificSubDirectory()), 
				Paths.get(targetDir, "ocp", "helm").toString(), 
				0);
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
