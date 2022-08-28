package dev.liamhbest.musicbot.commands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import dev.liamhbest.musicbot.Bot;
import dev.liamhbest.musicbot.audio.AudioHandler;
import dev.liamhbest.musicbot.commands.DJCommand;
import dev.liamhbest.musicbot.settings.Settings;
import dev.liamhbest.musicbot.utils.FormatUtil;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class VolumeCmd extends DJCommand {

    public VolumeCmd(Bot bot) {
        super(bot);
        this.name = "volume";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.help = "sets or shows volume";
        this.arguments = "[0-150]";
    }

    @Override
    public void doCommand(CommandEvent event) {
        AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
        Settings settings = event.getClient().getSettingsFor(event.getGuild());
        int volume = handler.getPlayer().getVolume();

        if (bot.getSettingsManager().getSettings(event.getGuild().getIdLong()).inLiveMode()) {
            event.getChannel().sendMessageEmbeds(
                    new EmbedBuilder().setColor(Color.RED).appendDescription(
                            "The bot is currently set to **live mode**. Please disable live mode first as using this command with live mode will not change anything."
                    ).build()
            ).queue();
            return;
        }

        if (event.getArgs().isEmpty()) {
            event.reply(FormatUtil.volumeIcon(volume)+" Current volume is `"+volume+"`");

        } else {
            int nvolume;
            try {
                nvolume = Integer.parseInt(event.getArgs());
            } catch (NumberFormatException e){
                nvolume = -1;
            }

            if (nvolume<0 || nvolume > 150) {
                event.reply(event.getClient().getError()+" Volume must be a valid integer between 0 and 150!");
            } else {
                handler.getPlayer().setVolume(nvolume);
                settings.setVolume(nvolume);
                event.reply(FormatUtil.volumeIcon(nvolume)+" Volume changed from `"+volume+"` to `"+nvolume+"`");
            }
        }
    }
    
}
