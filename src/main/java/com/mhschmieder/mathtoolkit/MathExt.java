/**
 * MIT License
 *
 * Copyright (c) 2020, 2022 Mark Schmieder
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * This file is part of the MathToolkit Library
 *
 * You should have received a copy of the MIT License along with the
 * MathToolkit Library. If not, see <https://opensource.org/licenses/MIT>.
 *
 * Project: https://github.com/mhschmieder/mathtoolkit
 */
package com.mhschmieder.mathtoolkit;

import java.math.BigDecimal;
import java.util.Random;

import org.apache.commons.math3.util.Precision;

/**
 * This class provides methods and constants that are in the C run-time
 * library that were left out of java.math.Math, as well as other useful
 * methods that are core math functions.
 * NOTE: Some of these methods and constants are patterned after the API spec
 * from the once-free JNL from VisualNumerics, the Math1 class from
 * "Java for Engineers and Scientists" (Stephen J. Chapman, Prentice-Hall), and
 * the Math2 class from "Technical Java" (Grant Palmer, Prentice-Hall), along
 * with the PhysicsUtilities class from PtPlot.
 */
public final class MathExt {

    /**
     * The default constructor is disabled, as this is a static utilities class.
     */
    private MathExt() {}

    // Reference for random number generator, to make sure there is only one
    // instance each time an application that uses this class is invoked.
    // Otherwise, successive calls would use the same root and generally find
    // the same first random number, defeating the purpose altogether.
    private static Random _randomNumberGenerator = null;

    public static double acosh( final double x ) {
        return Math.log( x + StrictMath.sqrt( sqr( x ) - 1.0d ) );
    }

    // Inverse hyperbolic functions.
    public static double asinh( final double x ) {
        return Math.log( x + StrictMath.sqrt( 1.0d + sqr( x ) ) );
    }

    public static double atanh( final double x ) {
        return 0.5d * StrictMath.log( ( 1.0d + x ) / ( 1.0d - x ) );
    }

    public static double boundedValue( final double value, final double min, final double max ) {
        // if max < value, return max
        // if min > value, return min
        // if min > max, return min
        return Math.min( StrictMath.max( value, min ), StrictMath.max( min, max ) );
    }

    public static double cosh( final double x ) {
        return 0.5d * ( StrictMath.exp( x ) + StrictMath.exp( -x ) );
    }

    public static double cot( final double x ) {
        return 1.0d / StrictMath.tan( x );
    }

    public static double csc( final double x ) {
        return 1.0d / StrictMath.sin( x );
    }

    // Return the exponent of a number represented by mantissa * 10^ (exponent).
    public static int exponent( final double number ) {
        return ( int ) StrictMath.floor( StrictMath.log10( Math.abs( number ) ) );
    }

    /**
     * Computes the factorial for the provided long integer.
     * <p>
     * Note that overflow may occur as early as an input value of 20.
     * 
     * @param x
     *            The number for which to compute the factorial
     * @return The computed factorial for the provided input number
     */
    public static long factorial( final long x ) {
        if ( x == 0 ) {
            return 1;
        }

        // Recursively call this function to achieve the factorial result.
        return x * factorial( x - 1 );
    }

    // Gamma and logarithmic gamma function.
    // NOTE: As the gamma function increases as a function of (z - 1)!, most
    // computers will experience an arithmetic overflow a z > 140 or so. For
    // this reason, the logarithm of the gamma function is provided as well.
    public static double gamma( final double x ) {
        final double grp1 = x + 0.5d;
        final double grp2 = x + 5.5d;
        double grp3 = MathConstants.GAMMA_COEFFICIENTS[ 0 ];

        for ( int i = 1; i < 7; i++ ) {
            grp3 += MathConstants.GAMMA_COEFFICIENTS[ i ] / ( x + i );
        }

        return ( StrictMath.pow( grp2, grp1 ) * StrictMath.exp( -grp2 ) * MathConstants.SQRT_TWO_PI
                * grp3 ) / x;
    }

    // Return the number of fractional digits required to display the given
    // number. No number larger than 16 is returned (if more than 16 digits are
    // required, 16 is returned).
    public static int getNumberOfFractionalDigits( final double num ) {
        int numdigits = 0;
        double numAdjusted = num;
        while ( ( numdigits <= 15 ) && ( numAdjusted != Math.floor( numAdjusted ) ) ) {
            numAdjusted *= 10.0d;
            numdigits += 1;
        }
        return numdigits;
    }

    // Return the number of integer digits required to display the given number.
    // No number larger than 16 is returned (if more than 16 digits are
    // required, 16 is returned).
    public static int getNumberOfIntegerDigits( final double num ) {
        int numdigits = 0;
        double numAdjusted = num;
        while ( ( numdigits <= 15 ) && ( ( int ) numAdjusted != 0.0 ) ) {
            numAdjusted /= 10.0d;
            numdigits += 1;
        }
        return numdigits;
    }

    // Logarithms taken to common and bases.
    public static double log2( final double x ) {
        return StrictMath.log( x ) * MathConstants.LN2_SCALE;
    }

    // The error function and its complement.
    // TODO: Implement these based on p. 498 of CRC Standard math Tables, 30th
    // edition (section 6.13, Error Functions).
    /*
     * public static double erf( double x ) { } public static double erfc(
     * double x ) { }
     */

    public static double logGamma( final double x ) {
        return StrictMath.log( gamma( x ) );
    }

    public static double logN( final double x, final double n ) {
        return StrictMath.log( x ) / StrictMath.log( n );
    }

    // Return the mantissa of a number represented by mantissa * 10^ (exponent).
    public static double mantissa( final double number ) {
        final int exponent = exponent( number );
        return number / StrictMath.pow( 10.0d, exponent );
    }

    // Return the common modulus (remainder) of a number and a given divisor.
    public static double mod( final double number, final double divisor ) {
        final double factor = StrictMath.floor( number / divisor );
        return number - ( divisor * factor );
    }

    // Return a number rounded to the nth decimal place.
    public static double nearestDecimal( final double number,
                                         final int numberOfDigitsAfterDecimalPoint )
            throws IllegalArgumentException, NumberFormatException {
        // Number of digits after decimal point < 0, is invalid input, so return
        // the number unchanged.
        if ( numberOfDigitsAfterDecimalPoint < 0 ) {
            throw new IllegalArgumentException();
        }

        // Number of digits after decimal point = 0, means round to the nearest
        // integer.
        if ( numberOfDigitsAfterDecimalPoint == 0 ) {
            return Math.round( number );
        }

        // Use the built-in BigDecimal class to do the work, as library code
        // usually covers more edge cases than home-grown algorithms.
        // NOTE: We use Apache Commons Math instead, which wraps the built-in
        // BigDecimal class with safe edge case detection and handling.
        // TODO: Check for Apache Commons Math updates, that are compatible
        // with Java 12 and its removal of BigDecimal.ROUND_HALF_UP.
        final double numberRounded = Precision
                .round( number, numberOfDigitsAfterDecimalPoint, BigDecimal.ROUND_HALF_UP );
        // final double numberRounded = Precision .round( number,
        // numberOfDigitsAfterDecimalPoint, RoundingMode.HALF_UP );

        return numberRounded;
    }

    // Return a normally-distributed Gaussian random number.
    public static double randomGaussian() {
        if ( _randomNumberGenerator == null ) {
            _randomNumberGenerator = new Random();
        }

        return _randomNumberGenerator.nextGaussian();
    }

    // Return a Rayleigh-distributed Gaussian random number.
    public static double randomRayleigh() {
        if ( _randomNumberGenerator == null ) {
            _randomNumberGenerator = new Random();
        }

        final double x = _randomNumberGenerator.nextGaussian();
        final double y = _randomNumberGenerator.nextGaussian();

        return Math.hypot( x, y );
    }

    // Given a number, round up to the nearest power of ten (times 1, 2, or 5).
    // NOTE: The argument must be strictly positive.
    public static double roundUp( final double val ) {
        final int exponent = ( int ) StrictMath.floor( StrictMath.log10( val ) );
        double valAdjusted = val;
        valAdjusted *= StrictMath.pow( 10.0d, -exponent );
        if ( valAdjusted > 5.0d ) {
            valAdjusted = 10.0d;
        }
        else if ( valAdjusted > 2.0d ) {
            valAdjusted = 5.0d;
        }
        else if ( valAdjusted > 1.0d ) {
            valAdjusted = 2.0d;
        }
        valAdjusted *= StrictMath.pow( 10.0d, exponent );
        return valAdjusted;
    }

    // Trigonometric functions.
    public static double sec( final double x ) {
        return 1.0d / StrictMath.cos( x );
    }

    // Return the value of x with the sign of y.
    public static double sign( final double x, final double y ) {
        return ( y < 0.0d ) ? ( x < 0.0d ) ? x : -x : ( x < 0.0d ) ? -x : x;
    }

    // Return the function sinc(x), where sinc(x) is defined as sin(x) / x.
    public static double sinc( final double x ) {
        return ( StrictMath.abs( x ) < 1.0e-30 ) ? 1.0d : StrictMath.sin( x ) / x;
    }

    // Hyperbolic functions.
    public static double sinh( final double x ) {
        return 0.5d * ( StrictMath.exp( x ) - StrictMath.exp( -x ) );
    }

    // Return the simple square of a double.
    public static double sqr( final double x ) {
        return x * x;
    }

    public static double tanh( final double x ) {
        final double e1 = StrictMath.exp( x );
        final double e2 = StrictMath.exp( -x );
        return ( e1 - e2 ) / ( e1 + e2 );
    }

}
