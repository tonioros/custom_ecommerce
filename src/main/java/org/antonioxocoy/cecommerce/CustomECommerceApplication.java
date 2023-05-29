package org.antonioxocoy.cecommerce;

import org.antonioxocoy.cecommerce.security.jwt.filters.EncriptionInterceptorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class CustomECommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomECommerceApplication.class, args);
	}
}
