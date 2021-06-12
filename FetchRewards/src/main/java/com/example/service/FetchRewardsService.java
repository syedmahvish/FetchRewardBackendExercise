package com.example.service;

import com.example.pojo.AddRewardRequest;
import com.example.pojo.GetRewardBalanceResponse;
import com.example.pojo.SpendAndGetRewardBalanceRequest;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.*;

public class FetchRewardsService {

   List<AddRewardRequest> rewardRequestList = new ArrayList<>();
   int totalPoints = 0;

    /**
     * Add transaction into list of type AddRewardRequest
     * @param addRewardRequest accepts input in format payer (string), points (integer), timestamp (date).
     * @return HTTPStatus : Ok - success
     *                      BAD_REQUEST : datatype/parameter mismatched.
     */
    public HttpStatus addRewards(AddRewardRequest addRewardRequest){
        Date timestamp = addRewardRequest.getTimestamp();
        if(!(timestamp instanceof Date)) return HttpStatus.BAD_REQUEST;

        String payer = addRewardRequest.getPayer();
        if(!(payer instanceof String)) return HttpStatus.BAD_REQUEST;

        Integer points = addRewardRequest.getPoints();
        if(!(points instanceof Integer)) return HttpStatus.BAD_REQUEST;

        totalPoints += points;

        rewardRequestList.add(addRewardRequest);

        return HttpStatus.OK;

    }

    /**
     * Check if total points < 0 , throw Exception with message
     * Normalize input ( Sort list acc to timestamp and handle negative payer balance)
     * Spend Points and return list of payer
     * @param spendAndGetRewardBalanceRequest : accepts points to spend
     * @return list of payer in format { "payer": <string>, "points": <integer> }
     * @throws IOException : If totalpoints < 0 or not enough points to spend.
     *
     */
    public List<GetRewardBalanceResponse> getRewardBalanceResponses(SpendAndGetRewardBalanceRequest spendAndGetRewardBalanceRequest) throws IOException{
        if(totalPoints <= 0) throw new IOException("Not enough balance to spend");

        normalizeRewardRequestList();

        return getRewardBalanceAfterSpend(spendAndGetRewardBalanceRequest.getPoints());

    }

    /**
     * Sort list according to timestamp
     * Update negative points of payer
     */
    private  void normalizeRewardRequestList(){

        //sort list according to timestamp
        Collections.sort(rewardRequestList, new Comparator<AddRewardRequest>(){
            @Override
            public int compare(AddRewardRequest r1, AddRewardRequest r2){
                return r1.getTimestamp().compareTo(r2.getTimestamp());
            }
        });

        //iterate through list and check if points of payer < 0
        //if so update.

        for(int i = 0; i < rewardRequestList.size(); i++){
            AddRewardRequest reward = rewardRequestList.get(i);
            for(int j = 0; j <= i; j++){
                AddRewardRequest tempReward = rewardRequestList.get(j);
                if(tempReward.getPayer().equals(reward.getPayer())){
                    if( reward.getPoints() < 0 && tempReward.getPoints() > 0){
                        int temp = reward.getPoints() + tempReward.getPoints();
                        reward.setPoints(temp);
                        tempReward.setPoints(0);
                    }else if(reward.getPoints() > 0 && tempReward.getPoints() < 0){
                        int temp = reward.getPoints() + tempReward.getPoints();
                        reward.setPoints(temp);
                        tempReward.setPoints(0);
                    }
                }
            }
        }
    }

    /**
     * Calculate point spends using payer balance.
     * @param spend Accpets points to spend.
     * @return List of payer { "payer": <string>, "points": <integer> }
     * @throws IOException : If balance is not enough to spend all points throws exception with message.
     */
    private List<GetRewardBalanceResponse> getRewardBalanceAfterSpend(int spend) throws IOException{
        int originalSpend = spend;
        Map<String, Integer> output = new HashMap<>();

        for(int i = 0; i < rewardRequestList.size(); i++){
            AddRewardRequest reward = rewardRequestList.get(i);
            if(spend > 0){
                if(reward.getPoints() > 0 && reward.getPoints() <= spend){
                    output.put(reward.getPayer(), output.getOrDefault(reward.getPayer(), 0) + reward.getPoints());
                    spend -= reward.getPoints();
                    reward.setPoints(0);
                }else if(reward.getPoints() > 0 && reward.getPoints() >= spend){
                    output.put(reward.getPayer(), output.getOrDefault(reward.getPayer(), 0) + spend);
                    int temp = reward.getPoints() - spend;
                    reward.setPoints(temp);
                    spend = 0;
                }
            }
        }

        if(spend > 0) throw new IOException("Not enough points to spend" + " spend :"  + spend + ", balance:"
                + (originalSpend - spend));

        List<GetRewardBalanceResponse> getRewardBalanceResponseList = new ArrayList<>();

        for(Map.Entry entry : output.entrySet()){
            GetRewardBalanceResponse balanceResponse = new GetRewardBalanceResponse();
            balanceResponse.setPayerName((String) entry.getKey());
            balanceResponse.setPoints((Integer) entry.getValue() * -1);
            getRewardBalanceResponseList.add(balanceResponse);
        }
        return  getRewardBalanceResponseList;
    }


    /**
     * @return list of payer balance { "payer": <string>, "points": <integer> } (class : GetRewardBalanceResponse)
     */
    public List<GetRewardBalanceResponse> checkandGetBalance(){
        Map<String,Integer> balanceMap = new HashMap<>();

        for(int i = 0; i < rewardRequestList.size(); i++){
            AddRewardRequest reward = rewardRequestList.get(i);
            balanceMap.put(reward.getPayer(), balanceMap.getOrDefault(reward.getPayer(), 0) + reward.getPoints());
        }

        List<GetRewardBalanceResponse> getRewardBalanceResponseList = new ArrayList<>();

        for(Map.Entry entry : balanceMap.entrySet()){
            GetRewardBalanceResponse balanceResponse = new GetRewardBalanceResponse();
            balanceResponse.setPayerName((String) entry.getKey());
            balanceResponse.setPoints((Integer) entry.getValue());
            getRewardBalanceResponseList.add(balanceResponse);
        }
        return  getRewardBalanceResponseList;
    }
}
