package fr.ambox.machine;

public class Config {

    private Env env;

    public Config() {
        this.env = Env.DEV;
        String envVar = System.getenv("ENV");
        if (envVar != null) {
            if (envVar.equals("prod")) {
                this.env = Env.PROD;
            }
        }
    }

    public String getHost() {
        return "127.0.0.1";
    }

    public String getUsername() {
        return "lang";
    }

    public String getPassword() {
        return "lang-password";
    }
}
