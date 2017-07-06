package visual_clustering.logic;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.interfaces.Node;
import basic_hierarchy.reader.GeneratedCSVReader;
import common.HierarchyOperations;
import common.CommonUtils;
import pl.pwr.hiervis.util.HierarchyUtils;
import utils.Constans;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.LinkedList;

import static common.HierarchyOperations.createHierarchyFromNode;
import static common.HierarchyOperations.getHierarchyLeaves;

public class PruneHierarchyAndPerformHkOnLeaves {
    private final Path methodOutputPath;
    private final File tmpResultsDirectory;
    private final LinkedList<String> methodInvocationCommandWithoutInput;
    private final boolean inputHierarchyContainsGroundTruth;
    private final boolean inputHierarchyContainsInstanceNames;
    private int numberOfGeneratedLeafHierarchies;
    private static final String nameOfLeafHierarchyFile = "leafHierarchy.csv";
    private File leafHierarchyResultDirectory;

    public PruneHierarchyAndPerformHkOnLeaves(Path outputDir, LinkedList<String> methodInvocationCommandWithoutInput) {
        this.methodOutputPath = outputDir;
        this.tmpResultsDirectory = createTmpDirectoryCleanIfExists(outputDir);
        this.methodInvocationCommandWithoutInput = methodInvocationCommandWithoutInput;
        this.inputHierarchyContainsGroundTruth = methodInvocationCommandWithoutInput.contains("-c");
        this.inputHierarchyContainsInstanceNames = methodInvocationCommandWithoutInput.contains("-in");
        this.numberOfGeneratedLeafHierarchies = 0;
    }
    public Hierarchy run(Hierarchy h, int cutHeight) {
        Hierarchy cutHierarchy = HierarchyOperations.prune(h, cutHeight);
        return performHkOnLeaves(cutHierarchy);
    }

    private Hierarchy performHkOnLeaves(Hierarchy cutHierarchy) {
        Hierarchy result = cutHierarchy;
        for(Node l: getHierarchyLeaves(cutHierarchy)) {
            this.leafHierarchyResultDirectory =  createDirectoryCleanIfExists(
                    this.tmpResultsDirectory.getAbsolutePath() + File.separator
                            + (this.numberOfGeneratedLeafHierarchies++) + File.separator);
            Hierarchy leafOch = performMethodOnNode(cutHierarchy.getDataNames(), l);
            result = HierarchyUtils.merge(leafOch, result, l.getId());
            System.out.println("checkpoint");
        }
        return result;
    }

    private Hierarchy performMethodOnNode(String[] baseHierarchyDataNames, Node l) {
        Hierarchy resultHierarchy = null;
        Hierarchy leafHierarchy = createHierarchyFromNode(baseHierarchyDataNames, l);
        try {
            String inputHierarchyPath = saveLeafHierarchyAndGetItsPath(leafHierarchy);
            startMethodProcess(inputHierarchyPath);
            resultHierarchy = getMethodResult();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultHierarchy;
    }

    private Hierarchy getMethodResult() throws IOException {
        String resultHierarchyFile = this.leafHierarchyResultDirectory.getAbsolutePath() + File.separator + Constans.finalHierarchyOfGroupsFileName;
        return new GeneratedCSVReader().load(resultHierarchyFile, inputHierarchyContainsInstanceNames, inputHierarchyContainsGroundTruth, false, false, true);
    }

    private void startMethodProcess(String inputHierarchyPath) throws IOException {
        LinkedList<String> methodInvocationCommand = tailorMethodInvocationParameters(inputHierarchyPath);
        System.out.println("Starting method for " + this.numberOfGeneratedLeafHierarchies + " leaf hierarchy.");
        Process process = new ProcessBuilder(methodInvocationCommand).redirectErrorStream(true).start();
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        try {
            if(process.waitFor() != 0) {
                System.err.println("Method invocation for " + inputHierarchyPath + " didn't finish successfully. Exiting.");
                System.exit(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        in.close();
    }

    private LinkedList<String> tailorMethodInvocationParameters(String inputHierarchyPath) {
        LinkedList<String> methodInvocationCommand = new LinkedList<String>(methodInvocationCommandWithoutInput);
        methodInvocationCommand.add("-i");
        methodInvocationCommand.add(inputHierarchyPath);
        int outputElemIndex = methodInvocationCommand.indexOf(this.methodOutputPath.toString());
        methodInvocationCommand.set(outputElemIndex, inputHierarchyPath.substring(0, inputHierarchyPath.lastIndexOf(File.separator)));
        return methodInvocationCommand;
    }

    private  String saveLeafHierarchyAndGetItsPath(Hierarchy leafHierarchy) throws IOException {
        String inputHierarchyPath = this.leafHierarchyResultDirectory.getAbsolutePath() + File.separator + this.nameOfLeafHierarchyFile;
        HierarchyUtils.save(inputHierarchyPath, leafHierarchy, false, this.inputHierarchyContainsGroundTruth,
                this.inputHierarchyContainsInstanceNames, true);
        return inputHierarchyPath;
    }

    private static File createTmpDirectoryCleanIfExists(Path pathToWhereCreate) {
        String tmpDir = pathToWhereCreate.toAbsolutePath() + File.separator + "tmp";
        return createDirectoryCleanIfExists(tmpDir);
    }
    private static File createDirectoryCleanIfExists(String directoryPath) {
        File dir = new File(directoryPath);
        if(dir.exists()) {
            CommonUtils.deleteDirectory(dir.toPath());
        }
        dir.mkdirs();//TODO niby mkdirs potrzebuje absolutnej sciezki nie relatywnej, zobaczyc, jak to dziala
        return dir;
    }
}
