package com.bookie.scrap.watcha.repository;

import com.bookie.scrap.common.DatabaseConnectionPool;
import com.bookie.scrap.watcha.dto.WatchaBookDetailDTO;
import com.bookie.scrap.watcha.dto.WatchaCommentDetailDTO;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class WatchaCommentJDBCImpl implements WatchaCommentRepository{

    private static final WatchaCommentJDBCImpl INSTANCE = new WatchaCommentJDBCImpl();
    private DataSource dataSource = DatabaseConnectionPool.getInstance().getDataSource();
    //private static HikariDataSource dataSource = (HikariDataSource) DatabaseConnectionPool.getInstance().getDataSource();

    public static WatchaCommentJDBCImpl getInstance() {return INSTANCE;}

    private static final Logger log = LoggerFactory.getLogger(WatchaCommentJDBCImpl.class);

    private final String selectQuery = "SELECT * FROM bookie_dev.bs_watcha_comment c WHERE c.book_code = ?";

    @Override
    public Optional<WatchaCommentDetailDTO> select(String bookCode) {

        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
        ) {
                preparedStatement.setString(1, bookCode);   // bookCode setting

                // Query 실행 결과
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // 결과값 판별
                    if(resultSet.next()){
                        // 존재시
                        WatchaCommentDetailDTO commentDetail = WatchaCommentDetailDTO.fromResultSet(resultSet);

                        // DTO 반환
                        return Optional.of(commentDetail);
                    }

                } catch (SQLException e) {
                    log.error("Error while processing ResultSet for bookCode: {}. SQLException: {}", bookCode, e.getMessage());
                }

        } catch (SQLException e) {
            log.error("Failed to retrieve data for bookCode: {}. SQLException: {}", bookCode, e.getMessage());
        }
        // 결과 미존재, 오류인 경우 빈 Optional 반환
        return Optional.empty();
    }

    @Override
    public boolean update(WatchaCommentDetailDTO watchaCommentDTO) {
        return false;
    }

    @Override
    public Optional<String> insert(WatchaCommentDetailDTO watchaCommentDTO) {
        return Optional.empty();
    }
}
