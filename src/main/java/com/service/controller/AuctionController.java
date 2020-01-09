package com.service.controller;


import com.service.entity.AuctionItem;
import com.service.entity.AuctionItemRepository;
import com.service.entity.Bid;
import com.service.entity.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("Duplicates")
@CrossOrigin
@RestController
@RequestMapping("/api/auction")
public class AuctionController {
    @Autowired
    AuctionItemRepository air;

    @Autowired
    BidRepository br;

    @GetMapping("/hello")
    public String hello() {
        return "HELLO";
    }

    @GetMapping("startAuction")
    public String startAuction(
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "make", required = false) String make,
            @RequestParam(value = "model", required = false) String model,
            @RequestParam(value = "variant", required = false) String variant,
            @RequestParam(value = "year", required = false) String year,
            @RequestParam(value = "mileage", required = false) String mileage,
            @RequestParam(value = "engine", required = false) String engine,
            @RequestParam(value = "gearbox", required = false) String gearbox,
            @RequestParam(value = "traction", required = false) String traction
            ) {

        AuctionItem auctionItem = new AuctionItem();
        auctionItem.setUserName(userName);
        auctionItem.setStatus("ongoing");
        auctionItem.setMake(make);
        auctionItem.setModel(model);
        auctionItem.setVariant(variant);
        auctionItem.setYear(year);
        auctionItem.setMileage(mileage);
        auctionItem.setEngine(engine);
        auctionItem.setGearbox(gearbox);
        auctionItem.setTraction(traction);

        auctionItem = air.save(auctionItem);

        if (auctionItem != null)
            return "OK";
        else
            return "FAIL";
    }

    @GetMapping("endAuction")
    public String endAuction(@RequestParam(value = "auctionId") Integer auctionId) {
        Optional<AuctionItem> optionalAuctionItem = air.findById(auctionId);

        if (optionalAuctionItem.isPresent() == false)
            return "FAIL";

        AuctionItem auctionItem = optionalAuctionItem.get();
        if (auctionItem.getStatus().equals("finished"))
            return "FAIL";

        auctionItem.setStatus("finished");
        auctionItem = air.save(auctionItem);

        if (auctionItem != null)
            return "OK";
        else
            return "FAIL";
    }

    @GetMapping("/getAuctions")
    public AuctionItem[] getAuctions() {
        List<AuctionItem> auctionItemList = air.findAllByStatusEquals("ongoing");

        AuctionItem[] result = new AuctionItem[auctionItemList.size()];
        for (int i = 0; i < auctionItemList.size(); i++) {
            result[i] = auctionItemList.get(i);
        }

        return result;
    }

    @GetMapping("/getAllAuctions")
    public AuctionItem[] getAllAuctions() {
        List<AuctionItem> auctionItemList = new ArrayList<>();
        air.findAll().iterator().forEachRemaining(auctionItemList::add);

        AuctionItem[] result = new AuctionItem[auctionItemList.size()];
        for (int i = 0; i < auctionItemList.size(); i++) {
            result[i] = auctionItemList.get(i);
        }

        return result;
    }

    @GetMapping("/placeBid")
    public String placeBid(
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "auctionItemId") Integer auctionItemId,
            @RequestParam(value = "bid") Integer bidValue
            ) {

        Optional<AuctionItem> optionalAuctionItem = air.findById(auctionItemId);
        if (optionalAuctionItem.isPresent() == false)
            return "AUCTION_NOT_PRESENT";

        if (optionalAuctionItem.get().getStatus().equals("finished"))
            return "AUCTION_FINISHED";

        List<Bid> bidList = br.findAllByAuctionItemIdEquals(auctionItemId);
        if (bidList.size() > 0) {
            Bid bid = bidList.get(bidList.size() - 1);

            if (bid.getUserName().equals(userName))
                return "AUCTION_STILL_HIGH_BID";

            if (bid.getBid() >= bidValue)
                return "AUCTION_LOW_BID";
        }

        Bid bid = new Bid();
        bid.setUserName(userName);
        bid.setAuctionItemId(auctionItemId);
        bid.setBid(bidValue);

        bid = br.save(bid);

        if (bid != null)
            return "OK";
        else
            return "FAIL";
    }

    @GetMapping("/getUserBids")
    public Bid[] getUserBids(@RequestParam(value = "userName") String userName) {
        List<Bid> bidList = br.findAllByUserNameEquals(userName);

        Bid[] result = new Bid[bidList.size()];
        for (int i = 0; i < bidList.size(); i++) {
            result[i] = bidList.get(i);
        }

        return result;
    }

    @GetMapping("/getAllBids")
    public Bid[] getAllBids() {
        List<Bid> bidList = new ArrayList<>();
        br.findAll().iterator().forEachRemaining(bidList::add);

        Bid[] result = new Bid[bidList.size()];
        for (int i = 0; i < bidList.size(); i++) {
            result[i] = bidList.get(i);
        }

        return result;
    }

}
