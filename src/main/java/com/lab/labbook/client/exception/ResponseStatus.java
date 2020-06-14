package com.lab.labbook.client.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseStatus {
    private String date;
    private String status;
    private String message;

    @Override
    public String toString() {
        return "ResponseStatus{" +
                "date=" + date +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
