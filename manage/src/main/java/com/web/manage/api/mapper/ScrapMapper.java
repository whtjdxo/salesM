package com.web.manage.api.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.web.manage.api.domain.ScrapBankDataVO;
import com.web.manage.api.domain.ScrapCompVO;
import com.web.manage.api.domain.ScrapDeliDataVO;
import com.web.manage.api.domain.ScrapErrorLogVO;
import com.web.manage.api.domain.ScrapLogVO;
import com.web.manage.api.domain.ScrapUserVO;
import com.web.manage.api.domain.ScrapVanDataVO;

@Mapper
public interface ScrapMapper {

    int getUserCheck(ScrapUserVO scrapUserVo);    
    boolean setUserAuthKey(ScrapUserVO scrapUserVo);
    int getUserAuthKeyCheck(ScrapUserVO scrapUserVo);

    public List<HashMap<String, Object>> getVanChainList(ScrapCompVO scrapCompVO);
    public List<HashMap<String, Object>> getBankChainList(ScrapCompVO scrapCompVO);

    boolean scrapUploadVanData(ScrapVanDataVO scrapVanDataVO);
    boolean scrapUploadDeliData(ScrapDeliDataVO    scrapDeliDataVO);
    boolean scrapUploadBankData(ScrapBankDataVO scrapBankDataVO);

    boolean writeScrapErrorLog(ScrapErrorLogVO errorVo);
    boolean writeScrapLog(ScrapLogVO logVO);
    
}
