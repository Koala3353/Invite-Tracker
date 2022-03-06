package com.general_hello.commands.commands;

import com.general_hello.commands.commands.Utils.EmbedUtil;
import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class CreateCodeCommand extends SlashCommand {
    public CreateCodeCommand() {
        this.name = "create";
        this.help = "Creates your unique code.";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        if (InviteUser.isCodeCreated(event.getUser())) {
            event.reply("The code is already created! Type /view_code to see it!").queue();
            return;
        }

        InviteUser.setCodeCreated(event.getUser());
        String code = InviteUser.getCodeFromUser(event.getUser());
        event.replyEmbeds(EmbedUtil.successEmbed("Successfully created your code! Your code is [" + code + "](https://discord.com) `" + code + "`. Share it to your friends to be credited when they join the server! ")).queue();
    }
}
