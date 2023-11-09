package com.sparta.springresttemplateclient.service;import com.sparta.springresttemplateclient.dto.ItemDto;import com.sparta.springresttemplateclient.entity.User;import lombok.extern.slf4j.Slf4j;import org.json.JSONArray;import org.json.JSONObject;import org.springframework.boot.web.client.RestTemplateBuilder;import org.springframework.http.ResponseEntity;import org.springframework.stereotype.Service;import org.springframework.web.client.RestTemplate;import org.springframework.web.util.UriComponentsBuilder;import java.net.URI;import java.util.ArrayList;import java.util.List;@Slf4j@Servicepublic class RestTemplateService {    private final RestTemplate restTemplate;    public RestTemplateService(RestTemplateBuilder builder) {        this.restTemplate = builder.build();    }    public ItemDto getCallObject(String query) {        // 요청 URL 만들기 서버입장 서버에 보낼 준비! 컨트롤러에서 query받아옴(?query=Mac)        URI uri = UriComponentsBuilder                .fromUriString("http://localhost:7070")                .path("/api/server/get-call-obj")                .queryParam("query", query)                .encode()                .build()                .toUri();        log.info("uri = " + uri);        ResponseEntity<ItemDto> responseEntity = restTemplate.getForEntity(uri, ItemDto.class);        //get방식으로 요청(요청 uri, 반환 정보- 변환 원하는  클래스 deserialize해서 json을 객체로 받아옴 )        log.info("statusCode = " + responseEntity.getStatusCode());        return responseEntity.getBody();    }    public List<ItemDto> getCallList() {        // 요청 URL 만들기        URI uri = UriComponentsBuilder                .fromUriString("http://localhost:7070")                .path("/api/server/get-call-list")                .encode()                .build()                .toUri();        log.info("uri = " + uri);        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);        //여러개가 넘어오면 String으로 받아서 다시 변환!        log.info("statusCode = " + responseEntity.getStatusCode());        log.info("Body = " + responseEntity.getBody());        return fromJSONtoItems(responseEntity.getBody());    }    public ItemDto postCall(String query) {        // 요청 URL 만들기  PathVariable 방식으로!!보내기        URI uri = UriComponentsBuilder                .fromUriString("http://localhost:7070")                .path("/api/server/post-call/{query}")                .encode()                .build()                .expand(query)                .toUri();        log.info("uri = " + uri);        User user = new User("Robbie", "1234");        ResponseEntity<ItemDto> responseEntity = restTemplate.postForEntity(uri, user, ItemDto.class);        //POST 방식(Body에  Data 넘길 수 있다!)  uri, 보낼 데이터, 전달 받을 데이터        log.info("statusCode = " + responseEntity.getStatusCode());        return responseEntity.getBody();    }    public List<ItemDto> exchangeCall(String token) {        return null;    }    public List<ItemDto> fromJSONtoItems(String responseEntity) {        JSONObject jsonObject = new JSONObject(responseEntity);        JSONArray items = jsonObject.getJSONArray("items");//키 값 -> 내부를 찾는다(배열)        List<ItemDto> itemDtoList = new ArrayList<>();        for (Object item : items) {            ItemDto itemDto = new ItemDto((JSONObject) item);            itemDtoList.add(itemDto);        }        return itemDtoList;    }}