package com.program.readmin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.*;
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

    public static void execFile (String batFile) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(System.getProperty("user.dir") + "\\scripts\\" + batFile);

        ExecutorService pool = Executors.newSingleThreadExecutor();

        try {
            Process process = builder.start();
            ProcessReader task = new ProcessReader(process.getInputStream());
            Future<List<String>> future = pool.submit(task);

            List<String> results = future.get();
            for (String res : results) {
                System.out.println(res);
            }

            int exitCode = process.waitFor();


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            pool.shutdown();
        }
    }
}
