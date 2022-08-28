package dev.liamhbest.musicbot.commands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import dev.liamhbest.musicbot.Bot;
import dev.liamhbest.musicbot.audio.AudioHandler;
import dev.liamhbest.musicbot.audio.QueuedTrack;
import dev.liamhbest.musicbot.audio.RequestMetadata;
import dev.liamhbest.musicbot.commands.DJCommand;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class SkipCommand extends DJCommand {

    public SkipCommand(Bot bot) {
        super(bot);
        this.name = "skip";
        this.help = "skips the current song";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.bePlaying = true;
    }

    @Override
    public void doCommand(CommandEvent event) {
        AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
        RequestMetadata rm = handler.getRequestMetadata();

        if (bot.getSettingsManager().getSettings(event.getGuild().getIdLong()).inLiveMode()) {
            event.getChannel().sendMessageEmbeds(
                    new EmbedBuilder().setColor(Color.RED).appendDescription(
                            "The bot is currently set to **live mode**. Please disable live mode first as using this command with live mode will not change anything."
                    ).build()
            ).queue();
            return;
        }

        event.reply(event.getClient().getSuccess() + " Skipped **"+handler.getPlayer().getPlayingTrack().getInfo().title
                + "** " + (rm.getOwner() == 0L ? "(autoplay)" : "(requested by **" + rm.user.username + "**)"));
        handler.getPlayer().stopTrack();

        if (!handler.getQueue().isEmpty()) {
            QueuedTrack track = handler.getQueue().pull();
            handler.getPlayer().playTrack(track.getTrack());
        }
    }
}
