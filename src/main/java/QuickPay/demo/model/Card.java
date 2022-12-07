package QuickPay.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("creditcard")
public class Card {
    @Id
    private String id;
    @Field(name = "holderName")
    private String holderName;
    @Field(name = "expirationTime")
    private String expirationTime;
    @Field(name = "securityCode")
    private int securityCode;
    @Field(name = "bankId")
    private int bankId;
    @Field(name = "location")
    private String location;
    @Field(name = "cardType")
    private String cardType;

    @Field(name = "cardNumber")
    private String cardNumber;

    @Field(name = "amount")
    private Double amount;

    public Card(String id, String holderName, String expirationTime,
                int securityCode, int bankId, String location, String cardType, String cardNumber) {
        this.id = id;
        this.holderName = holderName;
        this.expirationTime = expirationTime;
        this.securityCode = securityCode;
        this.bankId = bankId;
        this.location = location;
        this.cardType = cardType;
        this.cardNumber = cardNumber;
    }

    public Card(String card_number){
        this.cardNumber = card_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(int securityCode) {
        this.securityCode = securityCode;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCardType(){
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber(){
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }


}
