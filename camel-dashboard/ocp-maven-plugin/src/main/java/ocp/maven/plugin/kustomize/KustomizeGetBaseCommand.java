package ocp.maven.plugin.kustomize;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import ocp.maven.plugin.FileSystemUtils;

public class KustomizeGetBaseCommand extends BaseCommand {
    
    private KustomizeGetBaseCommand(KustomizeGetBaseCommand.Builder builder) {
        super(builder.getProject(), builder.getOverlay(), builder.getApplication(), builder.getWait());
    }

    @Override
    String[] createCommand() {
        return null;
    }

    @Override
    public void execute() throws MojoExecutionException {
        try {
            String jarPathRoot = String.format("/kustomize/%s", application);
            // copies kustomize base files from the jar file of this maven plugin
            FileSystemUtils.copyDirectoryFromJar(jarPathRoot, String.format("%s/src/ocp/kustomize", this.project.getBasedir()), jarPathRoot.length());
        } catch (Exception ex) {
            throw new MojoExecutionException(ex);
        }
    }

    public static class Builder extends BaseBuilder<KustomizeGetBaseCommand.Builder> {

        public Builder(MavenProject project, String overlay, String application) {
            super(project, overlay, application);
        }

        @Override
        KustomizeGetBaseCommand.Builder getBuilder() {
            return this;
        }

        public KustomizeGetBaseCommand build() {
            KustomizeGetBaseCommand command = new KustomizeGetBaseCommand(this);
            validate(command);
            return command;
        }

        private void validate(KustomizeGetBaseCommand command) {

        }
    }
}
