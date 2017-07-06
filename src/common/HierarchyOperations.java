package common;

import basic_hierarchy.common.Constants;
import basic_hierarchy.implementation.BasicInstance;
import basic_hierarchy.implementation.BasicNode;
import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.interfaces.Instance;
import basic_hierarchy.interfaces.Node;
import pl.pwr.hiervis.util.HierarchyUtils;

import java.util.Arrays;
import java.util.LinkedList;

public class HierarchyOperations {
    public static Hierarchy prune(Hierarchy h, int cutHeight) {
        Hierarchy prunedHierarchy = HierarchyUtils.clone(h, true, null);

        for(Node n: prunedHierarchy.getGroups()) {
            if(nodeIsLowerThanHeight(n.getId(), cutHeight)) {
                raiseNodeContentToLevel(prunedHierarchy, n, cutHeight);
            }
        }
        BasicNode[] prunedHierarchyNodes = Arrays.stream(prunedHierarchy.getGroups()).filter(x -> !nodeIsLowerThanHeight(x.getId(), cutHeight)).toArray(BasicNode[]::new);
        return HierarchyUtils.buildHierarchy(new LinkedList<>(Arrays.asList((BasicNode[])prunedHierarchyNodes)), prunedHierarchy.getDataNames(), true);
    }

    private static boolean nodeIsLowerThanHeight(String nodeId, int cutHeight) {
        return countNumberOfOccurences(nodeId, Constants.HIERARCHY_BRANCH_SEPARATOR) > cutHeight + 1;
    }

    private static void raiseNodeContentToLevel(Hierarchy h, Node n, int cutHeight) {
        String newNodeId = n.getId().substring(0, ordinalIndexOf(n.getId(), ".", cutHeight + 2));//plus two is because of two things: nodes naming convention (gen.0.0, the period after gen), and that we want to cut after the last number (period after number not before)//TODO: sprawdzic, czy bedzie dzialalo dobrze, szczegolnie dla wielo cyfrowych wskaznikow np gen.0.12.312
        Node whereToRise = findGroup(h, newNodeId);
        assert whereToRise != null: "Could not find a node with id " + newNodeId + " this should not happened because it means that hierarchy has vertical holes in it!";

        for(Instance i: n.getNodeInstances()) {
            i.setNodeId(newNodeId);
            whereToRise.addInstance(i);
        }
        n.setInstances(null);
    }

    public static Node[] getHierarchyLeaves(Hierarchy h) {
        return Arrays.stream(h.getGroups()).filter(x -> x.getChildren().isEmpty()).toArray(Node[]::new);
    }

    public static int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }

    public static Node findGroup(Hierarchy h, String nodeId )
    {
        for ( Node n : h.getGroups() ) {
            if ( n.getId().equals( nodeId ) )
                return n;
        }

        return null;
    }

    public static int countNumberOfOccurences(String s, String elem) {
        return s.length() - s.replace(elem, "").length();
    }

    public static Hierarchy createHierarchyFromNode(String[] dataNames, Node n) {
        LinkedList<BasicNode> nodes = new LinkedList<>();
        BasicNode root = new BasicNode(Constants.ROOT_ID, null, true);
        n.getNodeInstances().forEach(i ->
            root.addInstance(
                new BasicInstance(
                    i.getInstanceName(),
                    root.getId(),
                    i.getData(),
                    i.getTrueClass()
                )
            )
        );
        nodes.add(root);
        return HierarchyUtils.buildHierarchy(nodes, dataNames, true);
    }
}
