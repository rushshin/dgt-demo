package dgt.cloud.demo.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TranRs {
    //執行環境
    @JsonProperty("env")
    private String env;
    //執行回傳
    @JsonProperty("data")
    private String data;
}
