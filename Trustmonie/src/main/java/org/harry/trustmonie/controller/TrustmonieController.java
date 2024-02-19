package org.harry.trustmonie.controller;

import lombok.AllArgsConstructor;
import org.harry.trustmonie.DTOs.request.CreateAccountRequest;
import org.harry.trustmonie.service.TranxactrustServices;
import org.harry.trustmonie.user.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;


@RestController
@RequestMapping("api/v1/")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class TrustmonieController {
    private final TranxactrustServices tranxactrustServices;


    @PostMapping("business")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HttpResponse> createDoubleWalletUser(@RequestBody CreateAccountRequest accountRequest){
        String newWallet = tranxactrustServices.createDoubleWallet(accountRequest);
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("wallet", newWallet))
                        .message("Double wallet created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()


        );
    }

    @PostMapping("personal")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<HttpResponse> createSingleWalletUser(@RequestBody CreateAccountRequest accountRequest){
        String newWallet = tranxactrustServices.createSingleWallet(accountRequest);
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("wallet", newWallet))
                        .message("Single wallet created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()


        );
    }
//
//    @GetMapping("/confirm")
//    public ResponseEntity<HttpResponse> confirmUserAccount(@RequestParam ("token") String token) {
//        Boolean isSuccess = userService.verifyToken(token);
//        return ResponseEntity.ok().body(
//                HttpResponse.builder()
//                        .timeStamp(LocalDateTime.now().toString())
//                        .data(Map.of("Success", isSuccess))
//                        .message("Account Verified")
//                        .status(HttpStatus.CREATED)
//                        .statusCode(HttpStatus.CONTINUE.value())
//                        .build()
//
//
//        );
//    }
}
