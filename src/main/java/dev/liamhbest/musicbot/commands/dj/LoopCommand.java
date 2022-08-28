package dev.liamhbest.musicbot.commands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import dev.liamhbest.musicbot.Bot;
import dev.liamhbest.musicbot.commands.DJCommand;
import dev.liamhbest.musicbot.settings.LoopMode;
import dev.liamhbest.musicbot.settings.Settings;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class LoopCommand extends DJCommand {

    public LoopCommand(Bot bot) {
        super(bot);
        this.name = "loop";
        this.help = "loops the current song that is playing";
        this.arguments = "[enabled|disabled]";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        String args = event.getArgs();
        LoopMode value;
        Settings settings = event.getClient().getSettingsFor(event.getGuild());

        if (bot.getSettingsManager().getSettings(event.getGuild().getIdLong()).inLiveMode()) {
            event.getChannel().sendMessageEmbeds(
                    new EmbedBuilder().setColor(Color.RED).appendDescription(
                            "The bot is currently set to **live mode**. Please disable live mode first as using this command with live mode will not change anything."
                    ).build()
            ).queue();
            return;
        }

        if (args.isEmpty()) {
            if (settings.getRepeatMode() == LoopMode.DISABLED) {
                value = LoopMode.ENABLED;
            } else {
                value = LoopMode.DISABLED;
            }

        } else if (args.equalsIgnoreCase("false") || args.equalsIgnoreCase("off") || args.equalsIgnoreCase("disabled") || args.equalsIgnoreCase("disable")) {
            value = LoopMode.DISABLED;

        } else if (args.equalsIgnoreCase("true") || args.equalsIgnoreCase("on") || args.equalsIgnoreCase("enabled") || args.equalsIgnoreCase("enable")) {
            value = LoopMode.ENABLED;

        } else {
            EmbedBuilder error = new EmbedBuilder().setColor(Color.RED);
            error.appendDescription("**INVALID OPTIONS!** Use `!loop enable` or `!loop disable` to toggle on and off the loop mode.");
            return;
        }

        settings.setRepeatMode(value);

        EmbedBuilder embed = new EmbedBuilder();
        embed.appendDescription(":loop: Loop mode has now been **" + value.getUserFriendlyNameSentence() + "**.");

        if (value == LoopMode.DISABLED) {
            embed.setColor(Color.YELLOW);
        } else {
            embed.setColor(Color.GREEN);
        }

        event.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public void doCommand(CommandEvent event) { /* Intentionally Empty */ }
}
