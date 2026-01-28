package com.natural.memento.user.presentation;

import com.natural.memento.commons.response.ApiResponse;
import com.natural.memento.user.application.dto.request.auth.sola.SendSMSRequest;
import com.natural.memento.user.application.dto.request.auth.sola.VerifySMSRequest;
import com.natural.memento.user.application.dto.response.sola.VerifySMSResponse;
import com.natural.memento.user.application.service.PhoneVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth/phone")
public class PhoneVerificationController {

    private final PhoneVerificationService phoneVerificationService;

    @PostMapping("/send-sms")
    public ResponseEntity<ApiResponse<Void>> sendSMS(@RequestBody SendSMSRequest request) {
        phoneVerificationService.sendSMS(request.purpose(), request.phoneNumber());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/verify-sms")
    public ResponseEntity<ApiResponse<VerifySMSResponse>> verifySMS(
            @RequestBody VerifySMSRequest request) {
        String token = phoneVerificationService.verifySMSAndIssueToken(
                request.purpose(),
                request.phoneNumber(),
                request.code()
        );

        if (token == null) {
            return ResponseEntity.ok(ApiResponse.success(VerifySMSResponse.fail()));
        }
        return ResponseEntity.ok(ApiResponse.success(VerifySMSResponse.success(token)));
    }
}
