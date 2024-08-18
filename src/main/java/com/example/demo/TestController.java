package com.example.demo;

import com.example.demo.security.SecurityUtil;
import com.example.demo.security.member.MemberDetails;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final RestTemplate restTemplate;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        Test.Testg a = Test.Testg.builder()
                .refreshToken("Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3MjUzMDMyNTYsImlkIjoxLCJ1c2VybmFtZSI6InVzZXIxMiIsIm5pY2tuYW1lIjoibWluaSIsInJvbGUiOlsiUk9MRV9VU0VSIl19.LDiUrTVCnOYNSKpNsals6xNcdR0iwyMpvClKXSvfGhpKVo5HqqL9cQ84raHOjIPQ2YxMdIh-A3DIB0OJ6kcbpw")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Test.Testg> requestEntity = new HttpEntity<>(a, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://member/v1/refresh-token",
                requestEntity,
                String.class);

        return ResponseEntity.ok(response.getBody());
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
