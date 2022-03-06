package com.general_hello.commands.commands;

import com.general_hello.commands.Bot;
import com.general_hello.commands.commands.Utils.UtilNum;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.general_hello.commands.Database.SQLiteDataSource.getConnection;
import static com.general_hello.commands.Listener.cache;

public class InviteUser {
    private static final Logger LOGGER = LoggerFactory.getLogger(InviteUser.class);
    private static final String[] letters = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    public static User getUserFromCode(String code) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT User FROM InviteTracker WHERE Code = ?")) {

            preparedStatement.setString(1, String.valueOf(code));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Bot.jda.getUserById(resultSet.getLong("User"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getUserFromCodeNoCheck(String code) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT User FROM InviteTracker WHERE Code = ?")) {

            preparedStatement.setString(1, String.valueOf(code));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Bot.jda.getUserById(resultSet.getLong("User"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCodeFromUser(User user) {
        checkUser(user);
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Code FROM InviteTracker WHERE User = ?")) {

            preparedStatement.setString(1, user.getId());

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("Code");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCodeFromUserNoCheck(User user) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT Code FROM InviteTracker WHERE User = ?")) {

            preparedStatement.setString(1, user.getId());

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("Code");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getRealInvitesFromUser(User user) {
        checkUser(user);
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT RealInvites FROM InviteTracker WHERE User = ?")) {

            preparedStatement.setString(1, user.getId());

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("RealInvites");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void addRealInvitesFromUser(User user) {
        checkUser(user);
        int invitesToSet = getRealInvitesFromUser(user)+1;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("UPDATE InviteTracker SET RealInvites=" + (invitesToSet) + " WHERE User=" + user.getId()
        )) {
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addFakeInvitesFromUser(User user) {
        checkUser(user);
        int invitesToSet = getFakeInvitesFromUser(user)+1;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("UPDATE InviteTracker SET FakeInvites=" + (invitesToSet) + " WHERE User=" + user.getId()
                     )) {

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getFakeInvitesFromUser(User user) {
        checkUser(user);
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT FakeInvites FROM InviteTracker WHERE User = ?")) {

            preparedStatement.setString(1, user.getId());

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("FakeInvites");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public static void addUser(User newUser, User owner) {
        LOGGER.info("Added new info for " + newUser.getAsTag() + " (Owner -> " + owner + ")");
        try (final PreparedStatement preparedStatement = getConnection()
                .prepareStatement("INSERT INTO InvitedUsers" +
                        "(User, InvitedUser)" +
                        "VALUES (?, ?);")) {

            preparedStatement.setString(1, newUser.getId());
            preparedStatement.setString(2, String.valueOf(owner));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static int getUsersOwner(User user) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT User FROM InvitedUsers WHERE InvitedUser = ?")) {

            preparedStatement.setString(1, user.getId());

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("User");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public static void addLeftInvitesFromUser(User user) {
        checkUser(user);
        int invitesToSet = getLeftInvitesFromUser(user)+1;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("UPDATE InviteTracker SET LeftInvites=" + (invitesToSet) + " WHERE User=" + user.getId()
                     )) {

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getLeftInvitesFromUser(User user) {
        checkUser(user);
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT LeftInvites FROM InviteTracker WHERE User = ?")) {

            preparedStatement.setString(1, user.getId());

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("LeftInvites");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean isCodeCreated(User user) {
        checkUser(user);
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT IsCreated FROM InviteTracker WHERE User = ?")) {

            preparedStatement.setString(1, user.getId());

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return (resultSet.getInt("IsCreated") == 1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setCodeCreated(User user) {
        checkUser(user);
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("UPDATE InviteTracker SET IsCreated=1 WHERE User=" + user.getId()
                     )) {

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getInvitesFromUser(User user) {
        return getRealInvitesFromUser(user) + getFakeInvitesFromUser(user);
    }

    public static void newInfo(User user) {
        if (getCodeFromUserNoCheck(user) == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("Made new info for " + user.getAsTag());
            try (final PreparedStatement preparedStatement = getConnection()
                    .prepareStatement("INSERT INTO InviteTracker" +
                            "(User, Code)" +
                            "VALUES (?, ?);")) {

                preparedStatement.setString(1, user.getId());
                preparedStatement.setString(2, generateCode());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static String generateCode() {
        StringBuilder code = new StringBuilder();
        final int codeLength = 7;
        int x = 0;
        while (x < codeLength) {
            String letterChosen = letters[UtilNum.randomNum(0, letters.length - 1)];
            if (UtilNum.randomNum(0, 1) == 0) {
                code.append(letterChosen.toUpperCase());
            } else {
                code.append(letterChosen);
            }
            x++;
        }
        return code.toString();
    }

    private static void checkUser(User user) {
        if (!cache.contains(user.getIdLong())) {
            InviteUser.newInfo(user);
            cache.add(user.getIdLong());
            LOGGER.info("Made new info for " + user.getAsTag());
        }
    }
}
