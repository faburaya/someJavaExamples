package com.examples.jdbctest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.ZoneId;
import java.util.Base64;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Gewährt den Zugriff auf das durch Verschlüsselung geschützte Kennwort.
 */
class CredentialFetcher implements CredentialFetcherInterface {
    private final Path credentialFilePath;
    private final Path secretFilePath;
    private final CredentialFetcherInterface fallbackFetcher;

    /**
     * Erstellt eine Instanz von <code>PasswordFetcher</code>.
     * 
     * @param credentialFilePath Der Pfad zum Speichern des Kennworts.
     * @param secretFilePath     Def Pfad zum Speichern des Geheimnisses.
     * @param fallbackFetcher    Diese Implementierung beschafft die
     *                           Anmeldeinformationen, wenn sie aus dem Speicher
     *                           nicht abzurufen sind.
     */
    public CredentialFetcher(String credentialFilePath, String secretFilePath,
            CredentialFetcherInterface fallbackFetcher) {
        this.credentialFilePath = Paths.get(credentialFilePath);
        this.secretFilePath = Paths.get(secretFilePath);
        this.fallbackFetcher = fallbackFetcher;
    }

    private static final String ENCRIPTION_ALGORITHM = "AES";

    private static SecretKey createSecret() throws NoSuchAlgorithmException {
        SecureRandom randomGenerator = SecureRandom.getInstance("SHA1PRNG");
        randomGenerator.setSeed(Clock.tickMillis(ZoneId.systemDefault()).millis());
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCRIPTION_ALGORITHM);
        keyGenerator.init(128, randomGenerator);
        return keyGenerator.generateKey();
    }

    private void saveSecret(SecretKey secret) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(secretFilePath.toFile())) {
            outputStream.write(secret.getEncoded());
        }
    }

    private SecretKey readSecret() throws IOException {
        try (FileInputStream inputStream = new FileInputStream(secretFilePath.toFile())) {
            byte[] bytes = inputStream.readAllBytes();
            return new SecretKeySpec(bytes, ENCRIPTION_ALGORITHM);
        }
    }

    private static String encrypt(char[] text, SecretKey secret)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException {
        Cipher cipher = Cipher.getInstance(secret.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] data = StandardCharsets.UTF_8.encode(CharBuffer.wrap(text)).array();
        byte[] encryptedBytes = cipher.doFinal(data);
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private void saveEncodedCredential(String encodedUserId, String encodedPassword)
            throws IOException, AppSecurityException {
        try (FileWriter writer = new FileWriter(credentialFilePath.toFile())) {
            writer.append(encodedUserId);
            writer.append(System.lineSeparator());
            writer.append(encodedPassword);
        } catch (FileNotFoundException ex) {
            throw new AppSecurityException("Datei mit den Anmeldeinformationen kann nicht gefuden werden!");
        }
    }

    private Credential readCredentialFromFile(SecretKey secret) throws AppSecurityException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(credentialFilePath);
        } catch (IOException ex) {
            assert false : "Die Datei mit den Anmeldeinformationen existiert nicht!";
        }

        if (lines.size() != 2) {
            throw new AppSecurityException("Die Datei mit der Anmeldeinformation ist beschädigt!");
        }

        Cipher cipher = Cipher.getInstance(secret.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, secret);

        Base64.Decoder b64decoder = Base64.getDecoder();
        byte[] encryptedUserId = b64decoder.decode(lines.get(0));
        byte[] userIdBytes = cipher.doFinal(encryptedUserId);
        String userId = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(userIdBytes)).toString();

        byte[] encryptedPassword = b64decoder.decode(lines.get(1));
        byte[] passwordBytes = cipher.doFinal(encryptedPassword);
        char[] password = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(passwordBytes)).array();

        return new Credential(userId, password);
    }

    @Override
    public Credential getCredential() throws AppSecurityException {
        try {
            SecretKey secret = readSecret();
            return readCredentialFromFile(secret);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        Credential credential = fallbackFetcher.getCredential();
        try {
            SecretKey secret = createSecret();
            String encodedUserId = encrypt(credential.getUserId().toCharArray(), secret);
            String encodedPassword = encrypt(credential.getPassword(), secret);
            saveEncodedCredential(encodedUserId, encodedPassword);
            saveSecret(secret);
        } catch (AppSecurityException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AppSecurityException(
                    "Fehler bei der Verschlüsselung der Anmeldeinformationen können nicht gespeichert werden!", ex);
        }
        return credential;
    }
}
