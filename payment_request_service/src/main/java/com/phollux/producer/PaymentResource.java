package com.phollux.producer;

import com.phollux.model.Payment;
import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.annotations.Broadcast;
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
    @Broadcast
    @Channel("requests")
    Emitter<JsonObject> paymentRequestEmitter;


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
