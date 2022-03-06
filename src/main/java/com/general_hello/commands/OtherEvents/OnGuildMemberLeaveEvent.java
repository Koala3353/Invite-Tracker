package com.general_hello.commands.OtherEvents;

import com.general_hello.commands.commands.InviteUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class OnGuildMemberLeaveEvent extends ListenerAdapter {
    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        if (event.getUser().isBot()) {
            return;
        }

        User user = event.getUser();
        int owner = InviteUser.getUsersOwner(user);
        InviteUser.addLeftInvitesFromUser(event.getJDA().getUserById(owner));
    }
}
