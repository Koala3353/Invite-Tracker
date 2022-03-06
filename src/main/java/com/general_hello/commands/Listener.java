package com.general_hello.commands;

import com.general_hello.commands.commands.InviteUser;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Listener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    public static JDA jda;
    public static final ArrayList<Long> cache = new ArrayList<>();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (!cache.contains(event.getAuthor().getIdLong())) {
            InviteUser.newInfo(event.getAuthor());
            cache.add(event.getAuthor().getIdLong());
        }

        if (event.getChannel().getId().equals(Config.get("textchannel")) && !event.getAuthor().isBot()) {
            Role verifiedRole = event.getGuild().getRoleById(Config.get("roleid"));
            if (event.getMember().getRoles().contains(verifiedRole)) {
                return;
            }
            String code = event.getMessage().getContentDisplay();
            if (InviteUser.getUserFromCode(code) == null) {
                event.getMessage().reply("**Invalid/Non-existing code placed!** Kindly recheck your code!").queue();
                return;
            }

            User codeOwner = InviteUser.getUserFromCode(code);
            if (codeOwner.getId().equals(event.getAuthor().getId())) {
                event.getMessage().reply("You cannot use your own code!").queue();
                return;
            }
            long accountAgeDays = ChronoUnit.DAYS.between(event.getAuthor().getTimeCreated(), OffsetDateTime.now());
            if (accountAgeDays < 14) {
                LOGGER.info("Fake invites added to " + codeOwner.getName());
                InviteUser.addFakeInvitesFromUser(codeOwner);
            } else {
                LOGGER.info("Invites added to " + codeOwner.getName());
                InviteUser.addRealInvitesFromUser(codeOwner);
            }

            InviteUser.addUser(event.getAuthor(), codeOwner);
            event.getGuild().addRoleToMember(event.getMember(), verifiedRole).queue();
            event.getMessage().reply("You are successfully verified! Welcome to the server!").queue();
        }
    }
}