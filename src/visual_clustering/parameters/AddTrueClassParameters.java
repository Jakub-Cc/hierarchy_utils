package visual_clustering.parameters;

import common.Parameters;

import java.nio.file.Path;

public class AddTrueClassParameters implements Parameters {
    private Path destinationDataFilePath;
    private Path sourceDataFilePath;
    private boolean instanceName;

    public Path getSourceDataFilePath() {
        return sourceDataFilePath;
    }

    public void setSourceDataFilePath(Path sourceDataFilePath) {
        this.sourceDataFilePath = sourceDataFilePath;
    }

    public Path getDestinationDataFilePath() {
        return destinationDataFilePath;
    }

    public void setDestinationDataFilePath(Path destinationDataFilePath) {
        this.destinationDataFilePath = destinationDataFilePath;
    }

    public boolean isInstanceName() {
        return instanceName;
    }

    public void setInstanceName(boolean instanceName) {
        this.instanceName = instanceName;
    }
}
