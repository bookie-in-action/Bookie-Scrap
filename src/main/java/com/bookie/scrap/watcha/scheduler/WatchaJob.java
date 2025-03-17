package com.bookie.scrap.watcha.scheduler;

import com.bookie.scrap.common.request.Request;
import com.bookie.scrap.common.request.RequestFactory;
import com.bookie.scrap.watcha.domain.WatchaBaseRequestParam;
import com.bookie.scrap.watcha.dto.WatchaBookcaseDTO;
import com.bookie.scrap.watcha.entity.WatchaBookEntity;
import com.bookie.scrap.common.Repository;
import com.bookie.scrap.watcha.repository.WatchaBookMetaRepository;
import com.bookie.scrap.watcha.request.WatchaBookMetaRequestFactory;
import com.bookie.scrap.watcha.dto.WatchaBookMetaDto;
import com.bookie.scrap.watcha.request.WatchaBookcaseRequestFactory;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;


@Slf4j
public class WatchaJob implements Job {

    private final RequestFactory<List<WatchaBookcaseDTO>> bookcaseRequestFactory = WatchaBookcaseRequestFactory.getInstance();
    private final RequestFactory<WatchaBookMetaDto> bookRequestFactory = WatchaBookMetaRequestFactory.getInstance();

    private final Repository<WatchaBookEntity> bookMetaRepository = WatchaBookMetaRepository.getInstance();
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        WatchaBaseRequestParam requestParam = new WatchaBaseRequestParam("gcdkyKnXjN", 1, 12);
        Request<List<WatchaBookcaseDTO>> bookcaseRequest = bookcaseRequestFactory.createRequest(requestParam);
        List<WatchaBookcaseDTO> bookcaseList = bookcaseRequest.execute();

        for (WatchaBookcaseDTO bookcaseDTO : bookcaseList) {
            // bookcase repo에 저장

            String bookCode = bookcaseDTO.getBookCode();
            Request<WatchaBookMetaDto> bookMetaRequest = bookRequestFactory.createRequest(bookCode);
            WatchaBookMetaDto bookMetaDto = bookMetaRequest.execute();
            bookMetaRepository.insertOrUpdate(bookMetaDto.toEntity());

            // comment repo에 저장
        }

    }
}
