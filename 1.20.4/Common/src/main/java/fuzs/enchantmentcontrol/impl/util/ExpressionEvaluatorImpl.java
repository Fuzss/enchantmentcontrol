package fuzs.enchantmentcontrol.impl.util;

import com.google.common.collect.ImmutableMap;
import fuzs.enchantmentcontrol.api.v1.data.ExpressionEvaluator;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.ToDoubleFunction;

/**
 * Copied from a <a href="https://stackoverflow.com/a/26227947">StackOverflow post</a> by <a
 * href="https://stackoverflow.com/users/964243/boann">Boann</a>.
 * <p>
 * Changes from the original proposal include:
 * <ul>
 *     <li>Support for custom functions, including a few more built-in functions</li>
 *     <li>Support for variables (implemented as functions with zero arguments)</li>
 *     <li>Separate compilation and evaluation as outlined in the original StackOverflow comment</li>
 *     <li>Removal of the <code>functionName factor</code> syntax in favor of <code>functionName `(` expression `)`</code> to allow for zero argument functions</li>
 * </ul>
 */
public final class ExpressionEvaluatorImpl implements ExpressionEvaluator {
    private final String str;
    private final Expression expression;
    private final Map<String, Function> functions;
    int position = -1, character;
    @Nullable Double result;

    ExpressionEvaluatorImpl(String str) {
        this.str = str;
        this.expression = this.parse();
        this.functions = Collections.emptyMap();
    }

    ExpressionEvaluatorImpl(String str, Expression expression, Map<String, Function> functions) {
        this.str = str;
        this.expression = expression;
        this.functions = functions;
    }

    public static ExpressionEvaluator create(String str) {
        return new ExpressionEvaluatorImpl(str).with("sqrt", Math::sqrt)
                .with("sin", Math::sin)
                .with("cos", Math::cos)
                .with("tan", Math::tan)
                .with("abs", Math::abs)
                .with("floor", Math::floor)
                .with("ceil", Math::ceil)
                .with("signum", Math::signum)
                .with("round", Math::round)
                .with("log", Math::log)
                .with("pow", Math::pow)
                .with("min", Math::min)
                .with("max", Math::max)
                .with("random", Math::random);
    }

    @Override
    public String toString() {
        return this.getString();
    }

    @Override
    public String getString() {
        return this.str;
    }

    @Override
    public ExpressionEvaluator with(String name, int requiredArgs, Function function) {
        ImmutableMap.Builder<String, Function> builder = ImmutableMap.builderWithExpectedSize(
                this.functions.size() + 1);
        builder.putAll(this.functions);
        builder.put(name, args -> {
            if (requiredArgs != args.length) {
                throw new RuntimeException("Function " + name + " requires " + requiredArgs + " argument(s), found " +
                        Arrays.toString(args));
            }

            return function.applyAsDouble(args);
        });

        return new ExpressionEvaluatorImpl(this.str, this.expression, builder.build());
    }

    @Override
    public ExpressionEvaluator with(Map<String, Function> functions) {
        ImmutableMap.Builder<String, Function> builder = ImmutableMap.builderWithExpectedSize(
                this.functions.size() + functions.size());
        builder.putAll(this.functions);
        builder.putAll(functions);
        // duplicates are possible, keep the one from the map just passed in
        return new ExpressionEvaluatorImpl(this.str, this.expression, builder.buildKeepingLast());
    }

    @Override
    public double evaluate() {
        return this.result == null ? this.result = this.expression.applyAsDouble(this.functions) : this.result;
    }

    void nextChar() {
        this.character = (++this.position < this.str.length()) ? this.str.charAt(this.position) : -1;
    }

    boolean eat(int charToEat) {
        while (this.character == ' ') this.nextChar();
        if (this.character == charToEat) {
            this.nextChar();
            return true;
        }
        return false;
    }

    Expression parse() {
        this.nextChar();
        Expression x = this.parseExpression();
        if (this.position < this.str.length() || x == null) {
            throw this.unexpectedCharacter();
        }
        return x;
    }
    // Grammar:
    // expression = term | expression `+` term | expression `-` term
    // term = factor | term `*` factor | term `/` factor
    // factor = `+` factor | `-` factor | `(` expression `)` | number
    //        | functionName `(` expression `)` | functionName factor

    //        | factor `^` factor

    Expression parseExpression() {
        Expression x = this.parseTerm();
        while (true) {
            if (this.eat('+')) {
                if (x == null) throw this.unexpectedCharacter('+');
                Expression a = x, b = this.parseTerm();
                if (b == null) throw this.unexpectedCharacter();
                x = f -> a.applyAsDouble(f) + b.applyAsDouble(f); // addition
            } else if (this.eat('-')) {
                if (x == null) throw this.unexpectedCharacter('-');
                Expression a = x, b = this.parseTerm();
                if (b == null) throw this.unexpectedCharacter();
                x = f -> a.applyAsDouble(f) - b.applyAsDouble(f); // subtraction
            } else {
                return x;
            }
        }
    }

    Expression parseTerm() {
        Expression x = this.parseFactor();
        while (true) {
            if (this.eat('*')) {
                if (x == null) throw this.unexpectedCharacter('*');
                Expression a = x, b = this.parseTerm();
                if (b == null) throw this.unexpectedCharacter();
                x = f -> a.applyAsDouble(f) * b.applyAsDouble(f); // multiplication
            } else if (this.eat('/')) {
                if (x == null) throw this.unexpectedCharacter('/');
                Expression a = x, b = this.parseTerm();
                if (b == null) throw this.unexpectedCharacter();
                x = f -> a.applyAsDouble(f) / b.applyAsDouble(f); // division
            } else {
                return x;
            }
        }
    }

    Expression parseFactor() {
        if (this.eat('+')) {
            Expression a = this.parseFactor();
            if (a == null) throw this.unexpectedCharacter();
            return f -> +a.applyAsDouble(f); // unary plus
        } else if (this.eat('-')) {
            Expression a = this.parseFactor();
            if (a == null) throw this.unexpectedCharacter();
            return f -> -a.applyAsDouble(f); // unary minus
        }

        Expression x;
        int startPos = this.position;
        if (this.eat('(')) { // parentheses
            x = this.parseExpression();
            if (!this.eat(')')) throw new RuntimeException("Missing ')'");
        } else if ((this.character >= '0' && this.character <= '9') || this.character == '.') { // numbers
            while ((this.character >= '0' && this.character <= '9') || this.character == '.') this.nextChar();
            double a = Double.parseDouble(this.str.substring(startPos, this.position));
            x = f -> a;
        } else if (this.character >= 'a' && this.character <= 'z') { // functions
            while (this.character >= 'a' && this.character <= 'z') this.nextChar();
            String func = this.str.substring(startPos, this.position);
            List<Expression> args = new ArrayList<>();
            if (this.eat('(')) {
                Expression y;
                while ((y = this.parseExpression()) != null) {
                    args.add(y);
                    if (!this.eat(',')) break;
                }
                if (!this.eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
            }

            x = f -> {
                if (f.containsKey(func)) {
                    double[] doubles = args.stream().mapToDouble(v -> v.applyAsDouble(f)).toArray();
                    return f.get(func).applyAsDouble(doubles);
                } else {
                    throw new RuntimeException("Unknown function: " + func + "\nAvailable functions: " + f.keySet());
                }
            };
        } else {
            return null;
        }

        if (this.eat('^')) {
            Expression a = x, b = this.parseFactor();
            if (b == null) throw this.unexpectedCharacter();
            x = f -> Math.pow(a.applyAsDouble(f), b.applyAsDouble(f)); // exponentiation
        }

        return x;
    }

    private RuntimeException unexpectedCharacter() {
        return this.unexpectedCharacter(this.character);
    }

    private RuntimeException unexpectedCharacter(int character) {
        if (character == -1) {
            return new RuntimeException("Unexpected end of string");
        } else {
            return new RuntimeException("Unexpected character: " + (char) character);
        }
    }

    @FunctionalInterface
    interface Expression extends ToDoubleFunction<Map<String, Function>> {
        // NO-OP
    }
}
