package project.bankrut.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Transaction {
    private final String referenceNumber;
    private final String transactionType;
    private final LocalDateTime transactionDate;
    private final String sourceAccount;
    private final String sourceName;
    private final String targetAccount;
    private final String targetName;
    private final double amount;
    private final double balanceAfter;

    public Transaction(String sourceName, String sourceAccount, double amount, double balanceAfter) {
        this.transactionType = "Setor Tunai";
        this.sourceName = sourceName;
        this.sourceAccount = sourceAccount;
        this.targetName = sourceName;
        this.targetAccount = sourceAccount;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.transactionDate = LocalDateTime.now();
        this.referenceNumber = generateReferenceNumber();
    }

    public Transaction(String accountName, String accountNumber, double amount, 
                      double balanceAfter, boolean isWithdrawal) {
        this.transactionType = isWithdrawal ? "Tarik Tunai" : "Setor Tunai";
        this.sourceName = accountName;
        this.sourceAccount = accountNumber;
        this.targetName = accountName;
        this.targetAccount = accountNumber;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.transactionDate = LocalDateTime.now();
        this.referenceNumber = generateReferenceNumber();
    }
    
    public Transaction(String sourceName, String sourceAccount, String targetName, 
                      String targetAccount, double amount, double balanceAfter) {
        this.transactionType = "Transfer BANKRUT";
        this.sourceName = sourceName;
        this.sourceAccount = sourceAccount;
        this.targetName = targetName;
        this.targetAccount = targetAccount;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.transactionDate = LocalDateTime.now();
        this.referenceNumber = generateReferenceNumber();
    }
    
    private String generateReferenceNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public String getReferenceNumber() { 
        return referenceNumber; 
    }
    
    public String getTransactionType() { 
        return transactionType; 
    }
    
    public LocalDateTime getTransactionDate() { 
        return transactionDate; 
    }
    
    public String getSourceAccount() { 
        return sourceAccount; 
    }
    
    public String getSourceName() { 
        return sourceName; 
    }
    
    public String getTargetAccount() { 
        return targetAccount; 
    }
    
    public String getTargetName() { 
        return targetName; 
    }
    
    public double getAmount() { 
        return amount; 
    }
    
    public double getBalanceAfter() { 
        return balanceAfter; 
    }
    
    public String getFormattedDate() {
        return transactionDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss 'WIB'"));
    }
    
    public String getShortDate() {
        return transactionDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    
    public boolean isDebit() {
        return transactionType.equals("Tarik Tunai") || 
               (transactionType.equals("Transfer BANKRUT") && !sourceAccount.equals(targetAccount));
    }

    public String getFormattedAmount() {
        return String.format("Rp %,.2f", amount);
    }
    
    @Override
    public String toString() {
        return String.format("Transaction{ref='%s', type='%s', amount=%.2f, date='%s'}", 
                           referenceNumber, transactionType, amount, getShortDate());
    }
}