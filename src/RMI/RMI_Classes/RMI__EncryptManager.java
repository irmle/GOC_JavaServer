package RMI.RMI_Classes;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import sun.security.pkcs.PKCS8Key;


//암호화, 복호화를 전담할 암호화 매니저.
public class RMI__EncryptManager {

    //공용키이다.
    public static final EncryptKeyInfo Public_AESKeys = new EncryptKeyInfo();

    //RMI__EncryptManager 생성자, 공용키 생성.
    public RMI__EncryptManager() {

        //AES 대칭키 생성.
        Public_AESKeys.createAESKey();

        //RSA 비대칭키(공개키, 비밀키) 생성.
        Public_AESKeys.keypair = RMI__EncryptManager.createKeyPair(2048); // (2048/8) - 11 byte 암호화 가능. (245byte)

        RSA_PublicKey_base64 = encodeBase64_PublicKey(Public_AESKeys.keypair);
        RSA_PrivateKey_base64 = encodeBase64_PrivateKey(Public_AESKeys.keypair);

        /*long currTime = System.currentTimeMillis();

        //RSA키 생성.
        Public_AESKeys_TCP.keypair = RMI__EncryptManager.createKeyPair(2048);

        String RSA_PublicKey_base64_TCP = encodeBase64_PublicKey(Public_AESKeys_TCP.keypair);
        String RSA_PrivateKey_base64_TCP = encodeBase64_PrivateKey(Public_AESKeys_TCP.keypair);

        //RSA키 생성.
        Public_AESKeys_UDP.keypair = RMI__EncryptManager.createKeyPair(2048);

        String RSA_PublicKey_base64_UDP = encodeBase64_PublicKey(Public_AESKeys_UDP.keypair);
        String RSA_PrivateKey_base64_UDP = encodeBase64_PrivateKey(Public_AESKeys_UDP.keypair);

        long elaps = System.currentTimeMillis() - currTime;

        System.out.println("createKeyPair elaps time = "+elaps);

        //String sendRSAKeyData = RSA_PublicKey_base64+"§"+RSA_PrivateKey_base64;
        //System.out.println("TCP public="+RSA_PublicKey_base64_TCP+"\nTCP private"+RSA_PrivateKey_base64_TCP);
        System.out.println("UDP public="+RSA_PublicKey_base64_UDP+"\nUDP private"+RSA_PrivateKey_base64_UDP);


        String aesKey_TCP = Public_AESKeys_TCP.aesKey + "§" + Public_AESKeys_TCP.aesIV;
        String aesKey_UDP = Public_AESKeys_UDP.aesKey + "§" + Public_AESKeys_UDP.aesIV;

        System.out.println("AES aesKey_TCP=" + aesKey_TCP);
        System.out.println("AES aesKey_UDP=" + aesKey_UDP);*/

    }


    private static String RSA_PublicKey_base64 = null;
    private static String RSA_PrivateKey_base64 = null;

    //이미 생성된 RSA 공개키 반환.
    public static String getPublicKey()
    {
        if(RSA_PublicKey_base64 == null)
            throw new NullPointerException("RMI__EncryptManager 초기화 오류! RSA_PublicKey_base64 가 Null입니다.");

        return RSA_PublicKey_base64;
    }

    //이미 생성된 RSA 비밀키 반환. (내부 복호화용)
    public static String getPrivateKey()
    {
        if(RSA_PrivateKey_base64 == null)
            throw new NullPointerException("RMI__EncryptManager 초기화 오류! RSA_PrivateKey_base64 가 Null입니다.");

        return RSA_PrivateKey_base64;
    }



    //같은데이터를 여러명의 대상에게 보내는 경우는 공용 암호화 Key를 사용할 것.
    public static byte[] RMI_EncryptMethod_Arr(RMI_ID[] rmi_id, short rmi_ctx, boolean isEncrypt, byte[] data) {

        EncryptKeyInfo keys;
        byte[] processed = null;
        switch (rmi_ctx) {
            case RMI_Context.Reliable: //plain Data
                //평문은 아무것도 안함.
                return data;
            case RMI_Context.Reliable_Public_AES128: //AES-128-CBC //공용 대칭키 사용. 같은데이터를 여러명에게 보낼때.
                keys = RMI__EncryptManager.Public_AESKeys;
                if (isEncrypt)
                    processed = RMI__EncryptManager.encryptAES_128(data, keys.aesKey, keys.aesIV);
                else
                    processed = RMI__EncryptManager.decryptAES_128(data, keys.aesKey, keys.aesIV);
                break;
            case RMI_Context.Reliable_Public_AES256: //AES-256-CBC //공용 대칭키 사용. 같은데이터를 여러명에게 보낼때.
                keys = RMI__EncryptManager.Public_AESKeys;
                if (isEncrypt)
                    processed = RMI__EncryptManager.encryptAES_256(data, keys.aesKey, keys.aesIV);
                else
                    processed = RMI__EncryptManager.decryptAES_256(data, keys.aesKey, keys.aesIV);
                break;

            case RMI_Context.UnReliable: //plain Data
                //평문은 아무것도 안함.
                return data;
            case RMI_Context.UnReliable_Public_AES128: //AES-128-CBC //공용 대칭키 사용. 같은데이터를 여러명에게 보낼때.
                keys = RMI__EncryptManager.Public_AESKeys;
                if (isEncrypt)
                    processed = RMI__EncryptManager.encryptAES_128(data, keys.aesKey, keys.aesIV);
                else
                    processed = RMI__EncryptManager.decryptAES_128(data, keys.aesKey, keys.aesIV);
                break;
            case RMI_Context.UnReliable_Public_AES256: //AES-256-CBC //공용 대칭키 사용. 같은데이터를 여러명에게 보낼때.
                keys = RMI__EncryptManager.Public_AESKeys;
                if (isEncrypt)
                    processed = RMI__EncryptManager.encryptAES_256(data, keys.aesKey, keys.aesIV);
                else
                    processed = RMI__EncryptManager.decryptAES_256(data, keys.aesKey, keys.aesIV);
                break;
            default:

                //잘못 지정되었을 경우, 평문처리한다.
                processed = data;
                System.out.println("RMI_EncryptMethod_Arr, 공용 RMI_Context값 에러! => " + RMI_Context.name(rmi_ctx));
                throw new IllegalArgumentException("RMI_ID[] (범위) 로 보낼시, RMI_Context 는 Reliable/UnReliable Public_AES128/256만 지정 가능합니다.");
        }
        return processed;
    }

    public static byte[] RMI_EncryptMethod(RMI_ID rmi_id, short rmi_ctx, boolean isEncrypt, byte[] data) {

        //키 호출.
        EncryptKeyInfo keys;
        byte[] processed = null;

        switch (rmi_ctx) {
            //TCP
            case RMI_Context.Reliable: //plain Data
                //평문은 아무것도 안함.
                return data;
            case RMI_Context.Reliable_AES128: //AES-128-CBC
                keys = rmi_id.AESKey;
                if(rmi_id.unique_id == RMI_ID.ALL.unique_id)
                    keys = RMI__EncryptManager.Public_AESKeys;
                if (keys != null) {
                    if (isEncrypt)
                        processed = RMI__EncryptManager.encryptAES_128(data, keys.aesKey, keys.aesIV);
                    else
                        processed = RMI__EncryptManager.decryptAES_128(data, keys.aesKey, keys.aesIV);
                }
                else
                    throw new IllegalArgumentException("AES_128 : rmi_id.AESKey_TCP 가 Null입니다.");
                break;
            case RMI_Context.Reliable_AES256: //AES-256-CBC
                keys = rmi_id.AESKey;
                if(rmi_id.unique_id == RMI_ID.ALL.unique_id)
                    keys = RMI__EncryptManager.Public_AESKeys;
                if (keys != null) {
                    if (isEncrypt)
                        processed = RMI__EncryptManager.encryptAES_256(data, keys.aesKey, keys.aesIV);
                    else
                        processed = RMI__EncryptManager.decryptAES_256(data, keys.aesKey, keys.aesIV);
                }
                else
                    throw new IllegalArgumentException("AES_256 : rmi_id.AESKey_TCP 가 Null입니다.");
                break;
            case RMI_Context.Reliable_Public_AES128: //AES-128-CBC //공용 대칭키 사용. 같은데이터를 여러명에게 보낼때.
                keys = RMI__EncryptManager.Public_AESKeys;
                if (isEncrypt)
                    processed = RMI__EncryptManager.encryptAES_128(data, keys.aesKey, keys.aesIV);
                else
                    processed = RMI__EncryptManager.decryptAES_128(data, keys.aesKey, keys.aesIV);
                break;
            case RMI_Context.Reliable_Public_AES256: //AES-256-CBC //공용 대칭키 사용. 같은데이터를 여러명에게 보낼때.
                keys = RMI__EncryptManager.Public_AESKeys;
                if (isEncrypt)
                    processed = RMI__EncryptManager.encryptAES_256(data, keys.aesKey, keys.aesIV);
                else
                    processed = RMI__EncryptManager.decryptAES_256(data, keys.aesKey, keys.aesIV);
                break;


            //UDP
            case RMI_Context.UnReliable: //plain Data
                //평문은 아무것도 안함.
                return data;
            case RMI_Context.UnReliable_AES128: //AES-128-CBC
                keys = rmi_id.AESKey;
                if(rmi_id.unique_id == RMI_ID.ALL.unique_id)
                    keys = RMI__EncryptManager.Public_AESKeys;
                if (keys != null) {
                    if (isEncrypt)
                        processed = RMI__EncryptManager.encryptAES_128(data, keys.aesKey, keys.aesIV);
                    else
                        processed = RMI__EncryptManager.decryptAES_128(data, keys.aesKey, keys.aesIV);
                }
                else
                    throw new IllegalArgumentException("AES_128 : rmi_id.AESKey_UDP 가 Null입니다.");
                break;
            case RMI_Context.UnReliable_AES256: //AES-256-CBC
                keys = rmi_id.AESKey;
                if(rmi_id.unique_id == RMI_ID.ALL.unique_id)
                    keys = RMI__EncryptManager.Public_AESKeys;
                if (keys != null) {
                    if (isEncrypt)
                        processed = RMI__EncryptManager.encryptAES_256(data, keys.aesKey, keys.aesIV);
                    else
                        processed = RMI__EncryptManager.decryptAES_256(data, keys.aesKey, keys.aesIV);
                }
                else
                    throw new IllegalArgumentException("AES_256 : rmi_id.AESKey_UDP 가 Null입니다.");
                break;
            case RMI_Context.UnReliable_Public_AES128: //AES-128-CBC //공용 대칭키 사용. 같은데이터를 여러명에게 보낼때.
                keys = RMI__EncryptManager.Public_AESKeys;
                if (isEncrypt)
                    processed = RMI__EncryptManager.encryptAES_128(data, keys.aesKey, keys.aesIV);
                else
                    processed = RMI__EncryptManager.decryptAES_128(data, keys.aesKey, keys.aesIV);
                break;
            case RMI_Context.UnReliable_Public_AES256: //AES-256-CBC //공용 대칭키 사용. 같은데이터를 여러명에게 보낼때.
                keys = RMI__EncryptManager.Public_AESKeys;
                if (isEncrypt)
                    processed = RMI__EncryptManager.encryptAES_256(data, keys.aesKey, keys.aesIV);
                else
                    processed = RMI__EncryptManager.decryptAES_256(data, keys.aesKey, keys.aesIV);
                break;

            default:
                System.out.println("RMI_EncryptMethod RMI_Context값 에러! => " + rmi_ctx);
                throw new IllegalArgumentException("올바른 RMI_Context 값을 지정하십시오 : " + rmi_ctx);

        }

        return processed;
    }


    public static byte[] encryptRSA_public(byte[] plainData, String publicKey_base64text) {
        byte[] encryptData = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey_base64text));
            encryptData = cipher.doFinal(plainData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return encryptData;
    }

    public static byte[] encryptRSA_private(byte[] plainData, String privateKey_base64text) {
        byte[] encryptData = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, getPrivateKey(privateKey_base64text));
            encryptData = cipher.doFinal(plainData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return encryptData;
    }

    public static byte[] decryptRSA_private(byte[] encryptData, String privateKey_base64text) {
        byte[] plainData = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey_base64text));
            plainData = cipher.doFinal(encryptData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return plainData;
    }

    public static byte[] decryptRSA_public(byte[] encryptData, String publicKey_base64text) {
        byte[] plainData = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, getPublicKey(publicKey_base64text));
            plainData = cipher.doFinal(encryptData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return plainData;
    }


    public static KeyPair createKeyPair(int KeyBits) {
        KeyPair keypair = null;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(KeyBits);
            keypair = keyPairGenerator.genKeyPair();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return keypair;
    }

    public static PublicKey getPublicKey(String publicKey_base64) {
        PublicKey publicKey = null;
        try {
            String[] key_str = publicKey_base64.split("§");

            byte[] modulus = Base64.getDecoder().decode((key_str[0]));
            byte[] exponent = Base64.getDecoder().decode((key_str[1]));

            BigInteger bigModulus = new BigInteger(1, modulus);
            BigInteger bigExponent = new BigInteger(1, exponent);
            RSAPublicKeySpec spec = new RSAPublicKeySpec(bigModulus, bigExponent);

            publicKey = KeyFactory.getInstance("RSA").generatePublic(spec);

            System.out.println("publicKey.getEncoded().length"+publicKey.getEncoded().length);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return publicKey;
    }

    public static PrivateKey getPrivateKey(String privateKey_base64) {
        PrivateKey pvKey = null;
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey_base64));
            KeyFactory kf = KeyFactory.getInstance("RSA");

            pvKey = kf.generatePrivate(spec);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return pvKey;
    }

    public static String encodeBase64_PublicKey(KeyPair keypair) {
        String publicKey_text = null;
        PublicKey publicKey = keypair.getPublic();

        try {
            RSAPublicKeySpec spec = (RSAPublicKeySpec) KeyFactory.getInstance("RSA").getKeySpec(publicKey, RSAPublicKeySpec.class);

            byte[] tmp = null;
            if (spec.getModulus().toByteArray()[0] == 0) {
                tmp = new byte[spec.getModulus().toByteArray().length - 1];
                System.arraycopy(spec.getModulus().toByteArray(), 1, tmp, 0, tmp.length);
            }

            byte[] modulus = tmp;
            byte[] exponent = spec.getPublicExponent().toByteArray();

            publicKey_text = Base64.getEncoder().encodeToString(modulus) + "§" + Base64.getEncoder().encodeToString(exponent);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return publicKey_text;
    }

    public static String encodeBase64_PrivateKey(KeyPair keypair) {
        String privateKey_text = null;
        PrivateKey privateKey = keypair.getPrivate();

        try {

            byte[] keyBytes = privateKey.getEncoded();
            PKCS8Key pkcs8 = new PKCS8Key();
            pkcs8.decode(keyBytes);
            byte[] privateKey_bytes = pkcs8.encode();

            privateKey_text = Base64.getEncoder().encodeToString(privateKey_bytes);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return privateKey_text;
    }


    public static byte[] encryptAES_256(byte[] plainData, String sessionKey, String Initialization_Vector) {
        Cipher cipher = null;
        byte[] encryptData = null; //cipher.update(plainData);
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,
                    MakeKey(sessionKey, 256, 1),
                    MakeInitializationVector(Initialization_Vector, 1));
            encryptData = cipher.doFinal( plainData );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return encryptData;
    }

    public static byte[] decryptAES_256(byte[] encryptData, String sessionKey, String Initialization_Vector) {
        Cipher cipher = null;
        byte[] plainData = null; //cipher.update(encryptData);
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,
                    MakeKey(sessionKey, 256, 1),
                    MakeInitializationVector(Initialization_Vector, 1));
            plainData = cipher.doFinal(encryptData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return plainData;
    }


    public static byte[] encryptAES_128(byte[] plainData, String sessionKey, String Initialization_Vector) {
        Cipher cipher = null;
        byte[] encryptData = null; //cipher.update(plainData);
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,
                    MakeKey(sessionKey, 128, 1),
                    MakeInitializationVector(Initialization_Vector, 1));

            encryptData = cipher.doFinal(plainData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return encryptData;
    }

    public static byte[] decryptAES_128(byte[] encryptData, String sessionKey, String Initialization_Vector) {
        Cipher cipher = null;
        byte[] plainData = null; //cipher.update(encryptData);
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,
                    MakeKey(sessionKey, 128, 1),
                    MakeInitializationVector(Initialization_Vector, 1));
            plainData = cipher.doFinal(encryptData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return plainData;
    }

    public static void setAESKey(String receivedKey) {
        String[] received_data = receivedKey.split("§");
        /*aesKey_text = received_data[0];
        aesIV_text = received_data[1];*/
    }

    public static void createAESKey() {
        //Key 32바이트, //IV 16바이트
        String Key = getToken(32);
        String IV = getToken(16);

       /* byte[] key = Key.getBytes(StandardCharsets.UTF_8);
        byte[] iv = IV.getBytes(StandardCharsets.UTF_8);
        System.out.println("createAESKey key="+key.length+"/iv="+iv.length);*/
/*
        aesKey_text = Key;
        aesIV_text = IV;*/
    }

    //솔팅 및 Key 스트레칭을 통해 좀더 복잡화 함.
    public static SecretKeySpec MakeKey(String key_data, int Encrypt_bit_length, int count) throws NoSuchAlgorithmException, InvalidKeySpecException {

        //암호키를 생성하는 팩토리 객체 생성
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        //다이제스트를 이용하여, SHA-512로 단방향 해시값 생성 (salt 생성용)
        MessageDigest digest = MessageDigest.getInstance("SHA-1"); //"SHA-256"

        // C# : byte[] keyBytes = System.Text.Encoding.UTF8.GetBytes(key_data);
        byte[] keyBytes = key_data.getBytes(StandardCharsets.UTF_8);

        // C# : byte[] saltBytes = SHA512.Create().ComputeHash(keyBytes);
        byte[] saltBytes = digest.digest(keyBytes);

        // 256bit (AES256은 256bit의 키, 128bit의 블록사이즈를 가짐.)
        PBEKeySpec pbeKeySpec = new PBEKeySpec(key_data.toCharArray(), saltBytes, count, Encrypt_bit_length); //bit단위
        Key secretKey = factory.generateSecret(pbeKeySpec);

        // 256bit = 32byte
        byte[] key = new byte[Encrypt_bit_length / 8];
        System.arraycopy(secretKey.getEncoded(), 0, key, 0, Encrypt_bit_length / 8); //byte단위

        //AES 알고리즘을 적용하여 암호화키 생성
        SecretKeySpec secret = new SecretKeySpec(key, "AES");

        factory = null;
        digest = null;
        pbeKeySpec = null;
        secretKey = null;

        return secret;
    }

    //솔팅 및 Key 스트레칭을 통해 좀더 복잡화 함.
    public static IvParameterSpec MakeInitializationVector(String vector_data, int count) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //AES알고리즘의 블록 크기는 128bit 으로 고정이다!
        //암호키를 생성하는 팩토리 객체 생성
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        //다이제스트를 이용하여, SHA-512로 단방향 해시값 생성 (salt 생성용)
        MessageDigest digest = MessageDigest.getInstance("SHA-1"); //"SHA-256"

        // C# : byte[] keyBytes = System.Text.Encoding.UTF8.GetBytes(key_data);
        byte[] vectorBytes = vector_data.getBytes(StandardCharsets.UTF_8);

        // C# : byte[] saltBytes = SHA512.Create().ComputeHash(keyBytes);
        byte[] saltBytes = digest.digest(vectorBytes);

        // 128bit
        PBEKeySpec pbeKeySpec = new PBEKeySpec(vector_data.toCharArray(), saltBytes, count, 128);
        Key secretIV = factory.generateSecret(pbeKeySpec);

        // 128bit = 16byte
        byte[] iv = new byte[16];
        System.arraycopy(secretIV.getEncoded(), 0, iv, 0, 16);

        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        factory = null;
        digest = null;
        pbeKeySpec = null;
        secretIV = null;

        return ivSpec;
    }

    public static String getToken(int length) {
        char[] charaters = {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer sb = new StringBuffer();
        Random rn = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(charaters[rn.nextInt(charaters.length)]);
        }
        String token = sb.toString();
        charaters = null;
        sb = null;
        rn = null;
        return token;
    }

    public static class EncryptKeyInfo {

        public KeyPair keypair;

        //암호화 중인 Key 종류.
        public String privateKey;
        public String publicKey;
        public String aesKey;
        public String aesIV;

        public EncryptKeyInfo() {
        }

        public void setAESKey(String receivedKey) {
            String[] received_data = receivedKey.split("§");
            this.aesKey = received_data[0];
            this.aesIV = received_data[1];
        }

        public void setAESKey(String aesKey, String aesIV) {
            this.aesKey = aesKey;
            this.aesIV = aesIV;
        }

        public void createAESKey() {

            /*//Key 32바이트, //IV 16바이트
            byte[] Key = getToken(32).getBytes(StandardCharsets.UTF_8);
            byte[] IV = getToken(16).getBytes(StandardCharsets.UTF_8);

            this.aesKey = Base64.getEncoder().encodeToString(Key);
            this.aesIV = Base64.getEncoder().encodeToString(IV);*/


            this.aesKey = getToken(32);
            this.aesIV = getToken(16);
        }

        public String getToken(int length) {
            char[] charaters = {
                    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '/', '-', '+', '='};
            StringBuffer sb = new StringBuffer();
            Random rn = new Random();
            for (int i = 0; i < length; i++) {
                sb.append(charaters[rn.nextInt(charaters.length)]);
            }
            String token = sb.toString();
            charaters = null;
            sb = null;
            rn = null;
            return token;
        }
    }
}

