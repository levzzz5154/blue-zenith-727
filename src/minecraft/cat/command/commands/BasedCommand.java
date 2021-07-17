package cat.command.commands;

import cat.command.Command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class BasedCommand extends Command {

    public BasedCommand() {
        super("Test", "Hello", "Hi");
    }

    @Override
    public void execute(String[] args) {
        try {
            URL url = new URL("http://localhost:8080/users/retrieve?name=b");
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                try {
                    URLConnection c = url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    return reader.readLine();
                } catch (Exception e) { }
                return "error";
            });
            chat(future.get());
        } catch(Exception ex) {
            chat("error");
        }
    }
}
