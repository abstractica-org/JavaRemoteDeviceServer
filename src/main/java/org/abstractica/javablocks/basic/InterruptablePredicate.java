package org.abstractica.javablocks.basic;

public interface InterruptablePredicate<E>
{
    public boolean test(E obj) throws InterruptedException;
}
