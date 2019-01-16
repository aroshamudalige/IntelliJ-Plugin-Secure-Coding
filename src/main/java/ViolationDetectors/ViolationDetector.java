package ViolationDetectors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface ViolationDetector {

    String rule1Detection();
    String rule2Detection();
    String rule3Detection();
    String rule4Detection();
    String rule5Detection();
    Map<String, ArrayList<Integer>> lce = new HashMap<String, ArrayList<Integer>>();

}
