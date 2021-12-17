package com.mars.multi.thread.learn.test;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Scanner;

public class PipedTest {

    public static void main(String[] args) throws IOException {
        PipedWriter out = new PipedWriter();
        PipedReader in = new PipedReader();
        out.connect(in);
        Thread thread = new Thread(new Print(in), "PrintThread");
        thread.start();
        int recrive = 0;
        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                out.write(scanner.next());
            }
        } catch (IOException e) {

        } finally {
            out.close();
//            in.close();
        }
    }

    static class Print implements Runnable {
        private PipedReader in;

        public Print(PipedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            int receive = 0;
            while (true) {
                try {
                    if (((receive = in.read()) != -1)) {
                        receive = in.read();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(receive);
            }
        }
    }
}
