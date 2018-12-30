package com.eone.bot.db;

import com.eone.bot.model.FopNorm;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FopNormDao {

    private Connection connection;
    private PreparedStatement psGet3;
    private PreparedStatement psGet2;
    private PreparedStatement psGetCount;

    public FopNormDao(String dbUrl, String dbUser, String dbPassword) throws SQLException {
        this.connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        this.psGet3 = connection.prepareStatement(
                "select " +
                        "last_name, first_name, other_name, address, activity, status" +
                        " from fop2_norm " +
                        "where last_name like ? and first_name like ? and other_name like ?"
        );
        this.psGet2 = connection.prepareStatement(
                "select " +
                        " other_name" +
                        " from fop2_norm " +
                        "where last_name like ? and first_name like ?"
        );
        this.psGetCount = connection.prepareStatement("select " +
                "count(*)" +
                        " from fop2_norm " +
                        "where last_name like ? and first_name like ? and other_name like ?");
    }

    public Long getCount(String last, String first, String other) throws SQLException {
        psGetCount.setString(1, last);
        psGetCount.setString(2, first);
        psGetCount.setString(3, other);
        ResultSet resultSet = psGetCount.executeQuery();
        while (resultSet.next()) {
            return resultSet.getLong(1);
        }
        return null;
    }

    public List<FopNorm> getFops(String last, String first, String other) throws SQLException {
        psGet3.setString(1, last);
        psGet3.setString(2, first);
        psGet3.setString(3, other);
        ResultSet resultSet = psGet3.executeQuery();
        ArrayList<FopNorm> fops = new ArrayList<>();
        while (resultSet.next()) {
            FopNorm fop = new FopNorm(
                    resultSet.getString("last_name"),
                    resultSet.getString("first_name"),
                    resultSet.getString("other_name"),
                    resultSet.getString("address"),
                    resultSet.getString("activity"),
                    resultSet.getString("status"));
            fops.add(fop);
        }
        return fops;
    }

    public List<FopNorm> getFops(String last, String first) throws SQLException {
        psGet2.setString(1, last);
        psGet2.setString(2, first);
        ResultSet resultSet = psGet2.executeQuery();
        ArrayList<FopNorm> fops = new ArrayList<>();
        while (resultSet.next()) {
            FopNorm fop = new FopNorm();
            fop.setOther_name(resultSet.getString("other_name"));
            fops.add(fop);
        }
        return fops;
    }


}
