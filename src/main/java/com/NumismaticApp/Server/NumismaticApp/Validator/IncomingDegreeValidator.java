package com.NumismaticApp.Server.NumismaticApp.Validator;

import com.NumismaticApp.Server.NumismaticApp.Exception.DegreeErrorException;

import java.util.Optional;

public class IncomingDegreeValidator {

    public static void checkDegree(int degree) throws DegreeErrorException {

            int chDeg = Optional.ofNullable(degree).orElse(0);

            if (chDeg<0||chDeg>2){

                throw new DegreeErrorException("unknown degree");
            }

    }
}
