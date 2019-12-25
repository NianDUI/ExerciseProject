package main;

public class Main {
    public static void main(String[] args) {
        System.getProperties().forEach((o1, o2) -> {
            System.out.println(o1 + ": " + o2);
        });

        System.out.println();
        System.out.println(System.getProperty("user.dir"));

        Long l = 1l;
        l = null;
        System.out.println(l != null);
        System.out.println(l > 0);

    }
}
