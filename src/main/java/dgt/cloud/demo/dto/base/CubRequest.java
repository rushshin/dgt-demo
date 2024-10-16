package dgt.cloud.demo.dto.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class CubRequest<T> {

	@Valid
	@NotNull(message = "MWHEADER is null")
	@JsonProperty("MWHEADER")
	private MwHeader mwHeader;

	@Valid
	@NotNull(message = "TRANRQ is null")
	@JsonProperty("TRANRQ")
	private T tranRq;

}
