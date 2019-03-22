//--------------------------TRIGONOMETRIC CLASS-------------------------------//
//@author TitanJack
//@project MathTools
//The class contains most trigonometric functions including primary, reciprocal,
//and hyperbolic trig functions as well as their respective inverses

package Functions;

public class Trigonometric extends Function {

    private String type;
    private double coeff;
    private Function subFunc;

    public Trigonometric(String type, Function subFunc) {
        this(type, subFunc, 1);
    }

    public Trigonometric(String type, Function subFunc, double coeff) {
        this.type = type;
        this.subFunc = subFunc;
        this.coeff = coeff;
    }

    public double compute(double x) {
        double y = subFunc.compute(x);
        double result = 0;
        switch (type) {
            case "sin": result = Math.sin(y);
                break;
            case "cos": result = Math.cos(y);
                break;
            case "tan": result = Math.tan(y);
                break;
            case "csc": result = 1/Math.sin(y);
                break;
            case "sec": result = 1/Math.cos(y);
                break;
            case "cot": result = 1/Math.tan(y);
                break;
            case "arcsin": result = Math.asin(y);
                break;
            case "arccos": result = Math.acos(y);
                break;
            case "arctan": result = Math.atan(y);
                break;
            case "arccsc": result = Math.asin(1/y);
                break;
            case "arcsec": result = Math.acos(1/y);
                break;
            case "arccot": result = Math.atan(1/y);
                break;
            case "sinh": result = Math.sinh(y);
                break;
            case "cosh": result = Math.cosh(y);
                break;
            case "tanh": result = Math.tanh(y);
                break;
            case "csch": result = 1/Math.sinh(y);
                break;
            case "sech": result = 1/Math.cosh(y);
                break;
            case "coth": result = 1/Math.tanh(y);
                break;
            case "arcsinh": result = Math.log(y + Math.sqrt(y*y + 1.0));
                break;
            case "arccosh": result = Math.log(y + Math.sqrt(y*y - 1.0));
                break;
            case "arctanh": result = 0.5 * Math.log((1.0 + y)/(1.0 - y));
                break;
            case "arccsch":
                result = Math.log(1.0/y + Math.sqrt(1.0/(y*y) + 1.0));
                break;
            case "arcsech":
                result = Math.log(1.0/y + Math.sqrt(1.0/(y*y) - 1.0));
                break;
            case "arccoth": result = 0.5 * Math.log((y + 1.0)/(y - 1.0));
                break;
            default: System.exit(0);
                break;
        }
        result = coeff * result;
        return result;
    }

    public Function differentiate() {
        Function diff = null;
        switch (type) {
            case "sin": diff = new Trigonometric("cos", subFunc, 1);
                break;
            case "cos": diff = new Trigonometric("sin", subFunc, -1);
                break;
            case "tan": diff = new Exponential(new Trigonometric("sec",
                    subFunc), 2, 1);
                break;
            case "csc": diff = new GeoFunction(new Trigonometric("csc",
                    subFunc), new Trigonometric("cot", subFunc), -1);
                break;
            case "sec": diff = new GeoFunction(new Trigonometric("sec",
                    subFunc), new Trigonometric("tan", subFunc), 1);
                break;
            case "cot": diff = new Exponential(new Trigonometric("csc",
                    subFunc), 2, -1);
                break;
            case "arcsin": diff = new Rational(1, new Exponential(new
                    SumFunction(new Function[]{new Constant(1), new
                    Exponential(new Variable(), 2, -1)}), 0.5),
                    1);
                break;
            case "arccos": diff = new Rational(1, new Exponential(new
                    SumFunction(new Function[]{new Constant(1), new
                    Exponential(new Variable(), 2, -1)}), 0.5),
                    -1);
                break;
            case "arctan": diff = new Rational(1, new SumFunction(new
                    Function[]{new Constant(1), new  Exponential(new
                    Variable(), 2)}), 1);
                break;
            case "arccsc": diff = new Rational(1, new GeoFunction(new
                    Exponential(new SumFunction(new Function[]{new Constant(
                    1), new Rational(1, new Exponential(new
                    Variable(), 2), -1)}), 0.5), new
                    Exponential(new Variable(), 2)), -1);
                break;
            case "arcsec": diff = new Rational(1, new GeoFunction(new
                    Exponential(new SumFunction(new Function[]{new Constant(
                    1), new Rational(1, new Exponential(new
                    Variable(), 2), -1)}), 0.5), new
                    Exponential(new Variable(), 2)), 1);
                break;
            case "arccot": diff = new Rational(new Constant(1),
                    new SumFunction (new Function[]{new Constant(1),
                    new Exponential(new Variable(), 2)}), -1);
                break;
            case "sinh": diff = new Trigonometric("cosh", subFunc, 1);
                break;
            case "cosh": diff = new Trigonometric("sinh", subFunc, 1);
                break;
            case "tanh": diff = new Exponential(new Trigonometric("sech",
                    subFunc), 2, 1);
            case "csch": diff = new GeoFunction(new Trigonometric("csch",
                    subFunc), new Trigonometric("coth", subFunc), -1);
                break;
            case "sech": diff = new GeoFunction(new Trigonometric("sech",
                    subFunc), new Trigonometric("tanh", subFunc), 1);
                break;
            case "coth": diff = new Exponential(new Trigonometric("csc",
                    subFunc), 2, -1);
                break;
            case "arcsinh": diff = new Rational(1, new Exponential(
                    new SumFunction(new Function[]{new Exponential(new
                    Variable(), 2), new Constant(1)}), 0.5),
                    1);
                break;
            case "arccosh": diff = new Rational(1, new Exponential(
                    new SumFunction(new Function[]{new Exponential(new
                    Variable(), 2), new Constant(-1)}), 0.5),
                    1);
                break;
            case "arctanh": diff = new Rational(1, new SumFunction(new
                    Function[]{new Constant(1), new  Exponential(new
                    Variable(), 2, -1)}), 1);
                break;
            case "arccsch": diff = new Rational(1, new GeoFunction(new
                    Exponential(new SumFunction(new Function[]{new Rational(1,
                    new Exponential(new Variable(), 2)), new Constant(
                    1)}), 0.5), new Exponential(new Variable(),
                    2)), -1);
                break;
            case "arcsech": diff = new Rational(1, new GeoFunction(new
                    Exponential(new SumFunction(new Function[]{new Rational(1,
                    new Exponential(new Variable(), 2)), new Constant(
                    1)}), 0.5), new Exponential(new Variable(),
                    2)), 1);
                break;
            case "arccoth": diff = new Rational(new Constant(1),
                    new SumFunction (new Function[]{new Exponential(new
                    Variable(), 2, -1), new Constant(1), }),
                    -1);
                break;
        }
        if (diff == null) {
            System.exit(0);
            return null;
        } else {
            return new GeoFunction(new Function[]{diff,
                    subFunc.differentiate()}, coeff);
        }
    }

    public double getCoeff() {
        return coeff;
    }

    public Function setCoeff(double c) {
        coeff = c;
        return this;
    }

    public String toString() {
        String funcStr = type + "(" + subFunc.toString() + ")";
        if (coeff == -1) funcStr = "-" + funcStr;
        else if (coeff != 1) funcStr = coeff + funcStr;
        return funcStr;
    }
}
