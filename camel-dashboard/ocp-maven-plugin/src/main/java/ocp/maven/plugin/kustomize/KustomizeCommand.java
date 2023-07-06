package ocp.maven.plugin.kustomize;

import java.nio.file.Paths;
import org.apache.maven.project.MavenProject;
import ocp.maven.plugin.SystemUtils;

public class KustomizeCommand extends BaseKustomizeCommand {

    private KustomizeCommand(KustomizeCommand.Builder builder) {
        super(builder.getProject(), builder.getOverlay(), builder.getApplication(), builder.getWait());
    }

    @Override
    String[] createCommand() {
        String[] commands = {
                Paths.get(project.getBuild().getDirectory(), "ocp", "bin", SystemUtils.systemSpecificSubDirectory(), "oc").toString(),
                "kustomize",
                Paths.get(project.getBasedir().toPath().toString(), "target", "ocp", "kustomize", "my-camel", "overlays", overlay).toString(),
                "-o",
                Paths.get(project.getBasedir().toPath().toString(), "target", "ocp", "kustomize", "my-camel", String.format("%s-deployment.yaml", overlay)).toString(),
        };

        return commands;
    }

    public static class Builder extends BaseBuilder<KustomizeCommand.Builder> {

        public Builder(MavenProject project, String overlay, String application) {
            super(project, overlay, application);
        }

        @Override
        KustomizeCommand.Builder getBuilder() {
            return this;
        }

        public KustomizeCommand build() {
            KustomizeCommand command = new KustomizeCommand(this);
            validate(command);
            return command;
        }

        private void validate(KustomizeCommand command) {

        }
    }
}
