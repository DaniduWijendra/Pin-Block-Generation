/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pinblockproject;

import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author danidu_o
 */
public class ThripleDES {
       
    private static final String ALGORITHM = "TripleDES";
    private static final String MODE = "ECB";
    private static final String PADDING = "NoPadding";
    
     /** algorithm/mode/padding */
    private static final String TRANSFORMATION = ALGORITHM+"/"+MODE+"/"+PADDING;

    private final String key;
    private String pinLength;
    public ThripleDES(String key,String pinLength) {
        this.key = key;
        this.pinLength = pinLength;
    }
    
    public String generatePinBlock(String pan,String pin) throws Exception
    {
        pan = pan.substring(pan.length() - 12 -1,pan.length() -1);
        String paddingPan = "0000".concat(pan);
//        System.out.println(paddingPan);
        String ps = "FFFFFFFFFFFFFFFF";
        String paddingPin = "0" + pin.length() + pin + ps.substring(2 + pin.length(),ps.length());
//        System.out.println(paddingPin);
        byte [] pinBlock = xorBytes(h2b(paddingPan),h2b(paddingPin));
        return b2h(pinBlock);
    }
    
    public String decrypt(String block,String tmk) throws Exception
    {
//        System.out.println("this key" + block);
        byte [] tmp = h2b(tmk);
        byte [] tmp2 = new byte[24];
//        System.out.println("tmp " + tmp.length);
//        System.out.println("tmp2" + tmp2.length);
        System.arraycopy(tmp, 0, tmp2, 0, 16);
        System.arraycopy(tmp, 0, tmp2, 16, 8);//take first 16 elements and copy to tmp2 then fill next 8 by 0-8
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(tmp2, ALGORITHM));
        byte[] plaintext = cipher.doFinal(h2b(block));
        return b2h(plaintext);
    }
    public String encrypt(String block,String tmk) throws Exception
    {   
//        System.out.println("this key" + block);
        byte [] tmp = h2b(tmk);
        byte [] tmp2 = new byte[24];
//        System.out.println("tmp " + tmp.length);
//        System.out.println("tmp2" + tmp2.length);
        System.arraycopy(tmp, 0, tmp2, 0, 16);
        System.arraycopy(tmp, 0, tmp2, 16, 8);//take first 16 elements and copy to tmp2 then fill next 8 by 0-8
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(tmp2, ALGORITHM));
        byte[] plaintext = cipher.doFinal(h2b(block));
        return b2h(plaintext);
    }
    
    private byte[] xorBytes(byte [] a,byte [] b) throws Exception
    {
        if(a.length != b.length)
            throw new Exception();
        byte result[] = new byte[a.length];
        for(int i = 0 ;i < result.length ; i++)
        {
            int t =0;
            t = a[i]^b[i];
            t &=0xFF;
            result[i] = (byte)t;
        }
        return result;
    }
    public byte[] xorBytes3(byte []a, byte []b,byte []c) throws Exception
    {
        if((a.length !=b.length) && (a.length !=c.length))
            throw new Exception();
        byte tmp[] = new byte[a.length];
        byte result[] = new byte[a.length];
        tmp = xorBytes(a, b);
        result = xorBytes(tmp, c);
        return result;
    }
    public static byte[] hexaByte(String hex)
    {
        return DatatypeConverter.parseHexBinary(hex);
    }
    
    
    public static byte[]  h2b(String hex)
    {
        if((hex.length() & 0x01) == 0x01)
            throw new IllegalArgumentException();
        byte [] val = new byte[hex.length()/2];
        for(int i= 0 ; i < val.length ; i ++)
        {
            int index = i * 2;
            int j = Integer.parseInt(hex.substring(index, index + 2), 16);
            val[i] = (byte) j;
        }
        return val;
    }
     public static String b2h(byte[] bytes) {
        char[] hex = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; ++i) {
            int hi = (bytes[i] & 0xF0) >>> 4;
            int lo = (bytes[i] & 0x0F);
            hex[i * 2] = (char) (hi < 10 ? '0' + hi : 'A' - 10 + hi);
            hex[i * 2 + 1] = (char) (lo < 10 ? '0' + lo : 'A' - 10 + lo);
        }
        return new String(hex);
    }


}
