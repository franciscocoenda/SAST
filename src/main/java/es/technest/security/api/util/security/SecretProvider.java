package es.technest.security.api.util.security;

public interface SecretProvider {

   // Due to limitation on asymmetric encryption, only 117 bytes as maximum can be passed as text to be encrypted.
   int MAX_ASYMMETRIC_ENCRYPTABLE_LENGTH = 117;

   String getSecureRandomString(int generatedStringLengthInUTF8Encoding);
}
