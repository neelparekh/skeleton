package dao;

import api.ReceiptResponse;
import generated.tables.records.ReceiptsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;

public class ReceiptDao {
    DSLContext dsl;

    public ReceiptDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public int insert(String merchantName, BigDecimal amount) {
        ReceiptsRecord receiptsRecord = dsl
                .insertInto(RECEIPTS, RECEIPTS.MERCHANT, RECEIPTS.AMOUNT)
                .values(merchantName, amount)
                .returning(RECEIPTS.ID)
                .fetchOne();

        checkState(receiptsRecord != null && receiptsRecord.getId() != null, "Insert failed");

        return receiptsRecord.getId();
    }

    public List<ReceiptsRecord> getMultipleReceiptsRecords(List<Integer> receiptIDs) {
        // SELECT * From RECEIPTS WHERE ID IN receiptIDs
        return dsl.selectFrom(RECEIPTS)
                .where(RECEIPTS.ID.in(receiptIDs))
                .fetch();

    }

    public ReceiptsRecord getSingleReceiptsRecord(int receiptID) {
        // SELECT * From RECEIPTS WHERE ID IN receiptIDs
        return dsl.selectFrom(RECEIPTS)
                .where(RECEIPTS.ID.in(receiptID))
                .fetchOne();

    }

    public List<ReceiptsRecord> getAllReceipts() {
        return dsl.selectFrom(RECEIPTS).fetch();
    }
}
