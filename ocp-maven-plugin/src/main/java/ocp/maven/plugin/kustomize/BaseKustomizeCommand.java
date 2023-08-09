package ocp.maven.plugin.kustomize;

import java.io.*;
import org.apache.maven.project.MavenProject;
import ocp.maven.plugin.FileSystemUtils;
import ocp.maven.plugin.SystemUtils;

public abstract class BaseKustomizeCommand extends BaseCommand {
    public BaseKustomizeCommand(MavenProject project, String overlay, String application, boolean wait) {
        super(project, overlay, application, wait);
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
            // copies kustomize base files from the jar file of this maven plugin
            FileSystemUtils.copyDirectoryFromJar(String.format("/kustomize/%s", application), String.format("%s/ocp", targetDir), 0);
            // copies executables, oc and kustomize, from the jar file of this maven plugin 
            FileSystemUtils.copyDirectoryFromJar(String.format("/bin/%s", SystemUtils.systemSpecificSubDirectory()), String.format("%s/ocp", targetDir), 0);
            // copy kustomize overlays from the deployed application
            FileSystemUtils.copyDirectory(String.format("%s/src/ocp/kustomize", baseDir), String.format("%s/ocp/kustomize/my-camel", targetDir));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }    
}
