package com.store.reservation.global.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


public class SearchByCondition {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{
        private double radius;
        private double lat;
        private double lnt;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private List<StoreDto> searchResult;

        public static SearchByCondition.Response from(List<StoreDto> searchResult){
            return new SearchByCondition.Response(searchResult);
        }
    }
}
