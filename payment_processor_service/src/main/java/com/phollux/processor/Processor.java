package com.phollux.processor;

import com.phollux.model.Payment;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.util.Objects;
import java.util.Random;

@ApplicationScoped
public class Processor {
    private final Random random = new Random();

    private static final String PAYMENT_SUCCESS = "SUCCESS";
    private static final String PAYMENT_FAILED = "FAILED";
    private static final double IVA = 1.19;

    @Incoming("requests")
    @Outgoing("payments")
    @Blocking
    public JsonObject process(JsonObject p) throws InterruptedException {

        final Payment payment = new Payment();
        payment.setId(p.getString("id"));
        payment.setAmount(p.getString("amount"));
        payment.setPaymentType(p.getString("paymentType"));
        payment.setPaymentStatus(p.getString("paymentStatus"));
        payment.setPaymentStatusString(p.getString("paymentStatusString"));

        Thread.sleep(2000);

        try {

            final double paymentAmount = Double.parseDouble(payment.getAmount()) * IVA;

            payment.setPaymentStatus(random.nextBoolean() ? PAYMENT_SUCCESS : PAYMENT_FAILED);
            payment.setAmount(String.valueOf(paymentAmount));

            if (Objects.equals(payment.getPaymentStatus(), PAYMENT_SUCCESS)) {
                payment.setPaymentStatusString("Payment successful");
            } else {
                payment.setPaymentStatusString("Payment failed because of insufficient funds");
            }

            return JsonObject.mapFrom(payment);
        }
        catch (Exception e) {
            payment.setPaymentStatus(PAYMENT_FAILED);
            payment.setPaymentStatusString("Payment failed because of an error");
            return JsonObject.mapFrom(payment);
        }


    }
}
