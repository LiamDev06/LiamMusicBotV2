package dev.liamhbest.musicbot.commands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import dev.liamhbest.musicbot.Bot;
import dev.liamhbest.musicbot.commands.MusicCommand;
import dev.liamhbest.musicbot.settings.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.awt.*;

public class SettingsCmd extends MusicCommand {

    private final static String EMOJI = "\uD83C\uDFA7"; // 🎧
    
    public SettingsCmd(Bot bot) {
        super(bot);
        this.name = "settings";
        this.help = "shows the music bot settings";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.guildOnly = true;
    }
    
    @Override
    public void doCommand(CommandEvent event) {
        Settings s = event.getClient().getSettingsFor(event.getGuild());
        TextChannel tchan = s.getTextChannel(event.getGuild());
        VoiceChannel vchan = s.getVoiceChannel(event.getGuild());
        TextChannel achan = s.getNowPlayingChannel(event.getGuild());

        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(Color.green);
        embed.setTitle(EMOJI + " Music Bot Settings");
        embed.setFooter(event.getJDA().getGuilds().size() + " servers | "
                + event.getJDA().getGuilds().stream().filter(g -> g.getSelfMember().getVoiceState().inVoiceChannel()).count()
                + " audio connections", null);

        embed.appendDescription("**Commands Text Channel:** " + (tchan == null ? "Any" : tchan.getAsMention()));
        embed.appendDescription("\n**Music Voice Channel:** " + (vchan == null ? "Any" : vchan.getAsMention()));
        embed.appendDescription("\n**Announce Channel:** " + (achan == null ? "None" : achan.getAsMention()));
        embed.appendDescription("\n**Music DJ Roles:** Owner, Admin, Moderator, LiamMusicBot Access, Music Bot Access");
        embed.appendDescription("\n**Volume:** " + s.getVolume());
        embed.appendDescription("\n**Loop Mode:** " + s.getRepeatMode().getUserFriendlyName());

        if (s.inLiveMode()) {
            embed.appendDescription("\n**Live Mode:** Enabled");
        } else {
            embed.appendDescription("\n**Live Mode:** Disabled");
        }

        event.getChannel().sendMessage(embed.build()).queue();
    }
    
}
