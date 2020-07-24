package org.nom.mds;

import org.nom.mds.toolkit.CryptoUtils;

public class Enc {

    public static void main(String[] args) {
        String temp = CryptoUtils.encrypt(args[0]);
        System.out.println();
        System.out.println("encode ==> " + temp);
        System.out.println();
    }

}
