package com.program.readmin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class ShellExecute {
    class StreamGobbler extends Thread
    {
        private InputStream is;
        private String type;
        StreamGobbler(InputStream is, String type)
        {
            this.is = is;
            this.type = type;
        }

        public void run()
        {
            try
            {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line=null;
                while ( (line = br.readLine()) != null)
                    System.out.println(this.type+">  "+line);
            } catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
    }

    private static class ProcessReader implements Callable {
        private InputStream inputStream;

        public ProcessReader(InputStream inputStream){
            this.inputStream = inputStream;
        }

        @Override
        public Object call() throws Exception{
            return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.toList());
        }
    }
}
