package com.kibocommerce.sdkdemo;

import com.kibocommerce.sdk.inventory.ApiException;
import com.kibocommerce.sdk.inventory.api.InventoryControllerApi;
import com.kibocommerce.sdk.inventory.api.ModifyInventoryControllerApi;
import com.kibocommerce.sdk.inventory.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class SdkdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SdkdemoApplication.class, args);
    }

    private InventoryControllerApi inventoryControllerApi;

    private ModifyInventoryControllerApi modifyInventoryControllerApi;

    @Value("${client.tenantID}")
    private Integer tenantID;

    @Value("${client.siteID}")
    private Integer siteID;

    public SdkdemoApplication(InventoryControllerApi inventoryControllerApi, ModifyInventoryControllerApi modifyInventoryControllerApi) {
        this.inventoryControllerApi = inventoryControllerApi;
        this.modifyInventoryControllerApi = modifyInventoryControllerApi;
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {

            //upc of the product you want to refresh and get inventory for
            String upc = "testupc";
            //code for the exact location of the product you want to refresh and get inventory for
            String locationCode = "70";
            //qty you want to set for the product
            Integer productQty = 55;

            RefreshItem refreshItem = new RefreshItem();
            refreshItem.setUpc(upc);
            refreshItem.setQuantity(productQty);

            RefreshRequest refreshRequest = new RefreshRequest();
            refreshRequest.addItemsItem(refreshItem);

            refreshRequest.setLocationCode(locationCode);

            try {
                //call refresh API to create inventory for item
                var refreshResponse = modifyInventoryControllerApi.refresh(tenantID, refreshRequest);
                System.out.println(refreshResponse);
            }
            catch (ApiException ex) {
                System.out.println(ex.getResponseBody());
            }

            //let the inventory job complete
            TimeUnit.SECONDS.sleep(2);

            List<ItemQuantity> itemQuantities = new ArrayList<>();

            ItemQuantity itemQuantity = new ItemQuantity();
            itemQuantity.setUpc(upc);
            itemQuantity.setQuantity(1);

            itemQuantities.add(itemQuantity);

            RequestLocation requestLocation = new RequestLocation();
            requestLocation.setLocationCode(locationCode);
            requestLocation.setUnit(null);

            InventoryRequest inventoryRequest = new InventoryRequest();
            inventoryRequest.setType(InventoryRequest.TypeEnum.ALL);
            inventoryRequest.setItems(itemQuantities);
            inventoryRequest.setRequestLocation(requestLocation);

            try {
                //get the inventory for the given upc at the specified location code
                var inventoryResponse = inventoryControllerApi.postQueryInventory(tenantID, inventoryRequest, siteID);
                System.out.println(inventoryResponse);
            }
            catch (ApiException ex) {
                System.out.println(ex.getResponseBody());
            }

        };
    }
}
