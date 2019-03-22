package com.kontrerd.mtransfer.rest;

import com.kontrerd.mtransfer.App;
import com.kontrerd.mtransfer.core.model.Account;
import com.kontrerd.mtransfer.rest.model.TransferRequest;
import com.kontrerd.mtransfer.rest.model.UpdateRequest;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigDecimal;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

@Category(IntegrationTest.class)
public class AccountResourceIT {

    private HttpServer server;

    @Before
    public void setUp() {
        server = App.startServer(ServiceLocatorUtilities.createAndPopulateServiceLocator());
        RestAssured.baseURI = "http://localhost/api/v1";
        RestAssured.port = 9090;
        RestAssured.defaultParser = Parser.JSON;
    }

    @After
    public void tearDown() {
        server.shutdown();
    }

    @Test
    public void getAccount_nonExistingAccountIdGiven_ShouldThrowException() {
        given().log().all().
                pathParam("id", 123).
        when().
                get("/accounts/{id}").
        then().log().all().
                statusCode(400).
                body("message", is("Account not found! Account id 123"));
    }

    @Test
    public void getPaginatedAccountsList_sortOrderAndPaginationConfigGiven_ShouldRetrieveAccountsListOfSizeThree() {
        uploadAccounts();
        given().log().all().
                param("sortBy", "id").
                param("page", "2").
                param("pageSize", "3").
        when().
                get("/accounts/all").
        then().log().all().
                statusCode(200).
                body("size()", is(3)).
                body("id", hasItems(4, 5, 6));
    }

    @Test
    public void createAccount_accountGiven_ResponseStatusCodeSuccess() {
        given().log().all().
                contentType(JSON).
                body(new Account(123L, "Test Account", BigDecimal.valueOf(100))).
        when().
                post("/accounts").
        then().log().all().
                statusCode(200);
    }

    @Test
    public void createAccountsInBatch_accountsListGiven_ResponseStatusCodeSuccess() {
        uploadAccounts().then().statusCode(200);
    }

    @Test
    public void chargeAccount_chargeAmountAndAccountIdGiven_ShouldRetrieveAccountWithUpdatedBalance() {
        uploadAccounts();
        given().log().all().
                contentType(JSON).
                body(new UpdateRequest(1L, BigDecimal.valueOf(33))).
        when().
                post("/accounts/charge").
        then().log().all().
                statusCode(200).
                body("id", is(1)).
                body("balance", is(133));
    }

    @Test
    public void withdrawAccount_withdrawAmountAndAccountIdGiven_ShouldRetrieveAccountWithUpdatedBalance() {
        uploadAccounts();
        given().log().all().
                contentType(JSON).
                body(new UpdateRequest(1L, BigDecimal.valueOf(55))).
        when().
                post("/accounts/withdraw").
        then().log().all().
                statusCode(200).
                body("id", is(1)).
                body("balance", is(45));
    }

    @Test
    public void transfer_accountsIdsAndTransferAmountGiven_VerifyThatBalancesUpdated() {
        uploadAccounts();
        given().log().all().
                contentType(JSON).
                body(new TransferRequest(1L, 2L, BigDecimal.valueOf(55))).
        when().
                post("/accounts/transfer").
        then().log().all().
                statusCode(200);


        when().
                get("accounts/1").
        then().log().all().
                body("balance", is(45));

        when().
                get("accounts/2").
        then().log().all().
                body("balance", is(255));
    }

    private Response uploadAccounts() {
        return given()
                .body(Arrays.asList(
                        new Account(1L, "Account 1", BigDecimal.valueOf(100)),
                        new Account(2L, "Account 2", BigDecimal.valueOf(200)),
                        new Account(3L, "Account 3", BigDecimal.valueOf(300)),
                        new Account(5L, "Account 5", BigDecimal.valueOf(500)),
                        new Account(4L, "Account 4", BigDecimal.valueOf(400)),
                        new Account(6L, "Account 6", BigDecimal.valueOf(600)),
                        new Account(7L, "Account 7", BigDecimal.valueOf(700)),
                        new Account(8L, "Account 8", BigDecimal.valueOf(800)),
                        new Account(9L, "Account 9", BigDecimal.valueOf(900)),
                        new Account(10L, "Account 10", BigDecimal.valueOf(1000))))
                .contentType(JSON)
                .post("/accounts/batch");
    }
}