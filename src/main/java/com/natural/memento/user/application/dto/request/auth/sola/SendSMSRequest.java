package com.natural.memento.user.application.dto.request.auth.sola;

import com.natural.memento.user.domain.entity.PhoneAuthPurpose;

public record SendSMSRequest(
        String phoneNumber,
        PhoneAuthPurpose purpose
) {

}
