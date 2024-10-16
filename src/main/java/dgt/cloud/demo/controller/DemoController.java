package dgt.cloud.demo.controller;

import dgt.cloud.demo.dto.base.CubRequest;
import dgt.cloud.demo.dto.base.MwHeader;
import dgt.cloud.demo.dto.base.TranRs;
import dgt.cloud.demo.dto.controller.querydemoitem.QueryDemoRq;
import dgt.cloud.demo.dto.controller.querydemoitem.QueryDemoRs;
import dgt.cloud.demo.service.DemoService;
import dgt.cloud.demo.service.helper.DemoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;


@RestController
public class DemoController {
    @Autowired
    private DemoService demoService;
    @Autowired
    DemoHelper demoHelper;
    @Value("${env}")
    private String env;

    @PostMapping(value = "/demo")
    public QueryDemoRs demoController(@RequestBody CubRequest<QueryDemoRq> request) throws InterruptedException {
        long time1, time2;
        time1 = System.currentTimeMillis();

        QueryDemoRq tranRq = request.getTranRq();
        int delay = tranRq.getDelay();
        int forCount = tranRq.getForCount();
        TimeUnit.SECONDS.sleep(delay);

        for (int i = 0; i < forCount; i++) {
            for (int x = 0; x <= 10000000; x++) {
                "Foo".matches("F.*");
            }
        }
        time2 = System.currentTimeMillis();

        QueryDemoRs queryDemoRs = new QueryDemoRs();
        TranRs tranRs = new TranRs();
        tranRs.setEnv(env);
        tranRs.setData("總耗費時：" + (time2 - time1) / 1000 + "秒");
        MwHeader mwHeader = request.getMwHeader();
        queryDemoRs.setMwHeader(mwHeader);
        queryDemoRs.setTranrs(tranRs);

        return queryDemoRs;
    }

    @PostMapping(value = "/camel")
    public QueryDemoRs camelTest(@RequestBody CubRequest<QueryDemoRq> request) throws InterruptedException {
        String jsonBody = "";
        // validation and routing
//        final Exchange requestExchange = ExchangeBuilder.anExchange(camelContext).withBody(jsonBody).build();
//        ProducerTemplate pt = requestExchange.getContext().createProducerTemplate();
//        final Exchange response = pt.send("http://127.0.0.1:8080/demo", requestExchange);

        //final Exchange response = pt.send("http://127.0.0.1/BOP-C-CALENDARQLST.asp?bridgeEndpoint=true&socketTimeout=32000&clientConnectionManager=#connectionManager&httpMethod=POST&o360Seq=${exchange.getProperty('o360Seq')}", requestExchange);
//        from("123").routeId("Gateway Route")
//                .toD("http4://127.0.0.1/BOP-C-CALENDARQLST.asp?bridgeEndpoint=true&socketTimeout=32000&clientConnectionManager=#connectionManager&httpMethod=POST&o360Seq=${exchange.getProperty('o360Seq')}")

        QueryDemoRs queryDemoRs = new QueryDemoRs();
        MwHeader mwHeader = request.getMwHeader();
        queryDemoRs.setMwHeader(mwHeader);

        return queryDemoRs;
    }

    @PostMapping(value = "/httpclient")
    public QueryDemoRs httpclientTest(@RequestBody CubRequest<QueryDemoRq> request) throws InterruptedException, IOException {
        String jsonBody = "";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requestx = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create(" http://dgt-svc-cteam.midlxdgt02:8080/cteam-native/middleware/god")).POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(requestx, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());

        QueryDemoRs queryDemoRs = new QueryDemoRs();
        MwHeader mwHeader = request.getMwHeader();
        queryDemoRs.setMwHeader(mwHeader);
        TranRs tranRs = new TranRs();
        tranRs.setData(response.body());
        queryDemoRs.setTranrs(tranRs);

        return queryDemoRs;
    }

    // get request for health-check
    @GetMapping(value = "/health")
    public QueryDemoRs healthCheck() throws InterruptedException, IOException {
        String jsonBody = "health is good";

        QueryDemoRs getDemoRs = new QueryDemoRs();

        TranRs tranRs = new TranRs();
        tranRs.setData(jsonBody);
        getDemoRs.setTranrs(tranRs);

        System.out.println(System.getProperty("java.class.path"));

        return getDemoRs;
    }

}