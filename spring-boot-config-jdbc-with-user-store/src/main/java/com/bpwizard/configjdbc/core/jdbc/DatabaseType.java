package com.bpwizard.configjdbc.core.jdbc;


import java.util.Objects;

/**
 * Enum for a list of known databases.
 *
 * @author Horia Chiorean (hchiorea@redhat.com)
 * @since 5.0
 */
public class DatabaseType implements Comparable<DatabaseType> {

    public static DatabaseType UNKNOWN = new DatabaseType(Name.UNKNOWN, 0, 0);

    public enum Name {
        MYSQL,
        POSTGRES,
        DERBY,
        HSQL,
        H2,
        SQLITE,
        DB2,
        DB2_390,
        INFORMIX,
        INTERBASE,
        FIREBIRD,
        SQLSERVER,
        ACCESS,
        ORACLE,
        SYBASE,
        CASSANDRA,
        UNKNOWN;
    }

    private final int minorVersion;
    private final int majorVersion;
    private final Name name;

    /**
     * Create a new instance which has a name, major and minor version.
     *
     * @param name the name of the database; may not be null
     * @param majorVersion the major version; must be equal to or greater than 0
     * @param minorVersion the minor version; must be equal to or greater than 0
     */
    public DatabaseType(Name name, int majorVersion, int minorVersion) {
        this.name = Objects.requireNonNull(name);
        // CheckArg.isNonNegative(majorVersion, "majorVersion");
        this.minorVersion = minorVersion;
        // CheckArg.isNonNegative(minorVersion, "minorVersion");
        this.majorVersion = majorVersion;
    }

    /**
     * Get the DB minor version
     *
     * @return an integer
     */
    public int minorVersion() {
        return minorVersion;
    }

    /**
     * Get the DB major version
     *
     * @return an integer
     */
    public int majorVersion() {
        return majorVersion;
    }

    /**
     * Get the name of the database as a string
     *
     * @return the name, never {@code null}
     */
    public String nameString() {
        return name.name();
    }

    /**
     * Get the name of the database
     *
     * @return a {@link org.modeshape.common.database.DatabaseType.Name}, never {@code null}
     */
    public Name name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DatabaseType that = (DatabaseType) o;
        return Objects.equals(minorVersion, that.minorVersion) &&
                Objects.equals(majorVersion, that.majorVersion) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minorVersion, majorVersion, name);
    }

    @Override
    public int compareTo(DatabaseType other) {
        int nameComparison = this.name.compareTo(other.name);
        if (nameComparison != 0) {
            return nameComparison;
        }
        int majorVersionComparison = Integer.compare(this.majorVersion, other.majorVersion);
        if (majorVersionComparison != 0) {
            return majorVersionComparison;
        }
        int minorVersionComparison = Integer.compare(this.minorVersion, other.minorVersion);
        if (minorVersionComparison != 0) {
            return minorVersionComparison;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Database[name=" + name + ", majorVersion=" + majorVersion + ", minorVersion=" + minorVersion + "]";
    }
}
