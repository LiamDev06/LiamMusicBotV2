package dev.liamhbest.musicbot.commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import dev.liamhbest.musicbot.Bot;
import dev.liamhbest.musicbot.LiamMusicBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public abstract class DJCommand extends MusicCommand {

    public DJCommand(Bot bot) {
        super(bot);
        this.category = new Category("DJ", DJCommand::checkDJPermission);
    }

    /*
    This does not work
     */

    public static boolean checkDJPermission(CommandEvent event) {
        if (event.getAuthor().getId().equals(event.getClient().getOwnerId())) return true;
        if (event.getGuild() == null) return true;
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) return true;

        Member member = event.getMember();
        Guild guild = event.getGuild();

        try {
            if (hasRole(member, guild, "Owner")) return true;
        } catch (Exception ignored) {}

        try {
            if (hasRole(member, guild, "Admin")) return true;
        } catch (Exception ignored) {}

        try {
            if (hasRole(member, guild, "Senior Moderator")) return true;
        } catch (Exception ignored) {}

        try {
            if (hasRole(member, guild, "Moderator")) return true;
        } catch (Exception ignored) {}

        try {
            if (hasRole(member, guild, "Mod")) return true;
        } catch (Exception ignored) {}

        try {
            if (hasRole(member, guild, "LiamMusicBot Access")) return true;
        } catch (Exception ignored) {}

        try {
            if (hasRole(member, guild, "Music Bot Access")) return true;
        } catch (Exception ignored) {}

        return (hasRole(member, guild, "Music DJ"));
    }

    public static boolean checkDJPermission(Member member, Guild guild) {
        if (member.getIdLong() == LiamMusicBot.OWNER_ID) return true;
        if (guild == null) return true;
        if (member.hasPermission(Permission.ADMINISTRATOR)) return true;

        try {
            if (hasRole(member, guild, "Owner")) return true;
        } catch (Exception ignored) {}

        try {
            if (hasRole(member, guild, "Admin")) return true;
        } catch (Exception ignored) {}

        try {
            if (hasRole(member, guild, "Senior Moderator")) return true;
        } catch (Exception ignored) {}

        try {
            if (hasRole(member, guild, "Moderator")) return true;
        } catch (Exception ignored) {}

        try {
            if (hasRole(member, guild, "Mod")) return true;
        } catch (Exception ignored) {}

        try {
            if (hasRole(member, guild, "LiamMusicBot Access")) return true;
        } catch (Exception ignored) {}

        try {
            if (hasRole(member, guild, "Music Bot Access")) return true;
        } catch (Exception ignored) {}

        return (hasRole(member, guild, "Music DJ"));
    }

    private static boolean hasRole(Member member, Guild guild, String discordRole) {
        for (int i=0; i<member.getRoles().size(); i++){
            if (guild.getRolesByName(discordRole, false).get(0) == member.getRoles().get(i)) {
                return true;
            }
        }
        return false;
    }
}












