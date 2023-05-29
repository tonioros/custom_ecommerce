package org.antonioxocoy.cecommerce.security.jwt.filters;

import org.antonioxocoy.cecommerce.security.encription.EncryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class EncriptionInterceptorFilter implements ClientHttpRequestInterceptor {

    @Autowired
    EncryptService service;

    /**
     * The Request is modified here, as encryption has been applied on it
     * <p>
     * The Response received has been decrypted
     * The decrypted response body is further used in a new object of ClientHttpResponse
     * This new object will now be returned as a Modified Response
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String reqBody = new String(body);
        byte[] encryptedRequestBody = new byte[0];
        ClientHttpResponse decryptedRes = null;
        try {
            encryptedRequestBody = service.encrypt(reqBody).getBytes();
            ClientHttpResponse response = execution.execute(request, encryptedRequestBody);
            String responseBody = new String(response.getBody().readAllBytes());
            byte[] decryptedResponseBodyBytes = service.decrypt(responseBody).getBytes();
            String decryptedResponseBody = new String(decryptedResponseBodyBytes);

            // prepare modified response
            decryptedRes = new ClientHttpResponse() {
                @Override
                public HttpHeaders getHeaders() {
                    return response.getHeaders();
                }

                @Override
                public InputStream getBody() throws IOException {
                    // The expected modified response body to be populated here
                    return new ByteArrayInputStream(decryptedResponseBody.getBytes());
                }

                @Override
                public HttpStatus getStatusCode() throws IOException {
                    return (HttpStatus) response.getStatusCode();
                }

                @Override
                public int getRawStatusCode() throws IOException {
                    return response.getRawStatusCode();
                }

                @Override
                public String getStatusText() throws IOException {
                    return response.getStatusText();
                }

                @Override
                public void close() {

                }
            };
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        return decryptedRes;
    }
}
