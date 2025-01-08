//package com.bookie.scrap.watcha.repository;
//
//import com.bookie.scrap.common.DatabaseConnectionPool;
//import com.bookie.scrap.watcha.dto.WatchaBookEntity;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Optional;
//
//public class WatchaBookJDBCImpl implements WatchaRepository {
//
//    private static final WatchaBookJDBCImpl INSTANCE = new WatchaBookJDBCImpl();
//    private DataSource dataSource = DatabaseConnectionPool.getInstance().getDataSource();
//
//    private WatchaBookJDBCImpl() {}
//
//    public static WatchaBookJDBCImpl getInstance() {return INSTANCE;}
//
//    private final String selectQuery = "SELECT * FROM watcha_books WHERE book_code = ?";
//    private final String insertQuery = "INSERT INTO watcha_books (book_code, title, author, publisher) VALUES (?, ?, ?, ?)";
//    private final String updateQuery =  "UPDATE watcha_books SET title = ?, author = ?, publisher = ? WHERE book_code = ?";
//
//    @Override
//    public Optional<WatchaBookEntity> select(String bookCode) {
//
//        try (
//                Connection connection = dataSource.getConnection();
//                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)
//        ) {
//
//            preparedStatement.setString(1, bookCode);
//
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                if (resultSet.next()) {
////                    WatchaBookDetailDTO dto = new WatchaBookDetailDTO();
////                    dto.setBookCode(resultSet.getString("book_code"));
////                    dto.setTitle(resultSet.getString("title"));
////                    dto.setAuthor(resultSet.getString("author"));
////                    dto.setPublisher(resultSet.getString("publisher"));
////                    return Optional.of(dto);
//                }
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        return Optional.empty();
//    }
//
//    @Override
//    public boolean update(WatchaBookEntity watchaBookDTO) {
//
//        try (
//                Connection connection = dataSource.getConnection();
//                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)
//        ) {
//
////            preparedStatement.setString(1, watchaBookDTO.getBookCode());
////            preparedStatement.setString(2, watchaBookDTO.getTitle());
////            preparedStatement.setString(3, watchaBookDTO.getAuthor());
////            preparedStatement.setString(4, watchaBookDTO.getPublisher());
////
//            return preparedStatement.executeUpdate() > 0;
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    @Override
//    public Optional<String> insert(WatchaBookEntity watchaBookDTO) {
//        try (
//                Connection connection = dataSource.getConnection();
//                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)
//        ) {
//
////            preparedStatement.setString(1, watchaBookDTO.getBookCode());
////            preparedStatement.setString(2, watchaBookDTO.getTitle());
////            preparedStatement.setString(3, watchaBookDTO.getAuthor());
////            preparedStatement.setString(4, watchaBookDTO.getPublisher());
////
////            int rows = preparedStatement.executeUpdate();
////            if (rows > 0) {
////                return watchaBookDTO.getBookCode();
////            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        return Optional.empty();
//    }
//
//}
