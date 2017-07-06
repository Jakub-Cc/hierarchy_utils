package visual_clustering.parameters;

import common.Parameters;

import java.nio.file.Path;
import java.util.LinkedList;

public class PruneHierarchyAndPerformHkOnLeavesParameters implements Parameters {
    private Path inputHierarchy;
    private Path outputDir;
    private Path methodPath;
    private LinkedList<String> methodInvocationCommandWithoutInput;
    private int cutHeight;
    private boolean containsClassAttribute;
    private boolean containsInstanceName;

    public Path getInputHierarchy() {
        return inputHierarchy;
    }

    public void setInputHierarchy(Path inputHierarchy) {
        this.inputHierarchy = inputHierarchy;
    }

    public Path getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(Path outputDir) {
        this.outputDir = outputDir;
    }

    public LinkedList<String> getMethodInvocationCommandWithoutInput() {
        return methodInvocationCommandWithoutInput;
    }

    public void setMethodInvocationCommandWithoutInput(LinkedList<String> methodInvocationCommandWithoutInput) {
        this.methodInvocationCommandWithoutInput = methodInvocationCommandWithoutInput;
    }

    public int getCutHeight() {
        return cutHeight;
    }

    public void setCutHeight(int cutHeight) {
        this.cutHeight = cutHeight;
    }


    public Path getMethodPath() {
        return methodPath;
    }

    public void setMethodPath(Path methodPath) {
        this.methodPath = methodPath;
    }

    public boolean isContainsClassAttribute() {
        return containsClassAttribute;
    }

    public void setContainsClassAttribute(boolean containsClassAttribute) {
        this.containsClassAttribute = containsClassAttribute;
    }

    public boolean isContainsInstanceName() {
        return containsInstanceName;
    }

    public void setContainsInstanceName(boolean containsInstanceName) {
        this.containsInstanceName = containsInstanceName;
    }
}
