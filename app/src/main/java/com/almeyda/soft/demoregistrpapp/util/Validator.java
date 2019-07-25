package com.almeyda.soft.demoregistrpapp.util;

import android.text.TextUtils;

public class Validator {

    public static boolean validatePhoneNumber(String phoneNumber){

        boolean isValidPhoneNumber= false;
        int MAX_DIGITS = 9;

        if(!TextUtils.isEmpty(phoneNumber) && phoneNumber.length() == MAX_DIGITS){

            String DIGIT_NINE = "9";
            isValidPhoneNumber = phoneNumber.substring(0, 1).equals(DIGIT_NINE);

        }

        return isValidPhoneNumber;
    }
}
