package dev.liamhbest.musicbot.audio;

import dev.liamhbest.musicbot.Bot;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;

public class PlayerManager extends DefaultAudioPlayerManager {

    private final Bot bot;
    private final HashMap<Long, Long> cacheNowPlayingChannel;
    
    public PlayerManager(Bot bot) {
        this.bot = bot;
        this.cacheNowPlayingChannel = new HashMap<>();
    }
    
    public void init() {
        TransformativeAudioSourceManager.createTransforms(bot.getConfig().getTransforms()).forEach(this::registerSourceManager);

        AudioSourceManagers.registerRemoteSources(this);
        AudioSourceManagers.registerLocalSource(this);

        source(YoutubeAudioSourceManager.class).setPlaylistPageCount(10);
    }
    
    public Bot getBot() {
        return bot;
    }
    
    public boolean hasHandler(Guild guild) {
        return guild.getAudioManager().getSendingHandler()!=null;
    }
    
    public AudioHandler setUpHandler(Guild guild) {
        AudioHandler handler;

        if (guild.getAudioManager().getSendingHandler() == null) {
            AudioPlayer player = createPlayer();
            player.setVolume(bot.getSettingsManager().getSettings(guild).getVolume());

            handler = new AudioHandler(this, guild, player);

            player.addListener(handler);
            guild.getAudioManager().setSendingHandler(handler);

        } else {
            handler = (AudioHandler) guild.getAudioManager().getSendingHandler();
        }
        return handler;
    }

    public TextChannel getNowPlayingChannelCache(long guildId) {
        Guild guild = bot.getJDA().getGuildById(guildId);
        if (guild == null) return null;

        return guild.getTextChannelById(cacheNowPlayingChannel.get(guildId));
    }

    public void addNowPlayingCacheChannel(long guildId, long channelId) {
        if (cacheNowPlayingChannel.containsKey(guildId)) {
            cacheNowPlayingChannel.replace(guildId, channelId);
            return;
        }

        cacheNowPlayingChannel.put(guildId, channelId);
    }

}









