package org.abstractica.javablocks.network.impl;


import org.abstractica.javablocks.network.ErrorLog;

import java.io.IOException;

public class StdError implements ErrorLog
{
    @Override
    public void reportIOException(IOException e)
    {
        e.printStackTrace();
    }
}
