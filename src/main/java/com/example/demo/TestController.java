package com.example.demo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final RestTemplate restTemplate;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        System.out.println("테스트 호출");
        return null;
    }

    @CircuitBreaker(name="fail", fallbackMethod = "method")
    @GetMapping("/fail")
    public ResponseEntity<String> fail() {
        System.out.println("실패 호출");
        return restTemplate.getForEntity("http://ms2/fail", String.class);
    }

    @CircuitBreaker(name="fail", fallbackMethod = "method")
    @GetMapping("/sleep")
    public ResponseEntity<String> sleep() {
        System.out.println("지연 호출");
        return restTemplate.getForEntity("http://ms2/sleep", String.class);
    }

    public ResponseEntity<String> method(Throwable t) {
        System.out.println("오픈했다 건들지 마라");
        return new ResponseEntity<>("오류임", HttpStatus.OK);
    }
}
