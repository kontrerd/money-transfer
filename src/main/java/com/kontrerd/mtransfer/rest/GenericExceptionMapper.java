package com.kontrerd.mtransfer.rest;

import com.kontrerd.mtransfer.core.exceptions.MoneyTransferBusinessException;
import com.kontrerd.mtransfer.rest.model.ErrorEntity;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

@Provider
@Slf4j
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable ex) {
        log.error("Request failed: ", ex);
        Response errorResponse;
        if (ex instanceof MoneyTransferBusinessException) {
            errorResponse = Response
                    .status(BAD_REQUEST)
                    .entity(new ErrorEntity(
                            ((MoneyTransferBusinessException) ex).getErrorCode().getValue(),
                            ex.getMessage()))
                    .build();

        } else {
            errorResponse = Response
                    .status(INTERNAL_SERVER_ERROR)
                    .entity(
                            new ErrorEntity(INTERNAL_SERVER_ERROR.getStatusCode(), ex.getMessage()))
                    .build();
        }

        return errorResponse;
    }

}
