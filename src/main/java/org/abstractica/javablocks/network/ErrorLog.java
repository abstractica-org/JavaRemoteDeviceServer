package org.abstractica.javablocks.network;

import java.io.IOException;

public interface ErrorLog
{
    public void reportIOException(IOException e);
}
