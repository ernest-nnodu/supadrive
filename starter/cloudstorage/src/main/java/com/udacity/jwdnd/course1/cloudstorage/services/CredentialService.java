package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public int saveCredential(Credential credential) {

        credential.setEnckey(generateKey());
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), credential.getEnckey()));

        if (credential.getCredentialid() != null) {
            return credentialMapper.update(credential);
        }
        return credentialMapper.insert(credential);
    }

    public Credential getCredentialById(Integer credentialId) {
        Credential credential = credentialMapper.getCredentialById(credentialId);
        credential.setTemppassword(encryptionService.decryptValue(credential.getPassword(), credential.getEnckey()));
        return credential;
    }

    public List<Credential> getCredentialsByUserId(Integer userId) {
        List<Credential> credentials = credentialMapper.getCredentialsByUserId(userId);
        if (!credentials.isEmpty()) {
            credentials.forEach(credential -> {
                credential.setTemppassword(
                        encryptionService.decryptValue(credential.getPassword(), credential.getEnckey()));
            });
        }
        return credentials;
    }

    private String generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    public int deleteCredential(int credentialId) {
        return credentialMapper.delete(credentialId);
    }
}
