package dao;

import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.yaml.snakeyaml.events.Event;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;
import static java.util.stream.Collectors.toList;

public class TagDao {

    DSLContext dsl;

    public TagDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public int insert(String tagName, int receiptID) {
        TagsRecord tagsRecord = dsl
                .insertInto(TAGS, TAGS.TAG_NAME, TAGS.RECEIPT_ID)
                .values(tagName, receiptID)
                .returning(TAGS.ID)
                .fetchOne();
        checkState(tagsRecord != null && tagsRecord.getId() != null, "Insert failed");

        return tagsRecord.getId();
    }

    public void remove(String tagName, int receiptID) {
        dsl.deleteFrom(TAGS)
                .where(TAGS.TAG_NAME.eq(tagName))
                .and(TAGS.RECEIPT_ID.eq(receiptID));
    }

//    public void toggleTag(String tagName, int receiptID) {
//        // get the list of receipts with this tag.
//        List r = getReceiptIDsWithThisTag(tagName);
//
//        // if the entered receipt has already been tagged with this tag, delete the tag from this receipt.
//        // Otherwise, we should create another entry in the tag table to associate this tag with this receipt.
//        if (r != null && r.isEmpty()) {
//            // create tag in database
//        }
//    }

    public List<Integer> getReceiptIDsForThisTag(String tagName) {
        // Return a list of all receipt IDs that are labeled with this tag.
        List<TagsRecord> T = dsl.selectFrom(TAGS)
                .where(TAGS.TAG_NAME.eq(tagName))
                .fetch();
        List<Integer> receiptIDs = T.stream().map(TagsRecord::getReceiptId).collect(toList());
        return receiptIDs;
    }


}