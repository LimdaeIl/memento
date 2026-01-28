package com.natural.memento.user.application.dto.request.auth.sola;

import com.natural.memento.user.domain.type.PhoneAuthPurpose;

public record VerifySMSRequest(String phoneNumber, String code, PhoneAuthPurpose purpose) {}
