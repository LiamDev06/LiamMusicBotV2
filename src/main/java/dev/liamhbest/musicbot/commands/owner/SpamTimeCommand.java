package dev.liamhbest.musicbot.commands.owner;

import com.jagrosh.jdautilities.command.CommandEvent;
import dev.liamhbest.musicbot.Bot;
import dev.liamhbest.musicbot.commands.OwnerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;

public class SpamTimeCommand extends OwnerCommand {

    private final Bot bot;

    public SpamTimeCommand(Bot bot)  {
        super(bot);
        this.bot = bot;
        this.name = "spamtime";
        this.help = "spam someone";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getArgs().split(" ");

        if (args.length == 0) {
            // Select who to spam
            event.reply(new EmbedBuilder().setColor(Color.RED)
                    .setAuthor("Missing arguments! Please select someone to spam, use !spamtime <@user> <times> <message>").build());
            return;
        }

        if (args.length == 1) {
            // Select how many times
            event.reply(new EmbedBuilder().setColor(Color.RED)
                    .setAuthor("Missing arguments! Please select how many times to spam, use !spamtime <@user> <times> <message>").build());
            return;
        }

        if (args.length == 2) {
            event.reply(new EmbedBuilder().setColor(Color.RED)
                    .setAuthor("Missing arguments! Please input a message to spam with, use !spamtime <@user> <times> <message>").build());
            return;
        }

        if (event.getMessage().getMentionedMembers().size() == 0) {
            event.reply(new EmbedBuilder().setColor(Color.RED)
                    .setAuthor("Error! Invalid user spam input!").build());
            return;
        }

        Member who = event.getMessage().getMentionedMembers().get(0);

        int times;
        try {
            times = Integer.parseInt(event.getArgs().split(" ")[1]);
        } catch (Exception exception) {
            event.reply(new EmbedBuilder().setColor(Color.RED)
                    .setAuthor("Invalid times input!").build());
            return;
        }

        StringBuilder builder = new StringBuilder();
        int loop = 0;

        for (String s : event.getArgs().split(" ")) {
            if (loop > 1) {
                builder.append(s).append(" ");
            }

            loop++;
        }

        final String message = builder.toString().trim();
        event.getChannel().sendMessage("**INITIATING SPAM!** You will spam " + who.getEffectiveName() + " " + times + " times with the message `" + message + "`.").queue();

        for (int i = 0; i<times; i++) {
            event.getChannel().sendMessage("**" + message + "** <@" + who.getId() + ">").queue();
        }
    }
}














