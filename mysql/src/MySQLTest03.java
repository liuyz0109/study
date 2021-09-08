import java.sql.*;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/9/7
 * @description mysql测试类03
 */
public class MySQLTest03 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // 加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        // 用户信息
        // url格式：协议://主机ip:端口号/数据库名?参数1&参数2&参数3
        String url = "jdbc:mysql://localhost:3306/kefu?useUnicode=true&characterEncoding=utf8&useSSL=true";
        String username = "root";
        String password = "123456";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 创建连接
            connection = DriverManager.getConnection(url, username, password);
            // 开启手动提交事务
            connection.setAutoCommit(false);
            // 创建sql对象
            String sql = "update role set id = ? and name = ? and description=?";
            preparedStatement = connection.prepareStatement(sql);
            // 给参数进行赋值
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "李思思");
            preparedStatement.setString(3, "我是一个描述");
            // 执行sql
            preparedStatement.execute();
            // 出现异常时，因为开启了事务，会进行回滚，rollback() 可以不用显示定义
            int i = 10 / 0;
            // 手动提交事务
            connection.commit();
        } catch (Exception e) {
            if (null != connection)
                connection.rollback();
            e.printStackTrace();
        } finally {
            // 释放连接
            if (null != resultSet) {
                resultSet.close();
            }
            if (null != preparedStatement) {
                preparedStatement.close();
            }
            if (null != connection) {
                // 关闭手动提交事务
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }
}
