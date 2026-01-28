package com.natural.memento.user.infrastructure.solapi;

import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SolapiSmsSender implements SmsSender {

    private final DefaultMessageService messageService;

    @Value("${solapi.from}")
    private String fromNumber;

    @Override
    public void send(String to, String text) {
        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(to);
        message.setText(text);

        try {
            messageService.send(message);
        } catch (SolapiMessageNotReceivedException e) {
            throw new IllegalStateException("문자 발송 실패: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new IllegalStateException("문자 발송 중 오류: " + e.getMessage(), e);
        }
    }
}
