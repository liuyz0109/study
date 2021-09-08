import java.sql.*;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/9/7
 * @description mysql测试类02
 */
public class MySQLTest02 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // 加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        // 用户信息
        // url格式：协议://主机ip:端口号/数据库名?参数1&参数2&参数3
        String url = "jdbc:mysql://localhost:3306/kefu?useUnicode=true&characterEncoding=utf8&useSSL=true";
        String username = "root";
        String password = "123456";
        // 创建连接
        Connection connection = DriverManager.getConnection(url, username, password);
        // 创建sql对象
        String sql = "select * from role where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // 给参数进行赋值
        preparedStatement.setInt(1, 1);
        // 执行sql，获取结果
        ResultSet resultSet = preparedStatement.executeQuery();
        // 处理结果
        while (resultSet.next()) {
            System.out.println("-----------------start--------------------");
            System.out.println(resultSet.getString("id"));
            System.out.println(resultSet.getString("name"));
            System.out.println(resultSet.getString("description"));
            System.out.println("-----------------end--------------------");
        }
        // 释放连接
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}
