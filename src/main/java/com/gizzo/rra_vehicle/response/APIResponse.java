package com.gizzo.rra_vehicle.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class APIResponse<T>{

    String message;
    HttpStatus status;
    T data;

    public APIResponse(String message, HttpStatus status, T data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public ResponseEntity<APIResponse<T>> toResponseEntity(){
        assert  status != null;
        return ResponseEntity.status(status).body(this);
    }

}