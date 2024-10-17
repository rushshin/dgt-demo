package dgt.cloud.demo.interceptor;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

//@Slf4j
public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		ClientHttpResponse response = null;

		try {
			traceRequest(request, body);

			response = execution.execute(request, body);
		} finally {
			traceResponse(response);
		}

		return response;
	}

	private void traceRequest(HttpRequest request, byte[] body) throws IOException {
		Logger.getLogger("DGTcalendar Log").info("[DGTcalendar Log] Rest Request URI :" + request.getURI());
		Logger.getLogger("DGTcalendar Log").info("[DGTcalendar Log] Rest Request Method :" + request.getMethod());
		Logger.getLogger("DGTcalendar Log").info("[DGTcalendar Log] Send Request -> " + new String(body, StandardCharsets.UTF_8));

//		log.info("[DGTcalendar Log] Rest Request URI : {}", request.getURI());
//		log.info("[DGTcalendar Log] Rest Request Method : {}", request.getMethod());
//		log.info("[DGTcalendar Log] Send Request -> {}", new String(body, Charset.forName("UTF-8")));
	}

	private void traceResponse(ClientHttpResponse response) throws IOException {
		if (response != null) {
			Logger.getLogger("DGTcalendar Log").info("[DGTcalendar Log] Rest Response StatusCode: " + response.getStatusCode());
			Logger.getLogger("DGTcalendar Log").info("[DGTcalendar Log] Rest Response StatusText: " + response.getStatusText());
			Logger.getLogger("DGTcalendar Log").info("[DGTcalendar Log] Get Response <- " + StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8));

//			log.info("[DGTcalendar Log] Rest Response StatusCode: {}", response.getStatusCode());
//			log.info("[DGTcalendar Log] Rest Response StatusText: {}", response.getStatusText());
//			log.info("[DGTcalendar Log] Get Response <- {}", StreamUtils.copyToString(response.getBody(), Charset.forName("UTF-8")));
		}
	}

}
