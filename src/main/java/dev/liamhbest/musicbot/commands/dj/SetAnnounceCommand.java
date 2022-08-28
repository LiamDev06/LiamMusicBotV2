package dev.liamhbest.musicbot.commands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import dev.liamhbest.musicbot.Bot;
import dev.liamhbest.musicbot.commands.DJCommand;
import dev.liamhbest.musicbot.settings.Settings;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class SetAnnounceCommand extends DJCommand {

    private final Bot bot;

    public SetAnnounceCommand(Bot bot) {
        super(bot);
        this.bot = bot;
        this.name = "setannounce";
        this.help = "sets the announce channel for songs";
        this.arguments = "[#channel/channelid]";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.guildOnly = true;
    }

    @Override
    public void doCommand(CommandEvent event) {
        Settings settings = bot.getSettingsManager().getSettings(event.getGuild().getIdLong());

        if (event.getArgs().isEmpty()) {
            settings.setNowPlayingChannel(event.getChannel().getIdLong());

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.GREEN);
            embed.appendDescription("You set this channel (<#" + event.getChannel().getIdLong() + ">) as the song announcements channel.");
            event.getChannel().sendMessageEmbeds(embed.build()).queue();
            return;
        }

        if (event.getArgs().equalsIgnoreCase("none") || event.getArgs().equalsIgnoreCase("disable") || event.getArgs().equalsIgnoreCase("off")) {
            // Disabled completely
            settings.setNowPlayingChannel(0);

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.GREEN);
            embed.setAuthor("The song announcement channel has now been removed!");
            event.getChannel().sendMessageEmbeds(embed.build()).queue();
        }

    }

}




