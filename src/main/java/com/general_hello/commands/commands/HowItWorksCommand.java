package com.general_hello.commands.commands;

import com.general_hello.commands.Config;
import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.awt.*;
import java.time.OffsetDateTime;

public class HowItWorksCommand extends SlashCommand {
    public HowItWorksCommand() {
        this.name = "how";
        this.help = "Shows how this bot works.";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        if (!event.getGuild().getId().equals(Config.get("guild"))) return;
        Guild guild = event.getGuild();
        TextChannel sendCodeChannel = event.getGuild().getTextChannelById(Config.get("textchannel"));
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Welcome to " + guild.getName(), null, guild.getIconUrl());
        embedBuilder.setDescription("**Kindly follow the [steps below](https://discord.com) to be verified.**\n\n" +
                "**1.)** Copy the code your friend sent you. (Example: `sACVlQJ`)\n" +
                "**2.)** Send it at " + sendCodeChannel.getAsMention() + " or [here](https://discord.com/channels/" + Config.get("guild") + "/" + sendCodeChannel.getId() + ")\n" +
                "**3.)** You're done!");
        embedBuilder.setColor(Color.ORANGE);
        embedBuilder.setFooter("Take note that your friend will get credit once you get verified.").setTimestamp(OffsetDateTime.now());
        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
