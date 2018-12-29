package controller;

public class BindingBundle {
    private final String executable;
    private final String arguments;

    public BindingBundle(String executablePath, String argumentsText) {
        this.executable = executablePath;
        this.arguments = argumentsText;
    }

    public String getExecutable() {
        return executable;
    }

    public String getArguments() {
        return arguments;
    }
}
