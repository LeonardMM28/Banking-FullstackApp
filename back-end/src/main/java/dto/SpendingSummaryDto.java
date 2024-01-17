package dto;


import org.bson.Document;

public class SpendingSummaryDto extends BaseDto{
    private Double spendingSummary;

    public SpendingSummaryDto(Double spendingSummary){
        this.spendingSummary = spendingSummary;
    }
    public Double getSpendingSummary(){
        return spendingSummary;
    }

    @Override
    public Document toDocument() {
        Document document = new Document();
        document.append("SpendingSummary", spendingSummary);
        document.append("uniqueId", getUniqueId());
        return document;
    }
}
