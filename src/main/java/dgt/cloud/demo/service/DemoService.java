package dgt.cloud.demo.service;

import dgt.cloud.demo.dto.base.CubRequest;
import dgt.cloud.demo.dto.controller.querydemoitem.QueryDemoRq;
import dgt.cloud.demo.dto.controller.querydemoitem.QueryDemoRs;

public interface DemoService {
	public QueryDemoRs demoService(CubRequest<QueryDemoRq> request);
}
