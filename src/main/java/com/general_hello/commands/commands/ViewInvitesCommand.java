package com.general_hello.commands.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ViewInvitesCommand extends SlashCommand {
    public ViewInvitesCommand() {
        this.name = "invites";
        this.help = "Displays your invitations or the mention member one.";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "user", "The user").setRequired(false));

        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        User user = event.getUser();
        if (!event.getOptions().isEmpty()) {
            user = event.getOption("user").getAsUser();
        }
        SelfUser selfUser = event.getJDA().getSelfUser();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.YELLOW);
        embedBuilder.setTitle(user.getAsTag());
        embedBuilder.setDescription("You currently have **" + InviteUser.getInvitesFromUser(user) + "** users that you invited. (**" + InviteUser.getRealInvitesFromUser(user) + "** regular, **" + InviteUser.getFakeInvitesFromUser(user) + "** fake, **" + InviteUser.getLeftInvitesFromUser(user) + "** left)");
        embedBuilder.setFooter(selfUser.getName(), selfUser.getAvatarUrl());
        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
