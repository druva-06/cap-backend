package com.meritcap.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.util.HashMap;
import java.util.Map;

public class AwsSecretsEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String secretNamesStr = environment.getProperty("aws.secret.names");
        String region = environment.getProperty("aws.region");

        if (secretNamesStr == null || region == null) {
            System.out.println("AWS secret names or region not set; skipping secret load.");
            return;
        }

        String[] secretNames = secretNamesStr.split(",");

        Map<String, Object> allSecrets = new HashMap<>();

        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(Region.of(region))
                .build();

        ObjectMapper mapper = new ObjectMapper();

        for (String secretName : secretNames) {
            secretName = secretName.trim();  // remove extra spaces

            try {
                GetSecretValueRequest request = GetSecretValueRequest.builder()
                        .secretId(secretName)
                        .build();

                GetSecretValueResponse response = client.getSecretValue(request);
                String secretString = response.secretString();

                Map<String, Object> secrets = mapper.readValue(secretString, Map.class);

                allSecrets.putAll(secrets);  // merge into combined map

                System.out.println("Loaded secrets from: " + secretName);
            } catch (Exception e) {
                throw new RuntimeException("Failed to load secret: " + secretName, e);
            }
        }

        // Inject combined secrets as high-priority properties
        environment.getPropertySources().addFirst(new MapPropertySource("awsSecrets", allSecrets));
    }
}
