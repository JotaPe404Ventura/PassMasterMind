package com.example.passwordgenerator;

public class RandomStrings {
    private int stringLength;
    private String alphaNumeric;

    public RandomStrings(int lenght, String alphaNumeric){
        this.setStringLength(lenght);
        this.setAlphaNumeric(alphaNumeric);
    }

    public String getAlphaNumeric() {
        return alphaNumeric;
    }

    public void setAlphaNumeric(String alphaNumeric) {
        this.alphaNumeric = alphaNumeric;
    }

    public int getStringLength() {
        return stringLength;
    }

    public void setStringLength(int stringLength) {
        this.stringLength = stringLength;
    }


    public String getRandomString(){
        StringBuilder stringBuilder = new StringBuilder(getStringLength());

        for (int i = 0; i < getStringLength(); i ++){
            int index = (int) (getAlphaNumeric().length() * Math.random());
            stringBuilder.append(getAlphaNumeric().charAt(index));
        }

        return stringBuilder.toString();
    }
}

