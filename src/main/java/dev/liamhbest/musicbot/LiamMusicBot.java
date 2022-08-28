package dev.liamhbest.musicbot;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import dev.liamhbest.musicbot.commands.dj.*;
import dev.liamhbest.musicbot.entities.Prompt;
import dev.liamhbest.musicbot.gui.GUI;
import dev.liamhbest.musicbot.settings.SettingsManager;

import java.util.Arrays;
import javax.security.auth.login.LoginException;

import dev.liamhbest.musicbot.commands.dj.SettingsCmd;
import dev.liamhbest.musicbot.commands.owner.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiamMusicBot {

    public final static String PLAY_EMOJI  = "\u25B6"; // ▶
    public final static String PAUSE_EMOJI = "\u23F8"; // ⏸
    public final static String STOP_EMOJI  = "\u23F9"; // ⏹
    public final static String VERSION = "1.0.0";
    public final static String AUTHOR = "Liam (LiamHBest)";
    public final static long OWNER_ID = 392381841639997451L;

    public final static GatewayIntent[] INTENTS = { GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES };
    public final static Permission[] RECOMMENDED_PERMS = { Permission.ADMINISTRATOR };

    public static void main(String[] args) {
        // startup log
        Logger log = LoggerFactory.getLogger("Startup");
        
        // create prompt to handle startup
        Prompt prompt = new Prompt("LiamMusicBot", "Switching to nogui mode. You can manually start in nogui mode by including the -Dnogui=true flag.");
        
        // check for valid java version
        if (!System.getProperty("java.vm.name").contains("64")) {
            prompt.alert(Prompt.Level.WARNING, "Java Version", "It appears that you may not be using a supported Java version. Please use 64-bit java.");
        }
        
        // load config
        BotConfig config = new BotConfig(prompt);
        config.load();
        if (!config.isValid()) return;
        
        // set up the listener
        EventWaiter waiter = new EventWaiter();
        SettingsManager settings = new SettingsManager();
        Bot bot = new Bot(waiter, config, settings);

        // set up the command client
        CommandClientBuilder cb = new CommandClientBuilder()
                .setPrefix("!")
                .setAlternativePrefix("lmusic!")
                .setOwnerId(Long.toString(392381841639997451L))
                .setEmojis(config.getSuccess(), config.getWarning(), config.getError())
                .setHelpWord(config.getHelp())
                .setLinkedCacheSize(200)
                .setGuildSettingsManager(settings)
                .addCommands(
                        new SettingsCmd(bot),
                        new LiveModeCommand(bot),
                        new SpamTimeCommand(bot),
                        
                        new LyricsCmd(bot),
                        new NowplayingCmd(bot),
                        new PlayCmd(bot),
                        new PlaylistsCmd(bot),
                        new QueueCmd(bot),
                        new RemoveCmd(bot),
                        new SearchCmd(bot),
                        new SCSearchCmd(bot),
                        new ShuffleCmd(bot),

                        new ForceRemoveCmd(bot),
                        new SkipCommand(bot),
                        new MoveTrackCmd(bot),
                        new PauseCommand(bot),
                        new PlaynextCmd(bot),
                        new LoopCommand(bot),
                        new SkiptoCmd(bot),
                        new StopCmd(bot),
                        new VolumeCmd(bot),
                        new SetAnnounceCommand(bot),

                        new SettcCmd(bot),
                        new SetvcCmd(bot),
                        
                        new AutoplaylistCmd(bot),
                        new DebugCmd(bot),
                        new PlaylistCmd(bot),
                        new ShutdownCmd(bot)
                );

        if (config.useEval()) {
            cb.addCommand(new EvalCmd(bot));
        }

        cb.setStatus(OnlineStatus.ONLINE);
        cb.setActivity(Activity.playing("Music"));

        if (!prompt.isNoGUI()) {
            try {
                GUI gui = new GUI(bot);
                bot.setGUI(gui);
                gui.init();

            } catch (Exception e) {
                log.error("Could not start GUI. If you are "
                        + "running on a server or in a location where you cannot display a "
                        + "window, please run in nogui mode using the -Dnogui=true flag.");
            }
        }
        
        log.info("Loaded config from " + config.getConfigLocation());
        log.info("Enabling LiamMusicBot, a forked music bot. The author of this modified version is " + AUTHOR + "");
        log.info("LiamMusicBot is currently running version >> " + VERSION);
        
        // attempt to log in and start
        try {
            JDA jda = JDABuilder.create(config.getToken(), Arrays.asList(INTENTS))
                    .enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
                    .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOTE, CacheFlag.ONLINE_STATUS)

                    .setActivity(Activity.playing("Music"))
                    .setStatus(OnlineStatus.ONLINE)

                    .addEventListeners(cb.build(), waiter, new Listener(bot))
                    .setBulkDeleteSplittingEnabled(true)
                    .build();
            bot.setJDA(jda);

        } catch (LoginException ex) {
            prompt.alert(Prompt.Level.ERROR, "LiamMusicBot", ex + "\nPlease make sure you are "
                    + "editing the correct config.txt file, and that you have used the "
                    + "correct token (not the 'secret'!)\nConfig Location: " + config.getConfigLocation());
            System.exit(1);

        } catch (IllegalArgumentException ex) {
            prompt.alert(Prompt.Level.ERROR, "LiamMusicBot", "Some aspect of the configuration is "
                    + "invalid: " + ex + "\nConfig Location: " + config.getConfigLocation());
            System.exit(1);
        }

        for (Guild guild : bot.getJDA().getGuilds()) {
            settings.getSettings(guild).setLiveMode(false);
        }

        for (Guild guild : bot.getJDA().getGuildCache()) {
            settings.getSettings(guild).setLiveMode(false);
        }
    }
}
