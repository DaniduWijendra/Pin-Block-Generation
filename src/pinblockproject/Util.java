/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pinblockproject;

/**
 *
 * @author danidu_o
 */
public class Util {
    public static boolean validateNUMBER(String numericString) throws Exception { 
        return numericString.matches("^[0-9]*$") && numericString.length() == 16;
    }
     public static boolean validatePin(String numericString) throws Exception { 
        return numericString.matches("^[0-9]*$") && numericString.length() <= 6;
    }
}
