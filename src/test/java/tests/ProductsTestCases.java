package tests;

import org.testng.annotations.Test;
import java.sql.*;

import static org.testng.Assert.assertEquals;

public class ProductsTestCases {

    private static final String URL = System.getenv("db_url");
    private static final String USERNAME = System.getenv("db_username");
    private static final String PASSWORD = System.getenv("db_password");
    private static Connection connection;
    private static Statement statement;
    private ResultSet resultSet;

    static {

        System.out.println(USERNAME);
        System.out.println(PASSWORD);
        System.out.println(URL);

        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    @Test(description = "Display the name of the most expensive product.")
    public void connection_test() throws SQLException {

        
        resultSet = statement.executeQuery("select productName from products " +
                                               "where buyPrice = (select max(buyPrice) from products);");

        resultSet.next();

        System.out.println(resultSet.getString(1));

    }
    
    @Test(description = "Change the name of the first product and verify if it is changed.")
    public void update_test() throws SQLException {

        String productName = "2023 Honda Gold Wing";

        statement.executeUpdate("update products set productName = '" + productName + "' " +
                                    "where productCode = 'S10_1678';");

        resultSet = statement.executeQuery("select * from products where productCode = 'S10_1678'");

        resultSet.next();

        assertEquals(resultSet.getString(2),productName);

    }

    @Test(description = "Delete the previously updated product and verify if it is deleted.")
    public void delete_test() throws SQLException {

        String productName = "2023 Honda Gold Wing";

        statement.executeUpdate("delete from orderdetails where productCode = 'S10_1678';");
        statement.executeUpdate("delete from products where productCode = 'S10_1678';");

        resultSet = statement.executeQuery("select * from products where productCode = 'S10_1678'");

        resultSet.next();

        //Result after delete --> java.sql.SQLException: Illegal operation on empty result set.
        System.out.println(resultSet.getString(2));

    }

    @Test(description = "Add a new product to products table and verify if it is added.")
    public void insertion_test() throws SQLException {

        String productName = "2023 Honda Gold Wing";

        statement.executeUpdate("insert into products (productCode,productName,productLine,productScale,productVendor,productDescription,quantityInStock,buyPrice,MSRP) values ('S10_2023','2023 Honda Gold Wing','Motorcycles','1:10','Honda','Awesome motorcycle','1','55.12','123.12');" );

        resultSet = statement.executeQuery("select * from products where productCode = 'S10_2023'");

        resultSet.next();

        assertEquals(resultSet.getString(2),productName);

    }

}
