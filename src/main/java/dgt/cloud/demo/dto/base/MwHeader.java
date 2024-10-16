package dgt.cloud.demo.dto.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class MwHeader {

	@NotBlank(message = "MSGID is blank")
	@Size(message = "MSGID 長度不得超過{max}", max = 20)
	@JsonProperty("MSGID")
	private String msgId;

	@NotBlank(message = "SOURCECHANNEL is blank")
	@Size(message = "SOURCECHANNEL 長度不得超過{max}", max = 20)
	@JsonProperty("SOURCECHANNEL")
	private String sourceChannel;

	@NotBlank(message = "TXNSEQ is blank")
	@Size(message = "TXNSEQ 長度不得超過{max}", max = 20)
	@JsonProperty("TXNSEQ")
	private String txnSeq;

	@JsonProperty("RETURNCODE")
	private String returnCode;

	@JsonProperty("RETURNDESC")
	private String returnDesc;

	@JsonProperty("O360SEQ")
	private String o360Seq;

}
