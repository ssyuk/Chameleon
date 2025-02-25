package chameleon.utils;

public record Version(int major, int minor, int patch) {
    public Version {
        if (major < 0 || minor < 0 || patch < 0) {
            throw new IllegalArgumentException("Version components cannot be negative");
        }
    }

    public boolean isNewerThan(Version other) {
        return this.compareTo(other) > 0;
    }

    public boolean isOlderThan(Version other) {
        return this.compareTo(other) < 0;
    }

    public int compareTo(Version other) {
        if (major != other.major) {
            return Integer.compare(major, other.major);
        }
        if (minor != other.minor) {
            return Integer.compare(minor, other.minor);
        }
        return Integer.compare(patch, other.patch);
    }

    public String toString() {
        return String.format("%d.%d.%d", major, minor, patch);
    }

    public static Version fromString(String version) {
        String[] components = version.split("\\.", 3);
        if (components.length != 3) {
            throw new IllegalArgumentException("Invalid version string: " + version);
        }
        return new Version(Integer.parseInt(components[0]), Integer.parseInt(components[1]), Integer.parseInt(components[2]));
    }
}
