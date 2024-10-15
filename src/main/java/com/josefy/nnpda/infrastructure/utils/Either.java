package com.josefy.nnpda.infrastructure.utils;

import java.util.function.Function;

public sealed abstract class Either<A, B> permits Either.Left, Either.Right {
    private Either() {}

    public static final class Left<A, B> extends Either<A, B> {
        private final A value;

        public Left(A value) {
            this.value = value;
        }
        public A getValue() {
            return value;
        }

        @Override
        public <C> C fold(Function<? super A, ? extends C> left,
                          Function<? super B, ? extends C> right) {
            return left.apply(value);
        }
    }
    public static final class Right<A, B> extends Either<A, B> {
        private final B value;

        public Right(B value) {
            this.value = value;
        }
        public B getValue() {
            return value;
        }

        @Override
        public <C> C fold(Function<? super A, ? extends C> left,
                          Function<? super B, ? extends C> right) {
            return right.apply(value);
        }
    }

    public static <A, B> Either<A, B> left(A value) {
        return new Left<>(value);
    }

    public static <A, B> Either<A, B> right(B value) {
        return new Right<>(value);
    }

    public abstract <C> C fold(Function<? super A, ? extends C> left,
                               Function<? super B, ? extends C> right);
}
