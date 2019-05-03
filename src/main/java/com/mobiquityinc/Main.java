package com.mobiquityinc;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.Packer;

public class Main {
    public static void main(String[] args) throws APIException {
        if(args.length != 1) {
            System.err.println("I am expecting one argument with the full path of the radar image. Please, Can you run me again providing this information?");
            System.err.println("For example: ./path/to/input.txt");
            System.exit(-1);
        }

        String result = Packer.pack(args[0]);
        System.out.println(result);
    }
}
