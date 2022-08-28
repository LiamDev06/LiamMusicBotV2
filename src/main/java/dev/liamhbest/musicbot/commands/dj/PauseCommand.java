package dev.liamhbest.musicbot.commands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import dev.liamhbest.musicbot.Bot;
import dev.liamhbest.musicbot.audio.AudioHandler;
import dev.liamhbest.musicbot.commands.DJCommand;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class PauseCommand extends DJCommand {

    public PauseCommand(Bot bot) {
        super(bot);
        this.name = "pause";
        this.help = "pauses the current song";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.bePlaying = true;
    }

    @Override
    public void doCommand(CommandEvent event) {
        AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
        if (handler.getPlayer().isPaused()) {

            EmbedBuilder error = new EmbedBuilder().setColor(Color.RED);
            error.setAuthor("This song is already paused!");
            event.getChannel().sendMessage(error.build()).queue();

            return;
        }

        handler.getPlayer().setPaused(true);

        EmbedBuilder embed = new EmbedBuilder().setColor(Color.ORANGE);
        embed.setTitle(":stopwatch: Song Paused");
        embed.appendDescription("You paused the current song that is playing.");
        event.getChannel().sendMessage(embed.build()).queue();
    }
}
