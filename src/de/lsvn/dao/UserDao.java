package de.lsvn.dao;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.lsvn.model.User;
import de.lsvn.model.Event;
import de.lsvn.util.DbUtil;
import de.lsvn.util.PasswordHash;

import org.apache.commons.validator.Validator;

public class UserDao {

    private Connection connection;

    public UserDao() {
//        connection = DbUtil.getConnection();
    }

    public void addUser(User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT into mitglieder("
                    + "Vorname,Nachname,eMail,Telefon,Handy,TelefonDienstlich,Geburtstag,Mitgliedschaft,Stimmrecht,Jugend,AHK,Sonderstatus,eMailDienstlich,gueltig_von,gueltig_bis) "
                    + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
            // Parameters start with 1
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getTelephone());
            preparedStatement.setString(5, user.getHandy());
            preparedStatement.setString(6, user.getPhoneOffice());
            preparedStatement.setString(7, user.getBirthday());
            preparedStatement.setString(8, user.getMembership());
            preparedStatement.setBoolean(9, user.isVoting());
            preparedStatement.setBoolean(10, user.getYouth());
            preparedStatement.setBoolean(11, user.isAhk());
            preparedStatement.setString(12, user.getSpecialStatus());
            preparedStatement.setString(13, user.getEmailOffice());
            preparedStatement.setString(14, user.getMedFrom());
            preparedStatement.setString(15, user.getMedTo());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int userId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE from mitglieder where Id=?");
            // Parameters start with 1
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        connection = DbUtil.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE medicals JOIN (mitglieder JOIN adressen ON mitglieder.AdresseId = adressen.AId) ON mitglieder.Id = medicals.MId SET "
                    + "Vorname=?,Nachname=?,Postleitzahl=?,Ort=?,eMail=?,Telefon=?,Handy=?,TelefonDienstlich=?,Geburtstag=?,Mitgliedschaft=?,Stimmrecht=?,Scheinpilot=?,Jugend=?,AHK=?,Sonderstatus=?,eMailDienstlich=?,gueltig_von=?,gueltig_bis=?,Strasse=? "
                    + "WHERE Id=?");
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getPlz());
            preparedStatement.setString(4, user.getCity());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getTelephone());
            preparedStatement.setString(7, user.getHandy());
            preparedStatement.setString(8, user.getPhoneOffice());
            preparedStatement.setString(9, user.getBirthday());
            preparedStatement.setString(10, user.getMembership());
            preparedStatement.setBoolean(11, user.isVoting());
            preparedStatement.setBoolean(12, user.getLicense());
            preparedStatement.setBoolean(13, user.getYouth());
            preparedStatement.setBoolean(14, user.isAhk());
            preparedStatement.setString(15, user.getSpecialStatus());
            preparedStatement.setString(16, user.getEmailOffice());
            preparedStatement.setString(17, user.getMedFrom());
            preparedStatement.setString(18, user.getMedTo());
            preparedStatement.setString(19, user.getStreet());
            preparedStatement.setInt(20, user.getUserid());
            preparedStatement.executeUpdate();
            DbUtil.closeStatement(preparedStatement);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.closeConnection(connection);
        }
    }

    public List<User> getAllUsers() {
        connection = DbUtil.getConnection();
        List<User> users = new ArrayList<User>();
        try {
            Statement statement1 = connection.createStatement();
            ResultSet rs = statement1.executeQuery("SELECT * FROM medicals JOIN (mitglieder JOIN adressen ON mitglieder.AdresseId = adressen.AId) ON mitglieder.Id = medicals.MId ORDER BY Nachname");
            while (rs.next()) {
                User user = new User();
                user.setUserid(rs.getInt("Id"));
                user.setFirstName(rs.getString("Vorname"));
                user.setLastName(rs.getString("Nachname"));
                user.setEmail(rs.getString("eMail"));
                user.setEmailOffice(rs.getString("eMailDienstlich"));
                user.setTelephone(rs.getString("Telefon"));
                user.setPhoneOffice(rs.getString("TelefonDienstlich"));
                user.setHandy(rs.getString("Handy"));
                user.setBirthday(rs.getString("Geburtstag"));
                user.setMembership(rs.getString("Mitgliedschaft"));
                user.setVoting(rs.getBoolean("Stimmrecht"));
                user.setLicense(rs.getBoolean("Scheinpilot"));
                user.setYouth(rs.getBoolean("Jugend"));
                user.setAhk(rs.getBoolean("AHK"));
                user.setMs(rs.getBoolean("MS"));
                user.setPlz(rs.getString("Postleitzahl"));
                user.setCity(rs.getString("Ort"));
                user.setCountry(rs.getString("Land"));
                user.setStreet(rs.getString("Strasse"));
                user.setMedFrom(rs.getString("gueltig_von"));
                user.setMedTo(rs.getString("gueltig_bis"));
                user.setSpecialStatus(rs.getString("Sonderstatus"));
                users.add(user);
            }
            DbUtil.closeResultSet(rs);
            DbUtil.closeStatement(statement1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.closeConnection(connection);
        }

        return users;
    }

    public ArrayList<String> getUsr(int memberId) {
        ArrayList<String> returnArray = new ArrayList<String>();
        String usr = "";
        String ms = "false";
        connection = DbUtil.getConnection();
        try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("SELECT eMail FROM mitglieder WHERE Id = ?");
            preparedStatement.setInt(1, memberId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                usr = rs.getString("eMail");
            }
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.closeConnection(connection);
        }
        returnArray.add(usr);
        returnArray.add(ms);
        return returnArray;
    }

    public User getUserById(int userId) {
        connection = DbUtil.getConnection();
        User user = new User();
        try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("SELECT * FROM mitglieder JOIN authentifizierung ON mitglieder.Id = authentifizierung.AuthentId WHERE Id=?");
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                user.setUserid(rs.getInt("Id"));
                user.setFirstName(rs.getString("Vorname"));
                user.setLastName(rs.getString("Nachname"));
                user.setEmail(rs.getString("eMail"));
                user.setTelephone(rs.getString("Telefon"));
                user.setPwd(rs.getString("pwd"));
            }
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.closeConnection(connection);
        }

        return user;
    }

    public boolean resetUserPwd(int userId, String newPwd) {
        connection = DbUtil.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE authentifizierung JOIN mitglieder ON mitglieder.Id = authentifizierung.AuthentId SET pwd=? WHERE Id=?");
            preparedStatement.setInt(2, userId);
            preparedStatement.setString(1, newPwd);
            preparedStatement.executeUpdate();
            DbUtil.closePreparedStatement(preparedStatement);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DbUtil.closeConnection(connection);
        }
    }

    public List<Event> getSchedulerEvents(String start, String end) {
        connection = DbUtil.getConnection();
        List<Event> events = new ArrayList<Event>();
        try {
//            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM events JOIN mitglieder ON events.mitgliederId = mitglieder.id WHERE NOT ((end <= ?) OR (start >= ?))");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM events JOIN mitglieder ON mitglieder.Id = events.mitgliederId  WHERE NOT ((events.end <= ?) OR (events.start >= ?))");
            preparedStatement.setString(1, start);
            preparedStatement.setString(2, end);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Event event = new Event();
                String startString = formatDateTimeToISO(rs.getString("start"));
                String endString = formatDateTimeToISO(rs.getString("end"));
                event.setId(rs.getInt("id"));
                event.setMitgliederId(rs.getInt("mitgliederId"));
                event.setName(rs.getString("Nachname"));
                event.setStart(startString);
                event.setEnd(endString);
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.closeConnection(connection);
        }
        return events;
    }

    public void setNewSchedulerEvent(String start, String end, String resource, String eventId) {
        connection = DbUtil.getConnection();

        if (Integer.parseInt(eventId) > 0) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE events SET start=?, end=? WHERE id=? AND mitgliederId=?");
                preparedStatement.setString(1, start);
                preparedStatement.setString(2, end);
                preparedStatement.setInt(3, Integer.parseInt(eventId));
                preparedStatement.setInt(4, Integer.parseInt(resource));
                preparedStatement.executeUpdate();
                DbUtil.closePreparedStatement(preparedStatement);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DbUtil.closeConnection(connection);
            }
        } else {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO events (mitgliederId, start, end) VALUES (?, ?, ?)");
                preparedStatement.setInt(1, Integer.parseInt(resource));
                preparedStatement.setString(2, start);
                preparedStatement.setString(3, end);
                preparedStatement.executeUpdate();
                DbUtil.closePreparedStatement(preparedStatement);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DbUtil.closeConnection(connection);
            }
        }
    }

    public void purgeSchedulerEvent(String start, String end, String resource, String eventId, Integer sessionOwnerId) {
        connection = DbUtil.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM events WHERE id=? AND mitgliederId=?");
            preparedStatement.setInt(1, Integer.parseInt(eventId));
            preparedStatement.setInt(2, sessionOwnerId);
            preparedStatement.executeUpdate();
            DbUtil.closePreparedStatement(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.closeConnection(connection);
        }
    }

    private String formatDateTimeToISO(String dateString) {
        //convert space delimited datetime string to "T" delimited ISO 8601 notation
        dateString = dateString.replaceAll("\\s", "T");
        //remove trailing decimals
        dateString = dateString.replaceAll("\\.\\d$", "");
        return dateString;
    }

    public int validateToken(String token) {
        connection = DbUtil.getConnection();
        try {
            
        	Date date = new Date();
        	long timestamp = date.getTime();
            
            PreparedStatement cleanupStatement = connection.
                    prepareStatement("DELETE FROM password_change_requests WHERE Time < ?; ");
            cleanupStatement.setString(1, String.valueOf(timestamp - 3600000L));
            cleanupStatement.executeUpdate();
            DbUtil.closePreparedStatement(cleanupStatement);
            
            PreparedStatement preparedStatement = connection.
                    prepareStatement("SELECT Token, Time, UserID FROM password_change_requests");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
				try {
	                String storedToken = rs.getString("Token");
	                long storedTimestamp = rs.getLong("Time");
	                int usr = rs.getInt("UserID");
	                if(PasswordHash.validatePassword(token, storedToken) && timestamp < storedTimestamp + 3600000) {
	                    return usr;
	                }
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidKeySpecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.closeConnection(connection);
        }

        return -1;
    }
}
