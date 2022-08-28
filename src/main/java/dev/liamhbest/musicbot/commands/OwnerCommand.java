package dev.liamhbest.musicbot.commands;

import com.jagrosh.jdautilities.command.Command;
import dev.liamhbest.musicbot.Bot;

public abstract class OwnerCommand extends Command {

    public OwnerCommand(Bot bot) {
        this.category = new Category("Owner");
        this.ownerCommand = true;
    }

}