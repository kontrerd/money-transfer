package com.kontrerd.mtransfer;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kontrerd.mtransfer.core.model.Account;
import com.kontrerd.mtransfer.core.services.AccountService;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

public class App {

    public static void main(String[] args) throws IOException {
        ServiceLocator serviceLocator = ServiceLocatorUtilities.createAndPopulateServiceLocator();
        startServer(serviceLocator);
        if (ofNullable(args).map(Arrays::asList).orElse(emptyList()).contains("loadDummyData")) {
            uploadDummyData(serviceLocator);
        };
    }

    public static HttpServer startServer(ServiceLocator serviceLocator) {
        return GrizzlyHttpServerFactory.createHttpServer(
                UriBuilder.fromUri("http://localhost/").port(9090).build(),
                new ResourceConfig().packages("com.kontrerd.mtransfer"),
                serviceLocator);
    }

    private static void uploadDummyData(ServiceLocator serviceLocator) throws IOException {
        AccountService accountService = serviceLocator.getService(AccountService.class);
        List<Account> accounts = new ObjectMapper().readValue(
                App.class.getResourceAsStream("/dummy_data.json"),
                new TypeReference<List<Account>>(){});
        accountService.create(accounts);
    }
}
