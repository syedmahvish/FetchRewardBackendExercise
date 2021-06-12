package com.example.FetchRewards;

import com.example.pojo.AddRewardRequest;
import com.example.pojo.GetRewardBalanceResponse;
import com.example.pojo.SpendAndGetRewardBalanceRequest;
import com.example.service.FetchRewardsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
public class FetchRewardsController {

    FetchRewardsService fetchRewardsService = new FetchRewardsService();

    /***
     * Add each transaction to in memory holder(used Arraylist).
     * Method type : Post
     * Parameter : payer (string), points (integer), timestamp (date) (class : AddRewardRequest)
     * Return : HttpStatus: Ok : success,
     *          EXPECTATION_FAILED : if datatype/parameter does not matches
     * */
    @PostMapping("/addRewards")
    public ResponseEntity<HttpStatus> addRewards(@RequestBody AddRewardRequest addRewardRequest){
            HttpStatus responseStatus = fetchRewardsService.addRewards(addRewardRequest);
            return new ResponseEntity<>(responseStatus);

    }

    /**
     * Sort input on basis of timestamp, spend points and return list of payer.
     * Method type : Post
     * @param spendAndGetRewardBalanceRequest : takes point to spend
     * @return List of payer { "payer": <string>, "points": <integer> }  (class : GetRewardBalanceResponse)
     * @throws IOException : If totalpoints < 0 , return exception
     * If spendPoint > available points return Exception with message that indicates how much spend and how much left. Also update balance.
     * HttpStatus : Ok - success
     *              FORBIDDEN - unsuccessful.
     */
    @PostMapping("/spendPoints")
    public ResponseEntity<List<GetRewardBalanceResponse>> spendAndGetRewardBalance(@RequestBody SpendAndGetRewardBalanceRequest spendAndGetRewardBalanceRequest) throws IOException {
        try{
            List<GetRewardBalanceResponse> rewardsServiceRewardBalanceResponses = (List<GetRewardBalanceResponse>) fetchRewardsService.getRewardBalanceResponses(spendAndGetRewardBalanceRequest);
            return new ResponseEntity<List<GetRewardBalanceResponse>>(rewardsServiceRewardBalanceResponses, HttpStatus.OK);
        }catch (IOException error){
            System.out.println("Exception occur: " + error);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    /**
     * It checks balance and return list of payer balance.
     * @return  list of payer and balance. { "payer": <string>, "points": <integer> }
     * HttpStatus : Ok
     * If no input, returns empty list.
     */
    @GetMapping("/checkBalance")
    public ResponseEntity<List<GetRewardBalanceResponse>> checkBalanceResponse() {
        List<GetRewardBalanceResponse> rewardBalanceResponses = (List<GetRewardBalanceResponse>) fetchRewardsService.checkandGetBalance();
        return new ResponseEntity<List<GetRewardBalanceResponse>>(rewardBalanceResponses, HttpStatus.OK);
    }

}
