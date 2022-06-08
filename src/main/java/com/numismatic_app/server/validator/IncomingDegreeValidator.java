package com.numismatic_app.server.validator;

import com.numismatic_app.server.exception.DegreeErrorException;

import java.util.Optional;

/**
 * @deprecated
 */
public class IncomingDegreeValidator {
    private IncomingDegreeValidator() {
        throw new IllegalStateException("Utility class");
    }

    public static void checkDegree(int degree) throws DegreeErrorException {

            int chDeg = Optional.ofNullable(degree).orElse(0);

            if (chDeg<0||chDeg>2){

                throw new DegreeErrorException("unknown degree");
            }

    }
}
