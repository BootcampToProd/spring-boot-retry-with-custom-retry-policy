package com.bootcamptoprod.retry.config;

import com.bootcamptoprod.retry.retry.CustomRetryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfig {

    @Value("${retry.maxAttempts:3}")
    private int maxAttempts;

    @Value("${retry.backoffInMillis:2000}")
    private long backoffInMillis;

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(backoffInMillis);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        CustomRetryPolicy retryPolicy = new CustomRetryPolicy(maxAttempts);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }
}
