package io.github.yangziwen.cookieproxy;

public class Main {

    public static void main(String[] args) {
        Server.run("0.0.0.0", 8415);
        System.out.println("cookie-proxy started");
    }

}