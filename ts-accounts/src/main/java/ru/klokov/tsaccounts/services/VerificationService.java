package ru.klokov.tsaccounts.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.klokov.tsaccounts.exceptions.VerificationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class VerificationService {
    public void verifyUserEmail(String email) {
        String regex = "^[a-zA-Z0-9_\\-\\.]*@[a-zA-Z_\\-]*\\.[a-zA-Z]{2,}$";

        if(!this.verifyByRegExp(regex, email)) {
            log.error("Email verification error - \"{}\" - email does not meet requirements", email);
            throw new VerificationException(String.format("Email verification error - \"%s\" - email does not meet requirements", email));
        }
        log.debug("Success email \"{}\" verification", email);
    }

    public void verifyUsername(String username) {
        String regex = "^[a-zA-Z0-9_\\-\\.]{5,}$";

        if(!this.verifyByRegExp(regex, username)) {
            log.error("Username verification error - \"{}\" - username does not meet requirements", username);
            throw new VerificationException(String.format("Username verification error - \"%s\" - username does not meet requirements", username));
        }
        log.debug("Success username \"{}\" verification", username);
    }

    private boolean verifyByRegExp(String regex, String verifyString) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(verifyString);
        return matcher.matches();
    }
}
