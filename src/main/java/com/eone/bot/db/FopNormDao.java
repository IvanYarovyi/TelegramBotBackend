package com.eone.bot.db;

import com.eone.bot.db.model.FopNorm;
import com.eone.bot.telegram.FopRequestProcessor;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FopNormDao {


    private DataSource ds;

    public FopNormDao(DataSource dataSource) throws SQLException {
        this.ds = dataSource;
    }

    public Long getCount(String last, String first, String other) throws SQLException {
        try(Connection connection = ds.getConnection()) {
            PreparedStatement psGetCount = connection.prepareStatement(
                    "select " +
                            "count(*)" +
                            " from fop2_norm " +
                            "where last_name like ? and first_name like ? and other_name like ?");
            psGetCount.setString(1, last);
            psGetCount.setString(2, first);
            psGetCount.setString(3, other);
            ResultSet resultSet = psGetCount.executeQuery();
            while (resultSet.next()) {
                return resultSet.getLong(1);
            }
        }
        return null;
    }

    public List<FopNorm> getFops(String last, String first, String other) throws SQLException {
        try(Connection connection = ds.getConnection()) {
            PreparedStatement psGet3 = connection.prepareStatement(
                    "select " +
                            "last_name, first_name, other_name, address, activity, status" +
                            " from fop2_norm " +
                            "where last_name like ? and first_name like ? and other_name like ?"
            );
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
    }

    public List<FopNorm> getFops(String last, String first) throws SQLException {
        try(Connection connection = ds.getConnection()) {
            PreparedStatement psGet2 = connection.prepareStatement(
                    "select " +
                            " other_name" +
                            " from fop2_norm " +
                            "where last_name like ? and first_name like ?"
            );
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



}
