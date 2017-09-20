package controllers;

import api.CreateReceiptRequest;
import api.ReceiptResponse;

import dao.ReceiptDao;

import dao.TagDao;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/receipts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReceiptController {
    final ReceiptDao receipts;
    final TagDao tags;

    public ReceiptController(ReceiptDao receipts, TagDao tags) {
        this.receipts = receipts;
        this.tags = tags;
    }

    @POST
    public int createReceipt(@Valid @NotNull CreateReceiptRequest receipt) {
        return receipts.insert(receipt.merchant, receipt.amount);
    }

    @GET
    public List<ReceiptResponse> getReceipts() {
        List<ReceiptResponse> receiptResponses = new ArrayList<>();

        for (ReceiptsRecord receiptRecord : receipts.getAllReceipts()) {
            receiptResponses.add(new ReceiptResponse(receiptRecord, tags.getTagsForReceipt(receiptRecord.getId())));
        }

        return receiptResponses;
    }
}
