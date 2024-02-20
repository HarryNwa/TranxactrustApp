package org.harry.trustmonie.service;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.harry.email.service.EmailService;
import org.harry.trustmonie.data.repository.ConfirmationRepository;
import org.harry.trustmonie.data.repository.UserRepository;
import org.harry.trustmonie.exceptions.IllegalNameFormatException;
import org.harry.trustmonie.user.Confirmation;
import org.harry.trustmonie.user.WalletHolder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private ConfirmationRepository confirmationRepository;
    private EmailService emailService;
//    private final RestTemplate restTemplate;
    private WebClient.Builder webClientBuilder;

    public UserServiceImpl(EmailService emailService, UserRepository userRepository,
                           ConfirmationRepository confirmationRepository, WebClient webClient){
        this.emailService = emailService;
        this.confirmationRepository = confirmationRepository;
        this.userRepository = userRepository;
        this.webClientBuilder = (WebClient.Builder) webClient;
    }
    @Override
    public WalletHolder saveUser(WalletHolder user) {
        String mail = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if (!user.getEmail().matches(mail)) {
            throw new IllegalNameFormatException("Incorrect email");
        }
        if (userRepository.existsByEmail(user.getEmail())){ throw new RuntimeException("Email already exists.");}
        user.setEnabled(false);
        userRepository.saveAndFlush(user);
//        WalletHolder walletHolder = restTemplate.getForObject(
//                "http://localhost:8080/api/users/{walletHolderId}",
//                WalletHolder.class,
//                user.getEmail(),
//                user.getId()
//        );
//
//        Confirmation confirmation  = new Confirmation(user);
//        confirmationRepository.save(confirmation);

//        emailService.sendSimpleMailMessage(user.getUserName(), user.getEmail(), confirmation.getToken());
//        emailService.sendMimeMessageWithAttachment(user.getUserName(), user.getEmail(), confirmation.getToken());
//        emailService.sendMimeMessageWithEmbeddedFiles(user.getUserName(), user.getEmail(), confirmation.getToken());
//        emailService.sendHtmlEmail(user.getUserName(), user.getEmail(), confirmation.getToken());
//        emailService.sendHtmlEmailWithEmbeddedFiles(user.getUserName(), user.getEmail(), confirmation.getToken());
//        return user;
        WebClient webClient = webClientBuilder.baseUrl("http://localhost:9000/api/users").build();
        Mono<WalletHolder> walletHolderMono = webClient.get()
                .uri("/{walletHolderId}", user.getId())
                .retrieve()
                .bodyToMono(WalletHolder.class);

        WalletHolder walletHolder = walletHolderMono.block();

        Confirmation confirmation = new Confirmation(user);
        confirmationRepository.save(confirmation);

//        emailService.sendSimpleMailMessage(user.getUserName(), user.getEmail(), confirmation.getToken());
//        emailService.sendMimeMessageWithAttachment(user.getUserName(), user.getEmail(), confirmation.getToken());
//        emailService.sendMimeMessageWithEmbeddedFiles(user.getUserName(), user.getEmail(), confirmation.getToken());
//        emailService.sendHtmlEmail(user.getUserName(), user.getEmail(), confirmation.getToken());
        emailService.sendHtmlEmailWithEmbeddedFiles(user.getUserName(), user.getEmail(), confirmation.getToken());
        return walletHolder;
    }

//    @Override
//    public WalletHolder saveUser(WalletHolder user) {
//        String mail = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
//        if (!user.getEmail().matches(mail)) {
//            throw new IllegalNameFormatException("Incorrect email");
//        }
//        if (userRepository.existsByEmail(user.getEmail())) { throw new RuntimeException("Email already exists.");
//        }
//        user.setEnabled(false);
//        userRepository.saveAndFlush(user);
//
//        return webClientBuilder.baseUrl("http://localhost:9000/api/users")
//                .build()
//                .get()
//                .uri("/{walletHolderId}", user.getId())
//                .retrieve()
//                .bodyToMono(WalletHolder.class)
//                .flatMap(walletHolder -> {
//
//                    Confirmation confirmation = new Confirmation(user);
//                    confirmationRepository.save(confirmation);
//
//                    confirmationRepository.save(new Confirmation(user));
//                    emailService.sendHtmlEmailWithEmbeddedFiles(user.getUserName(), user.getEmail(), confirmation.getToken());
//                    return Mono.just(walletHolder);
//                })
//                .block();
//    }

    @Override
    public Boolean verifyToken(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token);
        WalletHolder user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);
        //confirmationRepository.delete(confirmation);
        return Boolean.TRUE;
    }

    @Override
    public void userSave(WalletHolder walletHolder) {
        userRepository.save(walletHolder);
    }
}


