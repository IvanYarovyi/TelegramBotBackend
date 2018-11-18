package com.eone.bot.db;

import com.eone.bot.model.Fop;
import com.eone.bot.model.FopNorm;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FopNormDao {

    private Connection connection;
    private PreparedStatement psGet;
    private PreparedStatement psCount;

    public FopNormDao(String dbUrl) throws SQLException {
        this.connection = DriverManager.getConnection(dbUrl);
        this.psGet = connection.prepareStatement(
                "select " +
                        "last_name, first_name, other_name, address, activity, status" +
                        " from fop2_norm " +
                        "where last_name = ? and first_name = ? and other_name = ?"
        );
        this.psCount = connection.prepareStatement(
                "select count(*) " +
                        " from fop2_norm " +
                        //"where fop2.full_name like UPPER(?)"
                        "where last_name = ? and first_name = ? and other_name = ?"
        );
    }

    public int getCount(String last, String first, String other) throws SQLException {
        psCount.setString(1, last);
        psCount.setString(2, first);
        psCount.setString(3, other);
        ResultSet resultSet = psCount.executeQuery();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
       return 0;
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
