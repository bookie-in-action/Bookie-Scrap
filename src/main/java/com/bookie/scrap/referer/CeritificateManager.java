package com.bookie.scrap.referer;//package scrap.util;
//
//import lombok.Getter;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//
//import javax.net.ssl.KeyManager;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.security.*;
//import java.security.cert.CertificateException;
//import java.security.cert.CertificateFactory;
//import java.security.cert.X509Certificate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Base64;
//
//public class CeritificateManager {
//
//    String certFilePath = "D:\\npki\\heesom_npki\\signCert.der";
//    String privateKeyPath = "D:\\npki\\heesom_npki\\signPri.key";
//    String userPassword = "goflvhxj0*";
//
//    Provider bouncyCastleProvider = new BouncyCastleProvider();
////    Provider bizFrameProvider = new BizFrameProvider();
//
//    X509Certificate certificate;
//    PrivateKey privateKey;
//
//    @Getter String rValue;
//    @Getter static CeritificateManager instance = new CeritificateManager();
//
//    private CeritificateManager() {
//        try {
//            Security.addProvider(bouncyCastleProvider);
////            Security.addProvider(bizFrameProvider);
//            initializeCertificate();
//            initializePrivateKeyInfo();
//        } catch (Exception e) {
//            System.out.println("error while initializing CertManager");
//            e.printStackTrace();
//        }
//    }
//
//    private void initializeCertificate() throws IOException, CertificateException {
//        try (InputStream inputStream = Files.newInputStream(Paths.get(certFilePath))) {
//
//            CertificateFactory certificateFactory = CertificateFactory.getInstance("X509", bouncyCastleProvider);
//
//            certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
//        }
//    }
//
//    private void initializePrivateKeyInfo() {
//
//        try(InputStream inputStream = Files.newInputStream(Paths.get(privateKeyPath))) {
//
//            KeyManager keyManager = KeyManager.getInstance(inputStream);
//            keyManager.decrypt(userPassword);
//
//            rValue =  Base64.getEncoder().encodeToString(keyManager.getRandomNum());
//            privateKey = keyManager.getPrivateKey();
//
//        } catch (Exception e) {
//            throw new IllegalStateException("error while initializing private key info, wrong password");
//        }
//    }
//
//    /**
//     * toSign을 SHA-256으로 해싱 → 해시값을 개인 키로 암호화 → 암호화된 서명 데이터(signedData) 생성.
//     * @param toSign
//     * @return
//     * @throws GeneralSecurityException
//     */
//    private byte[] createDigitalSignature(String toSign) throws GeneralSecurityException {
//        Signature signature = Signature.getInstance("SHA256withRSA");
//        signature.initSign(privateKey);
//        signature.update(toSign.getBytes(StandardCharsets.UTF_8)); //내부적으로 SHA-256 해시 함수가 적용되어 고유한 해시값이 생성
//        return signature.sign(); // 암호화
//    }
//
//    /**
//     * toSign을 SHA-256으로 해싱 → 공개 키로 암호화된 서명을 복호화 → 두 해시값을 비교.
//     * @param toSign
//     * @param signedData
//     * @return
//     * @throws GeneralSecurityException
//     */
//    private boolean verifyDigitalSignature(String toSign, byte[] signedData) throws GeneralSecurityException {
//        Signature signature = Signature.getInstance("SHA256withRSA");
//        signature.initVerify(certificate.getPublicKey());
//        signature.update(toSign.getBytes(StandardCharsets.UTF_8));
//
//        return signature.verify(signedData);
//    }
//
//    public String generateAndVerifySignature(String toSign) throws GeneralSecurityException {
//        byte[] signedData = createDigitalSignature(toSign);
//        boolean isValid = verifyDigitalSignature(toSign, signedData);
//
//        if (!isValid) {
//            throw new IllegalArgumentException("signiture not valid");
//        }
//
//        return Base64.getEncoder().encodeToString(signedData);
//    }
//
//    public String getLogSignature(String originData, String signedData) {
//        return originData + "$" +
//                Integer.toHexString(certificate.getSerialNumber().intValue()) + "$" +
//                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "$" +
//                signedData;
//    }
//
//    public String getPublicKeyPem() throws CertificateException, IOException {
//        StringBuilder publicKeyPemBuilder = new StringBuilder();
//
//        publicKeyPemBuilder.append("-----BEGIN+CERTIFICATE-----").append("\n");
//        publicKeyPemBuilder.append(Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(certificate.getEncoded())).append("\n");
//        publicKeyPemBuilder.append("-----END+CERTIFICATE-----").append("\n");
//
//        return publicKeyPemBuilder.toString();
//    }
//
//}
