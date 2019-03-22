package com.kontrerd.mtransfer.rest;

import com.kontrerd.mtransfer.core.model.Account;
import com.kontrerd.mtransfer.core.services.AccountService;
import com.kontrerd.mtransfer.core.exceptions.ValidationException;
import com.kontrerd.mtransfer.core.services.HistoryService;
import com.kontrerd.mtransfer.rest.model.TransferRequest;
import com.kontrerd.mtransfer.rest.model.UpdateRequest;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

@Path("api/v1/accounts")
public class AccountResource {

    @Inject
    private AccountService accountService;
    @Inject
    private HistoryService historyService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Long id) {
        return Response.ok(accountService.getById(id)).build();
    }

    @GET
    @Path("/{id}/history")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHistory(@PathParam("id") Long id) {
        return Response.ok(historyService.get(id)).build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("sortBy") String sortBy,
                           @QueryParam("page") Long page,
                           @QueryParam("pageSize") Long pageSize) {
        return Response.ok(accountService.getAll(sortBy, page, pageSize)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Account account) {
        return Response.ok(accountService.create(account)).build();
    }

    @POST
    @Path("/batch")
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(List<Account> accounts) {
        accountService.create(accounts);
        return Response.ok().build();
    }

    @POST
    @Path("/charge")
    @Produces(MediaType.APPLICATION_JSON)
    public Response charge(UpdateRequest request) {
        return Response
                .ok(accountService.charge(
                        getValue(request, UpdateRequest::getId),
                        getValue(request, UpdateRequest::getAmount)))
                .build();
    }

    @POST
    @Path("/withdraw")
    @Produces(MediaType.APPLICATION_JSON)
    public Response withdraw(UpdateRequest request) {
        return Response
                .ok(accountService.withdraw(
                        getValue(request, UpdateRequest::getId),
                        getValue(request, UpdateRequest::getAmount)))
                .build();
    }

    @POST
    @Path("/transfer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer(TransferRequest request) {
        accountService.transfer(
                getValue(request, TransferRequest::getSrcId),
                getValue(request, TransferRequest::getDestinationId),
                getValue(request, TransferRequest::getAmount));
        return Response.ok().build();
    }

    private <T, R> R getValue(T obj, Function<T, R> getter) {
        return ofNullable(obj).map(getter).orElseThrow(ValidationException::new);
    }
}
