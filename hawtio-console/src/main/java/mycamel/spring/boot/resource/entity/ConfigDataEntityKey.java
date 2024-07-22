package mycamel.spring.boot.resource.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfigDataEntityKey implements Comparable<ConfigDataEntityKey> {

	private String application;

	private String profile;

	private String label;

	@Override
	public int compareTo(ConfigDataEntityKey o) {
		int result = -1;
		if (application != null) {
			result = application.compareTo(o.getApplication());
		}
		if (result == 0 && profile != null) {
			result = profile.compareTo(o.getProfile());
		}
		if (result == 0 && label != null) {
			result = label.compareTo(o.getLabel());
		}
		return result;
		// how to compare this.getXList() and o.getXList() with compareTo?
	}

}
