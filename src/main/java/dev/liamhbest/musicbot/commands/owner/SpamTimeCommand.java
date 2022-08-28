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
        if (event.getArgs().length() == 0) {
            // Select who to spam
            event.reply(new EmbedBuilder().setColor(Color.RED)
                    .setAuthor("Missing arguments! Please select someone to spam, use !spamtime <@user> <times>").build());
            return;
        }

        if (event.getArgs().length() == 1) {
            // Select how many times
            event.reply(new EmbedBuilder().setColor(Color.RED)
                    .setAuthor("Missing arguments! Please select how many times to spam, use !spamtime <@user> <times>").build());
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

        if (who.getIdLong() == 447058319341781022L) {
            event.getChannel().sendMessage("**INITIATING SPAM!** You will spam " + who.getEffectiveName()).queue();

            for (int i = 0; i < times; i++) {
                event.getChannel().sendMessage("**SPAM! __GÅ UT GÅ UT!__** <@" + who.getId() + ">").queue();
            }
        } else {
            event.getChannel().sendMessage("**INITIATING SPAM!** You will spam " + who.getEffectiveName() + " " + times + " times!").queue();

            for (int i = 0; i<times; i++) {
                event.getChannel().sendMessage("**SPAM!** <@" + who.getId() + ">").queue();
            }
        }

        /* SIMP SPAM
        if (who.getIdLong() == 447058319341781022L) {
            event.getChannel().sendMessage("**INITIATING SPAM!** You will spam " + who.getEffectiveName() + " and " + "<@650639396797939728> " + + times + " times!").queue();

            for (int i = 0; i < times; i++) {
                event.getChannel().sendMessage("**SPAM! STOP SIMPING!** <@" + who.getId() + "> <@650639396797939728>").queue();
            }
        } else {
            event.getChannel().sendMessage("**INITIATING SPAM!** You will spam " + who.getEffectiveName() + " " + times + " times!").queue();

            for (int i = 0; i<times; i++) {
                event.getChannel().sendMessage("**SPAM!** <@" + who.getId() + ">").queue();
            }
        }

         */
    }
}














