package com.medical.clinic.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DecimalFormat;

public final class PayHereHashUtil {

    private PayHereHashUtil() {
    }

    public static String generateCheckoutHash(
            String merchantId,
            String orderId,
            double amount,
            String currency,
            String merchantSecret
    ) {
        String formattedAmount = new DecimalFormat("0.00").format(amount);
        String secretHash = md5(merchantSecret).toUpperCase();

        String hash = md5(merchantId + orderId + formattedAmount + currency + secretHash).toUpperCase();

        // TEMP DEBUG — remove after confirming
        System.out.println("HASH INPUT: " + merchantId + orderId + formattedAmount + currency + secretHash);
        System.out.println("FINAL HASH: " + hash);

        return hash;
    }

    public static String generateNotifySignature(
            String merchantId,
            String orderId,
            double amount,
            String currency,
            String statusCode,
            String merchantSecret
    ) {
        String formattedAmount = new DecimalFormat("0.00").format(amount);
        String secretHash = md5(merchantSecret).toUpperCase();
        return md5(merchantId + orderId + formattedAmount + currency + statusCode + secretHash).toUpperCase();
    }

    private static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5 generation failed", e);
        }
    }
}
