package com.general_hello.commands.OtherEvents;

import com.general_hello.commands.Bot;
import com.general_hello.commands.Database.SQLiteDataSource;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class OnReadyEvent extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(OnReadyEvent.class);

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Bot.jda = event.getJDA();
        LOGGER.info("Starting the bot...");
        try {
            final File dbFile = new File("database.db");

            if (!dbFile.exists()) {
                if (dbFile.createNewFile()) {
                    LOGGER.info("Created database file");
                } else {
                    LOGGER.info("Could not create database file");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            SQLiteDataSource.connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        LOGGER.info("Opened database successfully");

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("CREATE TABLE IF NOT EXISTS InviteTracker (" +
                        "User INTEGER," +
                        "Code TEXT," +
                        "RealInvites INTEGER DEFAULT 0," +
                        "FakeInvites INTEGER DEFAULT 0," +
                        "LeftInvites INTEGER DEFAULT 0," +
                        "IsCreated INTEGER DEFAULT 0" +
                        ")")) {
            LOGGER.info("Made a new table (InviteTracker)");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("CREATE TABLE IF NOT EXISTS InvitedUsers (" +
                        "User INTEGER," +
                        "InvitedUser INTEGER" +
                        ")")) {
            LOGGER.info("Made a new table (InvitedUsers)");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
