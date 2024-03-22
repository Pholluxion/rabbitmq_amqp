package com.phollux.producer;

import com.phollux.model.Payment;
import io.smallrye.mutiny.Multi;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.net.URI;
import java.util.*;

@Path("/payment")
public class PaymentResource {

    List<Payment> payments = new ArrayList<>();
    @Channel("requests")
    Emitter<JsonObject> paymentRequestEmitter;

    @Channel("payments")
    Multi<JsonObject> paymentsStream;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPayments() {

        paymentsStream.subscribe().with(
            item -> {
                final boolean removed = payments.removeIf(payment -> payment.getId().equals(item.getString("id")));
                if (removed) payments.add(item.mapTo(Payment.class));
            }
        );


        return Response.ok(payments).build();
    }


    @POST
    @Path("/request")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPaymentRequest(
            @QueryParam("amount") String amount,
            @QueryParam("paymentType") String paymentType
    ) {
        try {
            final  Payment payment = new Payment();
            payment.setAmount(amount);
            payment.setPaymentType(paymentType);
            payment.setId(UUID.randomUUID().toString());
            payment.setPaymentStatus("PENDING");
            payment.setPaymentStatusString("Payment pending");

            payments.add(payment);

            paymentRequestEmitter.send(JsonObject.mapFrom(payment));

            return Response.accepted().entity(payment).build();
        }
        catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }

    }
}
