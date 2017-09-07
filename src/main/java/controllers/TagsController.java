package controllers;

import dao.TagDao;
import dao.ReceiptDao;
import api.ReceiptResponse;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;



import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Path("/tags/{tag}")
@Consumes(MediaType.APPLICATION_JSON)
public class TagsController {
    final TagDao tags;
    final ReceiptDao receipts;

    public TagsController(ReceiptDao receipts, TagDao tags) {
        this.receipts = receipts;
        this.tags = tags;
    }


    @PUT
    public void toggleTag(@PathParam("tag") String tagName, @Valid @NotNull int receiptID) {
        // Toggles tags on a given receipt if the receipt already exists in the system.

        // Check if valid receipt was given:
        ReceiptsRecord rID = receipts.getSingleReceiptsRecord(receiptID);
        if (rID == null) {
            throw new NotFoundException();
        }
        // if the entered receipt has already been tagged with this tag, delete the tag from this receipt.
        // Otherwise, we should create another entry in the tag table to associate this tag with this receipt.
        // This logic is handles in the toggleTag method in the TagDao.
        tags.toggleTag(tagName, receiptID);
//        if (r != null && r.isEmpty()) {
//            // create tag in tags database
//            tags.insert(tagName, receiptID);
//        } else if (r.contains(receiptID)) {
//            // delete existing tag from tags database
//            tags.remove(tagName, receiptID);
//        }

    }

    @GET
    //@Produces(MediaType.APPLICATION_JSON)
    public List<ReceiptResponse> getReceiptsWithTag(@PathParam("tag") String tagName) {
        // get the list of receipt objects with this tag.
//        return tags.getReceiptIDsForThisTag(tagName);
        List<TagsRecord> TRs = tags.getTagsRecords(tagName);
        List<Integer> RIDs = TRs.stream().map(TagsRecord::getReceiptId).collect(toList());
        List<ReceiptsRecord> receiptRecords = receipts.getMultipleReceiptsRecords(RIDs);
        return receiptRecords.stream().map(ReceiptResponse::new).collect(toList());




    }




}
