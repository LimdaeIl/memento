package com.natural.memento.user.presentation;

import com.natural.memento.commons.response.ApiResponse;
import com.natural.memento.user.application.dto.request.LogoutRequest;
import com.natural.memento.user.application.dto.request.SendEmailCodeRequest;
import com.natural.memento.user.application.dto.request.SignInRequest;
import com.natural.memento.user.application.dto.request.SignupRequest;
import com.natural.memento.user.application.dto.request.TokenReissueRequest;
import com.natural.memento.user.application.dto.request.VerifyEmailCodeRequest;
import com.natural.memento.user.application.dto.response.SendEmailCodeResponse;
import com.natural.memento.user.application.dto.response.SignInResponse;
import com.natural.memento.user.application.dto.response.SignupResponse;
import com.natural.memento.user.application.dto.response.TokenReissueResponse;
import com.natural.memento.user.application.dto.response.VerifyEmailCodeResponse;
import com.natural.memento.user.application.service.AuthService;
import com.natural.memento.user.application.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    @PostMapping("/email/send-code")
    public ResponseEntity<ApiResponse<SendEmailCodeResponse>> sendEmailCode(
            @RequestBody @Valid SendEmailCodeRequest request
    ) {
        SendEmailCodeResponse response = emailService.sendEmailCode(request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/email/verify-code")
    public ResponseEntity<ApiResponse<VerifyEmailCodeResponse>> verifyEmailCode(
            @RequestBody @Valid VerifyEmailCodeRequest request
    ) {
        VerifyEmailCodeResponse response = emailService.verifyCode(request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }


    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(
            @RequestBody @Valid SignupRequest request
    ) {
        SignupResponse response = authService.signup(request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<SignInResponse>> signIn(
            @RequestBody @Valid SignInRequest request
    ) {
        SignInResponse response = authService.signIn(request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<TokenReissueResponse>> reissue(
            @RequestHeader(name = "Authorization", required = false) String at,
            @RequestBody TokenReissueRequest request
    ) {
        TokenReissueResponse response = authService.reissue(at, request.refreshToken());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader(name = "Authorization") String at,
            @RequestBody @Valid LogoutRequest request
    ) {
        authService.logout(at, request.refreshToken());
        return ResponseEntity.noContent().build();
    }


}
