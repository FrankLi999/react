
package org.springframework.boot.actuate.autoconfigure.jolokia;
public interface ControllerConfigExtractor {
    
    /**
     * Get all configuration name
     * @return enumeration of config names
     */
    Iterator<String> getNames();

    /**
     * Get the parameter for a certain
     * @param pKeyS string representation of the config key to fetch
     * @return the value of the configuration parameter or <code>null</code> if no such parameter exists
     */
    String getParameter(String pKeyS);
}
