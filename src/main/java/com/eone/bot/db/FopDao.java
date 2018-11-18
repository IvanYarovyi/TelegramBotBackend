package com.eone.bot.db;

import com.eone.bot.model.Fop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FopDao {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private PreparedStatement preparedStatementCount;

    public FopDao(String dbUrl) throws SQLException {
        this.connection = DriverManager.getConnection(dbUrl);
        this.preparedStatement = connection.prepareStatement(
                "select " +
                        "full_name, address, activity, status" +
                        " from fop2 " +
                        //"where fop2.full_name like UPPER(?)"
                        "where MATCH(fop2.full_name) AGAINST(? IN BOOLEAN MODE)"
        );
        this.preparedStatementCount = connection.prepareStatement(
                "select count(*) " +
                        " from fop2 " +
                        //"where fop2.full_name like UPPER(?)"
                        "where MATCH(fop2.full_name) AGAINST(? IN BOOLEAN MODE)"
        );
    }

    public int getCount(String query) throws SQLException {
        preparedStatementCount.setString(1, query);
        ResultSet resultSet = preparedStatementCount.executeQuery();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
       return 0;
    }

    public List<Fop> getFops(String query) throws SQLException {
        preparedStatement.setString(1, query);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Fop> fops = new ArrayList<>();
        while (resultSet.next()) {
            Fop fop = new Fop(resultSet.getString("full_name"),
                    resultSet.getString("address"),
                    resultSet.getString("activity"),
                    resultSet.getString("status"));
            fops.add(fop);
        }
        return fops;
    }


}
