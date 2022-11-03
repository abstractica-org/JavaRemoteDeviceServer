package org.abstractica.javablocks.basic;

public interface Output<E>
{
    public void put(E item) throws InterruptedException;
}
