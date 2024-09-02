package ru.klokov.tsaccounts.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.klokov.tscommon.exceptions.VerificationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class VerificationService {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_\\-\\.]*@[a-zA-Z_\\-]*\\.[a-zA-Z]{2,}$";
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_\\-\\.]{5,}$";
    private static final String PHONE_NUMBER_REGEX = "^\\+[0-9]{1}\\s\\([0-9]{3}\\)\\s[0-9]{3}\\-[0-9]{4}$";
    private static final String NAME_REGEX = "^[A-Z][a-zA-Z]*(?:[-'][A-Z][a-zA-Z]*)*$";

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

    public void verifyFirstName(String name) {
        if(!this.verifyByRegExp(NAME_REGEX, name)) {
            log.error("Name verification error - \"{}\" - name does not meet requirements", name);
            throw new VerificationException(String.format("Name verification error - \"%s\" - name does not meet requirements", name));
        }
        log.debug("Success first name \"{}\" verification", name);
    }

    public void verifySecondName(String name) {
        if(!this.verifyByRegExp(NAME_REGEX, name)) {
            log.error("Second name verification error - \"{}\" - second name does not meet requirements", name);
            throw new VerificationException(String.format("Second name verification error - \"%s\" - name does not meet requirements", name));
        }
        log.debug("Success second name \"{}\" verification", name);
    }

    public void verifyThirdName(String name) {
        if(!this.verifyByRegExp(NAME_REGEX, name)) {
            log.error("Third name verification error - \"{}\" - Third name does not meet requirements", name);
            throw new VerificationException(String.format("Third name verification error - \"%s\" - name does not meet requirements", name));
        }
        log.debug("Success third name \"{}\" verification", name);
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
