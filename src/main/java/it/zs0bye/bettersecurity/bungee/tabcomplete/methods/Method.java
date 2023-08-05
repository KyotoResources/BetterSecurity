package it.zs0bye.bettersecurity.bungee.tabcomplete.methods;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Method {

    private MethodType type;
    private List<String> suggestions;
    private String command;

    public boolean contains() {
        return this.suggestions.contains(this.command);
    }

}
