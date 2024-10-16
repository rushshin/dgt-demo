package dgt.cloud.demo.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
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
		log.info("[DGTcalendar Log] Rest Request URI : {}", request.getURI());
		log.info("[DGTcalendar Log] Rest Request Method : {}", request.getMethod());
		log.info("[DGTcalendar Log] Send Request -> {}", new String(body, Charset.forName("UTF-8")));
	}

	private void traceResponse(ClientHttpResponse response) throws IOException {
		if (response != null) {
			log.info("[DGTcalendar Log] Rest Response StatusCode: {}", response.getStatusCode());
			log.info("[DGTcalendar Log] Rest Response StatusText: {}", response.getStatusText());
			log.info("[DGTcalendar Log] Get Response <- {}", StreamUtils.copyToString(response.getBody(), Charset.forName("UTF-8")));
		}
	}

}
