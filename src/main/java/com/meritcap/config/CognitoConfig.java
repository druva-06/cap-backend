package com.meritcap.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
public class CognitoConfig {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.cognito.accessKeyId:}")
    private String accessKeyId;

    @Value("${aws.cognito.secretAccessKey:}")
    private String secretAccessKey;

    @Bean
    public CognitoIdentityProviderClient cognitoClient() {
        var builder = CognitoIdentityProviderClient.builder()
                .region(Region.of(awsRegion));

        if (accessKeyId != null && !accessKeyId.isEmpty() &&
                secretAccessKey != null && !secretAccessKey.isEmpty()) {
            builder.credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKeyId, secretAccessKey)));
        }

        return builder.build();
    }
}
