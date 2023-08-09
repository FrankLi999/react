package ocp.maven.plugin.kustomize;

import java.nio.file.Paths;
import ocp.maven.plugin.SystemUtils;
import org.apache.maven.project.MavenProject;

public class OCApplyCommand extends BaseKustomizeCommand {

    private OCApplyCommand(OCApplyCommand.Builder builder) {
        super(builder.getProject(), builder.getOverlay(), builder.getApplication(), builder.getWait());
    }

    @Override
    String[] createCommand() {
        String[] commands = {
                Paths.get(project.getBuild().getDirectory(), "ocp", "bin", SystemUtils.systemSpecificSubDirectory(), "oc").toString(),
                "apply",
                "-k",
                Paths.get(project.getBasedir().toPath().toString(), "target", "ocp", "kustomize", "my-camel", "overlays", overlay).toString()
        };
        return commands;
    }

    public static class Builder extends BaseBuilder<OCApplyCommand.Builder> {

        public Builder(MavenProject project, String overlay, String application) {
            super(project, overlay, application);
        }

        @Override
        OCApplyCommand.Builder getBuilder() {
            return this;
        }

        public OCApplyCommand build() {
            OCApplyCommand command = new OCApplyCommand(this);
            validate(command);
            return command;
        }

        private void validate(OCApplyCommand command) {

        }
    }
}
