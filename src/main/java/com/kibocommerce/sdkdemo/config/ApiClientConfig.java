package com.kibocommerce.sdkdemo.config;

import com.kibocommerce.sdk.auth.ApiException;
import com.kibocommerce.sdk.auth.api.AppAuthTicketsApi;
import com.kibocommerce.sdk.auth.model.MozuAppDevContractsOAuthAccessTokenResponse;
import com.kibocommerce.sdk.auth.model.MozuAppDevContractsOauthAuthRequest;
import com.kibocommerce.sdk.inventory.ApiClient;
import com.kibocommerce.sdk.inventory.api.InventoryControllerApi;
import com.kibocommerce.sdk.inventory.api.ModifyInventoryControllerApi;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ApiClientConfig {

    @Value("${url.host}")
    private String urlHostBasePath;

    @Value("${url.inventory.path}")
    private String inventoryServicePath;

    @Value("${url.auth.path}")
    private String authServicePath;

    @Value("${auth.clientId}")
    private String clientID;

    @Value("${auth.clientSecret}")
    private String clientSecret;

    @Value("${client.tenantID}")
    private Integer tenantID;

    @Value("${client.siteID}")
    private Integer siteID;

    @Bean
    public com.kibocommerce.sdk.auth.ApiClient authApiClient() {
        com.kibocommerce.sdk.auth.ApiClient authApiClient = new com.kibocommerce.sdk.auth.ApiClient();

        authApiClient.setBasePath(urlHostBasePath + authServicePath);
        authApiClient.setReadTimeout(7000);

        return authApiClient;
    }

    //auth api
    @Bean
    public AppAuthTicketsApi appAuthTicketsApi() {
        return new AppAuthTicketsApi(authApiClient());
    }

    @Bean
    public MozuAppDevContractsOAuthAccessTokenResponse authAccessToken() {

        MozuAppDevContractsOauthAuthRequest mozuAppDevContractsOauthAuthRequest = new MozuAppDevContractsOauthAuthRequest();
        mozuAppDevContractsOauthAuthRequest.clientId(clientID);
        mozuAppDevContractsOauthAuthRequest.clientSecret(clientSecret);
        mozuAppDevContractsOauthAuthRequest.setGrantType("client_credentials");

        try {
            MozuAppDevContractsOAuthAccessTokenResponse response = appAuthTicketsApi().oauthAuthenticateApp(tenantID, siteID, mozuAppDevContractsOauthAuthRequest);

            if (response != null) {
                return response;
            }
        }
        catch (ApiException ex) {
            System.out.println(ex.getResponseBody());
        }

        throw new RuntimeException("Could not acquire auth token");
    }

    @Bean
    public ApiClient inventoryApiClient() {
        ApiClient inventoryApiClient = new ApiClient();

        inventoryApiClient.setBasePath(urlHostBasePath + inventoryServicePath);
        inventoryApiClient.setReadTimeout(7000);
        inventoryApiClient.addDefaultHeader("Authorization", "Bearer " + authAccessToken().getAccessToken());

        return inventoryApiClient;
    }

    //inventory APIs

    @Bean
    public InventoryControllerApi inventoryControllerApi() {
        return new InventoryControllerApi(inventoryApiClient());
    }

    @Bean
    public ModifyInventoryControllerApi modifyInventoryControllerApi() {
        return new ModifyInventoryControllerApi(inventoryApiClient());
    }


}
