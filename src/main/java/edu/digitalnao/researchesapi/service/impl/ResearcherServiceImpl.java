package edu.digitalnao.researchesapi.service.impl;

import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.digitalnao.researchesapi.jpa.repository.ResearchersDetailRepository;
import edu.digitalnao.researchesapi.jpa.vo.ResearchersDetail;
import edu.digitalnao.researchesapi.service.ResearcherService;
import edu.digitalnao.researchesapi.web.model.ResearcherDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.beans.BeanUtils;
import java.lang.invoke.MethodHandles;
import java.text.MessageFormat;
import java.util.ArrayList;

@Service
public class ResearcherServiceImpl implements ResearcherService {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private String jsonString;
    private RestTemplate restTemplate;

    @Value("${edu-digitalnao-researchesapi.serpApi.apiKey}")
    private String serpApiApiKey;
    @Value("${edu-digitalnao-researchesapi.serpApi.google.scholar.author.url}")
    private String authorServiceUrl;

    private static final String HTTP_STATUS_MESSAGE = "HttpStatus: {}";

    @Autowired
    private ResearchersDetailRepository researchersDetailRepository;

    @Retryable(backoff = @Backoff(1000), maxAttempts = 2)
    public ResearcherDto getResearcherDetail(String id){
        ResearcherDto researcherDto = null;
        try{
            restTemplate = new RestTemplate();
            authorServiceUrl = MessageFormat.format(authorServiceUrl,id,serpApiApiKey);

            ResponseEntity<String> response
                    = restTemplate.getForEntity(authorServiceUrl , String.class);

            HttpStatus statusCode = response.getStatusCode();
            if(statusCode==HttpStatus.OK){
                researcherDto = new ResearcherDto();
                researcherDto.setId(id);

                try{
                    ObjectMapper mapper = new ObjectMapper();
                    ResearchersDetail researchersDetail = new ResearchersDetail();
                    JsonNode jsonNode = mapper.readTree(response.getBody());
                    researcherDto.setUrl(jsonNode.get("search_metadata").get("google_scholar_author_url").asText());
                    researcherDto.setEid(jsonNode.get("search_metadata").get("id").asText());
                    researcherDto.setAffiliationName(jsonNode.get("author").get("affiliations").asText());
                    researcherDto.setTotalResults(jsonNode.get("articles").size());

                    BeanUtils.copyProperties(researcherDto, researchersDetail);

                    this.researchersDetailRepository.save(researchersDetail);

                }catch(JsonProcessingException jsonProcessingException){
                    logger.error(jsonProcessingException.getMessage());
                }
            }
            return researcherDto;
        }catch(HttpClientErrorException exception){
            logger.info(HTTP_STATUS_MESSAGE, exception.getStatusCode().value());
            logger.error(exception.getResponseBodyAsString());
        }
        return researcherDto;
    }

    public ArrayList<ResearcherDto> getResearcherDetailList(){

        ArrayList<ResearcherDto> researcherDtoList = null;
        List<ResearchersDetail> researchersDetailList = this.researchersDetailRepository.findAll();

        if(!researchersDetailList.isEmpty()){
            researcherDtoList = new ArrayList<>();

            for(ResearchersDetail researchersDetail:researchersDetailList){
                ResearcherDto researcherDto = new ResearcherDto();
                BeanUtils.copyProperties(researchersDetail, researcherDto);
                researcherDtoList.add(researcherDto);
            }
        }
        return researcherDtoList;
    }
}
