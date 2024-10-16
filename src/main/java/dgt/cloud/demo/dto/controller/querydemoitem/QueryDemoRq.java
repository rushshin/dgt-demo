package dgt.cloud.demo.dto.controller.querydemoitem;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class QueryDemoRq {
	//等待時間
	@JsonProperty("delay")
	private int delay = 0;
	//for次數
	@JsonProperty("forCount")
	private int forCount = 0;

}
