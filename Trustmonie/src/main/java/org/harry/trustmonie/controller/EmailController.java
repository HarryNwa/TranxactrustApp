package org.harry.trustmonie.controller;


import lombok.RequiredArgsConstructor;
import org.harry.trustmonie.service.UserService;
import org.harry.trustmonie.user.HttpResponse;
import org.harry.trustmonie.user.WalletHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("api/users")
public class EmailController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<HttpResponse> createUser(@RequestBody WalletHolder user){
        WalletHolder newUser = userService.saveUser(user);
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("user", newUser))
                        .message("WalletHolder created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()


        );
    }

    @GetMapping
    public ResponseEntity<HttpResponse> confirmUserAccount(@RequestParam ("token") String token) {
        Boolean isSuccess = userService.verifyToken(token);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("Success", isSuccess))
                        .message("Account Verified")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CONTINUE.value())
                        .build()


        );
    }

//    @RequestMapping(method = RequestMethod.OPTIONS)
//    public ResponseEntity<?> options() {
//        return ResponseEntity.ok().build();
//    }
}

