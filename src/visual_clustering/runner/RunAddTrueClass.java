package visual_clustering.runner;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.reader.GeneratedCSVReader;
import pl.pwr.hiervis.util.HierarchyUtils;
import visual_clustering.parser.AddTrueClassCmdLineParser;
import visual_clustering.parameters.AddTrueClassParameters;
import visual_clustering.logic.AddTrueClass;

import java.io.IOException;

public class RunAddTrueClass {
    public static void main(String[] args) {
//        args = new String[]{"-d", "dest.csv",//najlepsze_z_manual_wzgledem_INTERANL/CHRIS_031.csv",
//                "-s", "source.csv"//orginalne_gt/GT_postprocess_b6_a-5_l-01_g-1_N-3000_d-5_P-1_Q-5_minSD-005_maxSd-10_031.gt.csv"
//        };
        AddTrueClassCmdLineParser parser = new AddTrueClassCmdLineParser();
        AddTrueClassParameters params = new AddTrueClassParameters();
        parser.parse(args, params);

        GeneratedCSVReader reader = new GeneratedCSVReader(true);
        try {
            Hierarchy sourceHierarchy = reader.load(params.getSourceDataFilePath().toString(), params.isInstanceName(),
                    true, true, false, true);
            Hierarchy destinationHierarchy = reader.load(params.getDestinationDataFilePath().toString(), params.isInstanceName(),
                    false, true, false, true);

            AddTrueClass.addGroundTruthAssignmentToHierarchy(destinationHierarchy, sourceHierarchy);

            HierarchyUtils.save("GT_" + params.getDestinationDataFilePath().getFileName(), destinationHierarchy, true, true,
                    params.isInstanceName(), true);
        } catch(IOException e) {
            System.err.println("While loading source/destination hierarchy " + e.getMessage());
            System.exit(1);
        }

    }
}
