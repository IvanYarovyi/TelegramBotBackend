package com.eone.bot.db;

import com.eone.bot.model.FopNorm;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FopNormDao {

    private Connection connection;
    private PreparedStatement psGet;

    public FopNormDao(String dbUrl) throws SQLException {
        this.connection = DriverManager.getConnection(dbUrl);
        this.psGet = connection.prepareStatement(
                "select " +
                        "last_name, first_name, other_name, address, activity, status" +
                        " from fop2_norm " +
                        "where last_name = ? and first_name = ? and other_name = ?"
        );
    }

    public List<FopNorm> getFops(String last, String first, String other) throws SQLException {
        psGet.setString(1, last);
        psGet.setString(2, first);
        psGet.setString(3, other);
        ResultSet resultSet = psGet.executeQuery();
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


}
