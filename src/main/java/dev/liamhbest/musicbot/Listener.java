package dev.liamhbest.musicbot;

import dev.liamhbest.musicbot.audio.AudioHandler;
import dev.liamhbest.musicbot.utils.OtherUtil;

import java.awt.*;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class Listener extends ListenerAdapter {

    private final Bot bot;

    public Listener(Bot bot) {
        this.bot = bot;
    }
    
    @Override
    public void onReady(ReadyEvent event) {
        if (event.getJDA().getGuildCache().isEmpty()) {
            Logger log = LoggerFactory.getLogger("MusicBot");
            log.warn("This bot is not on any guilds! Use the following link to add the bot to your guilds!");
            log.warn(event.getJDA().getInviteUrl(LiamMusicBot.RECOMMENDED_PERMS));
        }

        event.getJDA().getGuilds().forEach((guild) -> {
            try {
                String defpl = bot.getSettingsManager().getSettings(guild).getDefaultPlaylist();
                VoiceChannel vc = bot.getSettingsManager().getSettings(guild).getVoiceChannel(guild);

                if (defpl != null && vc != null && bot.getPlayerManager().setUpHandler(guild).playFromDefault()) {
                    guild.getAudioManager().openAudioConnection(vc);
                }
            }
            catch(Exception ignore) {}
        });
    }
    
    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
        bot.getNowplayingHandler().onMessageDelete(event.getGuild(), event.getMessageIdLong());
    }

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        bot.getAloneInVoiceHandler().onVoiceUpdate(event);
    }

    @Override
    public void onShutdown(@Nonnull ShutdownEvent event) {
        bot.shutdown();
    }

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        Member botMember = event.getGuild().getMemberById(event.getJDA().getSelfUser().getId());

        if (event.getChannelLeft().getMembers().contains(botMember) && event.getChannelLeft().getMembers().size() <= 1) {
            AudioHandler handler = (AudioHandler) event.getGuild().getAudioManager().getSendingHandler();

            handler.stopAndClear();
            event.getGuild().getAudioManager().closeAudioConnection();

            TextChannel channel = this.bot.getPlayerManager().getNowPlayingChannelCache(event.getGuild().getIdLong());
            if (channel == null) return;

            channel.sendMessageEmbeds(new EmbedBuilder()
                    .setColor(Color.YELLOW)
                    .appendDescription("**Voice Channel Empty!** The voice channel <#" + event.getChannelLeft().getId() + "> became empty and therefore I stopped the music and left.").build()).queue();
        }
    }

}
