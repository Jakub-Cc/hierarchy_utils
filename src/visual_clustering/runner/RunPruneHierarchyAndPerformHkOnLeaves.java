package visual_clustering.runner;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.reader.GeneratedCSVReader;
import pl.pwr.hiervis.util.HierarchyUtils;
import runner.ReadSeveralAndCalculateQualityMeasuresSeparately;
import visual_clustering.logic.PruneHierarchyAndPerformHkOnLeaves;
import visual_clustering.parameters.PruneHierarchyAndPerformHkOnLeavesParameters;
import visual_clustering.parser.PruneHierarchyAndPerformHkOnLeavesParser;

import java.io.File;
import java.io.IOException;

public class RunPruneHierarchyAndPerformHkOnLeaves {public static void main(String[] args) {
//        args = new String[]{
//    //				"-h",
//                "-ch", "2",
//                "-mp", "/home/tosterr/Programming/hkplusplus/out/artifacts/hkplusplus_jar/hkplusplus.jar",
//                "-i", "/home/tosterr/Desktop/GT_cla_013.gtcut_2_k2_s2147483600_w50_n70_r10_e10_l2_cf1.0_rf1.0_i1/GT_cla_013.gt.csv",//"/home/tosterr/Programming/hierarchy_utils/GT_CHRIS_031.csv",//"Edge_tool.sbowSIMPLE_BINARY_SEARCH/375.csv",//"easy3Simply.txt",
//                "-o", "usa",//.concat(String.valueOf(L)).concat("_E").concat(String.valueOf(E)),
//                "-lgmm",
//                "-s", "9",
//                "-v",
//                "-k", "2",
//                "-n", "70",//40//60 back
//                "-r", "10",//10 //60 back
//                "-e", "10",
//                "-l", "2",
//                "-c",
//                "-dm",
////                "-gi", //"666",
//                "-w", "50",
////    			"-in",
//                "-c"
//        };
        PruneHierarchyAndPerformHkOnLeavesParser parser = new PruneHierarchyAndPerformHkOnLeavesParser();
        PruneHierarchyAndPerformHkOnLeavesParameters params = new PruneHierarchyAndPerformHkOnLeavesParameters();
        parser.parse(args, params);

        Hierarchy hierarchyToCut = null;

        try {
            GeneratedCSVReader reader = new GeneratedCSVReader();
            hierarchyToCut = reader.load(params.getInputHierarchy().toString(), params.isContainsInstanceName(),
                    params.isContainsClassAttribute(), true, false, true);
        } catch(IOException e) {
            System.err.println("While loading input hierarchy " + e.getMessage());
            System.exit(1);
        }

        PruneHierarchyAndPerformHkOnLeaves action = new PruneHierarchyAndPerformHkOnLeaves(params.getOutputDir(),
                params.getMethodInvocationCommandWithoutInput());
        Hierarchy result = action.run(hierarchyToCut, params.getCutHeight());

        String resultHierarchyPath = params.getOutputDir() + File.separator + "automatic_bottom_" + params.getInputHierarchy().getFileName();
        try {
            HierarchyUtils.save(resultHierarchyPath, result, true, params.isContainsClassAttribute(),
                    params.isContainsInstanceName(), true);
        } catch(IOException e) {
            System.err.println("While saving the final hierarchy " + e.getMessage());
            System.exit(1);
        }

        ReadSeveralAndCalculateQualityMeasuresSeparately measuresCalculator = new ReadSeveralAndCalculateQualityMeasuresSeparately();
        measuresCalculator.run(new String[]{resultHierarchyPath}, "measures.csv", params.isContainsClassAttribute(),
                params.isContainsInstanceName(), true, false);
    }
}
