package com.marketplace.hyderabad.security;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final Map<String, String> store = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    public String generateOtp(String phone) {
        int v = 100000 + random.nextInt(900000);
        String otp = String.valueOf(v);
        store.put(phone, otp);
        return otp;
    }

    public boolean verifyOtp(String phone, String otp) {
        String expected = store.get(phone);
        if (expected == null) return false;
        boolean ok = expected.equals(otp);
        if (ok) store.remove(phone);
        return ok;
    }
}
