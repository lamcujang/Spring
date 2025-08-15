package com.dbiz.app.tenantservice.config.client;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@Configuration
public class ClientConfig {
	
	@LoadBalanced
	@Bean
	@Primary
	public RestTemplate restTemplateBean() {
		return new RestTemplate();
	}

	@Bean
	public RestTemplate externalRestTemplate() throws Exception {
		int connectTimeout = 5000; // 5s connect timeout
		int readTimeout = 10000;   // 10s read timeout

		// Tạo TrustManager bỏ qua xác thực chứng chỉ SSL
		TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}
					public void checkClientTrusted(X509Certificate[] certs, String authType) { }
					public void checkServerTrusted(X509Certificate[] certs, String authType) { }
				}
		};

		// Tạo SSLContext với TrustManager tùy chỉnh
		SSLContext sslContext = SSLContextBuilder.create()
				.loadTrustMaterial((chain, authType) -> true) // Bỏ qua xác thực SSL
				.build();

		// Tạo HttpClient với SSLContext tùy chỉnh và bỏ qua xác thực hostname
//		CloseableHttpClient httpClient = HttpClients.custom()
//				.setSSLContext(sslContext)
//				.setSSLHostnameVerifier((hostname, session) -> true) // Bỏ qua xác thực tên miền
//				.build();

		// Cấu hình RequestConfig để xử lý CookieSpec ERPnext
		RequestConfig requestConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.STANDARD) // Chấp nhận cookie với định dạng không chuẩn
				.setConnectTimeout(connectTimeout)
				.setSocketTimeout(readTimeout)
				.build();

		// Tạo HttpClient với SSLContext và RequestConfig tùy chỉnh
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLContext(sslContext)
				.setDefaultRequestConfig(requestConfig)
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE) // Bỏ qua xác thực hostname
				.build();

		//end erpnext

		// Sử dụng HttpComponentsClientHttpRequestFactory để thiết lập HttpClient
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
		factory.setConnectTimeout(connectTimeout);
		factory.setReadTimeout(readTimeout);

		// Trả về RestTemplate với HttpClient đã cấu hình
		return new RestTemplate(factory);
	}


	@Bean
	public RestTemplate externalRestTemplateProduct() throws Exception {
		int connectTimeout = 50000; // 5s connect timeout
		int readTimeout = 100000;   // 10s read timeout

		// Tạo TrustManager bỏ qua xác thực chứng chỉ SSL
		TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}
					public void checkClientTrusted(X509Certificate[] certs, String authType) { }
					public void checkServerTrusted(X509Certificate[] certs, String authType) { }
				}
		};

		// Tạo SSLContext với TrustManager tùy chỉnh
		SSLContext sslContext = SSLContextBuilder.create()
				.loadTrustMaterial((chain, authType) -> true) // Bỏ qua xác thực SSL
				.build();

		// Tạo HttpClient với SSLContext tùy chỉnh và bỏ qua xác thực hostname
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLContext(sslContext)
				.setSSLHostnameVerifier((hostname, session) -> true) // Bỏ qua xác thực tên miền
				.build();

		// Sử dụng HttpComponentsClientHttpRequestFactory để thiết lập HttpClient với timeout
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
		factory.setConnectTimeout(connectTimeout);
		factory.setReadTimeout(readTimeout);

		// Trả về RestTemplate với HttpClient đã cấu hình
		return new RestTemplate(factory);
	}
}










