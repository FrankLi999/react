package ocp.maven.plugin.kustomize;

import java.nio.file.Paths;
import ocp.maven.plugin.SystemUtils;
import org.apache.maven.project.MavenProject;

public class OCDeleteCommand extends BaseKustomizeCommand {

    private OCDeleteCommand(OCDeleteCommand.Builder builder) {
        super(builder.getProject(), builder.getOverlay(), builder.getApplication(), builder.getWait());
    }
    @Override
    String[] createCommand() {
        String[] commands = {
                Paths.get(project.getBuild().getDirectory(), "ocp", "bin", SystemUtils.systemSpecificSubDirectory(), "oc").toString(),
                "delete",
                "-k",
                Paths.get(project.getBasedir().toPath().toString(), "target", "ocp", "kustomize", "my-camel", "overlays", overlay).toString()
        };
        return commands;
    }
    public static class Builder extends BaseBuilder<OCDeleteCommand.Builder> {

        public Builder(MavenProject project, String overlay, String application) {
            super(project, overlay, application);
        }

        @Override
        OCDeleteCommand.Builder getBuilder() {
            return this;
        }

        public OCDeleteCommand build() {
            OCDeleteCommand command = new OCDeleteCommand(this);
            validate(command);
            return command;
        }

        private void validate(OCDeleteCommand command) {

        }
    }
}
