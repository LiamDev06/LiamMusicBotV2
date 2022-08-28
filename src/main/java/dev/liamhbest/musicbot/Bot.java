package dev.liamhbest.musicbot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import dev.liamhbest.musicbot.audio.AloneInVoiceHandler;
import dev.liamhbest.musicbot.audio.AudioHandler;
import dev.liamhbest.musicbot.audio.NowplayingHandler;
import dev.liamhbest.musicbot.audio.PlayerManager;
import dev.liamhbest.musicbot.gui.GUI;
import dev.liamhbest.musicbot.playlist.PlaylistLoader;
import dev.liamhbest.musicbot.settings.SettingsManager;
import java.util.Objects;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;

public class Bot {

    private final EventWaiter waiter;
    private final ScheduledExecutorService threadpool;
    private final BotConfig config;
    private final SettingsManager settings;
    private final PlayerManager players;
    private final PlaylistLoader playlists;
    private final NowplayingHandler nowplaying;
    private final AloneInVoiceHandler aloneInVoiceHandler;
    
    private boolean shuttingDown = false;
    private JDA jda;
    private GUI gui;
    
    public Bot(EventWaiter waiter, BotConfig config, SettingsManager settings) {
        this.waiter = waiter;
        this.config = config;
        this.settings = settings;
        this.playlists = new PlaylistLoader(config);
        this.threadpool = Executors.newSingleThreadScheduledExecutor();
        this.players = new PlayerManager(this);
        this.players.init();
        this.nowplaying = new NowplayingHandler(this);
        this.nowplaying.init();
        this.aloneInVoiceHandler = new AloneInVoiceHandler(this);
        this.aloneInVoiceHandler.init();
    }
    
    public BotConfig getConfig() {
        return config;
    }
    
    public SettingsManager getSettingsManager() {
        return settings;
    }
    
    public EventWaiter getWaiter() {
        return waiter;
    }
    
    public ScheduledExecutorService getThreadpool() {
        return threadpool;
    }
    
    public PlayerManager getPlayerManager() {
        return players;
    }
    
    public PlaylistLoader getPlaylistLoader() {
        return playlists;
    }
    
    public NowplayingHandler getNowplayingHandler() {
        return nowplaying;
    }

    public AloneInVoiceHandler getAloneInVoiceHandler() {
        return aloneInVoiceHandler;
    }
    
    public JDA getJDA() {
        return jda;
    }
    
    public void closeAudioConnection(long guildId) {
        Guild guild = jda.getGuildById(guildId);

        if (guild != null) threadpool.submit(() -> guild.getAudioManager().closeAudioConnection());
    }
    
    public void resetGame() {
        Activity game = config.getGame() == null || config.getGame().getName().equalsIgnoreCase("none") ? null : config.getGame();

        if (!Objects.equals(jda.getPresence().getActivity(), game)) {
            jda.getPresence().setActivity(Activity.playing("Music"));
        }
    }

    public void shutdown() {
        if (shuttingDown) return;

        shuttingDown = true;
        threadpool.shutdownNow();

        if (jda.getStatus() != JDA.Status.SHUTTING_DOWN) {
            jda.getGuilds().stream().forEach(g -> {
                g.getAudioManager().closeAudioConnection();
                AudioHandler ah = (AudioHandler)g.getAudioManager().getSendingHandler();

                if (ah != null) {
                    ah.stopAndClear();
                    ah.getPlayer().destroy();
                    nowplaying.updateTopic(g.getIdLong(), ah, true);
                }
            });
            jda.shutdown();
        }

        if (gui != null) gui.dispose();
        System.exit(0);
    }

    public void setJDA(JDA jda) {
        this.jda = jda;
    }
    
    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
