package io.github.yangziwen.cookieproxy;

public class Main {

    public static void main(String[] args) {
        Server.run("0.0.0.0", 8041);
        System.out.println("cookie-proxy started");
    }

}