package project.bankrut.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import project.bankrut.model.Transaction;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TransactionService {
    private static final String TRANSACTION_FILE = "transactions.json";
    private final ObjectMapper objectMapper;
    
    public TransactionService() {
        this.objectMapper = new ObjectMapper();
        initializeFile();
    }
    
    private void initializeFile() {
        File file = new File(TRANSACTION_FILE);
        if (!file.exists()) {
            try {
                ArrayNode emptyArray = objectMapper.createArrayNode();
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, emptyArray);
                System.out.println("File transaksi dibuat: " + TRANSACTION_FILE);
            } catch (IOException e) {
                System.err.println("Error membuat file transaksi: " + e.getMessage());
            }
        }
    }
    
    public boolean saveTransaction(Transaction transaction, String username) {
        try {
            ArrayNode transactionsArray = loadTransactionsArray();
            
            ObjectNode transactionNode = objectMapper.createObjectNode();
            transactionNode.put("username", username);
            transactionNode.put("referenceNumber", transaction.getReferenceNumber());
            transactionNode.put("transactionType", transaction.getTransactionType());
            transactionNode.put("transactionDate", transaction.getTransactionDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            transactionNode.put("sourceAccount", transaction.getSourceAccount());
            transactionNode.put("sourceName", transaction.getSourceName());
            transactionNode.put("targetAccount", transaction.getTargetAccount());
            transactionNode.put("targetName", transaction.getTargetName());
            transactionNode.put("amount", transaction.getAmount());
            transactionNode.put("balanceAfter", transaction.getBalanceAfter());
            
            transactionsArray.add(transactionNode);
            saveTransactionsArray(transactionsArray);
            
            System.out.println("Transaksi berhasil disimpan: " + transaction.getReferenceNumber());
            return true;
            
        } catch (IOException e) {
            System.err.println("Error menyimpan transaksi: " + e.getMessage());
            return false;
        }
    }
    
    public List<Transaction> getUserTransactions(String username) {
        List<Transaction> userTransactions = new ArrayList<>();
        
        try {
            ArrayNode transactionsArray = loadTransactionsArray();
            
            for (int i = transactionsArray.size() - 1; i >= 0; i--) { // Reverse order (newest first)
                ObjectNode transactionNode = (ObjectNode) transactionsArray.get(i);
                
                if (transactionNode.get("username").asText().equals(username)) {
                    Transaction transaction = reconstructTransaction(transactionNode);
                    if (transaction != null) {
                        userTransactions.add(transaction);
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error mengambil transaksi user: " + e.getMessage());
        }
        
        return userTransactions;
    }
    
    private Transaction reconstructTransaction(ObjectNode node) {
        try {
            String type = node.get("transactionType").asText();
            String sourceName = node.get("sourceName").asText();
            String sourceAccount = node.get("sourceAccount").asText();
            String targetName = node.get("targetName").asText();
            String targetAccount = node.get("targetAccount").asText();
            double amount = node.get("amount").asDouble();
            double balanceAfter = node.get("balanceAfter").asDouble();
            
            Transaction transaction;
            
            if (type.equals("Setor Tunai")) {
                transaction = new Transaction(sourceName, sourceAccount, amount, balanceAfter);
            } else if (type.equals("Tarik Tunai")) {
                transaction = new Transaction(sourceName, sourceAccount, amount, balanceAfter, true);
            } else {
                transaction = new Transaction(sourceName, sourceAccount, targetName, targetAccount, amount, balanceAfter);
            }
            setTransactionFields(transaction, node);
            return transaction;
        } catch (Exception e) {
            System.err.println("Error reconstructing transaction: " + e.getMessage());
            return null;
        }
    }
    
    private void setTransactionFields(Transaction transaction, ObjectNode node) {
        try {
            java.lang.reflect.Field refField = Transaction.class.getDeclaredField("referenceNumber");
            refField.setAccessible(true);
            refField.set(transaction, node.get("referenceNumber").asText());
            
            java.lang.reflect.Field dateField = Transaction.class.getDeclaredField("transactionDate");
            dateField.setAccessible(true);
            LocalDateTime date = LocalDateTime.parse(node.get("transactionDate").asText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            dateField.set(transaction, date);
            
        } catch (Exception e) {
            System.err.println("Error setting transaction fields: " + e.getMessage());
        }
    }
    
    private ArrayNode loadTransactionsArray() {
        try {
            File file = new File(TRANSACTION_FILE);
            if (file.exists() && file.length() > 0) {
                return (ArrayNode) objectMapper.readTree(file);
            }
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        }
        return objectMapper.createArrayNode();
    }
    
    private void saveTransactionsArray(ArrayNode transactionsArray) throws IOException {
        File file = new File(TRANSACTION_FILE);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, transactionsArray);
    }
}
