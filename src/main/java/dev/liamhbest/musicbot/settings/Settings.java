package dev.liamhbest.musicbot.settings;

import com.jagrosh.jdautilities.command.GuildSettingsProvider;
import java.util.Collection;
import java.util.Collections;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class Settings implements GuildSettingsProvider {

    private final SettingsManager manager;
    protected long textId;
    protected long voiceId;
    protected long roleId;
    private int volume;
    private String defaultPlaylist;
    private LoopMode loopMode;
    private String prefix;
    private double skipRatio;
    protected long nowPlayingChannelId;
    protected boolean liveMode;

    public Settings(SettingsManager manager, String textId, String voiceId, String roleId, int volume, String defaultPlaylist, LoopMode loopMode, String prefix, double skipRatio, String nowPlayingChannelId, boolean liveMode) {
        this.manager = manager;

        try {
            this.textId = Long.parseLong(textId);
        } catch (NumberFormatException e) {
            this.textId = 0;
        }

        try {
            this.voiceId = Long.parseLong(voiceId);
        } catch (NumberFormatException e) {
            this.voiceId = 0;
        }

        try {
            this.roleId = Long.parseLong(roleId);
        } catch (NumberFormatException e) {
            this.roleId = 0;
        }

        try {
            this.nowPlayingChannelId = Long.parseLong(nowPlayingChannelId);
        } catch (NumberFormatException e) {
            this.nowPlayingChannelId = 0;
        }

        this.volume = volume;
        this.defaultPlaylist = defaultPlaylist;
        this.loopMode = loopMode;
        this.prefix = prefix;
        this.skipRatio = skipRatio;
        this.liveMode = liveMode;
    }

    public Settings(SettingsManager manager, long textId, long voiceId, long roleId, int volume, String defaultPlaylist, LoopMode loopMode, String prefix, double skipRatio, String nowPlayingChannelId, boolean liveMode) {
        this.manager = manager;
        this.textId = textId;
        this.voiceId = voiceId;
        this.roleId = roleId;
        this.volume = volume;
        this.defaultPlaylist = defaultPlaylist;
        this.loopMode = loopMode;
        this.prefix = prefix;
        this.skipRatio = skipRatio;
        this.nowPlayingChannelId = Long.parseLong(nowPlayingChannelId);
        this.liveMode = liveMode;
    }

    // Getters
    public TextChannel getTextChannel(Guild guild) {
        return guild == null ? null : guild.getTextChannelById(textId);
    }

    public TextChannel getNowPlayingChannel(Guild guild) {
        return guild == null ? null : guild.getTextChannelById(nowPlayingChannelId);
    }

    public void setNowPlayingChannel(long nowPlayingChannelId) {
        this.nowPlayingChannelId = nowPlayingChannelId;
        this.manager.writeSettings();
    }

    public boolean inLiveMode() {
        return liveMode;
    }

    public void setLiveMode(boolean value) {
        this.liveMode = value;
        this.manager.writeSettings();
    }

    public VoiceChannel getVoiceChannel(Guild guild) {
        return guild == null ? null : guild.getVoiceChannelById(voiceId);
    }

    public Role getRole(Guild guild) {
        return guild == null ? null : guild.getRoleById(roleId);
    }

    public int getVolume() {
        return volume;
    }

    public String getDefaultPlaylist() {
        return defaultPlaylist;
    }

    public LoopMode getRepeatMode() {
        return loopMode;
    }

    public String getPrefix() {
        return prefix;
    }

    public double getSkipRatio() {
        return skipRatio;
    }

    @Override
    public Collection<String> getPrefixes() {
        return prefix == null ? Collections.EMPTY_SET : Collections.singleton(prefix);
    }

    // Setters
    public void setTextChannel(TextChannel tc) {
        this.textId = tc == null ? 0 : tc.getIdLong();
        this.manager.writeSettings();
    }

    public void setVoiceChannel(VoiceChannel vc) {
        this.voiceId = vc == null ? 0 : vc.getIdLong();
        this.manager.writeSettings();
    }

    public void setDJRole(Role role) {
        this.roleId = role == null ? 0 : role.getIdLong();
        this.manager.writeSettings();
    }

    public void setVolume(int volume) {
        this.volume = volume;
        this.manager.writeSettings();
    }

    public void setDefaultPlaylist(String defaultPlaylist) {
        this.defaultPlaylist = defaultPlaylist;
        this.manager.writeSettings();
    }

    public void setRepeatMode(LoopMode mode) {
        this.loopMode = mode;
        this.manager.writeSettings();
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        this.manager.writeSettings();
    }

    public void setSkipRatio(double skipRatio) {
        this.skipRatio = skipRatio;
        this.manager.writeSettings();
    }
}