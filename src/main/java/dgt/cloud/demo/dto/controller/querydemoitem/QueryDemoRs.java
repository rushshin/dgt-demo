package dgt.cloud.demo.dto.controller.querydemoitem;

import com.fasterxml.jackson.annotation.JsonProperty;
import dgt.cloud.demo.dto.base.MwHeader;
import dgt.cloud.demo.dto.base.TranRs;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryDemoRs {

	@JsonProperty("MWHEADER")
	private MwHeader mwHeader;

	@JsonProperty("TRANRS")
	private TranRs tranrs;

	public MwHeader getMwHeader() {
		return mwHeader;
	}

	public void setMwHeader(MwHeader mwHeader) {
		this.mwHeader = mwHeader;
	}

	public TranRs getTranrs() {
		return tranrs;
	}

	public void setTranrs(TranRs tranrs) {
		this.tranrs = tranrs;
	}
}
