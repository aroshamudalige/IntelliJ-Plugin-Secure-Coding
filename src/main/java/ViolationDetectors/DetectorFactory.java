package ViolationDetectors;

public class DetectorFactory {

    public ViolationDetector getViolatorType(String ViolatorType){
        if(ViolatorType == null){
            return null;
        }
        if(ViolatorType.equalsIgnoreCase("MethodLevelViolationDetector")){
            return new MethodLevelViolationDetector();

        } else if(ViolatorType.equalsIgnoreCase("ClassLevelViolationDetector")){
            return new ClassLevelViolationDetector();

        } else if(ViolatorType.equalsIgnoreCase("PackageLevelViolationDetector")){
            return new PackageLevelViolationDetector();
        }
        return null;
    }
}
