package org.abstractica.javablocks.basic.examples;


import org.abstractica.javablocks.basic.*;
import org.abstractica.javablocks.basic.impl.BasicBlockFactoryImpl;

public class SimpleKeyboardConsoleExample
{
    public static void main(String[] args)
    {
        BasicBlockFactory factory = BasicBlockFactoryImpl.getInstance();
        Input<String> keyboard = factory.getKeyboardBlock();

        Output<String> console = factory.getConsoleBlock();
        ThreadBlock<String> thread = factory.getThreadBlock();
        PushBlock<String, String> push = factory.getPushBlock(s -> s+", for fanden!");
        push.setOutput(console);
        thread.setInput(keyboard);
        thread.setOutput(push);
        thread.start();
    }
}
