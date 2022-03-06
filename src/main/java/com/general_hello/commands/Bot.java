package com.general_hello.commands;

import com.general_hello.commands.OtherEvents.OnGuildMemberLeaveEvent;
import com.general_hello.commands.OtherEvents.OnReadyEvent;
import com.general_hello.commands.commands.CreateCodeCommand;
import com.general_hello.commands.commands.HowItWorksCommand;
import com.general_hello.commands.commands.ViewCodeCommand;
import com.general_hello.commands.commands.ViewInvitesCommand;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class Bot {
    public static JDA jda;
    private static Bot bot;
    private final EventWaiter eventWaiter;
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);
    public EventWaiter getEventWaiter() {
        return eventWaiter;
    }

    public static Bot getBot() {
        return bot;
    }

    public Bot() throws LoginException, InterruptedException {
        bot = this;
        // Initialize the waiter and client
        CommandClientBuilder client = new CommandClientBuilder();


        // Set the client settings
        client.useDefaultGame();
        client.setOwnerId(Config.get("owner_id"));
        client.setCoOwnerIds(Config.get("owner_id_partner"));
        client.setPrefix(Config.get("prefix")).setAlternativePrefix(Config.get("prefix") + " ");
        client.useHelpBuilder(true);
        client.forceGuildOnly(Config.get("guild"));

        addCommands(client);
        eventWaiter = new EventWaiter();
        // Finalize the command client
        CommandClient commandClient = client.build();

        jda = JDABuilder.createDefault(Config.get("token"),
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_INVITES
        )
                .addEventListeners(eventWaiter, commandClient,
                        new OnReadyEvent(), new Listener(), new OnGuildMemberLeaveEvent())
                .setStatus(OnlineStatus.IDLE)
                .setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableCache(CacheFlag.ACTIVITY)
                .enableCache(CacheFlag.ONLINE_STATUS)
                .build().awaitReady();
    }

    public static void main(String[] args) throws LoginException {
        try {
            new Bot();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void addCommands(CommandClientBuilder clientBuilder) {
        // commands here
        clientBuilder.addSlashCommand(new ViewCodeCommand());
        clientBuilder.addSlashCommand(new ViewInvitesCommand());
        clientBuilder.addSlashCommand(new HowItWorksCommand());
        clientBuilder.addSlashCommand(new CreateCodeCommand());
    }
}