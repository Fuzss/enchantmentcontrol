package fuzs.enchantmentcontrol.api.v1.data;

import java.util.Map;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;
import java.util.function.ToDoubleFunction;

/**
 * A string math expression evaluator, similar to something like <a
 * href="https://github.com/ezylang/EvalEx">EvalEx</a>.
 * <p>
 * The implementation is immutable and the result once calculated via {@link #evaluate()} is therefore cached.
 * <p>
 * The idea of making enchantment costs configurable via such an approach is taken from the <a
 * href="https://github.com/Shadows-of-Fire/Apotheosis">Apotheosis</a> mod.
 */
@Deprecated
public interface ExpressionEvaluator {

    /**
     * @return the string being evaluated for serialization
     */
    String getString();

    /**
     * Register a variable to be referenced in parsed strings.
     * <p>
     * A variable by the same name already present is overridden.
     *
     * @param name     name of the variable
     * @param function the variable implementation
     * @return new expression evaluator instance
     */
    default ExpressionEvaluator with(String name, double value) {
        return this.with(name, 0, $ -> value);
    }

    /**
     * Register a variable to be referenced in parsed strings.
     * <p>
     * A variable by the same name already present is overridden.
     *
     * @param name     name of the variable
     * @param function the variable implementation
     * @return new expression evaluator instance
     */
    default ExpressionEvaluator with(String name, DoubleSupplier function) {
        return this.with(name, 0, $ -> function.getAsDouble());
    }

    /**
     * Register a function to be referenced in parsed strings.
     * <p>
     * A function by the same name already present is overridden.
     *
     * @param name     name of the function
     * @param function the function implementation
     * @return new expression evaluator instance
     */
    default ExpressionEvaluator with(String name, DoubleUnaryOperator function) {
        return this.with(name, 1, args -> function.applyAsDouble(args[0]));
    }

    /**
     * Register a function to be referenced in parsed strings.
     * <p>
     * A function by the same name already present is overridden.
     *
     * @param name     name of the function
     * @param function the function implementation
     * @return new expression evaluator instance
     */
    default ExpressionEvaluator with(String name, DoubleBinaryOperator function) {
        return this.with(name, 2, args -> function.applyAsDouble(args[0], args[1]));
    }

    /**
     * Register a function / variable to be referenced in parsed strings.
     * <p>
     * A function / variable by the same name already present is overridden.
     *
     * @param name         name of the function / variable
     * @param requiredArgs amount of arguments, variables always require zero arguments
     * @param function     the function / variable implementation
     * @return new expression evaluator instance
     */
    ExpressionEvaluator with(String name, int requiredArgs, Function function);

    /**
     * Register multiple functions / variables to be referenced in parsed strings.
     * <p>
     * Functions / variables by the same name already present are overridden.
     *
     * @param functions the functions / variables
     * @return new expression evaluator instance
     */
    ExpressionEvaluator with(Map<String, Function> functions);

    /**
     * @return the evaluated expression result, will throw an exception when missing functions / variables
     */
    double evaluate();

    /**
     * Register a function to be referenced in parsed strings.
     * <p>
     * Functions with zero arguments can be used as variables and allow for a syntax without parenthesis.
     */
    @FunctionalInterface
    interface Function extends ToDoubleFunction<double[]> {
        // NO-OP
    }
}
