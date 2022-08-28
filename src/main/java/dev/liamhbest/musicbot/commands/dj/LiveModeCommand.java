package dev.liamhbest.musicbot.commands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.liamhbest.musicbot.Bot;
import dev.liamhbest.musicbot.commands.DJCommand;
import dev.liamhbest.musicbot.entities.AudioPlayerSendHandler;
import dev.liamhbest.musicbot.settings.Settings;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class LiveModeCommand extends DJCommand {

    private final Bot bot;

    public LiveModeCommand(Bot bot) {
        super(bot);

        this.bot = bot;
        this.name = "livemode";
        this.help = "toggles live mode on and off. Music can be controlled from Mixxx software";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.guildOnly = true;
    }

    @Override
    public void doCommand(CommandEvent commandEvent) {
        Settings settings = bot.getSettingsManager().getSettings(commandEvent.getGuild().getIdLong());

        boolean mode = settings.inLiveMode();
        EmbedBuilder embed = new EmbedBuilder();

        if (!mode) {
            embed.setColor(Color.GREEN);
            embed.setTitle(":loud_sound: Live Mode");
            embed.appendDescription("Live mode has now been **enabled**. Music can now only be controlled from Mixxx.");

            settings.setLiveMode(true);
            // http://shaincast.caster.fm:43330/listen.mp3?authn199625e2324358a421d1c3de7f4558f6
            // 	http://127.0.0.1:8000/stream

            String identifier = "http://127.0.0.1:8000/stream.mp3";

            //TODO Make it so the bot doesn't crash if no music is playing through mixxx :)
            //TODO Make it possible to leave livemode
            //TODO Fix why 50% of the times sound is weird
            //TODO Add now playing announcements but with live mode

            AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
            AudioSourceManagers.registerRemoteSources(playerManager);
            final AudioPlayer player = playerManager.createPlayer();

            bot.getPlayerManager().addNowPlayingCacheChannel(commandEvent.getGuild().getIdLong(), commandEvent.getChannel().getIdLong());
            commandEvent.getGuild().getAudioManager().openAudioConnection(commandEvent.getMember().getVoiceState().getChannel());
            commandEvent.getGuild().getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));

            playerManager.loadItem(identifier, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    player.playTrack(track);
                    player.startTrack(track, true);
                    player.setVolume(100);
                    commandEvent.getChannel().sendMessage(":notes: Live Mode Started").queue();
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {

                }

                @Override
                public void noMatches() {
                    commandEvent.getChannel().sendMessage("**Something went wrong with live mode [ERROR-1]**").queue();
                }

                @Override
                public void loadFailed(FriendlyException exception) {
                    exception.printStackTrace();
                    commandEvent.getChannel().sendMessage("**Something went wrong with live mode [ERROR-2]**").queue();
                }
            });

        } else {
            embed.setColor(Color.YELLOW);
            embed.setTitle(":loud_sound: Live Mode");
            embed.appendDescription("Live mode has now been **disabled**. Music can now play via the !play command again.");

            settings.setLiveMode(false);

            bot.getPlayerManager().init();
            bot.getPlayerManager().setUpHandler(commandEvent.getGuild());
        }

        commandEvent.getChannel().sendMessageEmbeds(embed.build()).queue();
    }
}












