package app.com.free.todo.util;

public class Delay {
    public Delay(int delay) {
        try {
            wait((long) delay);
        } catch (InterruptedException e) {
        }
    }
}
