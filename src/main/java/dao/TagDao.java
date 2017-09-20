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

//    public int insert(String tagName, int receiptID) {
//        TagsRecord tagsRecord = dsl
//                .insertInto(TAGS, TAGS.TAG_NAME, TAGS.RECEIPT_ID)
//                .values(tagName, receiptID)
//                .returning(TAGS.ID)
//                .fetchOne();
//        checkState(tagsRecord != null && tagsRecord.getId() != null, "Insert failed");
//
//        return tagsRecord.getId();
//    }

//    public void remove(String tagName, int receiptID) {
//        dsl.deleteFrom(TAGS)
//                .where(TAGS.TAG_NAME.eq(tagName))
//                .and(TAGS.RECEIPT_ID.eq(receiptID));
//    }

    public void toggleTag(String tagName, int receiptID) {
        // lets see if we can find any tag records with this tag.
        TagsRecord tr = dsl
                .selectFrom(TAGS)
                .where(TAGS.RECEIPT_ID.eq(receiptID))
                .and(TAGS.TAG_NAME.eq(tagName))
                .fetchOne();

        // if the entered receipt has already been tagged with this tag, delete the tag from this receipt.
        // Otherwise, we should create another entry in the tag table to associate this tag with this receipt.
        if (tr != null && tr.getId() != null) {
            tr.delete();
        } else {
            tr = dsl
                    .insertInto(TAGS,  TAGS.TAG_NAME, TAGS.RECEIPT_ID)
                    .values(tagName, receiptID)
                    .returning(TAGS.ID)
                    .fetchOne();
        }

        checkState(tr != null && tr.getId() != null, "Insert failed");
    }

    public List<TagsRecord> getTagsRecords (String tagName) {
        // Return a list of all tag records that are labeled with this tag.
        return dsl.selectFrom(TAGS)
                .where(TAGS.TAG_NAME.eq(tagName))
                .fetch();
    }

    public List<TagsRecord> getTagsForReceipt (int receiptID) {
        // Return a list of all tag records that are associated with this receipt
        return dsl.selectFrom(TAGS)
                .where(TAGS.RECEIPT_ID.eq(receiptID))
                .fetch();

    }


}