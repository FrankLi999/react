package mycamel.spring.boot.camel.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
public class ResponseMessage {

	public enum Severity {

		I("I"), E("E"), W("W");

		private String level;

		Severity(String level) {
			this.level = level;
		}

		@JsonValue
		public String getLevel() {
			return this.level;
		}

		@Override
		public String toString() {
			return String.valueOf(this.level);
		}

		@JsonCreator
		public static Severity fromValue(String level) {
			for (Severity s : Severity.values()) {
				if (s.level.equals(level)) {
					return s;
				}
			}
			throw new IllegalArgumentException("Unexpected Severity level " + level);
		}

	}

	private String messageCode;

	private String logMessage;

	private Severity severity;

}
