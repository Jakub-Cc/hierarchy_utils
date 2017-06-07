package visual_clustering.utils;

import common.CmdLineParser;
import common.Parameters;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class VisualClusteringCmdLineParser extends CmdLineParser {

    public VisualClusteringCmdLineParser() {
        super();
    }

    @Override
    protected void createOptions() {
        Option destination = OptionBuilder.withArgName("file path")
                .hasArgs(1)
                .isRequired(false)
                .withDescription("Path to file that lacks of true class attribute, but has assigned class.")
                .withLongOpt("destination")
                .create('d');

        Option source = OptionBuilder.withArgName("file path")
                .hasArgs(1)
                .isRequired(false)
                .withDescription("Path to file that contains true class attribute, that will be transferred to destination file.")
                .withLongOpt("source")
                .create('s');

        options.addOption("in", "instance-name", false, "Provided input file contains also a unique name of every instance, which "
                + "will be omitted by this program. Assumed that instance names are in the third column (attribute) in "
                + "input file when the class attribute is also provided or in the second column otherwise.");

        options.addOption(destination);
        options.addOption(source);
    }

    @Override
    protected void parseParameters(Parameters paramsToSet) {
        VisualClusteringParameters params = (VisualClusteringParameters)paramsToSet;

        params.setDestinationDataFilePath(parsePath("d"));
        params.setSourceDataFilePath(parsePath("s"));
        params.setInstanceName(cmd.hasOption("in"));
    }
}
