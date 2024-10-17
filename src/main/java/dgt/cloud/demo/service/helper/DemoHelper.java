package dgt.cloud.demo.service.helper;

import dgt.cloud.demo.dto.controller.querydemoitem.QueryDemoRs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
//@Slf4j
public class DemoHelper {
	@Value("${env}")
	private String env;

	public QueryDemoRs getRs(){
		QueryDemoRs tranRs = new QueryDemoRs();
		return tranRs;
	}
}
