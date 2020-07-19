package de.craftlancer.core;

public class SemanticVersion {
    private final int major;
    private final int minor;
    private final int revision;
    
    public SemanticVersion(int major, int minor, int revision) {
        this.major = major;
        this.minor = minor;
        this.revision = revision;
    }
    
    public int getMajor() {
        return major;
    }
    
    public int getMinor() {
        return minor;
    }
    
    public int getRevision() {
        return revision;
    }
    
    public static SemanticVersion of(String string) {
        String[] arr = string.split("\\.");
        int major = Utils.parseIntegerOrDefault(arr.length > 0 ? arr[0] : "0", 0);
        int minor = Utils.parseIntegerOrDefault(arr.length > 1 ? arr[1] : "0", 0);
        int revision = Utils.parseIntegerOrDefault(arr.length > 2 ? arr[2] : "0", 0);
        return new SemanticVersion(major, minor, revision);
    }
}
