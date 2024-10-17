package dgt.cloud.demo.service.impl;


import dgt.cloud.demo.dto.base.CubRequest;
import dgt.cloud.demo.dto.controller.querydemoitem.QueryDemoRq;
import dgt.cloud.demo.dto.controller.querydemoitem.QueryDemoRs;
import dgt.cloud.demo.service.DemoService;
import dgt.cloud.demo.service.helper.DemoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

//
//@Slf4j
@Service
public class DemoServiceImpl implements DemoService {

	@Autowired
    DemoHelper demoHelper;

	@Override
	public QueryDemoRs demoService(CubRequest<QueryDemoRq> request) {
		try{
			QueryDemoRq tranRq = request.getTranRq();
			int delay = tranRq.getDelay();
			TimeUnit.SECONDS.sleep(delay);
		}catch (Exception e){
			//範例使用未做處理，實際使用Service異常需進行處理或由全域異常處理統一處理。
			Logger.getLogger("service error").log(java.util.logging.Level.SEVERE, e.getMessage(), e);
//			log.error(e.getMessage(), e);
		}finally {

			return demoHelper.getRs();
		}
	}
}
