package com.natural.memento.user.presentation;

import com.natural.memento.commons.response.ApiResponse;
import com.natural.memento.commons.security.CustomUserDetails;
import com.natural.memento.user.application.dto.request.user.UpdateEmailRequest;
import com.natural.memento.user.application.dto.request.user.UpdateUserRequest;
import com.natural.memento.user.application.dto.response.user.GetUserResponse;
import com.natural.memento.user.application.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "UserController")
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<GetUserResponse>> me(
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        GetUserResponse response = userService.me(principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PatchMapping("/email")
    public ResponseEntity<ApiResponse<GetUserResponse>> updateEmail(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody @Valid UpdateEmailRequest request
    ) {
        GetUserResponse response = userService.updateEmail(principal.getUserId(), request.email());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<GetUserResponse>> updateUser(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody @Valid UpdateUserRequest request
    ) {
        GetUserResponse response = userService.updateUser(principal.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        userService.delete(principal.getUserId());
        return ResponseEntity.noContent().build();

    }


}
