package ru.klokov.tsaccounts.services;

import org.junit.jupiter.api.Test;
import ru.klokov.tscommon.exceptions.VerificationException;

import static org.junit.jupiter.api.Assertions.*;

class VerificationServiceTest {

    private final VerificationService verificationService = new VerificationService();

    @Test
    void verifyUserEmailTest() {
        assertDoesNotThrow(() ->verificationService.verifyUserEmail("example@example.com"));
        assertThrows(VerificationException.class, () -> verificationService.verifyUserEmail("email"));
    }

    @Test
    void verifyUsernameTest() {
        assertDoesNotThrow(() ->verificationService.verifyUsername("username"));
        assertThrows(VerificationException.class, () -> verificationService.verifyUsername("user@#!$%1232141'''"));
    }

    @Test
    void verifyFirstNameTest() {
        assertDoesNotThrow(() ->verificationService.verifyFirstName("D'Amore"));
        assertThrows(VerificationException.class, () -> verificationService.verifyFirstName("Don_Don"));
    }

    @Test
    void verifySecondNameTest() {
        assertDoesNotThrow(() ->verificationService.verifySecondName("D'Artagnan"));
        assertThrows(VerificationException.class, () -> verificationService.verifySecondName("Din_Din"));
    }

    @Test
    void verifyThirdNameTest() {
        assertDoesNotThrow(() ->verificationService.verifyThirdName("Ivanovich"));
        assertThrows(VerificationException.class, () -> verificationService.verifyThirdName("ivanovich"));
    }

    @Test
    void verifyPhoneNumberTest() {
        assertDoesNotThrow(() ->verificationService.verifyPhoneNumber("+1 (222) 333-4455"));
        assertThrows(VerificationException.class, () -> verificationService.verifyPhoneNumber("-12453184234"));
        assertThrows(VerificationException.class, () -> verificationService.verifyPhoneNumber("aaaaa"));
    }
}