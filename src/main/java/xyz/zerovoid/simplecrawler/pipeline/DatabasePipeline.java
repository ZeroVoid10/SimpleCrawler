package xyz.zerovoid.simplecrawler.pipeline;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.zerovoid.simplecrawler.item.Items;
import xyz.zerovoid.simplecrawler.util.Request;

/**
 * @author 李曼婷
 * @since 0.3.0
 * TODO: Make DatabasePipeline generally.
 * TODO: Make this class more flexible.
 */
public class DatabasePipeline implements Pipeline {

    private static final Logger logger = 
        LoggerFactory.getLogger(DatabasePipeline.class);

    protected static final String MARIADB_DRIVER = "org.mariadb.jdbc.Driver";
    protected static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    protected final String host;
    protected final String port;
    protected final String baseName;
    protected final String user;
    protected final String password;
    protected final String dbms;

    protected Connection database;

    public DatabasePipeline(String host, String baseName , String port,
            String dbms, String user, String password) {
        this.host = host;
        this.baseName = baseName;
        this.port = port;
        this.user = user;
        this.password = password;
        this.dbms = dbms.toLowerCase();
        connectBase();
    }

    protected void connectBase() {
        if (dbms == "mariadb") {
            try {
		    	Class.forName(MARIADB_DRIVER);
                String url = "jdbc:" + this.dbms + "://" + this.host + ":" + 
                        this.port + "/" + this.baseName + "?user=" + 
                        this.user + "&password=" + this.password;
                logger.info("Database connection url : {}", url);
                database = DriverManager.getConnection(url);
		    } catch (ClassNotFoundException e) {
                logger.error("Can not find driver for : {}", dbms);
			    e.printStackTrace();
		    } catch (SQLException e) {
				logger.error("Can not connect to database.");
				e.printStackTrace();
			}
        } else if (dbms == "mysql") {
            try {
                Class.forName(MYSQL_DRIVER);
                String url = "jdbc:" + this.dbms + "://" + this.host + ":" + 
                        this.port + "/" + this.baseName + "?user=" + 
                        this.user + "&password=" + this.password;
                logger.info("Database connection url : {}", url);
                database = DriverManager.getConnection(url);
            } catch (ClassNotFoundException e) {
                logger.error("Can not find driver for : {}", dbms);
			    e.printStackTrace();
            } catch (SQLException e) {
				logger.error("Can not connect to database.");
				e.printStackTrace();
			}
        } else {
            logger.error("Sorry, we only support for mariadb and mysql.");
            throw new NullPointerException();
        }
    }

    public void addTable(String table, String key, String... cols) {
        String colsName = "( " + key + " VARCHAR(128) NOT NULL PRIMARY KEY, ";
        for (String col : cols) {
            colsName += col + "VARCHAR(128), ";
        }
        colsName += ");";
        String createTableSql = "create table " + table + colsName;
		try {
			Statement state = database.createStatement();
            state.executeUpdate(createTableSql);
		} catch (SQLException e) {
            logger.error("Error in creating table :{}", table);
			e.printStackTrace();
		}
    }

    protected void insertAll(String table, String key, ArrayList<String> list) {
        String insertSql = "INSERT INTO " + table + " VALUES('" + key + "'";
        for (String value : list) {
            insertSql += ", '" + value + "'";
        }
        insertSql += ");";
        logger.info("{}", insertSql);
        try {
			Statement state = database.createStatement();
            state.executeUpdate(insertSql);
		} catch (SQLException e) {
            logger.warn("Insert failed.");
			e.printStackTrace();
		}
    }

	@Override
	public void processItem(Items items) {
        Request request = items.getRequest();
        if (request.containTag("detail")) {
            processDetail(items);
        } else {
            processList(items);
        }
	}

    protected void processList(Items items) {
        for (Entry<String, Object> entry :
                items.getAll().entrySet()) {
            ArrayList<String> row = (ArrayList<String>) entry.getValue();
            insertAll("JobsItem", entry.getKey(), row);
        }
    }

    protected void processDetail(Items items) {
        for (Entry<String, Object> entry : 
                items.getAll().entrySet()) {
            ArrayList<String> row = (ArrayList<String>) entry.getValue();
            insertAll("JobDetail", entry.getKey(), row);
        }
    }

    public String getUser() {
        return user;
    }

    public static void main(String[] args) {
        DatabasePipeline pipeline = new
            DatabasePipeline("localhost", "simplecrawler", "3306", 
                    "mariadb", "zerovoid", "zerovoid");
    }
}
