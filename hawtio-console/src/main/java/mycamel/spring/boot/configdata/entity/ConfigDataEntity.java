package mycamel.spring.boot.configdata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;

@Entity
@Table(name = "MY_PROPERTIES")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class ConfigDataEntity {

	@Id
	@Column(name = "PROPERTIES_ID")
	private Long id;

	@Column(name = "APPLICATION")
	private String application;

	@Column(name = "PROFILE")
	private String profile;

	@Column(name = "LABEL")
	private String label;

	@Column(name = "PROP_KEY")
	private String propKey;

	@Column(name = "PROP_VALUE")
	private String propValue;

}
