package visual_clustering.utils;

import common.Parameters;

import java.nio.file.Path;

public class VisualClusteringParameters implements Parameters {
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
