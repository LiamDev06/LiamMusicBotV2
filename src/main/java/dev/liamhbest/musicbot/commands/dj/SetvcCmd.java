package dev.liamhbest.musicbot.commands.dj;

import java.util.List;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import dev.liamhbest.musicbot.Bot;
import dev.liamhbest.musicbot.commands.MusicCommand;
import dev.liamhbest.musicbot.settings.Settings;
import dev.liamhbest.musicbot.utils.FormatUtil;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class SetvcCmd extends MusicCommand {

    public SetvcCmd(Bot bot) {
        super(bot);

        this.name = "setvc";
        this.help = "sets the voice channel for playing music";
        this.arguments = "<channel|NONE>";
        this.aliases = bot.getConfig().getAliases(this.name);
    }
    
    @Override
    public void doCommand(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.reply(event.getClient().getError()+" Please include a voice channel or NONE");
            return;
        }

        Settings s = event.getClient().getSettingsFor(event.getGuild());
        if (event.getArgs().equalsIgnoreCase("none")) {
            s.setVoiceChannel(null);
            event.reply(event.getClient().getSuccess()+" Music can now be played in any channel");

        } else {
            List<VoiceChannel> list = FinderUtil.findVoiceChannels(event.getArgs(), event.getGuild());
            if (list.isEmpty()) {
                event.reply(event.getClient().getWarning() + " No Voice Channels found matching \""+event.getArgs() + "\"");

            } else if (list.size() > 1) {
                event.reply(event.getClient().getWarning() + FormatUtil.listOfVChannels(list, event.getArgs()));

            } else {
                s.setVoiceChannel(list.get(0));
                event.reply(event.getClient().getSuccess() + " Music can now only be played in " + list.get(0).getAsMention());
            }
        }
    }
}