package visual_clustering.logic;

import basic_hierarchy.interfaces.Hierarchy;
import basic_hierarchy.interfaces.Instance;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class AddTrueClass {

    public static void addGroundTruthAssignmentToHierarchy(Hierarchy destination, Hierarchy sourceWithGroundTruth) {
        LinkedList<Instance>  dest = new LinkedList<>(destination.getRoot().getSubtreeInstances());
        LinkedList<Instance>  source = new LinkedList<>(sourceWithGroundTruth.getRoot().getSubtreeInstances());

        for(Instance s: source) {
            boolean found = false;
            for(Iterator<Instance> dIter = dest.iterator(); dIter.hasNext() && !found;) {
                Instance d = dIter.next();
                if(Arrays.equals(s.getData(), d.getData())) {
                    d.setTrueClass(s.getTrueClass());
                    dIter.remove();
                    found = true;
                }
            }
        }

        if(!dest.isEmpty()) {
            System.err.println("Didn't set class attribute for all of the data instances from hierarchy, there are "
                    + dest.size() + " left.");
        }
    }
}
