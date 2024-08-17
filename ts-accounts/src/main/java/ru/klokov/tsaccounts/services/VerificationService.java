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
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_\\-\\.]*@[a-zA-Z_\\-]*\\.[a-zA-Z]{2,}$";
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_\\-\\.]{5,}$";
    private static final String PHONE_NUMBER_REGEX = "^\\+[0-9]{1}\\s\\([0-9]{3}\\)\\s[0-9]{3}\\-[0-9]{4}$";
    public void verifyUserEmail(String email) {
        if(!this.verifyByRegExp(EMAIL_REGEX, email)) {
            log.error("Email verification error - \"{}\" - email does not meet requirements", email);
            throw new VerificationException(String.format("Email verification error - \"%s\" - email does not meet requirements", email));
        }
        log.debug("Success email \"{}\" verification", email);
    }

    public void verifyUsername(String username) {
        if(!this.verifyByRegExp(USERNAME_REGEX, username)) {
            log.error("Username verification error - \"{}\" - username does not meet requirements", username);
            throw new VerificationException(String.format("Username verification error - \"%s\" - username does not meet requirements", username));
        }
        log.debug("Success username \"{}\" verification", username);
    }

    public void verifyPhoneNumber(String phoneNumber) {
        if(!this.verifyByRegExp(PHONE_NUMBER_REGEX, phoneNumber)) {
            log.error("Phone number verification error - \"{}\" - phone number does not meet requirements", phoneNumber);
            throw new VerificationException(String.format("Phone number verification error - \"%s\" - phone number does not meet requirements", phoneNumber));
        }
        log.debug("Success phoneNumber \"{}\" verification", phoneNumber);
    }

    private boolean verifyByRegExp(String regex, String verifyString) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(verifyString);
        return matcher.matches();
    }
}
