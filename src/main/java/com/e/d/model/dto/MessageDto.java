package com.e.d.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    private String type;   // 메시지 타입 (enter, chat 등)
    private String sender; // 발신자
    private String receiver; // 수신자 (옵션)
    private String data;   // 메시지 내용
}
