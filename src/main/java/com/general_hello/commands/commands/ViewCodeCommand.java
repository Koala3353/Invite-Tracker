package com.general_hello.commands.commands;

import com.general_hello.commands.commands.Utils.EmbedUtil;
import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class ViewCodeCommand extends SlashCommand {
    public ViewCodeCommand() {
        this.name = "view_code";
        this.help = "Shows your custom code";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        if (InviteUser.isCodeCreated(event.getUser())) {
            String code = InviteUser.getCodeFromUser(event.getUser());
            event.replyEmbeds(EmbedUtil.successEmbed("Your code is [" + code + "](https://discord.com) `" + code + "`. Share it to your friends to be credited when they join the server! ")).queue();
        } else {
            event.reply("You need to create your unique code first. Kindly use /create to make one.").queue();
        }
    }
}
