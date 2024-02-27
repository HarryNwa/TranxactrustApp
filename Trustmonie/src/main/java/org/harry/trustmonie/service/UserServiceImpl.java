package org.harry.trustmonie.service;


import lombok.RequiredArgsConstructor;
import org.harry.email.service.EmailService;
import org.harry.trustmonie.data.repository.ConfirmationRepository;
import org.harry.trustmonie.data.repository.UserRepository;
import org.harry.trustmonie.exceptions.IllegalNameFormatException;
import org.harry.trustmonie.user.Confirmation;
import org.harry.trustmonie.user.WalletHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;
    @Override
    public WalletHolder saveUser(WalletHolder user) {
        String mail = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if (!user.getEmail().matches(mail)) {
            throw new IllegalNameFormatException("Incorrect email");
        }
        if (userRepository.existsByEmail(user.getEmail())){ throw new RuntimeException("Email already exists.");}
        user.setEnabled(false);
        userRepository.save(user);

        Confirmation confirmation  = new Confirmation(user);
        confirmationRepository.save(confirmation);

//        emailService.sendSimpleMailMessage(user.getUserName(), user.getEmail(), confirmation.getToken());
//        emailService.sendMimeMessageWithAttachment(user.getUserName(), user.getEmail(), confirmation.getToken());
//        emailService.sendMimeMessageWithEmbeddedFiles(user.getUserName(), user.getEmail(), confirmation.getToken());
//        emailService.sendHtmlEmail(user.getUserName(), user.getEmail(), confirmation.getToken());
        emailService.sendHtmlEmailWithEmbeddedFiles(user.getUserName(), user.getEmail(), confirmation.getToken());
        return user;
    }

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

